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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
}
