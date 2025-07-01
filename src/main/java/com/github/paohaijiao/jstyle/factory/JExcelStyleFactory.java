package com.github.paohaijiao.jstyle.factory;

import com.github.paohaijiao.jstyle.model.JExcelStyle;
import org.apache.poi.ss.usermodel.*;

public class JExcelStyleFactory {
    private final Workbook workbook;
    private final JExcelStyle style;
    private JExcelStyleFactory(Workbook workbook) {
        this.workbook = workbook;
        this.style = new JExcelStyle();
    }

    public static JExcelStyleFactory create(Workbook workbook) {
        return new JExcelStyleFactory(workbook);
    }
    public JExcelStyleFactory withFontName(String fontName) {
        style.setFontName(fontName);
        return this;
    }

    public JExcelStyleFactory withFontSize(short fontSize) {
        style.setFontSize(fontSize);
        return this;
    }

    public JExcelStyleFactory withBold(boolean bold) {
        style.setFontBold(bold);
        return this;
    }

    public JExcelStyleFactory withItalic(boolean italic) {
        style.setFontItalic(italic);
        return this;
    }

    public JExcelStyleFactory withFontColor(short color) {
        style.setFontColor(color);
        return this;
    }

    public JExcelStyleFactory withFillForegroundColor(short color) {
        style.setFillForegroundColor(color);
        return this;
    }

    public JExcelStyleFactory withFillPattern(FillPatternType pattern) {
        style.setFillPattern(pattern);
        return this;
    }

    public JExcelStyleFactory withBorder(BorderStyle borderStyle) {
        style.setBorderTop(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        return this;
    }

    public JExcelStyleFactory withBorderColor(short color) {
        style.setBorderTopColor(color);
        style.setBorderBottomColor(color);
        style.setBorderLeftColor(color);
        style.setBorderRightColor(color);
        return this;
    }

    public JExcelStyleFactory withAlignment(HorizontalAlignment alignment) {
        style.setAlignment(alignment);
        return this;
    }

    public JExcelStyleFactory withVerticalAlignment(VerticalAlignment alignment) {
        style.setVerticalAlignment(alignment);
        return this;
    }

    public JExcelStyleFactory withWrapText(boolean wrap) {
        style.setWrapText(wrap);
        return this;
    }
    public JExcelStyleFactory withProperty(String propertyName, Object value) {
        style.set(propertyName, value);
        return this;
    }

    public JExcelStyle build() {
        return style;
    }

    public CellStyle buildAndApply() {
        return style.applyToCellStyle(workbook);
    }
}
