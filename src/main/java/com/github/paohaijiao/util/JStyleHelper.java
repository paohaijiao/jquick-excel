package com.github.paohaijiao.util;

import com.github.paohaijiao.jstyle.model.JCellStyle;
import org.apache.poi.ss.usermodel.*;

public class JStyleHelper {
    /**
     * @param workbook   workbook
     * @param jCellStyle cellStyle
     * @return
     */
    public static CellStyle createCellStyle(Workbook workbook, JCellStyle jCellStyle) {

        CellStyle cellStyle = workbook.createCellStyle();

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
            cellStyle.setRotation(jCellStyle.getRotation());
        }
        if (jCellStyle.getIndention() != null) {
            cellStyle.setIndention(jCellStyle.getIndention());
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
        if (jCellStyle.getBottomBorderColor() > 0) {
            cellStyle.setBottomBorderColor(jCellStyle.getBottomBorderColor());
        }
        if (jCellStyle.getLeftBorderColor() > 0) {
            cellStyle.setLeftBorderColor(jCellStyle.getLeftBorderColor());
        }
        if (jCellStyle.getRightBorderColor() > 0) {
            cellStyle.setRightBorderColor(jCellStyle.getRightBorderColor());
        }
        if (jCellStyle.getTopBorderColor() > 0) {
            cellStyle.setTopBorderColor(jCellStyle.getTopBorderColor());
        }
        if (jCellStyle.getFillForegroundColor() > 0) {
            cellStyle.setFillForegroundColor(jCellStyle.getFillForegroundColor());
        }
        if (jCellStyle.getFillBackgroundColor() > 0) {
            cellStyle.setFillBackgroundColor(jCellStyle.getFillBackgroundColor());
        }
        return cellStyle;
    }
}
