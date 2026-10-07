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

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
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
import java.nio.file.Files;
import java.util.List;

/**
 * iosource 子包 demo：框架只认 InputStream / OutputStream。
 *
 * <p>独立规则文件：{@code demo/iosource/jquick-excel.xml}。
 * 导入侧演示 FileInputStream 与 ByteArrayInputStream；
 * 导出侧演示 FileOutputStream 与 ByteArrayOutputStream
 * （Web 下载时把字节数组写进 HttpServletResponse.getOutputStream() 即可）。
 */
public class IoSourceDemo {

    private static final String XML = "demo/iosource/jquick-excel.xml";

    /** 从磁盘文件导入。 */
    @Test
    public void importFromFile() throws Exception {
        File file = DemoKit.prepareImportFile();
        try (InputStream in = new FileInputStream(file)) {
            IoSourceService service = DemoKit.importApi(XML, in, IoSourceService.class);
            List<JQuickRow> rows = service.importRows("field", "value");
            System.out.println("从文件导入 " + rows.size() + " 行");
            Assert.assertEquals(3, rows.size());
        }
    }

    /** 从字节数组导入（例如文件已上传到内存 / OSS SDK 返回 bytes）。 */
    @Test
    public void importFromBytes() throws Exception {
        byte[] bytes = Files.readAllBytes(DemoKit.prepareImportFile().toPath());
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            IoSourceService service = DemoKit.importApi(XML, in, IoSourceService.class);
            List<JQuickRow> rows = service.importRows("field", "value");
            System.out.println("从字节流导入 " + rows.size() + " 行");
            Assert.assertEquals(3, rows.size());
        }
    }

    /** 导出到磁盘文件。 */
    @Test
    public void exportToFile() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.personRows());
        File out = DemoKit.out("iosource-file.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            IoSourceService service = DemoKit.exportApi(XML, rows, os, IoSourceService.class);
            service.exportRows("field", "value");
        }
        Assert.assertTrue(out.length() > 0);
        System.out.println("导出文件大小: " + out.length() + " bytes");
    }

    /** 导出到字节数组，再用该字节数组重新打开工作簿验证。 */
    @Test
    public void exportToBytes() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.personRows());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IoSourceService service = DemoKit.exportApi(XML, rows, bos, IoSourceService.class);
        service.exportRows("field", "value");

        byte[] bytes = bos.toByteArray();
        System.out.println("字节数组长度: " + bytes.length + " bytes");
        Assert.assertTrue(bytes.length > 0);
        try (Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            Assert.assertEquals("输入输出源", wb.getSheetName(0));
        }
    }
}
