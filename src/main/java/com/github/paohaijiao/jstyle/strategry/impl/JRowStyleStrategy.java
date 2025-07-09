package com.github.paohaijiao.jstyle.strategry.impl;

import com.github.paohaijiao.jstyle.model.JRowStyle;
import com.github.paohaijiao.jstyle.strategry.IStyleStrategy;
import com.github.paohaijiao.util.JStyleHelper;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class JRowStyleStrategy implements IStyleStrategy {
    private int rowNum;

    public JRowStyleStrategy(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public void applyStyle(Workbook workbook, Sheet sheet, Map<String, Object> style) {
        int rowNum = this.rowNum-1>0?this.rowNum-1:0;
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        JRowStyle jRowStyle = transferToJRowStyle(style);
        if (null!=jRowStyle.getHeight()) {//1/height
            String string=jRowStyle.getHeight().toPlainString();
            row.setHeight(Short.parseShort(string));
        }
        if (null!=jRowStyle.getHeightInPoints() ) {
            String string=jRowStyle.getHeightInPoints().toPlainString();
            row.setHeightInPoints(Short.parseShort(string));
        }
        if(null!=jRowStyle.isZeroHeight()){
            row.setZeroHeight(jRowStyle.isZeroHeight());
        }

        Font font = workbook.createFont();
        if(null!=jRowStyle.getFontName()){
            font.setFontName(jRowStyle.getFontName());
        }
        if(null!=jRowStyle.getFontHeightInPoints()){
            BigDecimal string=jRowStyle.getFontHeightInPoints();
            font.setFontHeightInPoints(Short.parseShort(string.toString()));
        }
        if(null!=jRowStyle.isBold()){
            font.setBold(jRowStyle.isBold());
        }
        if(null!=jRowStyle.isItalic()){
            font.setItalic(jRowStyle.isItalic());
        }
        if(null!=jRowStyle.getUnderLine()){
            font.setUnderline(Font.U_SINGLE);
        }
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell=row.getCell(i);
            CellStyle cellStyle=cell.getCellStyle();
            JStyleHelper.applyCellStyle(cellStyle, jRowStyle.getRowStyle());
            cell.setCellStyle(cellStyle);
        }

    }
}
