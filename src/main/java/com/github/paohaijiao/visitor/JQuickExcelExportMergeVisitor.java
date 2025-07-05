
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
package com.github.paohaijiao.visitor;


import com.github.paohaijiao.enums.JMergeValueType;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName com.paohaijiao.javelin.visitor
 *
 * @author Martin
 * @version 1.0.0
 * @className JQuickExcelExportVisitor
 * @date 2025/6/18
 * @description
 */
public class JQuickExcelExportMergeVisitor extends JQuickExcelExportStyleVisitor {
    @Override
    public Void visitMergeOption(JQuickExcelParser.MergeOptionContext ctx) {
        for (JQuickExcelParser.MergeSpecContext mergeSpec : ctx.mergeSpec()) {
            visit(mergeSpec);
        }
        return null;
    }

    @Override
    public Void visitMergeSpec(JQuickExcelParser.MergeSpecContext ctx) {
        if (ctx.rowMerge() != null) {
            visit(ctx.rowMerge());
        } else if (ctx.colMerge() != null) {
            visit(ctx.colMerge());
        } else if (ctx.rangeMerge() != null) {
            visit(ctx.rangeMerge());
        }
        return null;
    }

    @Override
    public Void visitRowMerge(JQuickExcelParser.RowMergeContext ctx) {
        Map<String, Object> rowMerge =config.getRowMerge();
        Object range = visitRowRange(ctx.rowRange());
        JMergeValueType policy =null;
        if (ctx.mergePolicy() != null) {
             policy = visitMergePolicy(ctx.mergePolicy());
            JAssert.notNull(policy, "policy is null");
        }
        rowMerge.put(range.toString(),policy);
        config.setRowMerge(rowMerge);
        return null;
    }

    @Override
    public Object visitRowRange(JQuickExcelParser.RowRangeContext ctx) {
        if (ctx.singleRow() != null) {
            String number=ctx.singleRow().getText();
           return  Integer.parseInt(number);
        } else if (ctx.multiRowRange() != null) {
            String string=ctx.multiRowRange().getText();
            return string;
        }
        return null;
    }

    @Override
    public Void visitColMerge(JQuickExcelParser.ColMergeContext ctx) {
        Map<String, Object> colMerge =config.getColMerge();
        Object range = visitColRange(ctx.colRange());
        JMergeValueType policy =null;
        if (ctx.mergePolicy() != null) {
            policy = visitMergePolicy(ctx.mergePolicy());
        }
        colMerge.put(range.toString(),policy);
        config.setColMerge(colMerge);
        return null;
    }

    @Override
    public Object visitColRange(JQuickExcelParser.ColRangeContext ctx) {
        if (ctx.singleCol() != null) {
            String text=ctx.singleCol().getText();
            return  text;
        } else if (ctx.multiColRange() != null) {
            String string=ctx.multiColRange().getText();
            return string;
        }
        return null;
    }

    @Override
    public Void visitRangeMerge(JQuickExcelParser.RangeMergeContext ctx) {
        Map<String, Object> rangeMerge = config.getRangeMerge();
        String rangeRef = ctx.rangeRef().getText();
        if (ctx.mergePolicy() != null) {
            JMergeValueType properties = visitMergePolicy(ctx.mergePolicy());
            rangeMerge.put(rangeRef,properties);
        }
        config.setRangeMerge(rangeMerge);
        return null;
    }

    @Override
    public JMergeValueType visitMergePolicy(JQuickExcelParser.MergePolicyContext ctx) {
       String text= ctx.getText();
       return JMergeValueType.codeOf(text);
    }
}
