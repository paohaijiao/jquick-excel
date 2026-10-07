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

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * graph 子包 demo：GRAPH DSL 生成柱状图。
 *
 * <p>独立规则文件：{@code demo/graph/jquick-excel.xml}。
 */
public class GraphDemo {

    @Test
    public void exportGraph() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.productRows());
        File out = DemoKit.out("graph-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            GraphService service = DemoKit.exportApi(
                    "demo/graph/jquick-excel.xml", rows, os, GraphService.class);
            service.exportGraph("field", "value");
        }

        // 回读：图表由独立 sheet + drawing/charts 部件承载
        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Assert.assertNotNull("应当生成图表 sheet", wb.getSheet("季度销售统计"));
        }
        // xlsx 本质是 zip，断言里面确实存在图表与绘图部件
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
        System.out.println("图表已生成: " + out.getName());
    }
}
