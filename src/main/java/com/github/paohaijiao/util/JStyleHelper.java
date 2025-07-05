package com.github.paohaijiao.util;

import com.github.paohaijiao.jstyle.model.JCellStyle;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;

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
            BigDecimal color=jCellStyle.getBottomBorderColor();
            if(jCellStyle.getBorderBottom()==null){
                cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            }
            cellStyle.setBottomBorderColor(Short.parseShort(color.toPlainString()));
        }
        if (jCellStyle.getLeftBorderColor() != null) {
            BigDecimal color=jCellStyle.getLeftBorderColor();
            if(jCellStyle.getBorderLeft()==null){
                cellStyle.setBorderLeft(BorderStyle.THICK);
            }
            cellStyle.setLeftBorderColor(Short.parseShort(color.toPlainString()));
        }
        if (jCellStyle.getRightBorderColor()!= null) {
            BigDecimal color=jCellStyle.getRightBorderColor();
            if(jCellStyle.getBorderRight()==null){
                cellStyle.setBorderRight(BorderStyle.THIN);
            }
            cellStyle.setRightBorderColor(Short.parseShort(color.toPlainString()));
        }
        if (jCellStyle.getTopBorderColor() != null) {
            BigDecimal color=jCellStyle.getTopBorderColor();
            if(jCellStyle.getBorderTop()==null){
                cellStyle.setBorderTop(BorderStyle.THICK);
            }
            cellStyle.setTopBorderColor(Short.parseShort(color.toPlainString()));
        }
        if (jCellStyle.getFillForegroundColor() != null) {
            BigDecimal color=jCellStyle.getFillForegroundColor();;
            if(jCellStyle.getFillPattern()==null){
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            cellStyle.setFillForegroundColor(Short.parseShort(color.toPlainString()));
        }
        if (jCellStyle.getFillBackgroundColor() != null) {
            BigDecimal color=jCellStyle.getFillBackgroundColor();;
            if(jCellStyle.getFillPattern()==null){
                cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
            }
            cellStyle.setFillBackgroundColor(Short.parseShort(color.toPlainString()));
        }
        return cellStyle;
    }
}
