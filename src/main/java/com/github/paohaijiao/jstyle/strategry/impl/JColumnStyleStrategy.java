package com.github.paohaijiao.jstyle.strategry.impl;

import com.github.paohaijiao.jstyle.strategry.IStyleStrategy;
import com.github.paohaijiao.jstyle.model.JCellStyle;
import com.github.paohaijiao.util.JStyleHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.Map;

public class JColumnStyleStrategy extends  JFontBuilder implements IStyleStrategy {

    private int colNum;

    public JColumnStyleStrategy(int colNum) {
        this.colNum = colNum;
    }

    @Override
    public void applyStyle(Workbook workbook, Sheet sheet, Map<String, Object> style) {

        JCellStyle jCellStyle = transferToJCellStyle(style);
        for (Row row : sheet) {
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                cell = row.createCell(colNum);
            }
            CellStyle cellStyle = JStyleHelper.applyCellStyle(cell.getCellStyle(), jCellStyle);
            XSSFCellStyle xssfCellStyle =(XSSFCellStyle) cellStyle;
            XSSFFont font = xssfCellStyle.getFont();
            buildFont(cellStyle,font, jCellStyle);
            cell.setCellStyle(cellStyle);
        }
    }

}
