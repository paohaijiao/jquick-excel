package com.paohaijiao.javelin.generate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
public class TemplateGenerator {
    public static void main(String[] args) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("学生信息");
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("学生信息表");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0).setCellValue("导出日期：");
            dateRow.createCell(1).setCellValue("2023-11-15");
            Row headerRow = sheet.createRow(3);
            String[] headers = {"学号", "姓名", "性别", "年龄", "入学日期", "班级"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCell.setCellStyle(titleStyle);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            for (Cell cell : headerRow) {
                cell.setCellStyle(headerStyle);
            }

            // 设置列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.setColumnWidth(i, 15 * 256);
            }

            // 保存模板文件
            try (FileOutputStream out = new FileOutputStream("D:\\idea\\jthornrule\\jthornrule-quickExcel\\src\\main\\resources\\templates\\student_template.xlsx")) {
                workbook.write(out);
            }

            System.out.println("模板文件创建成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
