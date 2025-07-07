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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    protected int getUsedColumnCount(Sheet sheet,Row row ) {
        int maxColumns = 0;
        if (row != null) {
            int lastCellNum = row.getLastCellNum();
            if (lastCellNum > maxColumns) {
                maxColumns = lastCellNum;
            }
        }
        return maxColumns;
    }
    protected int getLastRowNum(Sheet sheet) {
        return sheet.getLastRowNum() + 1;
    }
}
