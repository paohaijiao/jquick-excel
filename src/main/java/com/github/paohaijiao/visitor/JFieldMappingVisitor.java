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

import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.model.JKeyValueModel;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;

public class JFieldMappingVisitor extends JFormatSpecVisitor {

    @Override
    public JKeyValueModel visitFieldMapping(JQuickExcelParser.FieldMappingContext ctx) {
        JKeyValueModel jKeyValueModel = new JKeyValueModel();
        Object value = null;
        String key = null;
        if (ctx.STRING() != null && !ctx.STRING().isEmpty()) {
            String str = ctx.STRING().get(0).getText();
            key = JStringUtils.trim(str);
        }
        JAssert.notNull(key, "key not null");
        jKeyValueModel.setKey(key);
        if (ctx.STRING() != null && 2 == ctx.STRING().size()) {
            String str = ctx.STRING().get(1).getText();//field Mapping
            value = JStringUtils.trim(str);
        } else {//invoke function to evalue the result
            value = visitFunctionCall(ctx.functionCall());
        }
        jKeyValueModel.setValue(value);
        return jKeyValueModel;
    }



    @Override
    public JKeyValueModel visitTransformRule(JQuickExcelParser.TransformRuleContext ctx) {
        JKeyValueModel jKeyValueModel = new JKeyValueModel();
        Object value = null;
        String key = null;
        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            key = JStringUtils.trim(str);
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
        if (ctx.functionCall() != null) {
            return visitFunctionCall(ctx.functionCall());
        }
        JAssert.notNull(visitFunctionCall(ctx.functionCall()), "invalid TransformValue");
        return null;
    }


}
