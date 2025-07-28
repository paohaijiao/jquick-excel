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


import com.github.paohaijiao.enums.JMethodEnums;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.model.JMethodCallModel;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class JQuickExcelExportFormulateVisitor extends JFieldMappingVisitor {

    @Override
    public Void visitFormulaOption(JQuickExcelParser.FormulaOptionContext ctx) {
        Map<String, String> cellFormulas = new HashMap<>();
        Map<String, String> rowFormulas = new HashMap<>();
        Map<String, String> colFormulas = new HashMap<>();
        for (JQuickExcelParser.FormulaTargetContext target : ctx.formulaTarget()) {
            if (target.rowFormula() != null) {
                JQuickExcelParser.RowFormulaContext rowCtx = target.rowFormula();
                String rowSpec = rowCtx.rowSpec().getText();
                String formula = rowCtx.formulaSpec().getText();
                rowFormulas.put(JStringUtils.trim(rowSpec), JStringUtils.trim(formula));
            } else if (target.colFormula() != null) {
                JQuickExcelParser.ColFormulaContext colCtx = target.colFormula();
                String colSpec = colCtx.colSpec().getText();
                String formula = colCtx.formulaSpec().getText();
                colFormulas.put(JStringUtils.trim(colSpec), JStringUtils.trim(formula));
            } else if (target.cellFormula() != null) {
                JQuickExcelParser.CellFormulaContext cellCtx = target.cellFormula();
                String cellRef = cellCtx.cellRef().getText();
                String formula = cellCtx.formulaSpec().getText();
                cellFormulas.put(JStringUtils.trim(cellRef), JStringUtils.trim(formula));
            }
        }
        config.setCellFormulas(cellFormulas);
        config.setRowFormulas(rowFormulas);
        config.setColFormulas(colFormulas);
        return null;
    }

    @Override
    public Void visitFormulaTarget(JQuickExcelParser.FormulaTargetContext ctx) {
        if (ctx.rowFormula() != null) {
            visitRowFormula(ctx.rowFormula());
        } else if (ctx.colFormula() != null) {
            visitColFormula(ctx.colFormula());
        } else if (ctx.cellFormula() != null) {
            visitCellFormula(ctx.cellFormula());
        }
        return null;
    }

    @Override
    public Void visitRowFormula(JQuickExcelParser.RowFormulaContext ctx) {
        String rowSpec = null;
        if (ctx.rowSpec() != null) {
            String text = ctx.rowSpec().getText();
            rowSpec = JStringUtils.trim(text);
        }
        String formula = null;
        if (ctx.formulaSpec() != null) {
            String text = ctx.formulaSpec().getText();
            formula = JStringUtils.trim(text);
        }
        if (StringUtils.isNotEmpty(rowSpec) && StringUtils.isNotEmpty(formula)) {
            config.getColFormulas().put(rowSpec, formula);
        }
        return null;
    }

    @Override
    public Void visitColFormula(JQuickExcelParser.ColFormulaContext ctx) {
        String colSpec = null;
        if (ctx.colSpec() != null) {
            String text = ctx.colSpec().getText();
            colSpec = JStringUtils.trim(text);
        }
        String formula = null;
        if (ctx.formulaSpec() != null) {
            String text = ctx.formulaSpec().getText();
            formula = JStringUtils.trim(text);
        }
        if (StringUtils.isNotEmpty(colSpec) && StringUtils.isNotEmpty(formula)) {
            config.getColFormulas().put(colSpec, formula);
        }
        return null;
    }

    @Override
    public Void visitCellFormula(JQuickExcelParser.CellFormulaContext ctx) {
        String cellRef = null;
        if (ctx.cellRef() != null) {
            String text = ctx.cellRef().getText();
            cellRef = JStringUtils.trim(text);
        }
        String formula = null;
        if (ctx.formulaSpec() != null) {
            String text = ctx.formulaSpec().getText();
            formula = JStringUtils.trim(text);
        }
        if (StringUtils.isNotEmpty(cellRef) && StringUtils.isNotEmpty(formula)) {
            config.getCellFormulas().put(cellRef, formula);
        }
        return null;
    }

    @Override
    public String visitCellRef(JQuickExcelParser.CellRefContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitFormulaSpec(JQuickExcelParser.FormulaSpecContext ctx) {
        if (ctx.STRING() != null) {
            String text = ctx.STRING().getText();
            return JStringUtils.trim(text);
        }
        return "";
    }
    @Override
    public JMethodCallModel visitFunctionCall(JQuickExcelParser.FunctionCallContext ctx) {
        JMethodCallModel methodCallModel = new JMethodCallModel();
        List<Object> list = new ArrayList<>();
        String functionName = null;
        if (ctx.IDENTIFIER() != null) {
            functionName = JStringUtils.trim(ctx.IDENTIFIER().getText());
        }
        JAssert.notNull(functionName, "Invalid functionName");
        methodCallModel.setMethod(JMethodEnums.methodOf(functionName));
        if (ctx.functionArg() != null) {
            for (JQuickExcelParser.FunctionArgContext functionArg : ctx.functionArg()) {
                Object object = visitFunctionArg(functionArg);
                list.add(object);
            }
        }
        methodCallModel.setList(list);
        return methodCallModel;
    }
}
