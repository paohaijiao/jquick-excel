
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
        Map<String, Object> rowMerge = new HashMap<>();
        rowMerge.put("type", "ROW");
        Map<String, Object> range = visitRowRange(ctx.rowRange());
        rowMerge.put("range", range);
        if (ctx.mergeProperties() != null) {
            Map<String, String> properties = visitMergeProperties(ctx.mergeProperties());
            rowMerge.putAll(properties);
        }
        config.setRowMerge(rowMerge);
        return null;
    }

    @Override
    public Map<String, Object> visitRowRange(JQuickExcelParser.RowRangeContext ctx) {
        Map<String, Object> range = new HashMap<>();
        if (ctx.singleRow() != null) {
            range.put("type", "SINGLE");
            range.put("value", Integer.parseInt(ctx.singleRow().NUMBER().getText()));
        } else if (ctx.multiRowRange() != null) {
            range.put("type", "MULTI");
            String[] parts = ctx.multiRowRange().getText().split("\\.\\.");
            range.put("start", Integer.parseInt(parts[0]));
            range.put("end", Integer.parseInt(parts[1]));
        }
        return range;
    }

    @Override
    public Void visitColMerge(JQuickExcelParser.ColMergeContext ctx) {
        Map<String, Object> colMerge = new HashMap<>();
        Map<String, Object> range = visitColRange(ctx.colRange());
        colMerge.put("range", range);
        if (ctx.mergeProperties() != null) {
            Map<String, String> properties = visitMergeProperties(ctx.mergeProperties());
            colMerge.putAll(properties);
        }

        config.setColMerge(colMerge);
        return null;
    }

    @Override
    public Map<String, Object> visitColRange(JQuickExcelParser.ColRangeContext ctx) {
        Map<String, Object> range = new HashMap<>();
        if (ctx.singleCol() != null) {
            range.put("type", "SINGLE");
            range.put("value", ctx.singleCol().IDENTIFIER().getText());
        } else if (ctx.multiColRange() != null) {
            range.put("type", "MULTI");
            String[] parts = ctx.multiColRange().getText().split("\\.\\.");
            range.put("start", parts[0]);
            range.put("end", parts[1]);
        }
        return range;
    }

    @Override
    public Void visitRangeMerge(JQuickExcelParser.RangeMergeContext ctx) {
        Map<String, Object> rangeMerge = new HashMap<>();
        rangeMerge.put("type", "RANGE");
        String rangeRef = ctx.rangeRef().getText();
        rangeMerge.put("range", rangeRef);
        if (ctx.mergeProperties() != null) {
            Map<String, String> properties = visitMergeProperties(ctx.mergeProperties());
            rangeMerge.putAll(properties);
        }

        config.setRangeMerge(rangeMerge);
        return null;
    }

    @Override
    public Map<String, String> visitMergeProperties(JQuickExcelParser.MergePropertiesContext ctx) {
        Map<String, String> properties = new HashMap<>();
        for (JQuickExcelParser.StylePropertyContext prop : ctx.styleProperty()) {
            String key = prop.IDENTIFIER(0).getText();
            String value = prop.IDENTIFIER(1).getText();
            properties.put(key, value);
        }
        return properties;
    }
}
