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

import com.github.paohaijiao.date.JDateUtil;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelBaseVisitor;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JQuickExcelCoreVisitor extends JQuickExcelBaseVisitor {

    protected JContext context;

    protected Cell cell;

    protected JExcelExportHandler excelProcessor;

    protected List<Map<String, Object>> data;

    protected JExcelExportModel config = new JExcelExportModel();

    @Override
    public Void visitHeaderOption(JQuickExcelParser.HeaderOptionContext ctx) {
        boolean header = Boolean.FALSE;
        if (ctx.BOOLEAN() != null) {
            String headerText = ctx.BOOLEAN().getText();
            header = JStringUtils.trim(headerText).equalsIgnoreCase("true");
        }
        config.setHeader(header);
        return null;
    }


    @Override
    public String visitIdentifier(JQuickExcelParser.IdentifierContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            String varName = JStringUtils.trim(ctx.IDENTIFIER().getText());
            return varName;
        }
        JAssert.throwNewException("Invalid varName");
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

    @Override
    public Date visitDate(JQuickExcelParser.DateContext ctx) {
        if (ctx.DATE() != null) {
           String string= ctx.DATE().getText();
           String str = string.replaceAll("[-/]", "");
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
           return JDateUtil.parse(sdf, str);
        }else{
            String string= ctx.DATETIMETYPE().getText();
            String str = string.replaceAll("[-/:Tt]", "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            return JDateUtil.parse(sdf, str);
        }
    }


}
