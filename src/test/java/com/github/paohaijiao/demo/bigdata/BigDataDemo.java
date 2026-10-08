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
package com.github.paohaijiao.demo.bigdata;

import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
 * 分类：<b>大数据量导入导出</b>。
 *
 * <p>导出通过框架入口 {@link JQuickExcelExportXmlParseFactory}：数据量达到
 * {@code JQuickExcelConfig#getStreamingExportThreshold()}（默认 5000）时自动切 SXSSF 流式。
 * 大文件导入走 {@link JExcelImportHandler#importDataInBatch}（{@link JExcelImportModel} 分批消费），
 * 避免一次性堆成大 List 造成 OOM。不使用任何自封装公共方法，夹具就地生成。
 * 产物目录：{@code D:\test\excel}。
 */
public class BigDataDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/bigdata/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 2 万行导出：超过 5000 阈值自动走 SXSSF 流式，写出后校验文件非空。 */
    @Test
    public void exportLargeData() throws Exception {
        int total = 20000;
        List<JQuickRow> rows = new ArrayList<>(total);
        for (int i = 1; i <= total; i++) {
            JQuickRow row = new JQuickRow();
            row.put("a", "用户" + i);
            row.put("b", "部门-" + (i % 100));
            row.put("c", i);
            rows.add(row);
        }

        File out = new File(OUT_DIR, "bigdata-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            BigDataService service = factory.createApi(BigDataService.class);
            service.exportLarge("field", "value");
        }
        System.out.println("【大数据】导出 " + total + " 行，文件大小=" + out.length() + " bytes");
        Assert.assertTrue(out.length() > 0);
    }

    /** 1 万行导入（XML 入口全量返回）：先就地生成流式夹具，再按规则导入。 */
    @Test
    public void importLargeData() throws Exception {
        int total = 10000;
        File fixture = new File(OUT_DIR, "bigdata-import-source.xlsx");
        try (SXSSFWorkbook wb = new SXSSFWorkbook()) {
            Sheet sheet = wb.createSheet("大数据");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("姓名");
            header.createCell(1).setCellValue("部门");
            header.createCell(2).setCellValue("编号");
            for (int i = 1; i <= total; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue("用户" + i);
                row.createCell(1).setCellValue("部门-" + (i % 100));
                row.createCell(2).setCellValue(i);
            }
            try (OutputStream os = new FileOutputStream(fixture)) {
                wb.write(os);
            }
            wb.dispose();
        }

        try (InputStream in = new FileInputStream(fixture)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            BigDataService service = factory.createApi(BigDataService.class);
            List<JQuickRow> rows = service.importLarge("field", "value");
            System.out.println("【大数据】XML 入口导入 " + rows.size() + " 行，首行 name=" + rows.get(0).get("name"));
            Assert.assertEquals(total, rows.size());
            Assert.assertEquals("用户1", rows.get(0).get("name"));
        }
    }

    /** 1 万行导入（分批消费）：每 1000 行一页，边读边处理，避免 OOM。 */
    @Test
    public void importInBatch() throws Exception {
        int total = 10000;
        File fixture = new File(OUT_DIR, "bigdata-batch-source.xlsx");
        try (SXSSFWorkbook wb = new SXSSFWorkbook()) {
            Sheet sheet = wb.createSheet("大数据");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("姓名");
            header.createCell(1).setCellValue("部门");
            header.createCell(2).setCellValue("编号");
            for (int i = 1; i <= total; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue("用户" + i);
                row.createCell(1).setCellValue("部门-" + (i % 100));
                row.createCell(2).setCellValue(i);
            }
            try (OutputStream os = new FileOutputStream(fixture)) {
                wb.write(os);
            }
            wb.dispose();
        }

        JExcelImportModel model = new JExcelImportModel();
        model.setSheet("大数据");
        model.setHeader(true);
        Map<String, String> mapping = new HashMap<>();
        mapping.put("姓名", "name");
        mapping.put("部门", "dept");
        mapping.put("编号", "no");
        model.setMappings(mapping);

        final int[] consumed = {0};
        final int[] pages = {0};
        try (InputStream in = new FileInputStream(fixture)) {
            JExcelImportHandler handler = new JExcelImportHandler(in);
            int totalRead = handler.importDataInBatch(model, 1000, batch -> {
                consumed[0] += batch.size();
                pages[0]++;
                return true;
            });
            System.out.println("【大数据】分批导入读取 " + totalRead + " 行，共 " + pages[0] + " 页，消费 " + consumed[0] + " 行");
            Assert.assertEquals(total, totalRead);
            Assert.assertEquals(total, consumed[0]);
            Assert.assertEquals(10, pages[0]);
        }
    }
}
