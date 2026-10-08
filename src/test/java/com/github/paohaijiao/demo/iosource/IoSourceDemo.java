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
package com.github.paohaijiao.demo.iosource;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类：<b>输入输出源</b> —— 框架只认 {@link InputStream} / {@link OutputStream}。
 *
 * <p>规则文件 {@code demo/iosource/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory} / {@link JQuickExcelImportXmlParseFactory}。
 * 导入侧演示 FileInputStream 与 ByteArrayInputStream；导出侧演示 FileOutputStream 与
 * ByteArrayOutputStream（Web 下载时把字节数组写进 HttpServletResponse 即可）。
 * 不使用任何自封装方法，字节拷贝就地内联。产物目录：{@code D:\test\excel}。
 */
public class IoSourceDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/iosource/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 从磁盘文件导入。 */
    @Test
    public void importFromFile() throws Exception {
        File file = new File(IoSourceDemo.class.getClassLoader()
                .getResource("demo/import-source.xlsx").toURI());
        try (InputStream in = new FileInputStream(file)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            IoSourceService service = factory.createApi(IoSourceService.class);
            List<JQuickRow> rows = service.importRows("field", "value");
            System.out.println("【输入输出源】从文件导入 " + rows.size() + " 行");
            Assert.assertEquals(3, rows.size());
        }
    }

    /** 从字节数组导入（例如文件已上传到内存 / OSS SDK 返回 bytes）。 */
    @Test
    public void importFromBytes() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int len;
        try (InputStream is = IoSourceDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            while ((len = is.read(chunk)) != -1) {
                buffer.write(chunk, 0, len);
            }
        }
        byte[] bytes = buffer.toByteArray();

        try (InputStream in = new ByteArrayInputStream(bytes)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            IoSourceService service = factory.createApi(IoSourceService.class);
            List<JQuickRow> rows = service.importRows("field", "value");
            System.out.println("【输入输出源】从字节流导入 " + rows.size() + " 行，字节数=" + bytes.length);
            Assert.assertEquals(3, rows.size());
        }
    }

    /** 导出到磁盘文件。 */
    @Test
    public void exportToFile() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", 20);
        rows.add(r1);

        File out = new File(OUT_DIR, "iosource-file.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            IoSourceService service = factory.createApi(IoSourceService.class);
            service.exportRows("field", "value");
        }
        Assert.assertTrue(out.length() > 0);
        System.out.println("【输入输出源】导出文件大小: " + out.length() + " bytes");
    }

    /** 导出到字节数组，再用该字节数组重新打开工作簿验证。 */
    @Test
    public void exportToBytes() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", 20);
        rows.add(r1);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, bos);
        JQuickFactory factory = new JQuickXmlFactory(parser, XML);
        IoSourceService service = factory.createApi(IoSourceService.class);
        service.exportRows("field", "value");

        byte[] bytes = bos.toByteArray();
        System.out.println("【输入输出源】字节数组长度: " + bytes.length + " bytes");
        Assert.assertTrue(bytes.length > 0);
        try (Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            Assert.assertEquals("输入输出源", wb.getSheetName(0));
        }
    }
}
