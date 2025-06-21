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
package com.paohaijiao.javelin.visitor;

import com.paohaijiao.javelin.enums.JQuickExcelFormatSpecEnums;
import com.paohaijiao.javelin.exception.JAssert;
import com.paohaijiao.javelin.model.JFormatSpec;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.util.JStringUtils;
import com.paohaijiao.javelin.visitor.function.JQuickExcelFunctionVisitor;

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
    public String visitCustomFormat(JQuickExcelParser.CustomFormatContext ctx) {
        if(ctx.STRING() != null) {
            String customString=ctx.STRING().toString();
            String newString= JStringUtils.trim(customString);
            return newString;
        }
        JAssert.throwNewException("invalid custom format");
        return null;
    }

    @Override
    public String visitStringFormat(JQuickExcelParser.StringFormatContext ctx) {
        if(ctx.STRING() != null) {
            String customString=ctx.STRING().toString();
            String newString= JStringUtils.trim(customString);
            return newString;
        }
        JAssert.throwNewException("invalid string format");
        return null;
    }
    @Override
    public String visitNumberFormat(JQuickExcelParser.NumberFormatContext ctx) {
        if(ctx.STRING() != null) {
            String customString=ctx.STRING().toString();
            String newString= JStringUtils.trim(customString);
            return newString;
        }
        JAssert.throwNewException("invalid NumberFormat ");
        return null;
    }

    @Override
    public String visitDateFormat(JQuickExcelParser.DateFormatContext ctx) {
        if(ctx.STRING() != null) {
            String customString=ctx.STRING().toString();
            String newString= JStringUtils.trim(customString);
            return newString;
        }
        JAssert.throwNewException("invalid DateFormat ");
        return null;
    }
    @Override
    public JFormatSpec visitFormatSpec(JQuickExcelParser.FormatSpecContext ctx) {
        JFormatSpec jFormatSpec = new JFormatSpec();
        if(ctx.dateFormat()!= null) {
            String value=visitDateFormat(ctx.dateFormat());
            jFormatSpec.setFormat(JQuickExcelFormatSpecEnums.DATE);
            jFormatSpec.setValue(value);
            return jFormatSpec;
        } else if (ctx.numberFormat()!= null) {
            String value=visitNumberFormat(ctx.numberFormat());
            jFormatSpec.setFormat(JQuickExcelFormatSpecEnums.NUMBER);
            jFormatSpec.setValue(value);
            return jFormatSpec;
        }else if (ctx.stringFormat()!= null) {
            String value=visitStringFormat(ctx.stringFormat());
            jFormatSpec.setFormat(JQuickExcelFormatSpecEnums.STRING);
            jFormatSpec.setValue(value);
            return jFormatSpec;
        }else if (ctx.customFormat()!= null) {
            String value= visitCustomFormat(ctx.customFormat());
            jFormatSpec.setFormat(JQuickExcelFormatSpecEnums.CUSTOM);
            jFormatSpec.setValue(value);
            return jFormatSpec;
        }
        JAssert.throwNewException("invalid format spec");
        return null;
    }





}
