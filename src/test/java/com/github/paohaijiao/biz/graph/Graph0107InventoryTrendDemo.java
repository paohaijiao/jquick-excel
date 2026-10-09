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
 * 场景 107：库存量趋势（🟢 纯 XML 无代码 + GRAPH 三维面积图）。
 *
 * <p>业务：按月登记期初 / 入库 / 出库，滚动计算期末（期末 = 期初 + 入库 - 出库），末行汇总；
 * 附三维面积图看库存趋势。统计与图表全部写在
 * {@code jquick/biz/graph/0107_graph_inventory-trend.xml}：
 * <ul>
 *   <li>FORMULAS：行内期末滚动引用上一行（{@code E3='E2+C3-D3'}）、末行入库 / 出库汇总；</li>
 *   <li>STYLE：合计行加粗高亮；</li>
 *   <li>GRAPH：三维面积图（AREA3D），图表数据写入独立的「库存量趋势图」sheet。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Graph0107InventoryTrendDemo {

    private static final String XML = "jquick/biz/graph/0107_graph_inventory-trend.xml";

    /** 导出：4 个月 + 1 行合计 + 三维面积图，回读校验滚动期末、汇总与图表 sheet。 */
    @Test
    public void exportInventoryTrend() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("1月", 5000, 2000, 2200));
        rows.add(detail("2月", 4800, 2400, 2000));
        rows.add(detail("3月", 5200, 1800, 2400));
        rows.add(detail("4月", 4600, 2600, 2100));
        rows.add(totalRow());

        File out = BizKit.outFile("graph", "0107_graph_inventory-trend.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("forestGreen", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0107InventoryTrendService service = factory.createApi(Graph0107InventoryTrendService.class);
            service.exportInventoryTrend("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("库存量趋势");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首月期末 = 期初 + 入库 - 出库
            Assert.assertEquals("B2+C2-D2", sheet.getRow(1).getCell(4).getCellFormula());
            Assert.assertEquals(4800.0, evaluator.evaluate(sheet.getRow(1).getCell(4)).getNumberValue(), 0.0001);
            // 滚动期末：引用上一行
            Assert.assertEquals("E2+C3-D3", sheet.getRow(2).getCell(4).getCellFormula());
            Assert.assertEquals(5200.0, evaluator.evaluate(sheet.getRow(2).getCell(4)).getNumberValue(), 0.0001);
            // 末行入库 / 出库汇总，期末取最后一行
            Assert.assertEquals("SUM(C2:C5)", sheet.getRow(5).getCell(2).getCellFormula());
            Assert.assertEquals(8800.0, evaluator.evaluate(sheet.getRow(5).getCell(2)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(D2:D5)", sheet.getRow(5).getCell(3).getCellFormula());
            Assert.assertEquals(8700.0, evaluator.evaluate(sheet.getRow(5).getCell(3)).getNumberValue(), 0.0001);
            Assert.assertEquals("E5", sheet.getRow(5).getCell(4).getCellFormula());
            Assert.assertEquals(5100.0, evaluator.evaluate(sheet.getRow(5).getCell(4)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());
            // 三维面积图数据落在独立的「库存量趋势图」sheet
            Assert.assertNotNull("应生成「库存量趋势图」图表 sheet", wb.getSheet("库存量趋势图"));

            System.out.println("【场景107】库存量趋势导出: " + out.getAbsolutePath()
                    + "，sheet 列表=" + wb.getNumberOfSheets() + " 个");
        }
    }

    /** 导入解析：读取导出的库存量趋势（先重算公式），打印每行字段。 */
    @Test
    public void importInventoryTrend() throws Exception {
        File src = BizKit.outFile("graph", "0107_graph_inventory-trend.xlsx");
        if (!src.exists()) {
            exportInventoryTrend();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("graph", "0107_graph_inventory-trend-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0107InventoryTrendService service = factory.createApi(Graph0107InventoryTrendService.class);
            List<JQuickRow> rows = service.importInventoryTrend("field", "value");

            System.out.println("【场景107】库存量趋势导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    月份=" + r.get("month") + ", 期初=" + r.get("opening") + ", 期末=" + r.get("closing"));
            }
            Assert.assertEquals("4 个月 + 1 行合计", 5, rows.size());
        }
    }

    /** 构造一条月度明细（e=期末由 FORMULAS 滚动计算，占位保列）。 */
    private static JQuickRow detail(String month, double opening, double inbound, double outbound) {
        JQuickRow row = new JQuickRow();
        row.put("a", month);
        row.put("b", opening);
        row.put("c", inbound);
        row.put("d", outbound);
        row.put("e", null);
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
        return row;
    }
}
