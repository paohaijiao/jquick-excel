package com.github.paohaijiao.jstyle.model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.HashMap;
import java.util.Map;

public class JExcelStyle {

    private final Map<String, Object> styleMap = new HashMap<>();
    private boolean locked;
    public static final String FONT_NAME = "font.name";
    public static final String FONT_SIZE = "font.size";
    public static final String FONT_BOLD = "font.bold";
    public static final String FONT_ITALIC = "font.italic";
    public static final String FONT_STRIKEOUT = "font.strikeout";
    public static final String FONT_UNDERLINE = "font.underline";
    public static final String FONT_COLOR = "font.color";
    public static final String FONT_XSSF_COLOR = "font.xssfColor";
    public static final String FILL_FOREGROUND_COLOR = "fill.foregroundColor";
    public static final String FILL_FOREGROUND_XSSF_COLOR = "fill.foregroundXSSFColor";
    public static final String FILL_BACKGROUND_COLOR = "fill.backgroundColor";
    public static final String FILL_PATTERN = "fill.pattern";
    public static final String BORDER_TOP = "border.top";
    public static final String BORDER_BOTTOM = "border.bottom";
    public static final String BORDER_LEFT = "border.left";
    public static final String BORDER_RIGHT = "border.right";
    public static final String BORDER_TOP_COLOR = "border.topColor";
    public static final String BORDER_BOTTOM_COLOR = "border.bottomColor";
    public static final String BORDER_LEFT_COLOR = "border.leftColor";
    public static final String BORDER_RIGHT_COLOR = "border.rightColor";
    public static final String ALIGNMENT = "alignment";
    public static final String VERTICAL_ALIGNMENT = "verticalAlignment";
    public static final String WRAP_TEXT = "wrapText";
    public static final String ROTATION = "rotation";
    public static final String INDENTION = "indention";
    public static final String DATA_FORMAT = "dataFormat";
    public static final String DATA_FORMAT_INDEX = "dataFormatIndex";
    public static final String STYLE_NAME = "styleName";
    private static final String DEFAULT_FONT_NAME = "宋体";
    private static final short DEFAULT_FONT_SIZE = 11;
    private static final boolean DEFAULT_FONT_BOLD = false;
    private static final boolean DEFAULT_FONT_ITALIC = false;
    private static final boolean DEFAULT_FONT_STRIKEOUT = false;
    private static final byte DEFAULT_FONT_UNDERLINE = Font.U_NONE;
    private static final short DEFAULT_FONT_COLOR = IndexedColors.BLACK.getIndex();
    private static final short DEFAULT_FILL_FOREGROUND_COLOR = IndexedColors.AUTOMATIC.getIndex();
    private static final short DEFAULT_FILL_BACKGROUND_COLOR = IndexedColors.AUTOMATIC.getIndex();
    private static final FillPatternType DEFAULT_FILL_PATTERN = FillPatternType.NO_FILL;
    private static final BorderStyle DEFAULT_BORDER_STYLE = BorderStyle.NONE;
    private static final short DEFAULT_BORDER_COLOR = IndexedColors.BLACK.getIndex();
    private static final HorizontalAlignment DEFAULT_ALIGNMENT = HorizontalAlignment.GENERAL;
    private static final VerticalAlignment DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignment.CENTER;
    private static final boolean DEFAULT_WRAP_TEXT = false;
    private static final short DEFAULT_ROTATION = 0;
    private static final short DEFAULT_INDENTION = 0;
    private static final short DEFAULT_DATA_FORMAT_INDEX = 0;

    public JExcelStyle() {
        setDefaultValues();
    }

