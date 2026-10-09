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
 * 场景 93：利润同比对比分析表（数据对比差异类，🟢 纯 XML）。
 *
 * <p>业务：逐利润表项目登记本期与上期金额，算「同比增长额 = 本期 - 上期」
 * 「同比增长率 = 同比增长额 / 上期金额」，末尾一行汇总两期金额并给出整体增长；增长率最低项标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0093_style_profit-yoy-comparison.xml}：
 * FORMULAS 求逐行增长额 / 增长率、SUM 求两期合计、B7-C7 与 D7/C7 求整体口径，
 * STYLE 给合计行高亮、给增长率最低项标红；Java 只构造数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0093ProfitYoyComparisonDemo {

    private static final String XML = "jquick/biz/style/0093_style_profit-yoy-comparison.xml";

    /** 导出：纯 XML 完成逐行同比、两期合计与整体增长。 */
    @Test
    public void exportProfitYoyComparison() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // d/e 留空，由 FORMULAS 计算
        rows.add(item("营业收入", 5000000.00, 4200000.00));
        rows.add(item("营业成本", 3200000.00, 3000000.00));
        rows.add(item("销售费用", 600000.00, 500000.00));
        rows.add(item("管理费用", 400000.00, 380000.00));
        rows.add(item("财务费用", 100000.00, 120000.00));
        // 合计占位行：两期合计与整体增长留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0093_style_profit-yoy-comparison.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("lavenderPurple", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0093ProfitYoyComparisonService service = factory.createApi(Style0093ProfitYoyComparisonService.class);
            service.exportProfitYoyComparison("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("利润同比对比分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行增长额 = 本期 - 上期 = 5000000 - 4200000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("B2-C2", d2.getCellFormula());
            Assert.assertEquals(800000.00, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 首行增长率 = 增长额 / 上期 = 800000 / 4200000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/C2", e2.getCellFormula());
            Assert.assertEquals(800000.00 / 4200000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 合计：本期金额 = SUM(B2:B6) = 9300000
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(9300000.00, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 合计：增长额 = B7 - C7 = 9300000 - 8200000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("B7-C7", d7.getCellFormula());
            Assert.assertEquals(1100000.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 增长率最低的「财务费用」（Excel 第 6 行，增长率为负）标红加粗
            XSSFCellStyle rateStyle = (XSSFCellStyle) sheet.getRow(5).getCell(4).getCellStyle();
            Assert.assertTrue("最低增长率应加粗", rateStyle.getFont().getBold());
            Assert.assertEquals("最低增长率应标红", (int) IndexedColors.RED.getIndex(), (int) rateStyle.getFont().getColor());

            System.out.println("【场景93】利润同比对比分析表导出: " + out.getAbsolutePath()
                    + "，整体增长额 " + evaluator.evaluate(d7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的利润同比对比分析表（先重算公式）。 */
    @Test
    public void importProfitYoyComparison() throws Exception {
        File src = BizKit.outFile("style", "0093_style_profit-yoy-comparison.xlsx");
        if (!src.exists()) {
            exportProfitYoyComparison();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0093_style_profit-yoy-comparison-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0093ProfitYoyComparisonService service = factory.createApi(Style0093ProfitYoyComparisonService.class);
            List<JQuickRow> rows = service.importProfitYoyComparison("field", "value");

            System.out.println("【场景93】利润同比对比分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    项目=" + r.get("itemName") + ", 本期金额=" + r.get("currentAmount")
                        + ", 上期金额=" + r.get("previousAmount") + ", 同比增长额=" + r.get("growthAmount")
                        + ", 同比增长率=" + r.get("growthRate"));
            }
            // 表头不计入数据行：5 个项目 + 1 行合计
            Assert.assertEquals("5 个项目 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一个利润表项目（a=项目，b=本期，c=上期，d/e 留空）。 */
    private static JQuickRow item(String itemName, double currentAmount, double previousAmount) {
        JQuickRow row = new JQuickRow();
        row.put("a", itemName);
        row.put("b", currentAmount);
        row.put("c", previousAmount);
        row.put("d", null);
        row.put("e", null);
        return row;
    }
}
