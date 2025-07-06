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
import com.github.paohaijiao.model.JTargetSpec;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;
import com.github.paohaijiao.validate.JAbstractValidationRule;
import com.github.paohaijiao.validate.JExcelValidationRule;
import com.github.paohaijiao.validate.impl.JCompositeRule;
import com.github.paohaijiao.validate.impl.string.JEndWithRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JQuickExcelImportValidateVisitor extends JFieldMapping {

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
        List<JExcelValidationRule > list=new ArrayList<JExcelValidationRule>();
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
    public JExcelValidationRule visitRuleSpec(JQuickExcelParser.RuleSpecContext ctx) {
        String method=ctx.IDENTIFIER().getText();
        JMethodValidationRuleType type=JMethodValidationRuleType.codeOf(method);
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
        return ctx.getText();
    }
    @Override
    public String visitColTarget(JQuickExcelParser.ColTargetContext ctx) {
        return ctx.getText();
    }
    @Override
    public String visitCellTarget(JQuickExcelParser.CellTargetContext ctx) {
        return ctx.getText();
    }
    @Override
    public String visitRangeTarget(JQuickExcelParser.RangeTargetContext ctx) {
        return ctx.getText();
    }







    protected JExcelImportModel performImport() {
        return config;
    }
}
