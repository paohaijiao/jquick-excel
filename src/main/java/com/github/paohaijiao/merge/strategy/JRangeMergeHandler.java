package com.github.paohaijiao.merge.strategy;

import com.github.paohaijiao.enums.JMergeValueType;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.util.JMergeUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

public class JRangeMergeHandler implements JMergeHandler {
    private final Workbook workbook;
    private final Sheet sheet;
    private JContext context;
    public JRangeMergeHandler(Workbook workbook, Sheet sheet,JContext context) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.context = context;
    }

    @Override
    public void merge( Map<String, Object> mergeProperties) {
        Integer firstRow = (Integer) mergeProperties.get("firstRow");
        Integer lastRow = (Integer) mergeProperties.get("lastRow");
        Integer firstCol = (Integer) mergeProperties.get("firstCol");
        Integer lastCol = (Integer) mergeProperties.get("lastCol");
        JMergeValueType mergeType = (JMergeValueType) mergeProperties.get("mergeType");
        if (firstRow == null || lastRow == null || firstCol == null || lastCol == null || mergeType == null) {
            throw new IllegalArgumentException(
                    "Missing required merge properties: firstRow, lastRow, firstCol, lastCol, mergeType");
        }
        if (firstRow > lastRow || firstCol > lastCol) {
            throw new IllegalArgumentException(
                    "Invalid range: firstRow must be <= lastRow and firstCol must be <= lastCol");
        }
        JMergeUtil merge=new JMergeUtil(workbook,sheet,context);
        merge.setMergedRegionValue(
                firstRow,
                lastRow,
                firstCol,
                lastCol,
                mergeType
        );
    }
}
