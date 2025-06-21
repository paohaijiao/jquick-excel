package com.paohaijiao.javelin.visitor.function;

import com.paohaijiao.javelin.enums.JMethodEnums;
import com.paohaijiao.javelin.exception.JAssert;
import com.paohaijiao.javelin.model.JMethodCallModel;
import com.paohaijiao.javelin.parser.JQuickExcelLexer;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.util.JStringUtils;
import com.paohaijiao.javelin.visitor.JQuickExcelCoreVisitor;
import com.paohaijiao.javelin.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JQuickExcelFunctionVisitor extends JQuickExcelCoreVisitor {
    @Override
    public Object visitFunctionArg(JQuickExcelParser.FunctionArgContext ctx) {
        if(ctx.STRING() != null) {
            String string= ctx.STRING().getText();
            String value= JStringUtils.trim(string);
            return value;
        }else if(ctx.NUMBER() != null) {
            String number= ctx.NUMBER().getText();
            String value= JStringUtils.trim(number);
            return new BigDecimal(value);
        }else if(ctx.BOOLEAN() != null) {
            String number= ctx.BOOLEAN().getText();
            String value= JStringUtils.trim(number);
            return new Boolean(value);
        }else if(ctx.functionCall() != null) {
           return visitFunctionCall(ctx.functionCall());
        }else if(ctx.variable() != null) {
            Object object= visitVariable(ctx.variable());
            JAssert.notNull(object,"the variable is not initialized");
            return object;
        }
        else if(ctx.quotedFunctionCall() != null) {
            Object object= visitQuotedFunctionCall(ctx.quotedFunctionCall());
            return object;
        }
        JAssert.throwNewException("Invalid FunctionArg");
        return null;
    }
    @Override
    public JMethodCallModel visitFunctionCall(JQuickExcelParser.FunctionCallContext ctx) {
        JMethodCallModel methodCallModel = new JMethodCallModel();
        List<Object> list=new ArrayList<>();
        String functionName=null;
        if(ctx.ID() != null) {
            functionName= JStringUtils.trim(ctx.ID().getText());
        }
        JAssert.notNull(functionName,"Invalid functionName");
        methodCallModel.setMethod(JMethodEnums.methodOf(functionName));
        if(ctx.functionArg()!= null) {
            for (JQuickExcelParser.FunctionArgContext functionArg : ctx.functionArg()){
                Object object= visitFunctionArg(functionArg);
                list.add(object);
            }
        }
        methodCallModel.setList(list);
        return methodCallModel;
    }
    @Override
    public Object visitQuotedFunctionCall(JQuickExcelParser.QuotedFunctionCallContext ctx) {
        if(ctx.STRING() != null) {
            String string= ctx.STRING().getText();
            String script= JStringUtils.trim(string);
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
