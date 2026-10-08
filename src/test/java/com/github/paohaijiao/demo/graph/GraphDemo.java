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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类：<b>GRAPH</b> —— 图表（饼图 / 柱状图等）配置。
 *
 * <p>规则文件 {@code demo/graph/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory}，不使用任何自封装方法。
 * 图表由独立 sheet + drawing/charts 部件承载。产物目录：{@code D:\test\excel}。
 */
public class GraphDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/graph/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导出声明式柱状图，回读校验图表 sheet 与 xl/charts 部件。 */
    @Test
    public void exportGraph() throws Exception {
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

        File out = new File(OUT_DIR, "graph-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            GraphService service = factory.createApi(GraphService.class);
            service.exportGraph("field", "value");
        }

        // 回读：图表由独立 sheet + drawing/charts 部件承载
        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Assert.assertNotNull("应当生成图表 sheet", wb.getSheet("季度销售统计"));
        }
        // xlsx 本质是 zip，断言里面确实存在图表部件
        boolean hasChart = false;
        try (java.util.zip.ZipFile zip = new java.util.zip.ZipFile(out)) {
            java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith("xl/charts/chart")) {
                    hasChart = true;
                }
            }
        }
        Assert.assertTrue("xlsx 中应包含 xl/charts/chart*.xml 部件", hasChart);
        System.out.println("【GRAPH】图表已生成: " + out.getName());
    }
}
