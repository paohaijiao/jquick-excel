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
package com.github.paohaijiao.demo.transform;

import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类：<b>TRANSFORM</b> —— 导入值转换表达式 / 导出前字段值转换。
 *
 * <p>全部调用都直接使用框架入口 {@link JQuickExcelExportXmlParseFactory} /
 * {@link JQuickExcelImportXmlParseFactory}，规则文件 {@code demo/transform/jquick-excel.xml}。
 * 字典通过 {@link JContext} 传入（{@code ${dict}}），不使用任何自封装方法。
 * 产物目录：{@code D:\test\excel}。
 */
public class TransformDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/transform/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导入：性别「男」按字典反查为 1，姓名大写，出生日期保持 yyyy-MM-dd。 */
    @Test
    public void importTransform() throws Exception {
        JContext ctx = new JContext();
        Map<String, Object> dict = new HashMap<>();
        dict.put("男", "1");
        dict.put("女", "2");
        ctx.put("dict", dict);

        try (InputStream in = TransformDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(ctx, in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            TransformService service = factory.createApi(TransformService.class);
            List<JQuickRow> rows = service.importTransform("field", "value");
            JQuickRow first = rows.get(0);
            System.out.println("导入转换后: name=" + first.get("name") + ", sex=" + first.get("sex")
                    + ", birthday=" + first.get("birthday"));
            Assert.assertEquals(3, rows.size());
            Assert.assertEquals("张三".toUpperCase(), first.get("name"));
            Assert.assertEquals("1", first.get("sex"));
            Assert.assertEquals("2004-09-01", first.get("birthday"));
        }
    }

    /** 导出：码值 1 按字典正查为「男」，姓名大写，年龄 20 加 1 变成 21。 */
    @Test
    public void exportTransform() throws Exception {
        JContext ctx = new JContext();
        Map<String, Object> dict = new HashMap<>();
        dict.put("1", "男");
        dict.put("2", "女");
        ctx.put("dict", dict);

        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", "1");
        r1.put("c", 20);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", "2");
        r2.put("c", 21);
        rows.add(r2);

        File out = new File(OUT_DIR, "transform-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(ctx, rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            TransformService service = factory.createApi(TransformService.class);
            service.exportTransform("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            org.apache.poi.ss.usermodel.Row dataRow = wb.getSheet("值转换").getRow(1);
            System.out.println("导出转换后: a=" + dataRow.getCell(0).getStringCellValue()
                    + ", b=" + dataRow.getCell(1).getStringCellValue()
                    + ", c=" + dataRow.getCell(2).getNumericCellValue());
            Assert.assertEquals("张三".toUpperCase(), dataRow.getCell(0).getStringCellValue());
            Assert.assertEquals("男", dataRow.getCell(1).getStringCellValue());
            Assert.assertEquals(21.0, dataRow.getCell(2).getNumericCellValue(), 0.0001);
        }
    }
}