    private void setDefaultValues() {
        styleMap.put(FONT_NAME, DEFAULT_FONT_NAME);
        styleMap.put(FONT_SIZE, DEFAULT_FONT_SIZE);
        styleMap.put(FONT_BOLD, DEFAULT_FONT_BOLD);
        styleMap.put(FONT_ITALIC, DEFAULT_FONT_ITALIC);
        styleMap.put(FONT_STRIKEOUT, DEFAULT_FONT_STRIKEOUT);
        styleMap.put(FONT_UNDERLINE, DEFAULT_FONT_UNDERLINE);
        styleMap.put(FONT_COLOR, DEFAULT_FONT_COLOR);
        styleMap.put(FILL_FOREGROUND_COLOR, DEFAULT_FILL_FOREGROUND_COLOR);
        styleMap.put(FILL_BACKGROUND_COLOR, DEFAULT_FILL_BACKGROUND_COLOR);
        styleMap.put(FILL_PATTERN, DEFAULT_FILL_PATTERN);
        styleMap.put(BORDER_TOP, DEFAULT_BORDER_STYLE);
        styleMap.put(BORDER_BOTTOM, DEFAULT_BORDER_STYLE);
        styleMap.put(BORDER_LEFT, DEFAULT_BORDER_STYLE);
        styleMap.put(BORDER_RIGHT, DEFAULT_BORDER_STYLE);
        styleMap.put(BORDER_TOP_COLOR, DEFAULT_BORDER_COLOR);
        styleMap.put(BORDER_BOTTOM_COLOR, DEFAULT_BORDER_COLOR);
        styleMap.put(BORDER_LEFT_COLOR, DEFAULT_BORDER_COLOR);
        styleMap.put(BORDER_RIGHT_COLOR, DEFAULT_BORDER_COLOR);
        styleMap.put(ALIGNMENT, DEFAULT_ALIGNMENT);
        styleMap.put(VERTICAL_ALIGNMENT, DEFAULT_VERTICAL_ALIGNMENT);
        styleMap.put(WRAP_TEXT, DEFAULT_WRAP_TEXT);
        styleMap.put(ROTATION, DEFAULT_ROTATION);
        styleMap.put(INDENTION, DEFAULT_INDENTION);
        styleMap.put(DATA_FORMAT_INDEX, DEFAULT_DATA_FORMAT_INDEX);
    }

    private void checkLocked() {
        if (locked) {
            throw new IllegalStateException("Style is locked and cannot be modified");
        }
    }
    public void set(String key, Object value) {
        checkLocked();
        styleMap.put(key, value);
    }

