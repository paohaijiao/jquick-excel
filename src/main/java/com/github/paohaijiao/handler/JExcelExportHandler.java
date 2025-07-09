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
import com.github.paohaijiao.enums.JExcelChartType;
import com.github.paohaijiao.enums.JMergeType;
import com.github.paohaijiao.enums.JMergeValueType;
import com.github.paohaijiao.evalue.JEvaluator;
import com.github.paohaijiao.factory.JExcelChartFactory;
import com.github.paohaijiao.formula.JAbstractExcelFormula;
import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import com.github.paohaijiao.jstyle.context.JStyleContext;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.merge.context.JMergeHandlerContext;
import com.github.paohaijiao.model.JExcelExportModel;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class JExcelExportHandler extends JExcelCommonHandler{

    private DataFormatter dataFormatter = new DataFormatter();

    private JExcelExportModel config=null;



    public JExcelExportHandler(JExcelExportModel config,List<Map<String, Object>> data) {
        this.config = config;
        try {
            this.exportData(config,data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void exportData(JExcelExportModel config,List<Map<String, Object>> data) throws IOException {
        workbook = new XSSFWorkbook();
        Object sheet = config.getSheet();
        if (null != sheet) {
            currentSheet = workbook.createSheet((String) sheet);
        } else {
            currentSheet = workbook.createSheet();
        }
        currentSheet.setDefaultColumnWidth(18);
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
            headerRow.setHeightInPoints(36);
            int colNum = 0;
            for (String header : data.get(0).keySet()) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(mappings.getOrDefault(header, header));
                CellStyle cellStyle=buildDefaultHeaderStyle(workbook);
                cell.setCellStyle(cellStyle);
            }
        }

        for (Map<String, Object> rowData : data) {
            Row row = currentSheet.createRow(rowNum++);
            row.setHeightInPoints(24);
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
                CellStyle cellStyle=buildDefaultDataOddStyle(workbook);
                cell.setCellStyle(cellStyle);
            }
        }
        applyFormulate(config, currentSheet.getLastRowNum(), lastColNum);
        applyStyle(config);
        applyMerge(config, currentSheet.getLastRowNum(), lastColNum);
        applyGraph(config);
        if(config.getFooter()!=null){
            buildDefaultFooter(workbook,currentSheet,this.getLastRowNum(currentSheet)-1,
                    this.getUsedColumnCount(currentSheet)-1,config.getFooter());
        }
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
                JMergeValueType value = (JMergeValueType)rowStyle.getValue();
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
                        merge.put("endCol", maxCol-1);
                        merge.put("mergeType", value);
                        rowHanler.merge(merge);
                    }
                } else {
                    Integer row = Integer.parseInt(rowStyle.getKey());
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.ROW);
                    HashMap<String, Object> merge = new HashMap<>();
                    merge.put("rowIndex", row);
                    merge.put("startCol", 0);
                    merge.put("endCol", maxCol-1);
                    merge.put("mergeType", value);
                    rowHanler.merge(merge);
                }
            }
        }
        Map<String, Object> colMerge = config.getColMerge();
        if (null != colMerge && !colMerge.isEmpty()) {
            for (Map.Entry<String, Object> colMergeMap : colMerge.entrySet()) {
                String key = colMergeMap.getKey().trim();
                JMergeValueType value = (JMergeValueType)colMergeMap.getValue();
                if (key.contains("..")) {
                    StringTokenizer tokenizer = new StringTokenizer(key, "..");
                    String start = tokenizer.nextToken();
                    String end = tokenizer.nextToken();
                    CellReference startCellReference=new CellReference(start);
                    Short startCol=startCellReference.getCol();
                    CellReference endCellReference=new CellReference(end);
                    Short endCol=endCellReference.getCol();
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.COLUMN);
                    for (int i = startCol; i <=endCol; i++) {
                        HashMap<String, Object> merge = new HashMap<>();
                        merge.put("columnIndex", i);
                        merge.put("startRow", 0);
                        merge.put("endRow", maxRow);
                        merge.put("mergeType", value);
                        rowHanler.merge(merge);
                    }
                } else {
                    int col = 0;
                    if (NumberUtil.isNumber(key)) {
                        col = Integer.parseInt(key);
                    } else {
                        CellReference cellReference = new CellReference(colMergeMap.getKey());
                        col = cellReference.getCol();
                    }
                    HashMap<String, Object> merge = new HashMap<>();
                    merge.put("columnIndex", col);
                    merge.put("startRow", 0);
                    merge.put("endRow", maxRow);
                    merge.put("mergeType", value);
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
                JMergeValueType value = (JMergeValueType)cellStyle.getValue();
                HashMap<String, Object> merge = new HashMap<>();
                CellRangeAddress mergedRegion = CellRangeAddress.valueOf(cell);
                merge.put("firstRow", mergedRegion.getFirstColumn());
                merge.put("lastRow", mergedRegion.getLastRow());
                merge.put("firstCol", mergedRegion.getFirstColumn());
                merge.put("lastCol", mergedRegion.getLastColumn());
                merge.put("mergeType", value);
                JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet);
                JMergeHandler rowHanler = styleContext.createHandler(JMergeType.RANGE);
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
                        JStyleContext styleContext = new JStyleContext(workbook,currentSheet).forRow( i);
                        styleContext.applyStyle(cssStyle);
                    }
                } else {
                    Integer row = Integer.parseInt(rowStyle.getKey());
                    Map<String, Object> cssStyle = rowStyle.getValue();
                    JStyleContext styleContext = new JStyleContext(workbook,currentSheet).forRow(row);
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
                    String start = tokenizer.nextToken();
                    String end = tokenizer.nextToken();
                    CellReference startCellReference=new CellReference(start);
                    Short startCol=startCellReference.getCol();
                    CellReference endCellReference=new CellReference(end);
                    Short endCol=endCellReference.getCol();
                    for (int i = startCol; i <= endCol; i++) {
                        Map<String, Object> cssStyle = colStyle.getValue();
                        JStyleContext styleContext = new JStyleContext(workbook,currentSheet).forColumn( i);
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
                    JStyleContext styleContext = new JStyleContext(workbook,currentSheet).forColumn( col);
                    styleContext.applyStyle(cssStyle);
                }
            }
        }
        Map<String, Map<String, Object>> cellStyles = config.getCellStyles();
        if (null != cellStyles && !cellStyles.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> cellStyle : cellStyles.entrySet()) {
                String cell = cellStyle.getKey();
                Map<String, Object> cssStyle = cellStyle.getValue();
                JStyleContext styleContext = new JStyleContext(workbook,currentSheet).forCell( cell);
                styleContext.applyStyle(cssStyle);
            }
        }
    }
    private void applyGraph(JExcelExportModel config) {
        if(config.getGraph() != null&&0!=config.getGraph().size()) {
            JExcelChartType excelChartType=JExcelChartType.codeOf( config.getGraph().getChartType());
            JExcelChartFactory.createChart((XSSFWorkbook)workbook, config.getGraph(), excelChartType, config.getGraph().getTitle());
        }
    }

    public Workbook getWorkBook(){
        return workbook;
    }
}
