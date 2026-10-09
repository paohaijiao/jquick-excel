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
package com.github.paohaijiao.biz.style;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
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
 * 场景 76：门店销售自助分析报表（自助报表类，🟢 纯 XML）。
 *
 * <p>业务：逐门店登记销售额与成本，算「毛利 = 销售额 - 成本」「毛利率 = 毛利 / 销售额」
 * 「销售占比 = 本店销售额 / 全部销售额」，末尾一行汇总销售额、成本、毛利与整体毛利率；
 * 毛利率最低的门店标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0076_style_store-sales-analysis.xml}：
 * FORMULAS 求逐行毛利 / 毛利率 / 占比与合计，STYLE 给合计行高亮、给最低毛利率门店标红；
 * Java 只构造销售数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0076StoreSalesAnalysisDemo {

    private static final String XML = "jquick/biz/style/0076_style_store-sales-analysis.xml";

    /** 导出：纯 XML 完成毛利、毛利率、销售占比、合计与最低毛利率标红。 */
    @Test
    public void exportStoreSalesAnalysis() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e/f/g 留空，由 FORMULAS 计算
        rows.add(store("城东店", "家电", 520000.00, 380000.00));
        rows.add(store("城西店", "服饰", 360000.00, 240000.00));
        rows.add(store("城南店", "生鲜", 280000.00, 230000.00));
        rows.add(store("城北店", "数码", 640000.00, 520000.00));
        rows.add(store("中心店", "家居", 410000.00, 300000.00));
        // 合计占位行：各列合计与整体毛利率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0076_style_store-sales-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("plum", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0076StoreSalesAnalysisService service = factory.createApi(Style0076StoreSalesAnalysisService.class);
            service.exportStoreSalesAnalysis("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("门店销售自助分析报表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行毛利 = 销售额 - 成本 = 520000 - 380000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(140000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 城西店毛利率 = 毛利 / 销售额 = 120000 / 360000
            Cell f3 = sheet.getRow(2).getCell(5);
            Assert.assertEquals("E3/C3", f3.getCellFormula());
            Assert.assertEquals(120000.00 / 360000.00, evaluator.evaluate(f3).getNumberValue(), 0.0001);
            // 销售占比 = 本店销售额 / 全部销售额（合计单元格 C7）
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("C2/C7", g2.getCellFormula());
            Assert.assertEquals(520000.00 / 2210000.00, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计：销售额 = SUM(C2:C6) = 2210000
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(2210000.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计：整体毛利率 = 毛利合计 / 销售额合计 = 540000 / 2210000
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("E7/C7", f7.getCellFormula());
            Assert.assertEquals(540000.00 / 2210000.00, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 毛利率最低的「城南店」（Excel 第 4 行）毛利率标红加粗
            XSSFCellStyle lowStyle = (XSSFCellStyle) sheet.getRow(3).getCell(5).getCellStyle();
            Assert.assertTrue("最低毛利率应加粗", lowStyle.getFont().getBold());
            Assert.assertEquals("最低毛利率应标红", (int) IndexedColors.RED.getIndex(), (int) lowStyle.getFont().getColor());

            System.out.println("【场景76】门店销售自助分析报表导出: " + out.getAbsolutePath()
                    + "，销售额合计 " + evaluator.evaluate(c7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的门店销售自助分析报表（先重算公式）。 */
    @Test
    public void importStoreSalesAnalysis() throws Exception {
        File src = BizKit.outFile("style", "0076_style_store-sales-analysis.xlsx");
        if (!src.exists()) {
            exportStoreSalesAnalysis();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0076_style_store-sales-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0076StoreSalesAnalysisService service = factory.createApi(Style0076StoreSalesAnalysisService.class);
            List<JQuickRow> rows = service.importStoreSalesAnalysis("field", "value");

            System.out.println("【场景76】门店销售自助分析报表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    门店=" + r.get("store") + ", 品类=" + r.get("category")
                        + ", 销售额=" + r.get("salesAmount") + ", 成本=" + r.get("costAmount")
                        + ", 毛利=" + r.get("grossProfit") + ", 毛利率=" + r.get("grossMargin")
                        + ", 销售占比=" + r.get("salesShare"));
            }
            // 表头不计入数据行：5 家门店 + 1 行合计
            Assert.assertEquals("5 家门店 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一家门店（a=门店，b=品类，c=销售额，d=成本，e/f/g 留空）。 */
    private static JQuickRow store(String storeName, String category, double salesAmount, double costAmount) {
        JQuickRow row = new JQuickRow();
        row.put("a", storeName);
        row.put("b", category);
        row.put("c", salesAmount);
        row.put("d", costAmount);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }
}
