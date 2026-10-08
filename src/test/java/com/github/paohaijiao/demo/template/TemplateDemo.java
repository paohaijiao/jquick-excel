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
package com.github.paohaijiao.demo.template;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.theme.factory.JExcelThemeFactory;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类：<b>导出模板</b> —— 主题模板。
 *
 * <p>主题编码经 {@code JQuickExcelExportXmlParseFactory(theme, rows, os)} 首参传入，
 * 直接使用框架入口，不使用任何自封装方法。规则文件 {@code demo/template/jquick-excel.xml}。
 * 产物目录：{@code D:\test\excel}。
 */
public class TemplateDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/template/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 用 oceanBlue 主题模板导出，回读校验表头加粗与主题色。 */
    @Test
    public void exportWithTheme() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", "上海");
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", "北京");
        rows.add(r2);

        File out = new File(OUT_DIR, "template-oceanBlue.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("oceanBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            TemplateService service = factory.createApi(TemplateService.class);
            service.exportTheme("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            XSSFCellStyle headerStyle =
                    (XSSFCellStyle) wb.getSheet("主题模板").getRow(0).getCell(0).getCellStyle();
            XSSFColor fillColor = headerStyle.getFillForegroundXSSFColor();
            String color = fillColor == null ? null : fillColor.getARGBHex();
            System.out.println("【导出模板】表头加粗=" + headerStyle.getFont().getBold() + "，填充色=" + color);
            Assert.assertTrue("主题表头应加粗", headerStyle.getFont().getBold());
            Assert.assertTrue("应使用 oceanBlue 表头底色 0F4C81", color != null && color.contains("0F4C81"));
        }
    }

    /** 列出框架内置的全部主题模板编码。 */
    @Test
    public void listThemes() {
        List<String> codes = JExcelThemeFactory.allCodes();
        System.out.println("【导出模板】内置主题 " + codes.size() + " 种: " + codes);
        Assert.assertTrue(codes.contains("oceanBlue"));
        Assert.assertTrue(codes.size() >= 42);
    }
}
