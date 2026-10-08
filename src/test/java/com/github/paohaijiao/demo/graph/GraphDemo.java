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
package com.github.paohaijiao.demo.graph;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 分类：<b>GRAPH</b> —— 声明式图表（柱形 / 折线 / 饼图）。
 *
 * <p>规则文件 {@code demo/graph/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory}，不使用任何自封装方法。
 * 每个 EXCEL 只承载一个 GRAPH 块；图表由独立 sheet + drawing/charts 部件承载，
 * 且图表数据会写入「TITLE 所示 sheet」。产物目录：{@code D:\test\excel}。
 */
public class GraphDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/graph/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** TYPE=COLUMN：柱形图，回读图表 sheet 数据与 xl/charts 部件类型。 */
    @Test
    public void exportColumnChart() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "产品A");
        r1.put("b", 120);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "产品B");
        r2.put("b", 200);
        rows.add(r2);
        JQuickRow r3 = new JQuickRow();
        r3.put("a", "产品C");
        r3.put("b", 150);
        rows.add(r3);

        File out = new File(OUT_DIR, "graph-column.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            GraphService service = factory.createApi(GraphService.class);
            service.exportColumnChart("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Assert.assertNotNull("应以 TITLE 生成图表数据 sheet", wb.getSheet("季度销售统计"));
            // 图表数据：第 0 行是「Categories + 各序列名」，第 1 行起是分类 + 数值
            org.apache.poi.ss.usermodel.Sheet chartSheet = wb.getSheet("季度销售统计");
            Assert.assertEquals("第一季度", chartSheet.getRow(0).getCell(1).getStringCellValue());
            Assert.assertEquals("产品A", chartSheet.getRow(1).getCell(0).getStringCellValue());
            Assert.assertEquals(120.0, chartSheet.getRow(1).getCell(1).getNumericCellValue(), 0.0001);
        }

        String chartXml = null;
        try (ZipFile zip = new ZipFile(out)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith("xl/charts/chart")) {
                    try (InputStream in = zip.getInputStream(entry)) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096];
                        int n;
                        while ((n = in.read(buf)) > 0) {
                            bos.write(buf, 0, n);
                        }
                        chartXml = new String(bos.toByteArray(), StandardCharsets.UTF_8);
                    }
                }
            }
        }
        Assert.assertNotNull("xlsx 中应包含 xl/charts/chart*.xml 部件", chartXml);
        Assert.assertTrue("COLUMN 应对应 barChart 部件", chartXml.contains("barChart"));
        System.out.println("【GRAPH】柱形图已生成: " + out.getName());
    }

    /** TYPE=LINE：折线图。 */
    @Test
    public void exportLineChart() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow row = new JQuickRow();
        row.put("a", "一月");
        row.put("b", 100);
        rows.add(row);

        File out = new File(OUT_DIR, "graph-line.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            GraphService service = factory.createApi(GraphService.class);
            service.exportLineChart("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Assert.assertNotNull("应以 TITLE 生成图表数据 sheet", wb.getSheet("月度销量趋势"));
        }

        String chartXml = null;
        try (ZipFile zip = new ZipFile(out)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith("xl/charts/chart")) {
                    try (InputStream in = zip.getInputStream(entry)) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096];
                        int n;
                        while ((n = in.read(buf)) > 0) {
                            bos.write(buf, 0, n);
                        }
                        chartXml = new String(bos.toByteArray(), StandardCharsets.UTF_8);
                    }
                }
            }
        }
        Assert.assertNotNull("xlsx 中应包含 xl/charts/chart*.xml 部件", chartXml);
        Assert.assertTrue("LINE 应对应 lineChart 部件", chartXml.contains("lineChart"));
        System.out.println("【GRAPH】折线图已生成: " + out.getName());
    }

    /** TYPE=PIE：饼图。 */
    @Test
    public void exportPieChart() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow row = new JQuickRow();
        row.put("a", "产品A");
        row.put("b", 120);
        rows.add(row);

        File out = new File(OUT_DIR, "graph-pie.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            GraphService service = factory.createApi(GraphService.class);
            service.exportPieChart("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Assert.assertNotNull("应以 TITLE 生成图表数据 sheet", wb.getSheet("销量占比"));
        }

        String chartXml = null;
        try (ZipFile zip = new ZipFile(out)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith("xl/charts/chart")) {
                    try (InputStream in = zip.getInputStream(entry)) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buf = new byte[4096];
                        int n;
                        while ((n = in.read(buf)) > 0) {
                            bos.write(buf, 0, n);
                        }
                        chartXml = new String(bos.toByteArray(), StandardCharsets.UTF_8);
                    }
                }
            }
        }
        Assert.assertNotNull("xlsx 中应包含 xl/charts/chart*.xml 部件", chartXml);
        Assert.assertTrue("PIE 应对应 pieChart 部件", chartXml.contains("pieChart"));
        System.out.println("【GRAPH】饼图已生成: " + out.getName());
    }
}
