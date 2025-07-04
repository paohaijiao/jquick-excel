package com.github.paohaijiao.merge.strategy;

import com.github.paohaijiao.enums.JMergeValueType;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.util.JMergeUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

public class JColumnMergeHandler implements JMergeHandler {
    private final Workbook workbook;
    private final Sheet sheet;

    public JColumnMergeHandler(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    @Override
    public void merge(Map<String, Object> mergeProperties) {
        Integer columnIndex = (Integer) mergeProperties.get("columnIndex");
        Integer startRow = (Integer) mergeProperties.get("startRow");
        Integer endRow = (Integer) mergeProperties.get("endRow");
        JMergeValueType mergeType = (JMergeValueType) mergeProperties.get("mergeType");
        if (columnIndex == null || startRow == null || endRow == null || mergeType == null) {
            throw new IllegalArgumentException("Missing required merge properties: columnIndex, startRow, endRow, mergeType");
        }
        JMergeUtil.setMergedRegionValue(
                workbook,
                sheet,
                startRow,
                endRow,
                columnIndex,
                columnIndex,
                mergeType
        );
    }
}
