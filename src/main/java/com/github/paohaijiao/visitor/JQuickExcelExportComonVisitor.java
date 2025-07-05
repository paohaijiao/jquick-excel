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

import com.github.paohaijiao.handler.JExcelProcessor;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JKeyValueModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;

import java.io.IOException;
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
public class JQuickExcelExportComonVisitor extends JQuickExcelExportGraphVisitor {

    public JQuickExcelExportComonVisitor(List<Map<String, Object>> data) {
        this.context = new JContext();
        this.excelProcessor = new JExcelProcessor(this.context);
        this.data = data;

    }

    public JQuickExcelExportComonVisitor(JContext context, List<Map<String, Object>> data) {
        this.excelProcessor = new JExcelProcessor(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public JExcelExportModel visitExportConfig(JQuickExcelParser.ExportConfigContext ctx) {
        if (ctx.exportOption() != null) {
            for (JQuickExcelParser.ExportOptionContext option : ctx.exportOption()) {
                visit(option);
            }
        }
        try {
            excelProcessor.exportData(data, config);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败: " , e);
        }
        return config;
    }

    @Override
    public Void visitMappingOption(JQuickExcelParser.MappingOptionContext ctx) {
        Map<String, String> mapping = new HashMap<>();
        for (JQuickExcelParser.FieldMappingContext fieldMappingContext : ctx.fieldMapping()) {
            JKeyValueModel fieldMappings = visitFieldMapping(fieldMappingContext);
            mapping.put(fieldMappings.getKey(), fieldMappings.getValue().toString());
        }
        config.setMapping(mapping);
        return null;
    }

    @Override
    public Void visitSheetOption(JQuickExcelParser.SheetOptionContext ctx) {
        Object sheetValue = ctx.STRING() != null ? ctx.STRING().getText().replaceAll("\"", "") :
                Integer.parseInt(ctx.NUMBER().getText());
        config.setSheet(sheetValue);
        return null;
    }

    @Override
    public Void visitHeaderOption(JQuickExcelParser.HeaderOptionContext ctx) {
        boolean header = Boolean.FALSE;
        if (ctx.STRING() != null) {
            String headerText = ctx.STRING().getText();
            header = JStringUtils.trim(headerText).equalsIgnoreCase("YES");
        }
        config.setHeader(header);
        return null;
    }

    @Override
    public Void visitRangeOption(JQuickExcelParser.RangeOptionContext ctx) {
        String range = JStringUtils.trim(ctx.STRING().getText());
        config.setRange(range);
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
