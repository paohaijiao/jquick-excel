package com.paohaijiao.javelin.visitor;

import com.paohaijiao.javelin.exception.JAssert;
import com.paohaijiao.javelin.model.JKeyValueModel;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.util.JStringUtils;

public class JFieldMapping extends JFormatSpecVisitor{

    @Override
    public JKeyValueModel visitFieldMapping(JQuickExcelParser.FieldMappingContext ctx) {
        JKeyValueModel jKeyValueModel = new JKeyValueModel();
        Object value=null;
        String key=null;
        if (ctx.STRING() != null&&!ctx.STRING().isEmpty()) {
            String str=ctx.STRING().get(0).getText();
             key= JStringUtils.trim(str);
        }
        JAssert.notNull(key, "key not null");
        jKeyValueModel.setKey(key);
        if (ctx.STRING() != null&&2==ctx.STRING().size()) {
            String str=ctx.STRING().get(1).getText();//field Mapping
            value= JStringUtils.trim(str);
        }else {//invoke function to evalue the result
            value=visitFunctionCall(ctx.functionCall());
        }
        jKeyValueModel.setValue(value);
        return jKeyValueModel;
    }
    @Override
    public JKeyValueModel visitCellFormat(JQuickExcelParser.CellFormatContext ctx) {
        JKeyValueModel jKeyValueModel = new JKeyValueModel();
        Object value=null;
        String key=null;
        if (ctx.STRING() != null) {
            String str=ctx.STRING().getText();
            key= JStringUtils.trim(str);
        }
        JAssert.notNull(key, "key not null");
        jKeyValueModel.setKey(key);
        if (ctx.formatSpec() != null) {
            value = visitFormatSpec(ctx.formatSpec());
        }
        jKeyValueModel.setValue(value);
        return jKeyValueModel;
    }
    @Override
    public JKeyValueModel visitTransformRule(JQuickExcelParser.TransformRuleContext ctx) {
        JKeyValueModel jKeyValueModel = new JKeyValueModel();
        Object value=null;
        String key=null;
        if (ctx.STRING() != null) {
            String str=ctx.STRING().getText();
            key= JStringUtils.trim(str);
        }
        JAssert.notNull(key, "key not null");
        jKeyValueModel.setKey(key);
        if (ctx.transformValue() != null) {
            value = visitTransformValue(ctx.transformValue());
        }
        jKeyValueModel.setValue(value);
        return jKeyValueModel;
    }

    @Override
    public Object visitTransformValue(JQuickExcelParser.TransformValueContext ctx) {
        if(ctx.functionCall() != null) {
            return visitFunctionCall(ctx.functionCall());
        }else if (ctx.quotedFunctionCall()!=null) {
            return visitQuotedFunctionCall(ctx.quotedFunctionCall());
        }
        JAssert.notNull(visitFunctionCall(ctx.functionCall()), "invalid TransformValue");
        return null;
    }



}