    public Object get(String key) {
        return styleMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = styleMap.get(key);
        return value != null && type.isAssignableFrom(value.getClass()) ? (T) value : null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(String key, T defaultValue, Class<T> type) {
        Object value = styleMap.get(key);
        return value != null && type.isAssignableFrom(value.getClass()) ? (T) value : defaultValue;
    }

    public boolean contains(String key) {
        return styleMap.containsKey(key);
    }

    public void remove(String key) {
        checkLocked();
        styleMap.remove(key);
    }

    public String getFontName() {
        return getOrDefault(FONT_NAME, DEFAULT_FONT_NAME, String.class);
    }

    public void setFontName(String fontName) {
        set(FONT_NAME, fontName);
    }

    public short getFontSize() {
        return getOrDefault(FONT_SIZE, DEFAULT_FONT_SIZE, Short.class);
    }

    public void setFontSize(short fontSize) {
        set(FONT_SIZE, fontSize);
    }

    public boolean isFontBold() {
        return getOrDefault(FONT_BOLD, DEFAULT_FONT_BOLD, Boolean.class);
    }

    public void setFontBold(boolean bold) {
        set(FONT_BOLD, bold);
    }

    public boolean isFontItalic() {
        return getOrDefault(FONT_ITALIC, DEFAULT_FONT_ITALIC, Boolean.class);
    }

    public void setFontItalic(boolean italic) {
        set(FONT_ITALIC, italic);
    }

    public boolean isFontStrikeout() {
        return getOrDefault(FONT_STRIKEOUT, DEFAULT_FONT_STRIKEOUT, Boolean.class);
    }

    public void setFontStrikeout(boolean strikeout) {
        set(FONT_STRIKEOUT, strikeout);
    }

    public byte getFontUnderline() {
        return getOrDefault(FONT_UNDERLINE, DEFAULT_FONT_UNDERLINE, Byte.class);
    }

    public void setFontUnderline(byte underline) {
        set(FONT_UNDERLINE, underline);
    }

    public short getFontColor() {
        return getOrDefault(FONT_COLOR, DEFAULT_FONT_COLOR, Short.class);
    }

    public void setFontColor(short color) {
        set(FONT_COLOR, color);
    }

    public XSSFColor getFontXSSFColor() {
        return get(FONT_XSSF_COLOR, XSSFColor.class);
    }

    public void setFontXSSFColor(XSSFColor color) {
        set(FONT_XSSF_COLOR, color);
    }

    public short getFillForegroundColor() {
        return getOrDefault(FILL_FOREGROUND_COLOR, DEFAULT_FILL_FOREGROUND_COLOR, Short.class);
    }

    public void setFillForegroundColor(short color) {
        set(FILL_FOREGROUND_COLOR, color);
    }

    public XSSFColor getFillForegroundXSSFColor() {
        return get(FILL_FOREGROUND_XSSF_COLOR, XSSFColor.class);
    }

    public void setFillForegroundXSSFColor(XSSFColor color) {
        set(FILL_FOREGROUND_XSSF_COLOR, color);
    }

    public short getFillBackgroundColor() {
        return getOrDefault(FILL_BACKGROUND_COLOR, DEFAULT_FILL_BACKGROUND_COLOR, Short.class);
    }

    public void setFillBackgroundColor(short color) {
        set(FILL_BACKGROUND_COLOR, color);
    }

    public FillPatternType getFillPattern() {
        return getOrDefault(FILL_PATTERN, DEFAULT_FILL_PATTERN, FillPatternType.class);
    }

    public void setFillPattern(FillPatternType pattern) {
        set(FILL_PATTERN, pattern);
    }

    // ================= 边框相关方法 =================

    public BorderStyle getBorderTop() {
        return getOrDefault(BORDER_TOP, DEFAULT_BORDER_STYLE, BorderStyle.class);
    }

    public void setBorderTop(BorderStyle style) {
        set(BORDER_TOP, style);
    }

    public BorderStyle getBorderBottom() {
        return getOrDefault(BORDER_BOTTOM, DEFAULT_BORDER_STYLE, BorderStyle.class);
    }

    public void setBorderBottom(BorderStyle style) {
        set(BORDER_BOTTOM, style);
    }

    public BorderStyle getBorderLeft() {
        return getOrDefault(BORDER_LEFT, DEFAULT_BORDER_STYLE, BorderStyle.class);
    }

    public void setBorderLeft(BorderStyle style) {
        set(BORDER_LEFT, style);
    }

    public BorderStyle getBorderRight() {
        return getOrDefault(BORDER_RIGHT, DEFAULT_BORDER_STYLE, BorderStyle.class);
    }

    public void setBorderRight(BorderStyle style) {
        set(BORDER_RIGHT, style);
    }

    public short getBorderTopColor() {
        return getOrDefault(BORDER_TOP_COLOR, DEFAULT_BORDER_COLOR, Short.class);
    }

    public void setBorderTopColor(short color) {
        set(BORDER_TOP_COLOR, color);
    }

    public short getBorderBottomColor() {
        return getOrDefault(BORDER_BOTTOM_COLOR, DEFAULT_BORDER_COLOR, Short.class);
    }

    public void setBorderBottomColor(short color) {
        set(BORDER_BOTTOM_COLOR, color);
    }

    public short getBorderLeftColor() {
        return getOrDefault(BORDER_LEFT_COLOR, DEFAULT_BORDER_COLOR, Short.class);
    }

    public void setBorderLeftColor(short color) {
        set(BORDER_LEFT_COLOR, color);
    }

    public short getBorderRightColor() {
        return getOrDefault(BORDER_RIGHT_COLOR, DEFAULT_BORDER_COLOR, Short.class);
    }

    public void setBorderRightColor(short color) {
        set(BORDER_RIGHT_COLOR, color);
    }

    // ================= 对齐相关方法 =================

    public HorizontalAlignment getAlignment() {
        return getOrDefault(ALIGNMENT, DEFAULT_ALIGNMENT, HorizontalAlignment.class);
    }

    public void setAlignment(HorizontalAlignment alignment) {
        set(ALIGNMENT, alignment);
    }

    public VerticalAlignment getVerticalAlignment() {
        return getOrDefault(VERTICAL_ALIGNMENT, DEFAULT_VERTICAL_ALIGNMENT, VerticalAlignment.class);
    }

    public void setVerticalAlignment(VerticalAlignment alignment) {
        set(VERTICAL_ALIGNMENT, alignment);
    }

    public boolean isWrapText() {
        return getOrDefault(WRAP_TEXT, DEFAULT_WRAP_TEXT, Boolean.class);
    }

    public void setWrapText(boolean wrapText) {
        set(WRAP_TEXT, wrapText);
    }

    public short getRotation() {
        return getOrDefault(ROTATION, DEFAULT_ROTATION, Short.class);
    }

    public void setRotation(short rotation) {
        set(ROTATION, rotation);
    }

    public short getIndention() {
        return getOrDefault(INDENTION, DEFAULT_INDENTION, Short.class);
    }

    public void setIndention(short indention) {
        set(INDENTION, indention);
    }
    public String getDataFormat() {
        return get(DATA_FORMAT, String.class);
    }

    public void setDataFormat(String format) {
        set(DATA_FORMAT, format);
    }

    public short getDataFormatIndex() {
        return getOrDefault(DATA_FORMAT_INDEX, DEFAULT_DATA_FORMAT_INDEX, Short.class);
    }

    public void setDataFormatIndex(short index) {
        set(DATA_FORMAT_INDEX, index);
    }


    public String getStyleName() {
        return get(STYLE_NAME, String.class);
    }

    public void setStyleName(String name) {
        set(STYLE_NAME, name);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public JExcelStyle copy() {
        JExcelStyle newStyle = new JExcelStyle();
        newStyle.styleMap.putAll(this.styleMap);
        return newStyle;
    }

    public Map<String, Object> toMap() {
        return new HashMap<>(styleMap);
    }

    public void fromMap(Map<String, Object> map) {
        checkLocked();
        styleMap.clear();
        styleMap.putAll(map);
    }

    @Override
    public String toString() {
        return "ExcelStyle" + styleMap.toString();
    }
    public CellStyle applyToCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        applyFontStyles(font);
        applyFillStyles(cellStyle);
        applyBorderStyles(cellStyle);
        applyAlignmentStyles(cellStyle);
        applyDataFormat(workbook, cellStyle);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void applyFontStyles(Font font) {
        font.setFontName(getFontName());
        font.setFontHeightInPoints(getFontSize());
        font.setBold(isFontBold());
        font.setItalic(isFontItalic());
        font.setStrikeout(isFontStrikeout());
        font.setUnderline(getFontUnderline());
        XSSFColor xssfColor = getFontXSSFColor();
        if (xssfColor != null && font instanceof XSSFFont) {
            ((XSSFFont) font).setColor(xssfColor);
        } else {
            font.setColor(getFontColor());
        }
    }

    private void applyFillStyles(CellStyle cellStyle) {
        XSSFColor xssfColor = getFillForegroundXSSFColor();
        if (xssfColor != null && cellStyle instanceof XSSFCellStyle) {
            ((XSSFCellStyle) cellStyle).setFillForegroundColor(xssfColor);
        } else {
            cellStyle.setFillForegroundColor(getFillForegroundColor());
        }
        cellStyle.setFillBackgroundColor(getFillBackgroundColor());
        cellStyle.setFillPattern(getFillPattern());
    }

    private void applyBorderStyles(CellStyle cellStyle) {
        cellStyle.setBorderTop(getBorderTop());
        cellStyle.setBorderBottom(getBorderBottom());
        cellStyle.setBorderLeft(getBorderLeft());
        cellStyle.setBorderRight(getBorderRight());
        cellStyle.setTopBorderColor(getBorderTopColor());
        cellStyle.setBottomBorderColor(getBorderBottomColor());
        cellStyle.setLeftBorderColor(getBorderLeftColor());
        cellStyle.setRightBorderColor(getBorderRightColor());
    }

    private void applyAlignmentStyles(CellStyle cellStyle) {
        cellStyle.setAlignment(getAlignment());
        cellStyle.setVerticalAlignment(getVerticalAlignment());
        cellStyle.setWrapText(isWrapText());
        cellStyle.setRotation(getRotation());
        cellStyle.setIndention(getIndention());
    }

    private void applyDataFormat(Workbook workbook, CellStyle cellStyle) {
        String format = getDataFormat();
        if (format != null) {
            cellStyle.setDataFormat(workbook.createDataFormat().getFormat(format));
        } else {
            short formatIndex = getDataFormatIndex();
            if (formatIndex > 0) {
                cellStyle.setDataFormat(formatIndex);
            }
        }
    }
    public void loadFromCellStyle(CellStyle cellStyle, Font font) {
        checkLocked();
        styleMap.clear();
        setDefaultValues();
        if (font != null) {
            loadFontStyles(font);
        }
        loadFillStyles(cellStyle);
        loadBorderStyles(cellStyle);
        loadAlignmentStyles(cellStyle);
        loadDataFormat(cellStyle);
    }
    private void loadFontStyles(Font font) {
        setFontName(font.getFontName());
        setFontSize(font.getFontHeightInPoints());
        setFontBold(font.getBold());
        setFontItalic(font.getItalic());
        setFontStrikeout(font.getStrikeout());
        setFontUnderline(font.getUnderline());
        setFontColor(font.getColor());
        if (font instanceof XSSFFont) {
            setFontXSSFColor(((XSSFFont) font).getXSSFColor());
        }
    }

    private void loadFillStyles(CellStyle cellStyle) {
        setFillForegroundColor(cellStyle.getFillForegroundColor());
        setFillBackgroundColor(cellStyle.getFillBackgroundColor());
        setFillPattern(cellStyle.getFillPattern());
        if (cellStyle instanceof XSSFCellStyle) {
            setFillForegroundXSSFColor(((XSSFCellStyle) cellStyle).getFillForegroundXSSFColor());
        }
    }

    private void loadBorderStyles(CellStyle cellStyle) {
        setBorderTop(cellStyle.getBorderTop());
        setBorderBottom(cellStyle.getBorderBottom());
        setBorderLeft(cellStyle.getBorderLeft());
        setBorderRight(cellStyle.getBorderRight());
        setBorderTopColor(cellStyle.getTopBorderColor());
        setBorderBottomColor(cellStyle.getBottomBorderColor());
        setBorderLeftColor(cellStyle.getLeftBorderColor());
        setBorderRightColor(cellStyle.getRightBorderColor());
    }

    private void loadAlignmentStyles(CellStyle cellStyle) {
        setAlignment(cellStyle.getAlignment());
        setVerticalAlignment(cellStyle.getVerticalAlignment());
        setWrapText(cellStyle.getWrapText());
        setRotation(cellStyle.getRotation());
        setIndention(cellStyle.getIndention());
    }

    private void loadDataFormat(CellStyle cellStyle) {
        setDataFormatIndex(cellStyle.getDataFormat());
    }
}
