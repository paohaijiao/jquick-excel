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

import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName com.paohaijiao.javelin.visitor
 *
 * @author Martin
 * @version 1.0.0
 * @className JFormatSpecVisitor
 * @date 2025/6/18
 * @description
 */
public class JFormatSpecVisitor extends JQuickExcelFunctionVisitor {

    @Override
    public Map<String, String> visitCellFormat(JQuickExcelParser.CellFormatContext ctx) {
        Map<String, String> formats = new HashMap<>();
        if(ctx.STRING() != null&&ctx.STRING().size()==2) {
            String  key=JStringUtils.trim(ctx.STRING().get(0).getText());
            String  value=JStringUtils.trim(ctx.STRING().get(1).getText());
            formats.put(key,value);
        }
        return formats;
    }
    @Override
    public Void visitFormatOption(JQuickExcelParser.FormatOptionContext ctx) {
        Map<String, String> formats = new HashMap<>();
        for (JQuickExcelParser.CellFormatContext cells : ctx.cellFormat()) {
            Map<String, String> formatSpec=  visitCellFormat(cells);
            formats.putAll(formatSpec);
        }
        config.setFormat(formats);
        return null;
    }


}
