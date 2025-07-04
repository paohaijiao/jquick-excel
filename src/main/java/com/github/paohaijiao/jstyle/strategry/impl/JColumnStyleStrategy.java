package com.github.paohaijiao.jstyle.strategry.impl;

import com.github.paohaijiao.jstyle.model.JRowStyle;
import com.github.paohaijiao.jstyle.strategry.IStyleStrategy;
import com.github.paohaijiao.jstyle.model.JCellStyle;
import com.github.paohaijiao.util.JStyleHelper;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class JColumnStyleStrategy implements IStyleStrategy {

    private int colNum;

    public JColumnStyleStrategy(int colNum) {
        this.colNum = colNum;
    }

    @Override
    public void applyStyle(Workbook workbook, Sheet sheet, Map<String, Object> style) {

        JCellStyle jCellStyle = transferToJCellStyle(style);
        CellStyle cellStyle = JStyleHelper.createCellStyle(workbook, jCellStyle);
        for (Row row : sheet) {
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }
            cell.setCellStyle(cellStyle);
        }
    }

}
