package com.github.paohaijiao.util;

import org.apache.poi.ss.usermodel.*;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JCellStyleCache {

    private static final WeakHashMap<Workbook, Map<String, CellStyle>> styleCache = new WeakHashMap<>();

    private static final WeakHashMap<Workbook, Map<String, Font>> fontCache = new WeakHashMap<>();

    private static final String KEY_HEADER = "_HEADER_STYLE_";

    private static final String KEY_DATA_EVEN = "_DATA_EVEN_STYLE_";

    private static final String KEY_DATA_ODD = "_DATA_ODD_STYLE_";

    private static final String KEY_FOOTER = "_FOOTER_STYLE_";

    private static final String KEY_FORMULA = "_FORMULA_STYLE_";

    private static Map<String, CellStyle> getStyleMap(Workbook workbook) {
        return styleCache.computeIfAbsent(workbook, k -> new ConcurrentHashMap<>());
    }

    private static Map<String, Font> getFontMap(Workbook workbook) {
        return fontCache.computeIfAbsent(workbook, k -> new ConcurrentHashMap<>());
    }

    public static CellStyle getHeaderStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_HEADER, k -> createHeaderStyle(wb));
    }

    public static CellStyle getDataEvenStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_DATA_EVEN, k -> createDataEvenStyle(wb));
    }

    public static CellStyle getDataOddStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_DATA_ODD, k -> createDataOddStyle(wb));
    }

    public static CellStyle getFooterStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_FOOTER, k -> createFooterStyle(wb));
    }

    public static CellStyle getFormulaStyle(Workbook wb) {
        return getStyleMap(wb).computeIfAbsent(KEY_FORMULA, k -> createFormulaCellStyle(wb));
    }

    public static CellStyle getFormatStyle(Workbook wb, String formatSpec) {
        String key = "_FORMAT_" + formatSpec;
        return getStyleMap(wb).computeIfAbsent(key, k -> createFormatStyle(wb, formatSpec));
    }

    public static CellStyle getOrCreateStyle(Workbook wb, String styleKey, StyleCreator creator) {
        return getStyleMap(wb).computeIfAbsent(styleKey, k -> creator.create(wb));
    }

    public static Font getOrCreateFont(Workbook wb, String fontKey, FontCreator creator) {
        return getFontMap(wb).computeIfAbsent(fontKey, k -> creator.create(wb));
    }

    public static void clearCache(Workbook workbook) {
        styleCache.remove(workbook);
        fontCache.remove(workbook);
    }

    private static CellStyle createHeaderStyle(Workbook wb) {
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

    private static CellStyle createDataEvenStyle(Workbook wb) {
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

    private static CellStyle createDataOddStyle(Workbook wb) {
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

    private static CellStyle createFooterStyle(Workbook wb) {
        CellStyle footerStyle = wb.createCellStyle();
        Font footerFont = wb.createFont();
        footerFont.setItalic(true);
        footerFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        footerStyle.setFont(footerFont);
        footerStyle.setAlignment(HorizontalAlignment.LEFT);
        footerStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        return footerStyle;
    }

    private static CellStyle createFormulaCellStyle(Workbook wb) {
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

    private static CellStyle createFormatStyle(Workbook wb, String formatSpec) {
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