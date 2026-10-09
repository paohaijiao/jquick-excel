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
 * 场景 108：广告投产分析（🟢 纯 XML 无代码 + GRAPH 散点图）。
 *
 * <p>业务：各门店广告投入与销售额，行末算投产比（销售额 / 广告投入），末行汇总；
 * 附散点图看投放与产出的关系。统计与图表全部写在
 * {@code jquick/biz/graph/0108_graph_ad-spend-sales.xml}：
 * <ul>
 *   <li>FORMULAS：行末投产比 {@code C/B}、末行汇总与整体投产比；</li>
 *   <li>STYLE：合计行加粗高亮；</li>
 *   <li>GRAPH：散点图（SCATTER），图表数据写入独立的「广告投产散点图」sheet。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Graph0108AdSpendSalesDemo {

    private static final String XML = "jquick/biz/graph/0108_graph_ad-spend-sales.xml";

    /** 导出：4 个门店 + 1 行合计 + 散点图，回读校验投产比、汇总与图表 sheet。 */
    @Test
    public void exportAdSpendSales() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("门店A", 50000, 300000));
        rows.add(detail("门店B", 80000, 440000));
        rows.add(detail("门店C", 120000, 720000));
        rows.add(detail("门店D", 150000, 810000));
        rows.add(totalRow());

        File out = BizKit.outFile("graph", "0108_graph_ad-spend-sales.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("royalGold", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0108AdSpendSalesService service = factory.createApi(Graph0108AdSpendSalesService.class);
            service.exportAdSpendSales("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("广告投产分析");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 行末投产比 = 销售额 / 广告投入
            Assert.assertEquals("C2/B2", sheet.getRow(1).getCell(3).getCellFormula());
            Assert.assertEquals(6.0, evaluator.evaluate(sheet.getRow(1).getCell(3)).getNumberValue(), 0.0001);
            // 末行汇总与整体投产比
            Assert.assertEquals("SUM(C2:C5)", sheet.getRow(5).getCell(2).getCellFormula());
            Assert.assertEquals(2270000.0, evaluator.evaluate(sheet.getRow(5).getCell(2)).getNumberValue(), 0.0001);
            Assert.assertEquals("C6/B6", sheet.getRow(5).getCell(3).getCellFormula());
            Assert.assertEquals(5.675, evaluator.evaluate(sheet.getRow(5).getCell(3)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());
            // 散点图数据落在独立的「广告投产散点图」sheet
            Assert.assertNotNull("应生成「广告投产散点图」图表 sheet", wb.getSheet("广告投产散点图"));

            System.out.println("【场景108】广告投产分析导出: " + out.getAbsolutePath()
                    + "，sheet 列表=" + wb.getNumberOfSheets() + " 个");
        }
    }

    /** 导入解析：读取导出的广告投产分析（先重算公式），打印每行字段。 */
    @Test
    public void importAdSpendSales() throws Exception {
        File src = BizKit.outFile("graph", "0108_graph_ad-spend-sales.xlsx");
        if (!src.exists()) {
            exportAdSpendSales();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("graph", "0108_graph_ad-spend-sales-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0108AdSpendSalesService service = factory.createApi(Graph0108AdSpendSalesService.class);
            List<JQuickRow> rows = service.importAdSpendSales("field", "value");

            System.out.println("【场景108】广告投产分析导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    门店=" + r.get("store") + ", 广告投入=" + r.get("adSpend")
                        + ", 销售额=" + r.get("sales") + ", 投产比=" + r.get("roi"));
            }
            Assert.assertEquals("4 个门店 + 1 行合计", 5, rows.size());
        }
    }

    /** 构造一条门店明细（d=投产比由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String store, double adSpend, double sales) {
        JQuickRow row = new JQuickRow();
        row.put("a", store);
        row.put("b", adSpend);
        row.put("c", sales);
        row.put("d", null);
        return row;
    }

    /** 合计行：只写「合计」文字，其余列以空值占位（必须补齐全部映射列）。 */
    private static JQuickRow totalRow() {
        JQuickRow row = new JQuickRow();
        row.put("a", "合计");
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        return row;
    }
}
