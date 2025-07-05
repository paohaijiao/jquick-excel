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
import com.github.paohaijiao.model.JFormulateCallModel;
import com.github.paohaijiao.model.JMethodCallModel;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JQuickExcelFunctionVisitor extends JQuickExcelCoreVisitor {
    @Override
    public Object visitFunctionArg(JQuickExcelParser.FunctionArgContext ctx) {
        if (ctx.STRING() != null) {
            String string = ctx.STRING().getText();
            String value = JStringUtils.trim(string);
            return value;
        } else if(ctx.IDENTIFIER() != null){
            String string = ctx.IDENTIFIER().getText();
            String value = JStringUtils.trim(string);
            return value;
        }else if (ctx.NUMBER() != null) {
            String number = ctx.NUMBER().getText();
            String value = JStringUtils.trim(number);
            return new BigDecimal(value);
        } else if (ctx.BOOLEAN() != null) {
            String number = ctx.BOOLEAN().getText();
            String value = JStringUtils.trim(number);
            return new Boolean(value);
        } else if (ctx.functionCall() != null) {
            return visitFunctionCall(ctx.functionCall());
        } else if (ctx.variable() != null) {
            Object object = visitVariable(ctx.variable());
            JAssert.notNull(object, "the variable is not initialized");
            return object;
        }else if (ctx.range() != null) {
            Object object = visitRange(ctx.range());
            JAssert.notNull(object, "the variable is not initialized");
            return object;
        }
        JAssert.throwNewException("Invalid FunctionArg");
        return null;
    }
    @Override
    public JFormulateCallModel visitFormulateCall(JQuickExcelParser.FormulateCallContext ctx) {
        JFormulateCallModel methodCallModel = new JFormulateCallModel();
        List<Object> list = new ArrayList<>();
        String functionName = null;
        if (ctx.IDENTIFIER() != null) {
            functionName = JStringUtils.trim(ctx.IDENTIFIER().getText());
        }
        JAssert.notNull(functionName, "Invalid functionName");
        methodCallModel.setMethod(functionName);
        if (ctx.functionArg() != null) {
            for (JQuickExcelParser.FunctionArgContext functionArg : ctx.functionArg()) {
                Object object = visitFunctionArg(functionArg);
                list.add(object);
            }
        }
        methodCallModel.setList(list);
        return methodCallModel;
    }
    @Override
    public String visitRange(JQuickExcelParser.RangeContext ctx) {
        return ctx.getText();
    }




}
