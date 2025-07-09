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
import org.apache.commons.lang3.StringUtils;

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
public class JQuickExcelExportStyleVisitor extends JQuickExcelExportFormulateVisitor {
    @Override
    public Void visitStyleOption(JQuickExcelParser.StyleOptionContext ctx) {
        for (JQuickExcelParser.StyleTargetContext target : ctx.styleTarget()) {
            visit(target);
        }
        return null;
    }

    @Override
    public Void visitStyleTarget(JQuickExcelParser.StyleTargetContext ctx) {
        if (ctx.rowStyle() != null) {
            return visitRowStyle(ctx.rowStyle());
        } else if (ctx.colStyle() != null) {
            return visitColStyle(ctx.colStyle());
        } else if (ctx.cellStyle() != null) {
            return visitCellStyle(ctx.cellStyle());
        } else if (ctx.rangeStyle() != null) {
            return visitRangeStyle(ctx.rangeStyle());
        }
        return null;
    }

    @Override
    public Void visitRowStyle(JQuickExcelParser.RowStyleContext ctx) {
        String rowSpec = ctx.rowSpec().getText();
        Map<String, Object> styleProps = new HashMap<>();
        for (JQuickExcelParser.StylePropertyContext prop : ctx.styleProperty()) {
            Map<String, Object> row = visitStyleProperty(prop);
            styleProps.putAll(row);
        }
        config.getRowStyles().put(rowSpec, styleProps);
        return null;
    }

    @Override
    public Void visitColStyle(JQuickExcelParser.ColStyleContext ctx) {
        String colSpec = ctx.colSpec().getText();
        Map<String, Object> styleProps = new HashMap<>();
        for (JQuickExcelParser.StylePropertyContext prop : ctx.styleProperty()) {
            Map<String, Object> row = visitStyleProperty(prop);
            styleProps.putAll(row);
        }
        config.getColStyles().put(colSpec, styleProps);
        return null;
    }

    @Override
    public Void visitCellStyle(JQuickExcelParser.CellStyleContext ctx) {
        String cellRef = ctx.cellRef().getText();
        Map<String, Object> styleProps = new HashMap<>();
        for (JQuickExcelParser.StylePropertyContext prop : ctx.styleProperty()) {
            Map<String, Object> row = visitStyleProperty(prop);
            styleProps.putAll(row);
        }
        config.getCellStyles().put(cellRef, styleProps);
        return null;
    }

    @Override
    public Void visitRangeStyle(JQuickExcelParser.RangeStyleContext ctx) {
        String rangeRef = ctx.rangeRef().getText();
        Map<String, Object> styleProps = new HashMap<>();
        for (JQuickExcelParser.StylePropertyContext prop : ctx.styleProperty()) {
            Map<String, Object> row = visitStyleProperty(prop);
            styleProps.putAll(row);
        }
        config.getRangeStyles().put(rangeRef, styleProps);
        return null;
    }

    @Override
    public String visitRangeRef(JQuickExcelParser.RangeRefContext ctx) {
        return ctx.getText();
    }

    @Override
    public Map<String, Object> visitStyleProperty(JQuickExcelParser.StylePropertyContext ctx) {
        Map<String, Object> style = new HashMap<>();
        String key=null;
        if (ctx.IDENTIFIER() != null ) {
             key = JStringUtils.trim(ctx.IDENTIFIER().getText());

        }
        Object value =null;
        if(null!=ctx.functionArg()){
            value=visitFunctionArg(ctx.functionArg());
        }
        style.put(key, value);
        return style;
    }

}
