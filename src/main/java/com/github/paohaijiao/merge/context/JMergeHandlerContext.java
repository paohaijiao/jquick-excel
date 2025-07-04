package com.github.paohaijiao.merge.context;

import com.github.paohaijiao.enums.JMergeType;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.merge.strategy.JColumnMergeHandler;
import com.github.paohaijiao.merge.strategy.JRangeMergeHandler;
import com.github.paohaijiao.merge.strategy.JRowMergeHandler;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class JMergeHandlerContext {

    private final Workbook workbook;
    private final Sheet sheet;

    public JMergeHandlerContext(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    public JMergeHandler createHandler(JMergeType mergeType) {
        switch (mergeType) {
            case ROW:
                return new JRowMergeHandler(workbook, sheet);
            case COLUMN:
                return new JColumnMergeHandler(workbook, sheet);
            case RANGE:
                return new JRangeMergeHandler(workbook, sheet);
            default:
                throw new IllegalArgumentException("Unsupported merge type: " + mergeType);
        }
    }

    /**
     * Creates appropriate merge handler based on the merge specification
     */
    public JMergeHandler createHandler(String mergeSpec) {
        if (mergeSpec.startsWith("ROWS")) {
            return createHandler(JMergeType.ROW);
        } else if (mergeSpec.startsWith("COLS")) {
            return createHandler(JMergeType.COLUMN);
        } else if (mergeSpec.startsWith("RANGE")) {
            return createHandler(JMergeType.RANGE);
        } else {
            throw new IllegalArgumentException("Unsupported merge specification: " + mergeSpec);
        }
    }
}
