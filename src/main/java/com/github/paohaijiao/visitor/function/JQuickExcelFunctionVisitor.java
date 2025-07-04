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
package com.github.paohaijiao.visitor.function;

import com.github.paohaijiao.enums.JMethodEnums;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.model.JMethodCallModel;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;
import com.github.paohaijiao.visitor.JQuickExcelCoreVisitor;
import com.github.paohaijiao.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

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
        } else if (ctx.NUMBER() != null) {
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
        } else if (ctx.quotedFunctionCall() != null) {
            Object object = visitQuotedFunctionCall(ctx.quotedFunctionCall());
            return object;
        }
        JAssert.throwNewException("Invalid FunctionArg");
        return null;
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

    @Override
    public Object visitQuotedFunctionCall(JQuickExcelParser.QuotedFunctionCallContext ctx) {
        if (ctx.STRING() != null) {
            String string = ctx.STRING().getText();
            String script = JStringUtils.trim(string);
            JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(script));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JQuickExcelParser parser = new JQuickExcelParser(tokens);
            ParseTree tree = parser.functionArg();
            JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(this.context);
            Object result = visitor.visit(tree);
            return result;
        }
        JAssert.throwNewException("Invalid QuotedFunctionCall");
        return null;
    }


}
