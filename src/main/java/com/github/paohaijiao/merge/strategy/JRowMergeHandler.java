package com.github.paohaijiao.merge.strategy;

import com.github.paohaijiao.enums.JMergeValueType;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.util.JMergeUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

public class JRowMergeHandler implements JMergeHandler {

    private final Workbook workbook;

    private final Sheet sheet;

    public JRowMergeHandler(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    @Override
    public void merge(Map<String, Object> mergeProperties) {

        Integer rowIndex = (Integer) mergeProperties.get("rowIndex");
        Integer startCol = (Integer) mergeProperties.get("startCol");
        Integer endCol = (Integer) mergeProperties.get("endCol");
        JMergeValueType mergeType = (JMergeValueType) mergeProperties.get("mergeType");
        if (rowIndex == null || startCol == null || endCol == null || mergeType == null) {
            throw new IllegalArgumentException(
                    "Missing required merge properties: rowIndex, startCol, endCol, mergeType");
        }
        if (startCol > endCol) {
            throw new IllegalArgumentException(
                    "Invalid column range: startCol must be <= endCol");
        }
        JMergeUtil.setMergedRegionValue(
                workbook,
                sheet,
                rowIndex,
                rowIndex,  // Same row for start and end since we're merging horizontally
                startCol,
                endCol,
                mergeType
        );

    }
}
