package com.paohaijiao.javelin.visitor;
import com.paohaijiao.javelin.model.JExcelImportModel;
import com.paohaijiao.javelin.param.JContext;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.util.JStringUtils;

import java.util.HashMap;
import java.util.Map;

public class JQuickExcelImportVisitor extends JFieldMapping {
    private JExcelImportModel config=new JExcelImportModel();

    public JQuickExcelImportVisitor(JContext contextParams) {
        this.context=contextParams;
    }
    public JQuickExcelImportVisitor() {
        this.context=new JContext();
    }

    @Override
    public Object visitImportConfig(JQuickExcelParser.ImportConfigContext ctx) {
        if (ctx.fileName() != null) {
            String fileName = JStringUtils.trim(ctx.fileName().getText());
            config.setFileName(fileName);
        }
        if (ctx.importOption() != null) {
            for (JQuickExcelParser.ImportOptionContext option : ctx.importOption()) {
                visit(option);
            }
        }
        if(null!=ctx.destination()){
           String dest= visitDestination(ctx.destination());
           config.setDestination(dest);
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
        }else {
            config.setHeader(false);
        }
        return null;
    }

    @Override
    public Object visitRangeOption(JQuickExcelParser.RangeOptionContext ctx) {
        String range= JStringUtils.trim(ctx.STRING().getText());
        config.setRange(range);
        return null;
    }

    /**
     * only process excelField to entity field mapping ,not change the cell value
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Object visitMappingOption(JQuickExcelParser.MappingOptionContext ctx) {
        Map<String, String> mappings = new HashMap<>();
        for (JQuickExcelParser.FieldMappingContext mapping : ctx.fieldMapping()) {
            String source = JStringUtils.trim(mapping.STRING(0).getText());
            String target = mapping.STRING(1) != null ? JStringUtils.trim(mapping.STRING(1).getText()): mapping.functionCall().getText();
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

    private JExcelImportModel performImport() {
        return config;
    }
}
