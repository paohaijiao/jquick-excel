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

import com.github.paohaijiao.config.JQuickExcelConfig;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.validate.JAbstractValidationRule;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class JExcelImportHandler extends JExcelCommonHandler {

    /** 兼容旧常量：超过这个行数建议采用分批消费。默认值对齐 JQuickExcelConfig#getImportBatchThreshold() */
    @Deprecated
    public static final int BIG_IMPORT_ROWS_THRESHOLD = JQuickExcelConfig.DEFAULT_IMPORT_BATCH_THRESHOLD;

    /**
     * 通过 XSSFWorkbook 导入（小数据量，兼容原 API）。
     */
    public JExcelImportHandler(XSSFWorkbook xssfWorkbook) {
        this.workbook = xssfWorkbook;
        this.context = new JContext();
    }

    /**
     * 通过 XSSFWorkbook + 上下文导入（小数据量，兼容原 API）。
     */
    public JExcelImportHandler(XSSFWorkbook xssfWorkbook, JContext contextParams) {
        this.workbook = xssfWorkbook;
        this.context = contextParams;
    }

    /**
     * 通过 InputStream 构造（大数据量优化）：当 JQuickExcelConfig#isBigFileImportEnabled()
     * 为 true 时优先采用 {@link OPCPackage#open} 共享 zip 包再构建 XSSFWorkbook，
     * 降低大文件导入时的峰值内存；失败时回退到 {@code new XSSFWorkbook(InputStream)}。
     */
    public JExcelImportHandler(InputStream inputStream) throws IOException {
        this(inputStream, new JContext());
    }

    /**
     * 通过 InputStream + 上下文构造（大数据量优化）。
     */
    public JExcelImportHandler(InputStream inputStream, JContext contextParams) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream required not null");
        }
        JQuickExcelConfig cfg = JQuickExcelConfig.getInstance();
        boolean useOpc = cfg.isBigFileImportEnabled();
        if (useOpc) {
            try {
                OPCPackage pkg = OPCPackage.open(inputStream);
                this.workbook = new XSSFWorkbook(pkg);
                this.context = contextParams == null ? new JContext() : contextParams;
                return;
            } catch (Exception ignored) {
                // 回退到默认方式
            }
        }
        this.workbook = new XSSFWorkbook(inputStream);
        this.context = contextParams == null ? new JContext() : contextParams;
    }

    public List<JQuickRow> importData(JExcelImportModel config) throws IOException {
        setSheet(config.getSheet());
        applyValidate(config);
        boolean hasHeader = config.getHeader();
        List<String> headers = new ArrayList<>();
        Map<String, String> mappings = config.getMappings();
        List<JQuickRow> data = new ArrayList<>();
        int startCol = 0;
        Row headerRow = currentSheet.getRow(0);
        int endCol = getUsedColumnCount(currentSheet);
        if (headerRow != null) {
            for (int i = 0; i <= endCol; i++) {
                Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String headerName = dataFormatter.formatCellValue(cell);
                headers.add(mappings.getOrDefault(headerName, headerName));
            }
        }
        int lastRowNum = this.getLastRowNum(currentSheet);
        int startRow = hasHeader ? 1 : 0;
        for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
            Row row = currentSheet.getRow(rowNum);
            if (row == null) continue;
            JQuickRow rowData = new JQuickRow();
            for (int colNum = startCol; colNum <= endCol; colNum++) {
                if (colNum >= headers.size()) break;
                Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Object value = getCellValue(cell);
                Map<String, String> transforms = config.getTransforms();
                String fieldName = headers.get(colNum);
                if (transforms.containsKey(fieldName)) {
                    value = applyTransform(fieldName, value, transforms.get(fieldName));
                }
                if (StringUtils.isNotEmpty(headers.get(colNum)) && null != value) {
                    rowData.put(headers.get(colNum), value);
                }
            }
            if (!rowData.isEmpty()) {
                data.add(rowData);
            }
        }
        return data;
    }

    /**
     * 分批导入，避免几十万行一次性堆积成大 List 导致 OOM。
     *
     * @param config   导入配置（SHEET / HEADER / MAPPING / TRANSFORM / VALIDATION 全部生效）
     * @param pageSize 每一页的行数
     * @param consumer 分页消费回调；返回 false 可提前终止后续读取
     * @return 总读取行数
     */
    public int importDataInBatch(JExcelImportModel config,
                                 int pageSize,
                                 java.util.function.Predicate<List<JQuickRow>> consumer) throws IOException {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must > 0");
        }
        setSheet(config.getSheet());
        applyValidate(config);
        boolean hasHeader = config.getHeader();
        Map<String, String> mappings = config.getMappings();
        Map<String, String> transforms = config.getTransforms();

        int usedCols = getUsedColumnCount(currentSheet);
        List<String> headers = new ArrayList<>();
        if (hasHeader) {
            Row headerRow = currentSheet.getRow(0);
            if (headerRow != null) {
                for (int c = 0; c <= usedCols; c++) {
                    Cell cell = headerRow.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String rawHeader = dataFormatter.formatCellValue(cell);
                    headers.add(mappings.getOrDefault(rawHeader, rawHeader));
                }
            }
        } else {
            for (int c = 0; c <= usedCols; c++) {
                headers.add(String.valueOf((char) ('A' + c)));
            }
        }

        int lastRowNum = getLastRowNum(currentSheet);
        int startRow = hasHeader ? 1 : 0;
        int totalRead = 0;
        List<JQuickRow> batch = new ArrayList<>(pageSize);
        for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
            Row row = currentSheet.getRow(rowNum);
            if (row == null) continue;
            JQuickRow rowData = new JQuickRow();
            for (int c = 0; c <= usedCols && c < headers.size(); c++) {
                Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Object value = getCellValue(cell);
                String fieldName = headers.get(c);
                if (transforms.containsKey(fieldName)) {
                    value = applyTransform(fieldName, value, transforms.get(fieldName));
                }
                if (StringUtils.isNotEmpty(fieldName) && value != null) {
                    rowData.put(fieldName, value);
                }
            }
            if (!rowData.isEmpty()) {
                batch.add(rowData);
                totalRead++;
            }
            if (batch.size() >= pageSize) {
                boolean proceed = consumer.test(batch);
                batch = new ArrayList<>(pageSize);
                if (!proceed) break;
            }
        }
        if (!batch.isEmpty()) {
            consumer.test(batch);
        }
        return totalRead;
    }


    protected void applyValidate(JExcelImportModel config) {
        int maxCol = this.getUsedColumnCount(this.currentSheet);
        Map<String, List<JAbstractValidationRule>> rowValidate = config.getRowValidate();
        for (Map.Entry<String, List<JAbstractValidationRule>> entry : rowValidate.entrySet()) {
            String rowNum = entry.getKey();
            List<JAbstractValidationRule> rules = entry.getValue();
            if (rowNum.contains("..")) {
                StringTokenizer tokenizer = new StringTokenizer(rowNum, "..");
                int start = Integer.parseInt(tokenizer.nextToken());
                int rowStart = start - 1 > 0 ? start - 1 : 0;
                int end = Integer.parseInt(tokenizer.nextToken());
                int rowEnd = end - 1 > 0 ? end - 1 : 0;
                for (int i = rowStart; i <= rowEnd; i++) {
                    for (int j = 0; j < maxCol; j++) {
                        String cellValue = getCellValueStringByIndex(this.currentSheet, i, j);
                        for (JAbstractValidationRule rule : rules) {
                            rule.test(cellValue);
                        }
                    }
                }
            } else {
                for (int j = 0; j < maxCol; j++) {
                    Integer rowNumInteger = Integer.valueOf(rowNum);
                    int i = rowNumInteger - 1 > 0 ? rowNumInteger - 1 : 0;
                    String cellValue = getCellValueStringByIndex(this.currentSheet, i, j);
                    for (JAbstractValidationRule rule : rules) {
                        boolean bool = rule.test(cellValue);
                        if (!bool) {
                            throw new RuntimeException("not validate data");
                        }
                    }
                }
            }

        }
        Map<String, List<JAbstractValidationRule>> coalValidate = config.getColValidate();
        for (Map.Entry<String, List<JAbstractValidationRule>> keyset : coalValidate.entrySet()) {
            String colNum = keyset.getKey();
            if (StringUtils.isBlank(colNum)) {
                continue;
            }
            List<JAbstractValidationRule> rules = keyset.getValue();
            if (colNum.contains("..")) {
                StringTokenizer tokenizer = new StringTokenizer(colNum, "..");
                String start = tokenizer.nextToken();
                String end = tokenizer.nextToken();
                CellReference startCellReference = new CellReference(start);
                Short startCol = startCellReference.getCol();
                CellReference endCellReference = new CellReference(end);
                Short endCol = endCellReference.getCol();
                for (int i = startCol; i <= endCol; i++) {
                    for (int j = 0; j < this.getLastRowNum(this.currentSheet); j++) {
                        String cellValue = getCellValueStringByIndex(this.currentSheet, i, j);
                        for (JAbstractValidationRule rule : rules) {
                            rule.test(cellValue);
                        }
                    }
                }
            } else {
                for (int i = 0; i < this.getLastRowNum(this.currentSheet); i++) {
                    CellReference cellReference = new CellReference(colNum);
                    Short col = cellReference.getCol();
                    String cellValue = getCellValueStringByIndex(this.currentSheet, i, col.intValue());
                    for (JAbstractValidationRule rule : rules) {
                        rule.test(cellValue);
                    }
                }
            }
        }
        for (Map.Entry<String, List<JAbstractValidationRule>> keyset : config.getCellValidate().entrySet()) {
            String key = keyset.getKey();
            if (StringUtils.isBlank(key)) {
                continue;
            }

            List<JAbstractValidationRule> rules = keyset.getValue();
            CellReference cellReference = new CellReference(key);
            String cellValue = getCellValueStringByIndex(this.currentSheet, cellReference.getRow(), cellReference.getCol());
            for (JAbstractValidationRule rule : rules) {
                rule.test(cellValue);
            }
        }
        for (Map.Entry<String, List<JAbstractValidationRule>> keyset : config.getRangeValidate().entrySet()) {
            String key = keyset.getKey();
            if (StringUtils.isBlank(key)) {
                continue;
            }
            List<JAbstractValidationRule> rules = keyset.getValue();
            CellRangeAddress rangeRegion = CellRangeAddress.valueOf(key);
            int firstRow = rangeRegion.getFirstRow();
            int lastRow = rangeRegion.getLastRow();
            int firstCol = rangeRegion.getFirstColumn();
            int lastCol = rangeRegion.getLastColumn();
            for (int i = firstRow; i <= lastRow; i++) {
                for (int j = firstCol; j <= lastCol; j++) {
                    Cell cell = getCellByIndex(this.currentSheet, i, j);
                    DataFormatter formatter = new DataFormatter();
                    String cellValue = formatter.formatCellValue(cell);
                    for (JAbstractValidationRule rule : rules) {
                        rule.test(cellValue);
                    }
                }
            }
        }
    }

}
