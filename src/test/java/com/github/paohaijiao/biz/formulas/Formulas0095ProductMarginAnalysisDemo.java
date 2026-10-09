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
package com.github.paohaijiao.biz.formulas;

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
 * 场景 95：产品毛利贡献分析表（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：逐产品登记销售额与成本，算「毛利 = 销售额 - 成本」「毛利率 = 毛利 / 销售额」
 * 「贡献占比 = 毛利 / 毛利合计」，末尾一行汇总销售额、成本与毛利；毛利率最低的产品标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0095_formulas_product-margin-analysis.xml}：
 * FORMULAS 求逐行毛利 / 毛利率 / 贡献占比、SUM 求各列合计，
 * STYLE 给合计行高亮、给毛利率最低的产品标红；Java 只构造数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0095ProductMarginAnalysisDemo {

    private static final String XML = "jquick/biz/formulas/0095_formulas_product-margin-analysis.xml";

    /** 导出：纯 XML 完成逐行毛利、毛利率、贡献占比与合计。 */
    @Test
    public void exportProductMarginAnalysis() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // d/e/f 留空，由 FORMULAS 计算
        rows.add(product("A产品", 500000.00, 350000.00));
        rows.add(product("B产品", 300000.00, 240000.00));
        rows.add(product("C产品", 450000.00, 400000.00));
        rows.add(product("D产品", 200000.00, 120000.00));
        rows.add(product("E产品", 250000.00, 180000.00));
        // 合计占位行：销售额 / 成本 / 毛利汇总留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0095_formulas_product-margin-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sakuraPink", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0095ProductMarginAnalysisService service = factory.createApi(Formulas0095ProductMarginAnalysisService.class);
            service.exportProductMarginAnalysis("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("产品毛利贡献分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行毛利 = 销售额 - 成本 = 500000 - 350000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("B2-C2", d2.getCellFormula());
            Assert.assertEquals(150000.00, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 首行毛利率 = 毛利 / 销售额 = 150000 / 500000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/B2", e2.getCellFormula());
            Assert.assertEquals(0.30, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 首行贡献占比 = 毛利 / 毛利合计 = 150000 / 410000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2/D7", f2.getCellFormula());
            Assert.assertEquals(150000.00 / 410000.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 毛利率最低的「C产品」（Excel 第 4 行）= 50000 / 450000
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals("D4/B4", e4.getCellFormula());
            Assert.assertEquals(50000.00 / 450000.00, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 合计：毛利 = B7 - C7 = 1700000 - 1290000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("B7-C7", d7.getCellFormula());
            Assert.assertEquals(410000.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 最低毛利率标红加粗
            XSSFCellStyle marginStyle = (XSSFCellStyle) e4.getCellStyle();
            Assert.assertTrue("最低毛利率应加粗", marginStyle.getFont().getBold());
            Assert.assertEquals("最低毛利率应标红", (int) IndexedColors.RED.getIndex(), (int) marginStyle.getFont().getColor());

            System.out.println("【场景95】产品毛利贡献分析表导出: " + out.getAbsolutePath()
                    + "，合计毛利 " + evaluator.evaluate(d7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的产品毛利贡献分析表（先重算公式）。 */
    @Test
    public void importProductMarginAnalysis() throws Exception {
        File src = BizKit.outFile("formulas", "0095_formulas_product-margin-analysis.xlsx");
        if (!src.exists()) {
            exportProductMarginAnalysis();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0095_formulas_product-margin-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0095ProductMarginAnalysisService service = factory.createApi(Formulas0095ProductMarginAnalysisService.class);
            List<JQuickRow> rows = service.importProductMarginAnalysis("field", "value");

            System.out.println("【场景95】产品毛利贡献分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    产品名称=" + r.get("productName") + ", 销售额=" + r.get("salesAmount")
                        + ", 成本=" + r.get("costAmount") + ", 毛利=" + r.get("grossProfit")
                        + ", 毛利率=" + r.get("grossMargin") + ", 贡献占比=" + r.get("contributionRatio"));
            }
            // 表头不计入数据行：5 款产品 + 1 行合计
            Assert.assertEquals("5 款产品 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一款产品（a=产品，b=销售额，c=成本，d/e/f 留空）。 */
    private static JQuickRow product(String productName, double salesAmount, double costAmount) {
        JQuickRow row = new JQuickRow();
        row.put("a", productName);
        row.put("b", salesAmount);
        row.put("c", costAmount);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }
}
