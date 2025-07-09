package com.github.paohaijiao.handler;

import com.github.paohaijiao.evalue.JEvaluator;
import com.github.paohaijiao.model.JMethodCallModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class JExcelCommonHandler {

    protected Workbook workbook;

    protected Sheet currentSheet;

    protected JContext contextParams = new JContext();

    protected Object applyTransform(String key, Object value, String transform) {
        this.contextParams.put(key, value);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(transform));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.transformValue();
        List<Map<String, Object>> data = new ArrayList<>();
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(this.contextParams, data);
        @SuppressWarnings("unchecked")
        JMethodCallModel methodCallModel = (JMethodCallModel) visitor.visit(tree);
        List<Object> list = methodCallModel.getList();
        Object object = JEvaluator.evaluateFunction(methodCallModel.getMethod().getMethod(), list);
        return object;
    }

    protected void setSheet(Object sheetConfig) {
        if (sheetConfig == null) {
            currentSheet = workbook.getSheetAt(0);
        } else if (sheetConfig instanceof Integer) {
            currentSheet = workbook.getSheetAt((Integer) sheetConfig - 1);
        } else if (sheetConfig instanceof String) {
            currentSheet = workbook.getSheet((String) sheetConfig);
            if (currentSheet == null) {
                currentSheet = workbook.createSheet((String) sheetConfig);
            }
        }
    }
    protected int getUsedColumnCount(Sheet sheet) {
        if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
            return 0;
        }
        Set<Integer> usedColumns = new HashSet<>();
        for (Row row : sheet) {
            if (row != null) {
                for (Cell cell : row) {
                    if (cell != null && !isCellEmpty(cell)) {
                        usedColumns.add(cell.getColumnIndex());
                    }
                }
            }
        }
        return usedColumns.size();
    }
    private static boolean isCellEmpty(Cell cell) {
        if (cell == null) {
            return true;
        }
        switch (cell.getCellType()) {
            case BLANK:
                return true;
            case STRING:
                return cell.getStringCellValue().trim().isEmpty();
            default:
                return false;
        }
    }
    protected int getLastRowNum(Sheet sheet) {
        return sheet.getLastRowNum() + 1;
    }
    public static String getCellValueStringByIndex(Sheet sheet, int rowIndex, int columnIndex) {
        Object object = getCellValueByIndex(sheet, rowIndex, columnIndex);
        if (object == null) {
            return null;
        }else{
            return object.toString();
        }
    }
    public static Object getCellValueByIndex(Sheet sheet, int rowIndex, int columnIndex) {
        if (sheet == null) {
            return null;
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        return getCellValue(cell);
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return null;
        }
    }
    public static CellStyle buildDefaultHeaderStyle(Workbook wb){
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
        return headerStyle;
    }

    public static CellStyle buildDefaultDataEvenStyle(Workbook wb){
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
        return dataEven;
    }
    public static CellStyle buildDefaultDataOddStyle(Workbook wb){
        CellStyle dataOdd = wb.createCellStyle();
        dataOdd.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        dataOdd.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataOdd.setBorderBottom(BorderStyle.THIN);
        dataOdd.setBorderTop(BorderStyle.THIN);
        dataOdd.setBorderRight(BorderStyle.THIN);
        dataOdd.setBorderLeft(BorderStyle.THIN);
        dataOdd.setAlignment(HorizontalAlignment.RIGHT);
        dataOdd.setVerticalAlignment(VerticalAlignment.CENTER);
       // dataOdd.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
        return dataOdd;
    }

    public static CellStyle buildDefaultFooterStyle(Workbook wb){
        CellStyle footerStyle = wb.createCellStyle();
        Font footerFont = wb.createFont();
        footerFont.setItalic(true);
        footerFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        footerStyle.setFont(footerFont);
        footerStyle.setAlignment(HorizontalAlignment.LEFT);
        footerStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        return footerStyle;
    }

    public static CellStyle buildDefaultFormulaCellStyle(Workbook wb){
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
        return formulaCell;
    }
    public static void buildDefaultFooter(Workbook workbook, Sheet sheet, int rowNum,int maxColumn, String label) {
        CellStyle cellStyle= buildDefaultFormulaCellStyle(workbook);
        Row footerRow = sheet.createRow(rowNum);
        Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue(label);
        footerCell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, maxColumn));
    }
}
