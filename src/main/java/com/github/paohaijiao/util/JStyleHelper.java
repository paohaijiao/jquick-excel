package com.github.paohaijiao.util;

import com.github.paohaijiao.color.JColorEnum;
import com.github.paohaijiao.jstyle.model.JCellStyle;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;

public class JStyleHelper {
    /**
     * @param cellStyle   workbook
     * @param jCellStyle cellStyle
     * @return
     */
    public static CellStyle applyCellStyle(CellStyle cellStyle, JCellStyle jCellStyle) {
        if (jCellStyle.getAlignment() != null) {
            cellStyle.setAlignment(jCellStyle.getAlignment());
        }
        if (jCellStyle.getVerticalAlignment() != null) {
            cellStyle.setVerticalAlignment(jCellStyle.getVerticalAlignment());
        }
        if (jCellStyle.getBorderBottom() != null) {
            cellStyle.setBorderBottom(jCellStyle.getBorderBottom());
        }
        if (jCellStyle.getBorderLeft() != null) {
            cellStyle.setBorderLeft(jCellStyle.getBorderLeft());
        }
        if (jCellStyle.getBorderRight() != null) {
            cellStyle.setBorderRight(jCellStyle.getBorderRight());
        }
        if (jCellStyle.getBorderTop() != null) {
            cellStyle.setBorderTop(jCellStyle.getBorderTop());
        }
        if (jCellStyle.getFillPattern() != null) {
            cellStyle.setFillPattern(jCellStyle.getFillPattern());
        }
        if (jCellStyle.getWrapText() != null) {
            cellStyle.setWrapText(jCellStyle.getWrapText());
        }

        if (jCellStyle.getRotation() != null) {
            String string=jCellStyle.getRotation().toPlainString();
            cellStyle.setRotation(Short.parseShort(string));
        }
        if (jCellStyle.getIndention() != null) {
            String string=jCellStyle.getIndention().toPlainString();
            cellStyle.setIndention(Short.parseShort(string));
        }
        if (jCellStyle.getLocked() != null) {
            cellStyle.setLocked(jCellStyle.getLocked());
        }
        if (jCellStyle.getHidden() != null) {
            cellStyle.setHidden(jCellStyle.getHidden());
        }
        if (jCellStyle.getShrinkToFit() != null) {
            cellStyle.setShrinkToFit(jCellStyle.getShrinkToFit());
        }
        if (jCellStyle.getQuotePrefixed() != null) {
            cellStyle.setQuotePrefixed(jCellStyle.getQuotePrefixed());
        }
        if (jCellStyle.getBottomBorderColor()!= null) {
            String color=jCellStyle.getBottomBorderColor();
            JColorEnum colorEnum=JColorEnum.codeOf(color);
            cellStyle.setBottomBorderColor(colorEnum.getIndexedColor().getIndex());
        }
        if (jCellStyle.getLeftBorderColor() != null) {
            String color=jCellStyle.getLeftBorderColor();
            JColorEnum colorEnum=JColorEnum.codeOf(color);
            cellStyle.setLeftBorderColor(colorEnum.getIndexedColor().getIndex());
        }
        if (jCellStyle.getRightBorderColor()!= null) {
            String color=jCellStyle.getRightBorderColor();
            JColorEnum colorEnum=JColorEnum.codeOf(color);
            cellStyle.setRightBorderColor(colorEnum.getIndexedColor().getIndex());
        }
        if (jCellStyle.getTopBorderColor() != null) {
            String color=jCellStyle.getTopBorderColor();
            JColorEnum colorEnum=JColorEnum.codeOf(color);
            cellStyle.setTopBorderColor(colorEnum.getIndexedColor().getIndex());
        }
        if (jCellStyle.getFillForegroundColor() != null) {
            String color=jCellStyle.getFillForegroundColor();;
            JColorEnum colorEnum=JColorEnum.codeOf(color);
            cellStyle.setFillForegroundColor(colorEnum.getIndexedColor().getIndex());
        }
        if (jCellStyle.getFillBackgroundColor() != null) {
            String color=jCellStyle.getFillBackgroundColor();;
            JColorEnum colorEnum=JColorEnum.codeOf(color);
            cellStyle.setFillBackgroundColor(colorEnum.getIndexedColor().getIndex());
        }
        return cellStyle;
    }
}
