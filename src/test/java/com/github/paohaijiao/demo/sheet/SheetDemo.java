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
package com.github.paohaijiao.demo.sheet;

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
import java.util.List;

/**
 * 分类：<b>SHEET</b> —— 工作表的指定（导出）与选择（导入）。
 *
 * <p>全部调用都直接使用框架入口：
 * <ul>
 *   <li>导出：{@code new JQuickExcelExportXmlParseFactory(rows, os)} → {@code new JQuickXmlFactory(parser, xml)} → 代理接口；</li>
 *   <li>导入：{@code new JQuickExcelImportXmlParseFactory(in)} → {@code new JQuickXmlFactory(parser, xml)} → 代理接口。</li>
 * </ul>
 * 不使用任何自封装的公共方法或工具类。规则文件：{@code demo/sheet/jquick-excel.xml}。
 * 产物目录：{@code D:\test\excel}。
 */
public class SheetDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/sheet/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导出：SHEET 指定输出工作表名称，回读确认第 1 张表名。 */
    @Test
    public void exportSheet() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", 20);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", 21);
        rows.add(r2);
        JQuickRow r3 = new JQuickRow();
        r3.put("a", "王五");
        r3.put("b", 22);
        rows.add(r3);

        File out = new File(OUT_DIR, "sheet-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            SheetService service = factory.createApi(SheetService.class);
            service.exportSheet("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Assert.assertEquals("学生信息", wb.getSheetName(0));
        }
        System.out.println("【SHEET】导出指定工作表: " + out.getAbsolutePath());
    }

    /** 导入：SHEET="学生信息" 按名称选择。 */
    @Test
    public void importByName() throws Exception {
        try (InputStream in = SheetDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            SheetService service = factory.createApi(SheetService.class);
            List<JQuickRow> rows = service.importByName("field", "value");
            Assert.assertEquals(3, rows.size());
            Assert.assertEquals("张三", rows.get(0).get("name"));
            System.out.println("【SHEET】按名称导入 \"学生信息\": " + rows.size() + " 行");
        }
    }

    /** 导入：SHEET=1 按 1 基索引选择。 */
    @Test
    public void importByIndex() throws Exception {
        try (InputStream in = SheetDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            SheetService service = factory.createApi(SheetService.class);
            List<JQuickRow> rows = service.importByIndex("field", "value");
            Assert.assertEquals(3, rows.size());
            System.out.println("【SHEET】按索引 SHEET=1 导入: " + rows.size() + " 行");
        }
    }

    /** 导入：选择第 2 张工作表「班级信息」。 */
    @Test
    public void importClassSheet() throws Exception {
        try (InputStream in = SheetDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            SheetService service = factory.createApi(SheetService.class);
            List<JQuickRow> rows = service.importClassSheet("field", "value");
            Assert.assertEquals(2, rows.size());
            Assert.assertEquals("计算机1班", rows.get(0).get("className"));
            System.out.println("【SHEET】读取 \"班级信息\": " + rows.size() + " 行");
        }
    }
}
