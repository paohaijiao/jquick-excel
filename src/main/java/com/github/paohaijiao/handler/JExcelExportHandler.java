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
import com.github.paohaijiao.config.JQuickExcelConfig;
import com.github.paohaijiao.enums.JExcelChartType;
import com.github.paohaijiao.enums.JMergeType;
import com.github.paohaijiao.enums.JMergeValueType;
import com.github.paohaijiao.factory.JExcelChartFactory;
import com.github.paohaijiao.formula.JAbstractExcelFormula;
import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import com.github.paohaijiao.jstyle.context.JStyleContext;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.merge.context.JMergeHandlerContext;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.theme.JExcelTheme;
import com.github.paohaijiao.theme.factory.JExcelThemeFactory;
import com.github.paohaijiao.util.JCellStyleCache;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class JExcelExportHandler extends JExcelCommonHandler {

    private JExcelExportModel config = new JExcelExportModel();

    /**
     * 是否偏好流式写入：true 时，当数据量 ≥ {@link JQuickExcelConfig#getStreamingExportThreshold()}
     * 会自动升级为 SXSSFWorkbook 流式写入，降低 OOM 风险。默认跟随全局配置。
     */
    private boolean preferStreaming;

    /**
     * 流式窗口（SXSSF rowAccessWindowSize），<=0 表示采用全局默认。
     */
    private int streamingWindowSize;

    public JExcelExportHandler(JExcelExportModel config, JContext context, List<JQuickRow> data) {
        this.config = config;
        this.context = context;
        this.preferStreaming = JQuickExcelConfig.getInstance().isStreamingExportEnabled();
        this.streamingWindowSize = JQuickExcelConfig.getInstance().getStreamingRowAccessWindowSize();
        try {
            this.exportData(config, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JExcelExportHandler(JExcelExportModel config, List<JQuickRow> data) {
        this.config = config;
        this.context = new JContext();
        this.preferStreaming = JQuickExcelConfig.getInstance().isStreamingExportEnabled();
        this.streamingWindowSize = JQuickExcelConfig.getInstance().getStreamingRowAccessWindowSize();
        try {
            this.exportData(config, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPreferStreaming() {
        return preferStreaming;
    }

    public JExcelExportHandler setPreferStreaming(boolean preferStreaming) {
        this.preferStreaming = preferStreaming;
        return this;
    }

    public int getStreamingWindowSize() {
        return streamingWindowSize;
    }

    public JExcelExportHandler setStreamingWindowSize(int streamingWindowSize) {
        this.streamingWindowSize = streamingWindowSize;
        return this;
    }

    /**
     * 清理 SXSSF 临时磁盘文件。若导出时采用流式，写入结束后建议调用本方法。
     */
    public void dispose() {
        if (this.workbook instanceof SXSSFWorkbook) {
            ((SXSSFWorkbook) this.workbook).dispose();
        }
    }

    public void exportData(JExcelExportModel config, List<JQuickRow> data) throws IOException {
        int size = data == null ? 0 : data.size();
        JQuickExcelConfig global = JQuickExcelConfig.getInstance();
        boolean useStreaming = preferStreaming && global.shouldUseStreaming(size);
        boolean forceDisableStreaming = needsRandomRowAccess(config);
        if (useStreaming && forceDisableStreaming) {
            useStreaming = false;
        }

        Workbook nativeWorkbook;
        if (useStreaming) {
            int window = streamingWindowSize > 0
                    ? streamingWindowSize
                    : global.getStreamingRowAccessWindowSize();
            SXSSFWorkbook sxssf = new SXSSFWorkbook(new XSSFWorkbook(), window);
            sxssf.setCompressTempFiles(global.isStreamingCompressTempFiles());
            nativeWorkbook = sxssf;
        } else {
            nativeWorkbook = new XSSFWorkbook();
        }
        this.workbook = nativeWorkbook;

        Object sheetObj = config.getSheet();
        if (null != sheetObj) {
            currentSheet = workbook.createSheet((String) sheetObj);
        } else {
            currentSheet = workbook.createSheet();
        }
        currentSheet.setDefaultColumnWidth(18);
        if (currentSheet instanceof SXSSFSheet) {
            ((SXSSFSheet) currentSheet).trackAllColumnsForAutoSizing();
        }

        int lastColNum = 0;
        if (null != data && !data.isEmpty()) {
            lastColNum = data.get(0).size();
        }
        boolean hasHeader = config.getHeader();
        Map<String, String> mappings = config.getMapping();
        Map<String, String> transforms = config.getTransforms();
        JExcelTheme theme = resolveTheme(config);
        int rowNum = 0;
        if (hasHeader && !data.isEmpty()) {
            Row headerRow = currentSheet.createRow(rowNum++);
            headerRow.setHeightInPoints(36);
            final CellStyle headerCellStyle = (theme != null)
                    ? JCellStyleCache.getThemeStyle(workbook, theme, JCellStyleCache.KIND_HEADER)
                    : JCellStyleCache.getHeaderStyle(workbook);
            int colNum = 0;
            for (String header : data.get(0).keySet()) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(mappings.getOrDefault(header, header));
                cell.setCellStyle(headerCellStyle);
            }
        }

        @SuppressWarnings("unchecked")
        final Map<String, String> formats = config.getFormat();
        int dataRowIndex = 0;
        for (Map<String, Object> rowData : data) {
            Row row = currentSheet.createRow(rowNum++);
            row.setHeightInPoints(24);
            int colNum = 0;
            final boolean isOdd = (dataRowIndex & 1) == 1; // 第 1 条数据行是偶数行(dataRowIndex=0) → 用 EVEN 样式
            for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                Cell cell = row.createCell(colNum++);
                if (transforms.containsKey(entry.getKey())) {
                    Object value = applyTransform(entry.getKey(), entry.getValue(), transforms.get(entry.getKey()));
                    setCellValue(cell, value);
                } else {
                    Object value = entry.getValue() != null ? entry.getValue() : null;
                    setCellValue(cell, value);
                }
                String fmt = formats == null ? null : formats.get(entry.getKey());
                CellStyle cellStyle = JCellStyleCache.getThemedDataWithFormat(workbook, theme, isOdd, fmt);
                cell.setCellStyle(cellStyle);
            }
            dataRowIndex++;
        }
        applyFormulate(config, currentSheet.getLastRowNum(), lastColNum);
        applyStyle(config);
        applyMerge(config, currentSheet.getLastRowNum(), lastColNum);
        applyGraph(config);
        if (config.getFooter() != null) {
            int footerRowNum = this.getLastRowNum(currentSheet) - 1;
            int footerMaxCol = this.getUsedColumnCount(currentSheet) - 1;
            if (theme != null) {
                CellStyle footerCellStyle = JCellStyleCache.getThemeStyle(workbook, theme, JCellStyleCache.KIND_FORMULA);
                Row footerRow = currentSheet.createRow(footerRowNum);
                Cell footerCell = footerRow.createCell(0);
                footerCell.setCellValue(config.getFooter());
                footerCell.setCellStyle(footerCellStyle);
                currentSheet.addMergedRegion(new CellRangeAddress(footerRowNum, footerRowNum, 0, footerMaxCol));
            } else {
                CellStyle footerCellStyle = JCellStyleCache.getFormulaStyle(workbook);
                Row footerRow = currentSheet.createRow(footerRowNum);
                Cell footerCell = footerRow.createCell(0);
                footerCell.setCellValue(config.getFooter());
                footerCell.setCellStyle(footerCellStyle);
                currentSheet.addMergedRegion(new CellRangeAddress(footerRowNum, footerRowNum, 0, footerMaxCol));
            }
        }
    }

    private JExcelTheme resolveTheme(JExcelExportModel config) {
        String code = config.getTheme();
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return JExcelThemeFactory.create(code);
    }

    /**
     * 按行奇偶性返回缓存复用的基础数据行样式（每 workbook + 每 theme + 每奇偶 只创建 1 个，避免 64000 样式上限）。
     *
     * <p>兼容原命名语义：dataRowIndex=0/2/4… 统一使用『DataOdd』命名的样式，dataRowIndex=1/3/5… 使用『DataEven』命名的样式。
     */
    private CellStyle buildDataStyle(Workbook workbook, JExcelTheme theme, int dataRowIndex) {
        final boolean useOddName = (dataRowIndex & 1) == 0;
        return JCellStyleCache.getThemedDataWithFormat(workbook, theme, useOddName, null);
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

    /**
     * 仅应用"纯 dataFormat 样式"到单元格（该方法会覆盖 cell 已有的边框/背景/字体等样式，绝大多数场景下请改用
     * {@link JCellStyleCache#getThemedDataWithFormat(Workbook, JExcelTheme, boolean, String)} 叠加基础样式+格式）。
     * 内部强制走缓存，保证 workbook 中同一种 formatSpec 对应唯一 CellStyle，避免 64000 上限。
     */
    private void applyCellFormat(Cell cell, String formatSpec) {
        cell.setCellStyle(JCellStyleCache.getFormatStyle(workbook, formatSpec));
    }

    private void applyFormulate(JExcelExportModel config, Integer maxRow, Integer maxCol) {
        if (null == config) {
            return;
        }
        if (config.getHeader()) {
            maxRow = maxRow + 1;
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
            if (rowNum.contains("..")) {
                StringTokenizer tokenizer = new StringTokenizer(rowNum, "..");
                int start = Integer.parseInt(tokenizer.nextToken());
                int end = Integer.parseInt(tokenizer.nextToken());
                for (int i = start; i <= end; i++) {
                    for (int j = 0; j < maxCol; j++) {
                        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                        JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                        int r = i - 1 > 0 ? i - 1 : 0;
                        factory.applyFormula(currentSheet, r, j, formula);
                    }
                }
            } else {
                for (int i = 0; i < maxCol; i++) {
                    Integer row = Integer.valueOf(rowNum);
                    JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                    JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                    int r = row - 1 > 0 ? row - 1 : 0;
                    factory.applyFormula(currentSheet, r, i, formula);
                }
            }
        }
        for (Map.Entry<String, String> keyset : colFormulas.entrySet()) {
            String colNum = keyset.getKey();
            String formulate = keyset.getValue();

            if (colNum.contains("..")) {
                StringTokenizer tokenizer = new StringTokenizer(colNum, "..");
                String start = tokenizer.nextToken();
                String end = tokenizer.nextToken();
                CellReference startCellReference = new CellReference(start);
                Short startCol = startCellReference.getCol();
                CellReference endCellReference = new CellReference(end);
                Short endCol = endCellReference.getCol();
                for (int i = startCol; i <= endCol; i++) {
                    for (int j = 0; j < maxRow; j++) {
                        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                        JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                        factory.applyFormula(currentSheet, j, i + 1, formula);
                    }
                }
            } else {
                for (int i = 0; i < maxRow; i++) {
                    CellReference cellReference = new CellReference(colNum);
                    Short col = cellReference.getCol();
                    JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
                    JAbstractExcelFormula formula = factory.createFormulaInstance(formulate);
                    factory.applyFormula(currentSheet, i, col + 1, formula);
                }
            }
        }
    }

    private void applyMerge(JExcelExportModel config, Integer maxRow, Integer maxCol) {
        if (config == null) {
            return;
        }
        Map<String, Object> rowMerge = config.getRowMerge();
        if (rowMerge != null) {
            for (Map.Entry<String, Object> rowStyle : rowMerge.entrySet()) {
                String key = rowStyle.getKey().trim();
                JMergeValueType value = (JMergeValueType) rowStyle.getValue();
                if (key.contains("..")) {
                    StringTokenizer tokenizer = new StringTokenizer(key, "..");
                    int start = Integer.parseInt(tokenizer.nextToken());
                    int end = Integer.parseInt(tokenizer.nextToken());
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet, context);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.ROW);
                    for (int i = start; i <= end; i++) {
                        HashMap<String, Object> merge = new HashMap<>();
                        merge.put("rowIndex", i);
                        merge.put("startCol", 0);
                        merge.put("endCol", maxCol - 1);
                        merge.put("mergeType", value);
                        rowHanler.merge(merge);
                    }
                } else {
                    Integer row = Integer.parseInt(rowStyle.getKey());
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet, context);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.ROW);
                    HashMap<String, Object> merge = new HashMap<>();
                    merge.put("rowIndex", row);
                    merge.put("startCol", 0);
                    merge.put("endCol", maxCol - 1);
                    merge.put("mergeType", value);
                    rowHanler.merge(merge);
                }
            }
        }
        Map<String, Object> colMerge = config.getColMerge();
        if (null != colMerge && !colMerge.isEmpty()) {
            for (Map.Entry<String, Object> colMergeMap : colMerge.entrySet()) {
                String key = colMergeMap.getKey().trim();
                JMergeValueType value = (JMergeValueType) colMergeMap.getValue();
                if (key.contains("..")) {
                    StringTokenizer tokenizer = new StringTokenizer(key, "..");
                    String start = tokenizer.nextToken();
                    String end = tokenizer.nextToken();
                    CellReference startCellReference = new CellReference(start);
                    Short startCol = startCellReference.getCol();
                    CellReference endCellReference = new CellReference(end);
                    Short endCol = endCellReference.getCol();
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet, context);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.COLUMN);
                    for (int i = startCol; i <= endCol; i++) {
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
                    JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet, context);
                    JMergeHandler rowHanler = styleContext.createHandler(JMergeType.COLUMN);
                    rowHanler.merge(merge);
                }
            }
        }
        Map<String, Object> rangeMerge = config.getRangeMerge();
        if (null != rangeMerge && !rangeMerge.isEmpty()) {
            for (Map.Entry<String, Object> cellStyle : rangeMerge.entrySet()) {
                String cell = cellStyle.getKey();
                JMergeValueType value = (JMergeValueType) cellStyle.getValue();
                HashMap<String, Object> merge = new HashMap<>();
                CellRangeAddress mergedRegion = CellRangeAddress.valueOf(cell);
                merge.put("firstRow", mergedRegion.getFirstColumn());
                merge.put("lastRow", mergedRegion.getLastRow());
                merge.put("firstCol", mergedRegion.getFirstColumn());
                merge.put("lastCol", mergedRegion.getLastColumn());
                merge.put("mergeType", value);
                JMergeHandlerContext styleContext = new JMergeHandlerContext(workbook, currentSheet, context);
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
                        JStyleContext styleContext = new JStyleContext(workbook, currentSheet).forRow(i);
                        styleContext.applyStyle(cssStyle);
                    }
                } else {
                    Integer row = Integer.parseInt(rowStyle.getKey());
                    Map<String, Object> cssStyle = rowStyle.getValue();
                    JStyleContext styleContext = new JStyleContext(workbook, currentSheet).forRow(row);
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
                    CellReference startCellReference = new CellReference(start);
                    Short startCol = startCellReference.getCol();
                    CellReference endCellReference = new CellReference(end);
                    Short endCol = endCellReference.getCol();
                    for (int i = startCol; i <= endCol; i++) {
                        Map<String, Object> cssStyle = colStyle.getValue();
                        JStyleContext styleContext = new JStyleContext(workbook, currentSheet).forColumn(i);
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
                    JStyleContext styleContext = new JStyleContext(workbook, currentSheet).forColumn(col);
                    styleContext.applyStyle(cssStyle);
                }
            }
        }
        Map<String, Map<String, Object>> cellStyles = config.getCellStyles();
        if (null != cellStyles && !cellStyles.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> cellStyle : cellStyles.entrySet()) {
                String cell = cellStyle.getKey();
                Map<String, Object> cssStyle = cellStyle.getValue();
                JStyleContext styleContext = new JStyleContext(workbook, currentSheet).forCell(cell);
                styleContext.applyStyle(cssStyle);
            }
        }
    }

    public void applyGraph(JExcelExportModel config) {
        if (config.getGraph() != null && 0 != config.getGraph().size()) {
            JExcelChartType excelChartType = JExcelChartType.codeOf(config.getGraph().getChartType());
            XSSFWorkbook nativeXssf;
            if (workbook instanceof SXSSFWorkbook) {
                nativeXssf = ((SXSSFWorkbook) workbook).getXSSFWorkbook();
            } else if (workbook instanceof XSSFWorkbook) {
                nativeXssf = (XSSFWorkbook) workbook;
            } else {
                return;
            }
            JExcelChartFactory.createChart(nativeXssf, config.getGraph(), excelChartType, config.getGraph().getTitle());
        }
    }

    public Workbook getWorkBook() {
        return workbook;
    }

    /**
     * 判断配置是否需要"随机访问/回写前面的行"——若返回 true，SXSSF 自动升级必须被强制禁用，
     * 降级为 XSSF 以避免抛出不直观的 {@code Attempting to write a row[N] in the range [0,M] that is already written to disk}。
     *
     * <p>会触发"禁用流式"的条件（所有写完全部数据后才回头修改的能力）：
     * <ul>
     *   <li>FORMULAS：D5='ABS(D2)'、整行/整列公式 —— 需要对前面行 setCellFormula</li>
     *   <li>ROW / COLUMN / CELL / RANGE 样式 —— 需要对前面行/单元格 setCellStyle</li>
     *   <li>MERGE：行/列/区域合并 —— 需要对已写出行的合并区域 addMergedRegion</li>
     *   <li>GRAPH 图表：非空 chartType / series / categories —— 需要访问已有行构建绘图锚</li>
     *   <li>FOOTER：页脚 —— 需要在数据末尾行 setCellValue/setCellStyle</li>
     * </ul>
     */
    static boolean needsRandomRowAccess(JExcelExportModel config) {
        if (config == null) {
            return false;
        }
        if (!isEmpty(config.getCellFormulas())
                || !isEmpty(config.getRowFormulas())
                || !isEmpty(config.getColFormulas())) {
            return true;
        }
        // 2. 样式：ROW / COLUMN / CELL / RANGE
        if (!isEmpty(config.getRowStyles())
                || !isEmpty(config.getColStyles())
                || !isEmpty(config.getCellStyles())
                || !isEmpty(config.getRangeStyles())) {
            return true;
        }
        // 3. 合并：行 / 列 / 区域
        if (!isEmpty(config.getRowMerge())
                || !isEmpty(config.getColMerge())
                || !isEmpty(config.getRangeMerge())) {
            return true;
        }
        // 4. 图表：graph 非空且配置了 chartType 或 series 或 categories
        com.github.paohaijiao.graph.model.JChartData graph = config.getGraph();
        if (graph != null && !graph.isEmpty()) {
            return true;
        }
        // 5. 页脚：会在所有数据写完后再写页脚行（行号<lastRow）
        if (config.getFooter() != null && !config.getFooter().toString().trim().isEmpty()) {
            return true;
        }
        return false;
    }

    private static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
