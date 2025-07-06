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

import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;

import java.util.HashMap;
import java.util.Map;

public class JQuickExcelImportVisitor extends JQuickExcelImportValidateVisitor {



    public JQuickExcelImportVisitor(JContext contextParams) {
        this.context = contextParams;
    }

    public JQuickExcelImportVisitor() {
        this.context = new JContext();
    }

    @Override
    public Object visitImportConfig(JQuickExcelParser.ImportConfigContext ctx) {
        if (ctx.importOption() != null) {
            for (JQuickExcelParser.ImportOptionContext option : ctx.importOption()) {
                visit(option);
            }
        }
        return performImport();
    }

    @Override
    public Object visitSheetOption(JQuickExcelParser.SheetOptionContext ctx) {
        Object sheetValue = ctx.STRING() != null ?
                ctx.STRING().getText().replaceAll("\"", "") :
                Integer.parseInt(ctx.NUMBER().getText());
        config.setSheet(sheetValue);
        return null;
    }

    @Override
    public Object visitHeaderOption(JQuickExcelParser.HeaderOptionContext ctx) {
        if (ctx.STRING() != null) {
            String header = JStringUtils.trim(ctx.STRING().getText());
            config.setHeader(header.equalsIgnoreCase("YES"));
        } else {
            config.setHeader(false);
        }
        return null;
    }

    @Override
    public Object visitRangeOption(JQuickExcelParser.RangeOptionContext ctx) {
        String range = JStringUtils.trim(ctx.STRING().getText());
        config.setRange(range);
        return null;
    }

    /**
     * only process excelField to entity field mapping ,not change the cell value
     *
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Object visitMappingOption(JQuickExcelParser.MappingOptionContext ctx) {
        Map<String, String> mappings = new HashMap<>();
        for (JQuickExcelParser.FieldMappingContext mapping : ctx.fieldMapping()) {
            String source = JStringUtils.trim(mapping.STRING(0).getText());
            String target = mapping.STRING(1) != null ? JStringUtils.trim(mapping.STRING(1).getText()) : mapping.functionCall().getText();
            mappings.put(source, target);
        }
        config.setMappings(mappings);
        return null;
    }

    @Override
    public Object visitTransformOption(JQuickExcelParser.TransformOptionContext ctx) {
        Map<String, String> transforms = new HashMap<>();
        for (JQuickExcelParser.TransformRuleContext rule : ctx.transformRule()) {
            String field = rule.STRING().getText().replaceAll("\"", "");
            String transform = rule.transformValue().getText();
            transforms.put(field, transform);
        }
        config.setTransforms(transforms);
        return null;
    }
}
