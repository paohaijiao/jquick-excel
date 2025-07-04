package com.github.paohaijiao.jstyle.strategry.impl;

import com.github.paohaijiao.jstyle.model.JRowStyle;
import com.github.paohaijiao.jstyle.strategry.IStyleStrategy;
import com.github.paohaijiao.util.JStyleHelper;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class JRowStyleStrategy implements IStyleStrategy {
    private int rowNum;

    public JRowStyleStrategy(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public void applyStyle(Workbook workbook, Sheet sheet, Map<String, Object> style) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        JRowStyle jRowStyle = transferToJRowStyle(style);
        if (jRowStyle.getHeight() > 0) {
            row.setHeight(jRowStyle.getHeight());
        }
        if (jRowStyle.getHeightInPoints() > 0) {
            row.setHeightInPoints(jRowStyle.getHeightInPoints());
        }
        row.setZeroHeight(jRowStyle.isZeroHeight());
        if (jRowStyle.getRowStyle() != null) {
            CellStyle cellStyle = JStyleHelper.createCellStyle(workbook, jRowStyle.getRowStyle());
            for (Cell cell : row) {
                cell.setCellStyle(cellStyle);
            }
        }
    }
}
