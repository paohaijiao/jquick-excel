package com.github.paohaijiao.jstyle.strategry.impl;

import com.github.paohaijiao.jstyle.strategry.IStyleStrategy;
import com.github.paohaijiao.jstyle.model.JCellStyle;
import com.github.paohaijiao.util.JStyleHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.util.HashMap;
import java.util.Map;

public class JCellStyleStrategy implements IStyleStrategy {

    private final String cellRef;

    public JCellStyleStrategy(String cellRef) {
        this.cellRef = cellRef;
    }

    public JCellStyleStrategy(int rowNum, int colNum) {
        this.cellRef = new CellReference(rowNum, colNum).formatAsString();
    }

    @Override
    public void applyStyle(Workbook workbook, Sheet sheet, Map<String, Object> style) {
        CellReference reference = new CellReference(cellRef);
        int rowNum = reference.getRow();
        int colNum = reference.getCol();
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        JCellStyle jCellStyle = transferToJCellStyle(style);
        CellStyle cellStyle = JStyleHelper.applyCellStyle(cell.getCellStyle(), jCellStyle);
        cell.setCellStyle(cellStyle);
    }
}
