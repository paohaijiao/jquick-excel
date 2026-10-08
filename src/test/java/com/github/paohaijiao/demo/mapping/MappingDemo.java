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
package com.github.paohaijiao.demo.mapping;

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
 * 分类：<b>MAPPING</b> —— 源字段与表头文本映射（导出：字段→表头；导入：表头→字段）。
 *
 * <p>直接使用 {@link JQuickExcelExportXmlParseFactory} / {@link JQuickExcelImportXmlParseFactory} 入口，无自封装方法。
 */
public class MappingDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/mapping/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导出：源字段 a/b/c 经 MAPPING 变成表头文本。 */
    @Test
    public void exportMapping() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", 20);
        r1.put("c", "计算机1班");
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", 21);
        r2.put("c", "软件工程2班");
        rows.add(r2);

        File out = new File(OUT_DIR, "mapping-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            MappingService service = factory.createApi(MappingService.class);
            service.exportMapping("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            org.apache.poi.ss.usermodel.Row header = wb.getSheet("字段映射").getRow(0);
            Assert.assertEquals("姓名", header.getCell(0).getStringCellValue());
            Assert.assertEquals("年龄", header.getCell(1).getStringCellValue());
            Assert.assertEquals("班级", header.getCell(2).getStringCellValue());
        }
        System.out.println("【MAPPING】导出字段->表头: " + out.getAbsolutePath());
    }

    /** 导入：中文表头经 MAPPING 变成业务字段名。 */
    @Test
    public void importMapping() throws Exception {
        try (InputStream in = MappingDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            MappingService service = factory.createApi(MappingService.class);
            List<JQuickRow> rows = service.importMapping("field", "value");
            Assert.assertEquals(3, rows.size());
            Assert.assertEquals("2024001", rows.get(0).get("no"));
            Assert.assertEquals("张三", rows.get(0).get("name"));
            Assert.assertEquals("计算机1班", rows.get(0).get("className"));
            System.out.println("【MAPPING】导入表头->字段: " + rows.size() + " 行，首行=" + rows.get(0));
        }
    }
}
