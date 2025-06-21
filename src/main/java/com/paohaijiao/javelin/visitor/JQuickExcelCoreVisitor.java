package com.paohaijiao.javelin.visitor;

import com.paohaijiao.javelin.exception.JAssert;
import com.paohaijiao.javelin.param.JContext;
import com.paohaijiao.javelin.parser.JQuickExcelBaseVisitor;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.util.JStringUtils;
import org.apache.poi.ss.usermodel.Cell;

public class JQuickExcelCoreVisitor extends JQuickExcelBaseVisitor {

    protected JContext context;

    protected Cell cell;

    @Override
    public String visitFileName(JQuickExcelParser.FileNameContext ctx) {
        if(ctx.STRING() != null) {
            String fileName = JStringUtils.trim(ctx.STRING().getText());
            return fileName;
        }
        JAssert.throwNewException("Invalid file name");
        return null;
    }
    @Override
    public String visitIdentifier(JQuickExcelParser.IdentifierContext ctx) {
        if(ctx.ID() != null) {
            String varName = JStringUtils.trim(ctx.ID().getText());
            return varName;
        }
        JAssert.throwNewException("Invalid varName");
        return null;
    }
    @Override
    public String visitDestination(JQuickExcelParser.DestinationContext ctx) {
        if(ctx.identifier() != null) {
            String varName = JStringUtils.trim(ctx.identifier().getText());
            return varName;
        }
        JAssert.throwNewException("Invalid Destination");
        return null;
    }
    @Override
    public Object visitVariable(JQuickExcelParser.VariableContext ctx) {
        if (ctx.identifier() != null) {
            String varName = visitIdentifier(ctx.identifier());
            return this.context.get(varName);
        }
        JAssert.throwNewException("Invalid Variable");
        return null;
    }

}
