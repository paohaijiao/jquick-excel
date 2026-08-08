package com.github.paohaijiao.util;

import com.github.paohaijiao.theme.JExcelTheme;
import org.apache.poi.ss.usermodel.*;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JCellStyleCache {

    private static final WeakHashMap<Workbook, Map<String, CellStyle>> STYLE_CACHE = new WeakHashMap<>();

    private static final WeakHashMap<Workbook, Map<String, Font>> FONT_CACHE = new WeakHashMap<>();

    /* ===== 默认样式 Key ===== */
    private static final String KEY_HEADER        = "_DEF_HEADER_STYLE_";
    private static final String KEY_DATA_EVEN     = "_DEF_DATA_EVEN_STYLE_";
    private static final String KEY_DATA_ODD      = "_DEF_DATA_ODD_STYLE_";
    private static final String KEY_FOOTER        = "_DEF_FOOTER_STYLE_";
    private static final String KEY_FORMULA       = "_DEF_FORMULA_STYLE_";
    private static final String KEY_MERGE_CENTER  = "_MERGE_CENTER_STYLE_";

    /* ===== 类别常量：给 Theme 样式缓存使用 ===== */
    public static final String KIND_HEADER   = "H";
    public static final String KIND_DATA_EVEN= "E";
    public static final String KIND_DATA_ODD = "O";
    public static final String KIND_TITLE    = "T";
    public static final String KIND_FOOTER   = "F";
    public static final String KIND_FORMULA  = "R";

    private static Map<String, CellStyle> getStyleMap(Workbook workbook) {
        return STYLE_CACHE.computeIfAbsent(workbook, k -> new ConcurrentHashMap<>());
    }

    private static Map<String, Font> getFontMap(Workbook workbook) {
        return FONT_CACHE.computeIfAbsent(workbook, k -> new ConcurrentHashMap<>());
    }

    /* ========================= 默认样式：无主题场景 ========================= */

    public static CellStyle getHeaderStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_HEADER, k -> createDefaultHeaderStyle(wb));
    }

    public static CellStyle getDataEvenStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_DATA_EVEN, k -> createDefaultDataEvenStyle(wb));
    }

    public static CellStyle getDataOddStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_DATA_ODD, k -> createDefaultDataOddStyle(wb));
    }

    public static CellStyle getFooterStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_FOOTER, k -> createDefaultFooterStyle(wb));
    }

    public static CellStyle getFormulaStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_FORMULA, k -> createDefaultFormulaCellStyle(wb));
    }

    public static CellStyle getMergeCenterStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_MERGE_CENTER, k -> createMergeCenterStyle(wb));
    }

    public static CellStyle getFormatStyle(Workbook wb, String formatSpec) {
        String key = "_FORMAT_" + formatSpec;
        return getStyleMap(wb).computeIfAbsent(key, k -> createFormatOnlyStyle(wb, formatSpec));
    }

    /**
     * 仅用于"完全自定义"的缓存入口，例如 RANGE STYLE / CELL STYLE 等。
     * 请调用方提供唯一 styleKey，相同样式配置必须使用相同 key，否则会产生重复样式条目浪费额度。
     */
    public static CellStyle getOrCreateStyle(Workbook wb, String styleKey, StyleCreator creator) {
        return getStyleMap(wb).computeIfAbsent(styleKey, k -> creator.create(wb));
    }

    public static Font getOrCreateFont(Workbook wb, String fontKey, FontCreator creator) {
        return getFontMap(wb).computeIfAbsent(fontKey, k -> creator.create(wb));
    }

    /* ========================= 主题样式：配合 JAbstractExcelTheme ========================= */

    /**
     * 获取主题下某类别的样式（每个 workbook + 每个 theme + 每个 kind 只创建一次）。
     *
     * @param kind 取值 {@link #KIND_HEADER}/{@link #KIND_DATA_EVEN}/{@link #KIND_DATA_ODD}/
     *             {@link #KIND_TITLE}/{@link #KIND_FOOTER}/{@link #KIND_FORMULA}
     */
    public static CellStyle getThemeStyle(final Workbook wb, final JExcelTheme theme, final String kind) {
        final String key = "_THEME_" + theme.getCode() + "_" + kind;
        return getStyleMap(wb).computeIfAbsent(key, k -> {
            switch (kind) {
                case KIND_HEADER:    return theme.buildHeaderStyle(wb);
                case KIND_DATA_EVEN: return theme.buildDataEvenStyle(wb);
                case KIND_DATA_ODD:  return theme.buildDataOddStyle(wb);
                case KIND_TITLE:     return theme.buildTitleStyle(wb);
                case KIND_FOOTER:    return theme.buildFooterStyle(wb);
                case KIND_FORMULA:   return theme.buildFormulaCellStyle(wb);
                default: throw new IllegalArgumentException("Unknown theme style kind: " + kind);
            }
        });
    }

    /**
     * "基础数据行样式 + 指定日期/数字格式"的组合样式（缓存复用，避免克隆膨胀）。
     *
     * <p>用于导出时某个字段既要保留斑马纹边框/底色/字体，又需要对本单元格叠加 yyyy-MM-dd / #,##0.00 等格式场景。
     *
     * @param theme   主题，{@code null} 表示用默认斑马纹
     * @param isOdd   true → 奇数行样式；false → 偶数行样式
     * @param format  POI 支持的 dataFormat 文本；空/null 表示不叠加格式，直接返回基础样式
     */
    public static CellStyle getThemedDataWithFormat(Workbook wb, JExcelTheme theme, boolean isOdd, String format) {
        CellStyle base = resolveBaseDataStyle(wb, theme, isOdd);
        if (format == null || format.isEmpty()) {
            return base;
        }
        String key = (theme == null ? "_DEF_" : "_THEME_" + theme.getCode() + "_")
                + (isOdd ? "ODD" : "EVEN") + "_FMT_" + format;
        return getStyleMap(wb).computeIfAbsent(key, k -> cloneStyleWithFormat(wb, base, format));
    }

    private static CellStyle resolveBaseDataStyle(Workbook wb, JExcelTheme theme, boolean isOdd) {
        if (theme == null) {
            return isOdd ? getDataOddStyle(wb) : getDataEvenStyle(wb);
        }
        return isOdd
                ? getThemeStyle(wb, theme, KIND_DATA_ODD)
                : getThemeStyle(wb, theme, KIND_DATA_EVEN);
    }

    /**
     * 克隆一个已有 CellStyle 并叠加 DataFormat（克隆本身是同一个 StylesSource 里新建；需要缓存避免每单元格都克隆）。
     */
    static CellStyle cloneStyleWithFormat(Workbook wb, CellStyle base, String format) {
        CellStyle clone = wb.createCellStyle();
        clone.cloneStyleFrom(base);
        if (format != null && !format.isEmpty()) {
            clone.setDataFormat(wb.createDataFormat().getFormat(format));
        }
        return clone;
    }

    /**
     * 克隆已有样式（用于"基础样式 + 自定义覆盖"的场景，调用方应在外部对结果做缓存）。
     */
    public static CellStyle cloneStyle(Workbook wb, CellStyle base) {
        CellStyle clone = wb.createCellStyle();
        clone.cloneStyleFrom(base);
        return clone;
    }

    /**
     * 清理某个 Workbook 对应的全部样式与字体缓存。Workbook.close() 后一般不需要显式调用（WeakHashMap 自动回收）；
     * 若要在长生命周期进程中立即释放引用，可手动调用。
     */
    public static void clearCache(Workbook workbook) {
        STYLE_CACHE.remove(workbook);
        FONT_CACHE.remove(workbook);
    }

    /* ========================= 私有：样式模板构造 ========================= */

    private static CellStyle createDefaultHeaderStyle(Workbook wb) {
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return headerStyle;
    }

    private static CellStyle createDefaultDataEvenStyle(Workbook wb) {
        CellStyle dataEven = wb.createCellStyle();
        dataEven.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        dataEven.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataEven.setBorderBottom(BorderStyle.THIN);
        dataEven.setBorderTop(BorderStyle.THIN);
        dataEven.setBorderRight(BorderStyle.THIN);
        dataEven.setBorderLeft(BorderStyle.THIN);
        dataEven.setAlignment(HorizontalAlignment.RIGHT);
        dataEven.setVerticalAlignment(VerticalAlignment.CENTER);
        dataEven.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
        return dataEven;
    }

    private static CellStyle createDefaultDataOddStyle(Workbook wb) {
        CellStyle dataOdd = wb.createCellStyle();
        dataOdd.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        dataOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataOdd.setBorderBottom(BorderStyle.THIN);
        dataOdd.setBorderTop(BorderStyle.THIN);
        dataOdd.setBorderRight(BorderStyle.THIN);
        dataOdd.setBorderLeft(BorderStyle.THIN);
        dataOdd.setAlignment(HorizontalAlignment.RIGHT);
        dataOdd.setVerticalAlignment(VerticalAlignment.CENTER);
        return dataOdd;
    }

    private static CellStyle createDefaultFooterStyle(Workbook wb) {
        CellStyle footerStyle = wb.createCellStyle();
        Font footerFont = wb.createFont();
        footerFont.setItalic(true);
        footerFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        footerStyle.setFont(footerFont);
        footerStyle.setAlignment(HorizontalAlignment.LEFT);
        footerStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        return footerStyle;
    }

    private static CellStyle createDefaultFormulaCellStyle(Workbook wb) {
        CellStyle formulaCell = wb.createCellStyle();
        formulaCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        formulaCell.setBorderBottom(BorderStyle.THIN);
        formulaCell.setBorderTop(BorderStyle.THIN);
        formulaCell.setBorderRight(BorderStyle.THIN);
        formulaCell.setBorderLeft(BorderStyle.THIN);
        formulaCell.setAlignment(HorizontalAlignment.RIGHT);
        formulaCell.setVerticalAlignment(VerticalAlignment.CENTER);
        Font formulaFont = wb.createFont();
        formulaFont.setItalic(true);
        formulaCell.setFont(formulaFont);
        return formulaCell;
    }

    private static CellStyle createMergeCenterStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 仅包含 DataFormat 的样式。<strong>不推荐外部单独使用</strong>，因为这样赋值给 Cell 会覆盖原有的边框/斑马纹；
     * 请改用 {@link #getThemedDataWithFormat(Workbook, JExcelTheme, boolean, String)} 在基础样式上叠加。
     *
     * @deprecated 保留兼容；推荐使用 {@link #getThemedDataWithFormat}
     */
    @Deprecated
    private static CellStyle createFormatOnlyStyle(Workbook wb, String formatSpec) {
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(wb.createDataFormat().getFormat(formatSpec));
        return style;
    }

    @FunctionalInterface
    public interface StyleCreator {
        CellStyle create(Workbook workbook);
    }

    @FunctionalInterface
    public interface FontCreator {
        Font create(Workbook workbook);
    }
}
