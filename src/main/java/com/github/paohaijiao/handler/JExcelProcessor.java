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
package com.github.paohaijiao.handler;

import cn.hutool.core.util.NumberUtil;
import com.github.paohaijiao.enums.JMergeType;
import com.github.paohaijiao.evalue.JEvaluator;
import com.github.paohaijiao.formula.JAbstractExcelFormula;
import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import com.github.paohaijiao.jstyle.context.JStyleContext;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.merge.context.JMergeHandlerContext;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JExcelImportModel;
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
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class JExcelProcessor {
    private Workbook workbook;
    private Sheet currentSheet;
    private DataFormatter dataFormatter = new DataFormatter();

    private JContext contextParams = new JContext();

    public JExcelProcessor() {
        this.contextParams = new JContext();
    }

    public JExcelProcessor(JContext contextParams) {
        this.contextParams = contextParams;
    }

    public List<Map<String, Object>> importData(JExcelImportModel config) throws IOException {
            workbook = new XSSFWorkbook();//fis;
            setSheet(config.getSheet());
            String range = config.getRange();
            int[] rangeBounds = parseRange(range);
            boolean hasHeader = config.getHeader();
            List<String> headers = new ArrayList<>();
            Map<String, String> mappings = config.getMappings();
            List<Map<String, Object>> data = new ArrayList<>();
            int startRow = rangeBounds != null ? rangeBounds[0] : (hasHeader ? 1 : 0);
            int endRow = rangeBounds != null ? rangeBounds[1] : currentSheet.getLastRowNum();
            int startCol = rangeBounds != null ? rangeBounds[2] : 0;
            int endCol = rangeBounds != null ? rangeBounds[3] : getMaxColumnCount();
            if (hasHeader) {
                Row headerRow = currentSheet.getRow(rangeBounds != null ? rangeBounds[0] : 0);
                for (int i = startCol; i <= endCol; i++) {
                    Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String headerName = dataFormatter.formatCellValue(cell);
                    headers.add(mappings.getOrDefault(headerName, headerName));
                }
            } else {
                for (int i = startCol; i <= endCol; i++) {
                    headers.add("column_" + (i + 1));
                }
            }
            for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
                Row row = currentSheet.getRow(rowNum);
                if (row == null) continue;
                Map<String, Object> rowData = new LinkedHashMap<>();
                for (int colNum = startCol; colNum <= endCol; colNum++) {
                    if (colNum >= headers.size()) break;
                    Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Object value = dataFormatter.formatCellValue(cell);
                    Map<String, String> transforms = config.getTransforms();
                    String fieldName = headers.get(colNum);
                    if (transforms.containsKey(fieldName)) {
                        value = applyTransform(fieldName, value, transforms.get(fieldName));
                    }
                    rowData.put(headers.get(colNum), value);
                }
                data.add(rowData);
            }
            return data;

    }

    public void exportData(List<Map<String, Object>> data, JExcelExportModel config) throws IOException {
        workbook = new XSSFWorkbook();
        Object sheet = config.getSheet();
        if (null != sheet) {
            currentSheet = workbook.createSheet((String) sheet);
        } else {
            currentSheet = workbook.createSheet();
        }
        int lastColNum = 0;
        if (null != data && !data.isEmpty()) {
            lastColNum = data.get(0).size();
        }

        boolean hasHeader = config.getHeader();
        Map<String, String> mappings = config.getMapping();
        Map<String, String> transforms = config.getTransforms();
        int rowNum = 0;
        if (hasHeader && !data.isEmpty()) {
            Row headerRow = currentSheet.createRow(rowNum++);
            int colNum = 0;
            for (String header : data.get(0).keySet()) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(mappings.getOrDefault(header, header));
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                cell.setCellStyle(headerStyle);
            }
        }

        for (Map<String, Object> rowData : data) {
            Row row = currentSheet.createRow(rowNum++);
            int colNum = 0;
            for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                Cell cell = row.createCell(colNum++);
                @SuppressWarnings("unchecked")
                Map<String, String> formats = config.getFormat();
                if (formats.containsKey(entry.getKey())) {
                    applyCellFormat(cell, formats.get(entry.getKey()));
                }
                if (transforms.containsKey(entry.getKey())) {
                    Object value = applyTransform(entry.getKey(), entry.getValue(), transforms.get(entry.getKey()));
                    setCellValue(cell, value);
                } else {
                    Object value = entry.getValue() != null ? entry.getValue() : null;
                    setCellValue(cell, value);
                }

            }
        }
        applyFormulate(config, currentSheet.getLastRowNum(), lastColNum);
        applyStyle(config);
        applyMerge(config, currentSheet.getLastRowNum(), lastColNum);
        for (int i = 0; i < data.size(); i++) {
            autoSizeColumns(data.get(i).keySet().size());
        }
        FileOutputStream fos=(FileOutputStream)contextParams.get("fos");
        workbook.write(fos);
    }

    private String getFormulaValue(String formula, int rowNum, int colNum) {
        if (null == formula) {
            return null;
        }
        if (formula.contains("${rowNum}")) {
            String value = formula.replaceAll("\\$\\{rowNum\\}", rowNum + "");
            return value;
        } else if (formula.contains("${colNum}")) {
            String value = formula.replaceAll("\\$\\{colNum\\}", colNum + "");
            return value;
        } else {
            return formula;
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value != null) {
            if (value instanceof Number) {
                if (value instanceof Integer) {
                    Integer i = (Integer) value;
                    cell.setCellValue(i.doubleValue());
                } else if (value instanceof Long) {
                    Long l = (Long) value;
                    cell.setCellValue(l.doubleValue());
                } else if (value instanceof Double) {
                    Double d = (Double) value;
                    cell.setCellValue((Double) d);
                } else if (value instanceof Float) {
                    Float f = (Float) value;
                    cell.setCellValue(f.doubleValue());
                } else if (value instanceof BigDecimal) {
                    BigDecimal bigDecimal = (BigDecimal) value;
                    cell.setCellValue(bigDecimal.doubleValue());
                }
            } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    private void autoSizeColumns(int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            currentSheet.autoSizeColumn(i);
        }
    }

    private void setSheet(Object sheetConfig) {
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

    private int[] parseRange(String range) {
        if (range == null || range.isEmpty()) return null;
        try {
            String[] parts = range.split(":");
            String start = parts[0];
            String end = parts.length > 1 ? parts[1] : start;
            int[] startCoords = parseCellReference(start);
            int[] endCoords = parseCellReference(end);
            return new int[]{
                    startCoords[0], endCoords[0],
                    startCoords[1], endCoords[1]
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid range format: " + range);
        }
    }

    private int[] parseCellReference(String ref) {
        String colPart = ref.replaceAll("[0-9]", "");
        String rowPart = ref.replaceAll("[^0-9]", "");
        int col = 0;
        for (int i = 0; i < colPart.length(); i++) {
            col = col * 26 + (colPart.charAt(i) - 'A' + 1);
        }
        int row = Integer.parseInt(rowPart) - 1;
        return new int[]{row, col - 1};
    }

    private int getMaxColumnCount() {
        int maxCols = 0;
        for (Row row : currentSheet) {
            if (row.getLastCellNum() > maxCols) {
                maxCols = row.getLastCellNum();
            }
        }
        return maxCols;
    }

    private Object applyTransform(String key, Object value, String transform) {
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

    private void applyCellFormat(Cell cell, String formatSpec) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(formatSpec));
        cell.setCellStyle(style);
    }

    private void applyFormulate(JExcelExportModel config, Integer maxRow, Integer maxCol) {
        if (null == config) {
            return;
        }
        if(config.getHeader()){
            maxRow=maxRow+1;
        }
        Map<String, String> cellFormulas = config.getCellFormulas();
        Map<String, String> rowFormulas = config.getRowFormulas();
        Map<String, String> colFormulas = config.getColFormulas();
        for (Map.Entry<String, String> keyset : cellFormulas.entrySet()) {
            String key = keyset.getKey();
            String formulate = keyset.getValue();
            JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
            JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
            factory.applyFormula(currentSheet, key, formula);
        }
        for (Map.Entry<String, String> keyset : rowFormulas.entrySet()) {
            String rowNum = keyset.getKey();
            String formulate = keyset.getValue();
            if(rowNum.contains("..")){
                StringTokenizer tokenizer = new StringTokenizer(rowNum, "..");
                int start = Integer.parseInt(tokenizer.nextToken());
                int end = Integer.parseInt(tokenizer.nextToken());
                for (int i = start; i <= end; i++) {
                    for (int j = 0; j < maxCol; j++) {
                        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                        JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                        int r=i-1>0?i-1:0;
                        factory.applyFormula(currentSheet, r, j, formula);
                    }
                }
            }else{
                for (int i = 0; i < maxCol; i++) {
                    Integer row=Integer.valueOf(rowNum);
                    JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                    JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                    int r=row-1>0?row-1:0;
                    factory.applyFormula(currentSheet, r, i, formula);
                }
            }
        }
        for (Map.Entry<String, String> keyset : colFormulas.entrySet()) {
            String colNum = keyset.getKey();
            String formulate = keyset.getValue();

            if(colNum.contains("..")){
                StringTokenizer tokenizer = new StringTokenizer(colNum, "..");
                String start = tokenizer.nextToken();
                String end = tokenizer.nextToken();
                CellReference startCellReference=new CellReference(start);
                Short startCol=startCellReference.getCol();
                CellReference endCellReference=new CellReference(end);
                Short endCol=endCellReference.getCol();
                for (int i = startCol; i <= endCol; i++) {
                    for (int j = 0; j < maxRow; j++) {
                        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                        JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                        factory.applyFormula(currentSheet, j, i+1, formula);
                    }
                }
            }else{
                for (int i = 0; i < maxRow; i++) {
                    CellReference cellReference=new CellReference(colNum);
                    Short col=cellReference.getCol();
                    JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                    JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                    factory.applyFormula(currentSheet, i, col+1, formula);
                }
            }
        }
    }

    private void applyMerge(JExcelExportModel config, Integer maxRow, Integer maxCol) {
        if (config == null) {
            return; // No merge configurations to apply
        }
        Map<String, Object> rowMerge = config.getRowMerge();
        if (rowMerge != null) {
            for (Map.Entry<String, Object> rowStyle : rowMerge.entrySet()) {
                String key = rowStyle.getKey().trim();
                if (key.contains("..")) {
                    StringTokenizer tokenizer = new StringTokenizer(key, "..");
                    int start = Integer.parseInt(tokenizer.nextToken());
                    int end = Integer.parseInt(tokenizer.nextToken());
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.ROW);
                    for (int i = start; i <= end; i++) {
                        HashMap<String, Object> merge = new HashMap<>();
                        merge.put("rowIndex", i);
                        merge.put("startCol", 0);
                        merge.put("endCol", maxCol);
                        rowHanler.merge(merge);
                    }
                } else {
                    Integer row = Integer.parseInt(rowStyle.getKey());
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.ROW);
                    HashMap<String, Object> merge = new HashMap<>();
                    merge.put("rowIndex", row);
                    merge.put("startCol", 0);
                    merge.put("endCol", maxCol);
                    rowHanler.merge(merge);
                }
            }
        }
        Map<String, Object> colMerge = config.getColMerge();
        if (null != colMerge && !colMerge.isEmpty()) {
            for (Map.Entry<String, Object> colStyle : colMerge.entrySet()) {
                String key = colStyle.getKey().trim();
                if (key.contains("..")) {
                    StringTokenizer tokenizer = new StringTokenizer(key, "..");
                    int start = Integer.parseInt(tokenizer.nextToken());
                    int end = Integer.parseInt(tokenizer.nextToken());
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.COLUMN);
                    for (int i = start; i <= end; i++) {
                        HashMap<String, Object> merge = new HashMap<>();
                        merge.put("columnIndex", i);
                        merge.put("startRow", 0);
                        merge.put("endRow", maxRow);
                        rowHanler.merge(merge);
                    }
                } else {
                    int col = 0;
                    if (NumberUtil.isNumber(key)) {
                        col = Integer.parseInt(key);
                    } else {
                        CellReference cellReference = new CellReference(colStyle.getKey());
                        col = cellReference.getCol();
                    }
                    HashMap<String, Object> merge = new HashMap<>();
                    merge.put("columnIndex", col);
                    merge.put("startRow", 0);
                    merge.put("endRow", maxRow);
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.COLUMN);
                    rowHanler.merge(merge);
                }
            }
        }
        Map<String, Object> rangeMerge = config.getRangeMerge();
        if (null != rangeMerge && !rangeMerge.isEmpty()) {
            for (Map.Entry<String, Object> cellStyle : rangeMerge.entrySet()) {
                String cell = cellStyle.getKey();
                HashMap<String, Object> merge = new HashMap<>();
                CellRangeAddress mergedRegion = CellRangeAddress.valueOf(cell);
                merge.put("firstRow", mergedRegion.getFirstColumn());
                merge.put("lastRow", mergedRegion.getLastRow());
                merge.put("firstCol", mergedRegion.getFirstColumn());
                merge.put("lastCol", mergedRegion.getLastColumn());
                JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                JMergeHandler rowHanler = styleContext.createHandler(JMergeType.COLUMN);
                rowHanler.merge(merge);
            }
        }
    }

    private void applyStyle(JExcelExportModel config) {
        Map<String, Map<String, Object>> rowStyles = config.getRowStyles();
        if (null != rowStyles && !rowStyles.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> rowStyle : rowStyles.entrySet()) {
                String key = rowStyle.getKey().trim();
                if (key.contains("..")) {
                    StringTokenizer tokenizer = new StringTokenizer(key, "..");
                    int start = Integer.parseInt(tokenizer.nextToken());
                    int end = Integer.parseInt(tokenizer.nextToken());
                    for (int i = start; i <= end; i++) {
                        Map<String, Object> cssStyle = rowStyle.getValue();
                        JStyleContext styleContext = JStyleContext.forRow(workbook, currentSheet, i);
                        styleContext.applyStyle(cssStyle);
                    }
                } else {
                    Integer row = Integer.parseInt(rowStyle.getKey());
                    Map<String, Object> cssStyle = rowStyle.getValue();
                    JStyleContext styleContext = JStyleContext.forRow(workbook, currentSheet, row);
                    styleContext.applyStyle(cssStyle);
                }
            }
        }
        Map<String, Map<String, Object>> colStyles = config.getColStyles();
        if (null != colStyles && !colStyles.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> colStyle : colStyles.entrySet()) {
                String key = colStyle.getKey().trim();
                if (key.contains("..")) {
                    StringTokenizer tokenizer = new StringTokenizer(key, "..");
                    int start = Integer.parseInt(tokenizer.nextToken());
                    int end = Integer.parseInt(tokenizer.nextToken());
                    for (int i = start; i <= end; i++) {
                        Map<String, Object> cssStyle = colStyle.getValue();
                        JStyleContext styleContext = JStyleContext.forColumn(workbook, currentSheet, i);
                        styleContext.applyStyle(cssStyle);
                    }
                } else {
                    int col = 0;
                    if (NumberUtil.isNumber(key)) {
                        col = Integer.parseInt(key);
                    } else {
                        CellReference cellReference = new CellReference(colStyle.getKey());
                        col = cellReference.getCol();
                    }
                    Map<String, Object> cssStyle = colStyle.getValue();
                    JStyleContext styleContext = JStyleContext.forColumn(workbook, currentSheet, col);
                    styleContext.applyStyle(cssStyle);
                }
            }
        }
        Map<String, Map<String, Object>> cellStyles = config.getCellStyles();
        if (null != cellStyles && !cellStyles.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> cellStyle : cellStyles.entrySet()) {
                String cell = cellStyle.getKey();
                Map<String, Object> cssStyle = cellStyle.getValue();
                JStyleContext styleContext = JStyleContext.forCell(workbook, currentSheet, cell);
                styleContext.applyStyle(cssStyle);
            }
        }
    }
}
