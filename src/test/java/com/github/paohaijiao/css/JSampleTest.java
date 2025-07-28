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
package com.github.paohaijiao.css;
import com.github.paohaijiao.handler.JExcelCommonHandler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * packageName com.github.paohaijiao.export.formulate
 *
 * @author Martin
 * @version 1.0.0
 * @className JFormulateIntegrationTest
 * @date 2025/7/5
 * @description
 */
public class JSampleTest {

    public static void main(String[] args) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("业务数据报表");
        sheet.setDefaultColumnWidth(18);
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(36);
        String[] headers = {"指标", "Q1", "Q2", "Q3", "Q4", "年度合计", "同比"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(JExcelCommonHandler.buildDefaultHeaderStyle(workbook));
        }
        Object[][] data = {
                {"营业收入", 1250000, 1380000, 1450000, 1620000, "=SUM(B2:E2)", "12.5%"},
                {"营业成本", 750000, 820000, 880000, 950000, "=SUM(B3:E3)", "8.7%"},
                {"毛利率", "=1-C2/F2", "=1-D3/F3", "=1-C4/F4", "=1-C5/F5", "-", "3.2%"},
                {"净利润", 320000, 380000, 420000, 480000, "=SUM(B5:E5)", "15.2%"},
                {"净利润率", "=B5/B2", "=C5/C2", "=D5/D2", "=E5/E2", "=F5/F2", "2.4%"}
        };
        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            row.setHeightInPoints(24);
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = row.createCell(j);

                if (data[i][j] instanceof String) {
                    cell.setCellValue((String) data[i][j]);
                    if (((String)data[i][j]).startsWith("=")) {
                        cell.setCellStyle(JExcelCommonHandler.buildDefaultFormulaCellStyle(workbook));
                    }
                } else if (data[i][j] instanceof Integer) {
                    cell.setCellValue((Integer) data[i][j]);
                }
                if (cell.getCellStyle() == null) {
                    if (i % 2 == 0) {
                        cell.setCellStyle(JExcelCommonHandler.buildDefaultDataOddStyle(workbook));
                    } else {
                        cell.setCellStyle(JExcelCommonHandler.buildDefaultDataOddStyle(workbook));
                    }
                }
            }
        }
        addProfessionalFooter(workbook, sheet, data.length + 2,JExcelCommonHandler.buildDefaultFooterStyle(workbook));
        try (FileOutputStream fileOut = new FileOutputStream("ProfessionalReport.xlsx")) {
            workbook.write(fileOut);
            System.out.println("专业Excel报表已生成!");
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("cmd /c start ProfessionalReport.xlsx");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, CellStyle> createProfessionalStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.put("header", headerStyle);
        CellStyle dataEven = wb.createCellStyle();
        dataEven.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        dataEven.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataEven.setBorderBottom(BorderStyle.THIN);
        dataEven.setBorderTop(BorderStyle.THIN);
        dataEven.setBorderRight(BorderStyle.THIN);
        dataEven.setBorderLeft(BorderStyle.THIN);
        dataEven.setAlignment(HorizontalAlignment.RIGHT);
        dataEven.setVerticalAlignment(VerticalAlignment.CENTER);
        dataEven.setDataFormat(wb.createDataFormat().getFormat("#,##0"));

        styles.put("dataEven", dataEven);
        CellStyle dataOdd = wb.createCellStyle();
        dataOdd.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        dataOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataOdd.setBorderBottom(BorderStyle.THIN);
        dataOdd.setBorderTop(BorderStyle.THIN);
        dataOdd.setBorderRight(BorderStyle.THIN);
        dataOdd.setBorderLeft(BorderStyle.THIN);
        dataOdd.setAlignment(HorizontalAlignment.RIGHT);
        dataOdd.setVerticalAlignment(VerticalAlignment.CENTER);
        dataOdd.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
        styles.put("dataOdd", dataOdd);

        CellStyle growthEven = wb.createCellStyle();
        growthEven.cloneStyleFrom(dataEven);
        growthEven.setAlignment(HorizontalAlignment.CENTER);
        growthEven.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
        Font growthFontEven = wb.createFont();
        growthFontEven.setColor(IndexedColors.DARK_GREEN.getIndex());
        growthEven.setFont(growthFontEven);
        styles.put("growthEven", growthEven);

        // 奇数行增长率样式
        CellStyle growthOdd = wb.createCellStyle();
        growthOdd.cloneStyleFrom(dataOdd);
        growthOdd.setAlignment(HorizontalAlignment.CENTER);
        growthOdd.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
        Font growthFontOdd = wb.createFont();
        growthFontOdd.setColor(IndexedColors.DARK_GREEN.getIndex());
        growthOdd.setFont(growthFontOdd);
        styles.put("growthOdd", growthOdd);

        // 公式单元格样式
        CellStyle formulaCell = wb.createCellStyle();
        //formulaCell.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        formulaCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        formulaCell.setBorderBottom(BorderStyle.THIN);
        formulaCell.setBorderTop(BorderStyle.THIN);
        formulaCell.setBorderRight(BorderStyle.THIN);
        formulaCell.setBorderLeft(BorderStyle.THIN);
        formulaCell.setAlignment(HorizontalAlignment.RIGHT);
        formulaCell.setVerticalAlignment(VerticalAlignment.CENTER);
        Font formulaFont = wb.createFont();
        formulaFont.setItalic(true);
        formulaCell.setFont(formulaFont);
        styles.put("formulaCell", formulaCell);

        // 专业页脚样式
        CellStyle footerStyle = wb.createCellStyle();
        Font footerFont = wb.createFont();
        footerFont.setItalic(true);
        footerFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        footerStyle.setFont(footerFont);
        footerStyle.setAlignment(HorizontalAlignment.LEFT);
        styles.put("footer", footerStyle);

        return styles;
    }

    private static void addProfessionalFooter(Workbook workbook, Sheet sheet, int rowNum, CellStyle style) {
        Row footerRow = sheet.createRow(rowNum);
        Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue("数据来源: 财务部 | 生成时间: " + new java.util.Date() + " | 制表人: [请填写姓名]");
        footerCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 6));
    }
}
