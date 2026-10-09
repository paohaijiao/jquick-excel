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
package com.github.paohaijiao.biz.graph;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
 * 场景 105：产品季度销量对比（🟢 纯 XML 无代码 + GRAPH 柱形图）。
 *
 * <p>业务：四个产品的一至四季度销量，行末计算全年合计，末行按季度汇总；附柱形图对比各产品分季走势。
 * 统计与图表全部写在 {@code jquick/biz/graph/0105_graph_product-quarterly-sales.xml}：
 * <ul>
 *   <li>FORMULAS：行末全年合计 {@code SUM(B2:E2)}、末行分季度汇总；</li>
 *   <li>STYLE：合计行加粗高亮；</li>
 *   <li>GRAPH：柱形图（COLUMN），图表数据写入独立的「产品季度销量图」sheet。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Graph0105ProductQuarterlySalesDemo {

    private static final String XML = "jquick/biz/graph/0105_graph_product-quarterly-sales.xml";

    /** 导出：4 个产品 + 1 行合计 + 柱形图，回读校验全年合计、末行汇总与图表 sheet。 */
    @Test
    public void exportProductQuarterlySales() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("产品A", 120, 180, 150, 200));
        rows.add(detail("产品B", 200, 210, 220, 240));
        rows.add(detail("产品C", 150, 190, 170, 210));
        rows.add(detail("产品D", 180, 160, 200, 190));
        rows.add(totalRow());

        File out = BizKit.outFile("graph", "0105_graph_product-quarterly-sales.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("oceanBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0105ProductQuarterlySalesService service = factory.createApi(Graph0105ProductQuarterlySalesService.class);
            service.exportProductQuarterlySales("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("产品季度销量");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 行末全年合计 = SUM(B2:E2)
            Assert.assertEquals("SUM(B2:E2)", sheet.getRow(1).getCell(5).getCellFormula());
            Assert.assertEquals(650.0, evaluator.evaluate(sheet.getRow(1).getCell(5)).getNumberValue(), 0.0001);
            // 末行按季度汇总
            Assert.assertEquals("SUM(B2:B5)", sheet.getRow(5).getCell(1).getCellFormula());
            Assert.assertEquals(650.0, evaluator.evaluate(sheet.getRow(5).getCell(1)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(F2:F5)", sheet.getRow(5).getCell(5).getCellFormula());
            Assert.assertEquals(2970.0, evaluator.evaluate(sheet.getRow(5).getCell(5)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());
            // 柱形图数据落在独立的「产品季度销量图」sheet
            Assert.assertNotNull("应生成「产品季度销量图」图表 sheet", wb.getSheet("产品季度销量图"));

            System.out.println("【场景105】产品季度销量对比导出: " + out.getAbsolutePath()
                    + "，sheet 列表=" + wb.getNumberOfSheets() + " 个");
        }
    }

    /** 导入解析：读取导出的产品季度销量对比（先重算公式），打印每行字段。 */
    @Test
    public void importProductQuarterlySales() throws Exception {
        File src = BizKit.outFile("graph", "0105_graph_product-quarterly-sales.xlsx");
        if (!src.exists()) {
            exportProductQuarterlySales();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("graph", "0105_graph_product-quarterly-sales-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0105ProductQuarterlySalesService service = factory.createApi(Graph0105ProductQuarterlySalesService.class);
            List<JQuickRow> rows = service.importProductQuarterlySales("field", "value");

            System.out.println("【场景105】产品季度销量对比导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    产品=" + r.get("product") + ", 一季度=" + r.get("q1") + ", 全年=" + r.get("total"));
            }
            Assert.assertEquals("4 行明细 + 1 行合计", 5, rows.size());
        }
    }

    /** 构造一条产品明细（f=全年由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String product, double q1, double q2, double q3, double q4) {
        JQuickRow row = new JQuickRow();
        row.put("a", product);
        row.put("b", q1);
        row.put("c", q2);
        row.put("d", q3);
        row.put("e", q4);
        row.put("f", null);
        return row;
    }

    /** 合计行：只写「合计」文字，其余列以空值占位（必须补齐全部映射列）。 */
    private static JQuickRow totalRow() {
        JQuickRow row = new JQuickRow();
        row.put("a", "合计");
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }
}
