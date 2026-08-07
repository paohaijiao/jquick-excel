/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) [2025-2099] Martin (goudingcheng@gmail.com)
 */
package com.github.paohaijiao.theme;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public abstract class JAbstractExcelTheme implements JExcelTheme {

    private final String code;

    private final String name;

    private final String description;

    protected JAbstractExcelTheme(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }


    protected abstract JRgbColor headerBackground();

    protected abstract JRgbColor headerForeground();

    protected abstract JRgbColor dataEvenBackground();

    protected abstract JRgbColor dataOddBackground();

    protected abstract JRgbColor dataForeground();

    protected abstract JRgbColor borderColor();

    protected abstract JRgbColor accentBackground();

    protected abstract JRgbColor footerForeground();

    protected abstract JRgbColor titleBackground();

    protected abstract JRgbColor titleForeground();


    protected short headerFontSize() {
        return 12;
    }

    protected short titleFontSize() {
        return 16;
    }

    protected short dataFontSize() {
        return 11;
    }

    protected short footerFontSize() {
        return 10;
    }

    protected BorderStyle borderStyle() {
        return BorderStyle.THIN;
    }

    protected HorizontalAlignment dataAlignment() {
        return HorizontalAlignment.RIGHT;
    }

    /**
     * 偶数数据行的数字格式，返回 {@code null} 表示不强制格式。
     */
    protected String evenDataFormat() {
        return null;
    }

    /**
     * 奇数数据行的数字格式，返回 {@code null} 表示不强制格式。
     */
    protected String oddDataFormat() {
        return null;
    }


    @Override
    public CellStyle buildHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        applyFill(style, headerBackground());
        style.setFont(createFont(wb, headerForeground(), true, false, headerFontSize()));
        applyBorders(style);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    @Override
    public CellStyle buildDataEvenStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        applyFill(style, dataEvenBackground());
        style.setFont(createFont(wb, dataForeground(), false, false, dataFontSize()));
        applyBorders(style);
        style.setAlignment(dataAlignment());
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        applyDataFormat(wb, style, evenDataFormat());
        return style;
    }

    @Override
    public CellStyle buildDataOddStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        applyFill(style, dataOddBackground());
        style.setFont(createFont(wb, dataForeground(), false, false, dataFontSize()));
        applyBorders(style);
        style.setAlignment(dataAlignment());
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        applyDataFormat(wb, style, oddDataFormat());
        return style;
    }

    @Override
    public CellStyle buildTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        applyFill(style, titleBackground());
        style.setFont(createFont(wb, titleForeground(), true, false, titleFontSize()));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    @Override
    public CellStyle buildFooterStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFont(createFont(wb, footerForeground(), false, true, footerFontSize()));
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.BOTTOM);
        return style;
    }

    @Override
    public CellStyle buildFormulaCellStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        applyFill(style, accentBackground());
        style.setFont(createFont(wb, dataForeground(), false, true, dataFontSize()));
        applyBorders(style);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    // ===== 内部工具 =====

    protected void applyFill(CellStyle style, JRgbColor color) {
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        if (color == null) {
            return;
        }
        if (style instanceof XSSFCellStyle) {
            ((XSSFCellStyle) style).setFillForegroundColor(toXssfColor(color));
        } else {
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }
    }

    protected void applyBorders(CellStyle style) {
        BorderStyle bs = borderStyle();
        style.setBorderTop(bs);
        style.setBorderBottom(bs);
        style.setBorderLeft(bs);
        style.setBorderRight(bs);
        JRgbColor color = borderColor();
        if (color != null && style instanceof XSSFCellStyle) {
            XSSFColor col = toXssfColor(color);
            XSSFCellStyle xs = (XSSFCellStyle) style;
            xs.setTopBorderColor(col);
            xs.setBottomBorderColor(col);
            xs.setLeftBorderColor(col);
            xs.setRightBorderColor(col);
        }
    }

    protected Font createFont(Workbook wb, JRgbColor color, boolean bold, boolean italic, short size) {
        Font font = wb.createFont();
        font.setBold(bold);
        font.setItalic(italic);
        font.setFontHeightInPoints(size);
        if (color != null) {
            if (font instanceof XSSFFont) {
                ((XSSFFont) font).setColor(toXssfColor(color));
            } else {
                font.setColor(IndexedColors.BLACK.getIndex());
            }
        }
        return font;
    }

    protected void applyDataFormat(Workbook wb, CellStyle style, String format) {
        if (format != null && !format.isEmpty()) {
            style.setDataFormat(wb.createDataFormat().getFormat(format));
        }
    }

    protected XSSFColor toXssfColor(JRgbColor color) {
        return new XSSFColor(color.toBytes(), new DefaultIndexedColorMap());
    }
}
