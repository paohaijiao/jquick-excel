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
        if (jRowStyle.getRowStyle() != null) {
            CellStyle cellStyle = JStyleHelper.createCellStyle(workbook, jRowStyle.getRowStyle());
            for (Cell cell : row) {
                cell.setCellStyle(cellStyle);
            }
        }
    }
}
