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

import com.github.paohaijiao.enums.JMethodValidationRuleType;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.model.JRuleItem;
import com.github.paohaijiao.model.JTargetSpec;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;
import com.github.paohaijiao.validate.JAbstractValidationRule;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.*;

public class JQuickExcelImportValidateVisitor extends JFieldMappingVisitor {

    protected JExcelImportModel config = new JExcelImportModel();

    @Override
    public Void visitValidationOption(JQuickExcelParser.ValidationOptionContext ctx) {
        for (JQuickExcelParser.ValidationRuleContext ruleCtx : ctx.validationRule()) {
            visitValidationRule(ruleCtx);
        }
        return null;
    }
    @Override
    public Void visitValidationRule(JQuickExcelParser.ValidationRuleContext ctx) {
        JTargetSpec targetSpec=null;
        if(null!=ctx.targetSpec()){
             targetSpec=visitTargetSpec(ctx.targetSpec());
        }
        List<JAbstractValidationRule > list=new ArrayList<JAbstractValidationRule>();
        if(null!=ctx.ruleSpec()&&!ctx.ruleSpec().isEmpty()){
            for (JQuickExcelParser.RuleSpecContext ruleSpecContext :ctx.ruleSpec()) {
                list.add(visitRuleSpec(ruleSpecContext));
            }
        }
        JAssert.notNull(targetSpec,"no target spec");
        if(targetSpec.getType().equalsIgnoreCase("row")){
            this.config.getRowValidate().put(targetSpec.getValue(),list);
        }
        if(targetSpec.getType().equalsIgnoreCase("col")){
            this.config.getColValidate().put(targetSpec.getValue(),list);
        }
        if(targetSpec.getType().equalsIgnoreCase("cell")){
            this.config.getCellValidate().put(targetSpec.getValue(),list);
        }
        if(targetSpec.getType().equalsIgnoreCase("range")){
            this.config.getRangeValidate().put(targetSpec.getValue(),list);
        }
        return null;
    }
    @Override
    public JAbstractValidationRule visitRuleSpec(JQuickExcelParser.RuleSpecContext ctx) {
        String method=ctx.IDENTIFIER().getText();
        JRuleItem item=visitRuleItem(ctx.ruleItem());
        JMethodValidationRuleType type=JMethodValidationRuleType.codeOf(method);
        try{
            Class<? extends JAbstractValidationRule> ruleClass = type.getClazz();
            if (type == JMethodValidationRuleType.COMPOSITE) {
                return (JAbstractValidationRule) ruleClass.getDeclaredConstructor().newInstance();
            }
            Constructor<?> constructor = ruleClass.getDeclaredConstructor(boolean.class, Map.class, String.class);
            return (JAbstractValidationRule) constructor.newInstance(item.getRequired(), item.getMap(), item.getCustomMessage());
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JTargetSpec visitTargetSpec(JQuickExcelParser.TargetSpecContext ctx) {
        JTargetSpec jTargetSpec=new JTargetSpec();
        if(null!=ctx.rowTarget()){
            String value=visitRowTarget(ctx.rowTarget());
            jTargetSpec.setType("row");
            jTargetSpec.setValue(JStringUtils.trim(value));
        } else if (null!=ctx.colTarget()) {
            String value=visitColTarget(ctx.colTarget());
            jTargetSpec.setType("col");
            jTargetSpec.setValue(JStringUtils.trim(value));
        } else if (null!=ctx.cellTarget()) {
            String value=visitCellTarget(ctx.cellTarget());
            jTargetSpec.setType("cell");
            jTargetSpec.setValue(JStringUtils.trim(value));
        }else {
            String value= visitRangeTarget(ctx.rangeTarget());
            jTargetSpec.setType("range");
            jTargetSpec.setValue(JStringUtils.trim(value));
        }
        return jTargetSpec;
    }
    @Override
    public String visitRowTarget(JQuickExcelParser.RowTargetContext ctx) {
        if(null!=ctx&&null!=ctx.rowSpec()){
            return ctx.rowSpec().getText();
        }else{
            return null;
        }
    }
    @Override
    public String visitColTarget(JQuickExcelParser.ColTargetContext ctx) {
        if(null!=ctx&&null!=ctx.colSpec()){
            return ctx.colSpec().getText();
        }else{
            return null;
        }

    }
    @Override
    public String visitCellTarget(JQuickExcelParser.CellTargetContext ctx) {
        if(null!=ctx){
            return ctx.getText();
        }else{
            return null;
        }
    }
    @Override
    public String visitRangeTarget(JQuickExcelParser.RangeTargetContext ctx) {
        if(null!=ctx){
            return ctx.getText();
        }else{
            return null;
        }
    }

    @Override
    public JRuleItem visitRuleItem(JQuickExcelParser.RuleItemContext ctx) {
        JRuleItem ruleItem =new JRuleItem();
        if (ctx.ruleRequired()!=null) {
           Boolean bool= visitRuleRequired(ctx.ruleRequired());
           ruleItem.setRequired(bool);
        }
        if (ctx.ruleMsg()!=null) {
            String msg=visitRuleMsg(ctx.ruleMsg());
            ruleItem.setCustomMessage(msg);
        }
        if (ctx.map()!=null) {
            HashMap<String,Object> map=visitMap(ctx.map());
            ruleItem.setMap(map);
        }

        return ruleItem;
    }
    @Override
    public HashMap<String,Object> visitMap(JQuickExcelParser.MapContext ctx) {
        HashMap<String,Object> map=new HashMap<>();
        for (JQuickExcelParser.MapItemContext mapItemContext:ctx.mapItem()){
            HashMap<String,Object>  value=  visitMapItem(mapItemContext);
            for (Map.Entry<String, Object> entry:value.entrySet()){
                String key=JStringUtils.trim(entry.getKey());
                map.put(key,entry.getValue());
            }
        }
        return map;
    }
    @Override
    public HashMap<String,Object> visitMapItem(JQuickExcelParser.MapItemContext ctx) {
        HashMap<String,Object> map=new HashMap<>();
        String key=ctx.mapkey().getText();
        Object value=visitMapValue(ctx.mapValue());
        map.put(key,value);
        return map;
    }

    @Override
    public Object visitMapValue(JQuickExcelParser.MapValueContext ctx) {
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
        }else if (ctx.date() != null) {
            Date object = visitDate(ctx.date());
            return object;
        }
        JAssert.throwNewException("Invalid FunctionArg");
        return null;
    }

    @Override
    public Boolean visitRuleRequired(JQuickExcelParser.RuleRequiredContext ctx) {
        String text=ctx.BOOLEAN().getText();
        return Boolean.parseBoolean(text);
    }
    @Override
    public String visitRuleMsg(JQuickExcelParser.RuleMsgContext ctx) {
        if(null!=ctx.STRING()){
            String string= ctx.STRING().getText();
            return JStringUtils.trim(string);
        }else {
            return null;
        }

    }
    protected JExcelImportModel performImport() {
        return config;
    }
}
