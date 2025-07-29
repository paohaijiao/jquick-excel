package com.github.paohaijiao.merge.context;

import com.github.paohaijiao.enums.JMergeType;
import com.github.paohaijiao.merge.JMergeHandler;
import com.github.paohaijiao.merge.strategy.JColumnMergeHandler;
import com.github.paohaijiao.merge.strategy.JRangeMergeHandler;
import com.github.paohaijiao.merge.strategy.JRowMergeHandler;
import com.github.paohaijiao.param.JContext;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class JMergeHandlerContext {

    private final Workbook workbook;
    private final Sheet sheet;
    private final JContext context;

    public JMergeHandlerContext(Workbook workbook, Sheet sheet,JContext context) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.context = context;
    }

    public JMergeHandler createHandler(JMergeType mergeType) {
        switch (mergeType) {
            case ROW:
                return new JRowMergeHandler(workbook, sheet,context);
            case COLUMN:
                return new JColumnMergeHandler(workbook, sheet,context);
            case RANGE:
                return new JRangeMergeHandler(workbook, sheet,context);
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
