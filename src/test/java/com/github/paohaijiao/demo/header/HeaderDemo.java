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
package com.github.paohaijiao.demo.header;

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
 * 分类：<b>HEADER</b> —— 表头开关。
 *
 * <p>导出 HEADER=false 只写数据行；导入 HEADER=false 会连第 1 行一起读成数据
 * （但列名映射仍取第 1 行），因此比 HEADER=true 多 1 行。
 * 直接使用 {@link JQuickExcelExportXmlParseFactory} / {@link JQuickExcelImportXmlParseFactory} 入口，无自封装方法。
 */
public class HeaderDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/header/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导出：HEADER=true，第 1 行是表头。 */
    @Test
    public void exportWithHeader() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", 20);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", 21);
        rows.add(r2);

        File out = new File(OUT_DIR, "header-true.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            HeaderService service = factory.createApi(HeaderService.class);
            service.exportWithHeader("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Assert.assertEquals("姓名", wb.getSheet("有表头").getRow(0).getCell(0).getStringCellValue());
        }
        System.out.println("【HEADER】HEADER=true 导出: " + out.getAbsolutePath());
    }

    /** 导出：HEADER=false，第 1 行就是数据。 */
    @Test
    public void exportWithoutHeader() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", 20);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", 21);
        rows.add(r2);

        File out = new File(OUT_DIR, "header-false.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            HeaderService service = factory.createApi(HeaderService.class);
            service.exportWithoutHeader("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            // 无表头：第 1 行直接是数据「张三」
            Assert.assertEquals("张三", wb.getSheet("无表头").getRow(0).getCell(0).getStringCellValue());
        }
        System.out.println("【HEADER】HEADER=false 导出: " + out.getAbsolutePath());
    }

    /** 导入：HEADER=true 跳过表头，得到 3 行数据。 */
    @Test
    public void importWithHeader() throws Exception {
        try (InputStream in = HeaderDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            HeaderService service = factory.createApi(HeaderService.class);
            List<JQuickRow> rows = service.importWithHeader("field", "value");
            Assert.assertEquals(3, rows.size());
            System.out.println("【HEADER】导入 HEADER=true: " + rows.size() + " 行");
        }
    }

    /** 导入：HEADER=false 连表头行一起读，得到 4 行。 */
    @Test
    public void importWithoutHeader() throws Exception {
        try (InputStream in = HeaderDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            HeaderService service = factory.createApi(HeaderService.class);
            List<JQuickRow> rows = service.importWithoutHeader("field", "value");
            Assert.assertEquals(4, rows.size());
            System.out.println("【HEADER】导入 HEADER=false: " + rows.size() + " 行（表头行被当作数据）");
        }
    }
}
