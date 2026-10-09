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
 * 场景 85：月度费用预算执行偏差对比表（数据对比差异类，🟢 纯 XML）。
 *
 * <p>业务：逐费用项目对比预算与实际支出，算「偏差金额 = 预算 - 实际」与「偏差率 = 偏差 / 预算」，
 * 末尾一行汇总预算、实际与总偏差；超支最大的项目标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0085_style_expense-budget-variance.xml}：
 * FORMULAS 求逐行偏差 / 偏差率、SUM 求合计，STYLE 给合计行高亮、给超支最大项目标红；
 * Java 只构造费用数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0085ExpenseBudgetVarianceDemo {

    private static final String XML = "jquick/biz/style/0085_style_expense-budget-variance.xml";

    /** 导出：纯 XML 完成逐行偏差、偏差率、合计与超支标红。 */
    @Test
    public void exportExpenseBudgetVariance() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // d/e 留空，由 FORMULAS 计算
        rows.add(item("办公费", 50000.00, 46000.00, "节约"));
        rows.add(item("差旅费", 120000.00, 138000.00, "超支"));
        rows.add(item("招待费", 80000.00, 92000.00, "超支"));
        rows.add(item("培训费", 60000.00, 55000.00, "节约"));
        rows.add(item("水电费", 40000.00, 38000.00, "节约"));
        // 合计占位行：预算 / 实际 / 总偏差留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0085_style_expense-budget-variance.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sage", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0085ExpenseBudgetVarianceService service = factory.createApi(Style0085ExpenseBudgetVarianceService.class);
            service.exportExpenseBudgetVariance("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("月度费用预算执行偏差对比表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行偏差 = 预算 - 实际 = 50000 - 46000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("B2-C2", d2.getCellFormula());
            Assert.assertEquals(4000.00, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 超支最大的「差旅费」（Excel 第 3 行）偏差 = 120000 - 138000
            Cell d3 = sheet.getRow(2).getCell(3);
            Assert.assertEquals("B3-C3", d3.getCellFormula());
            Assert.assertEquals(-18000.00, evaluator.evaluate(d3).getNumberValue(), 0.0001);
            // 合计：预算合计 = SUM(B2:B6) = 350000
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(350000.00, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 合计：总偏差 = 预算合计 - 实际合计 = 350000 - 369000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("B7-C7", d7.getCellFormula());
            Assert.assertEquals(-19000.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 超支最大的偏差标红加粗
            XSSFCellStyle overStyle = (XSSFCellStyle) d3.getCellStyle();
            Assert.assertTrue("超支最大应加粗", overStyle.getFont().getBold());
            Assert.assertEquals("超支最大应标红", (int) IndexedColors.RED.getIndex(), (int) overStyle.getFont().getColor());

            System.out.println("【场景85】月度费用预算执行偏差对比表导出: " + out.getAbsolutePath()
                    + "，总偏差 " + evaluator.evaluate(d7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的月度费用预算执行偏差对比表（先重算公式）。 */
    @Test
    public void importExpenseBudgetVariance() throws Exception {
        File src = BizKit.outFile("style", "0085_style_expense-budget-variance.xlsx");
        if (!src.exists()) {
            exportExpenseBudgetVariance();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0085_style_expense-budget-variance-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0085ExpenseBudgetVarianceService service = factory.createApi(Style0085ExpenseBudgetVarianceService.class);
            List<JQuickRow> rows = service.importExpenseBudgetVariance("field", "value");

            System.out.println("【场景85】月度费用预算执行偏差对比表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    费用项目=" + r.get("expenseItem") + ", 预算金额=" + r.get("budgetAmount")
                        + ", 实际支出=" + r.get("actualAmount") + ", 偏差金额=" + r.get("varianceAmount")
                        + ", 偏差率=" + r.get("varianceRate") + ", 控制结论=" + r.get("controlConclusion"));
            }
            // 表头不计入数据行：5 个费用项目 + 1 行合计
            Assert.assertEquals("5 个费用项目 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条费用记录（a=项目，b=预算，c=实际，d/e 留空，f=控制结论）。 */
    private static JQuickRow item(String expenseItem, double budgetAmount, double actualAmount, String conclusion) {
        JQuickRow row = new JQuickRow();
        row.put("a", expenseItem);
        row.put("b", budgetAmount);
        row.put("c", actualAmount);
        row.put("d", null);
        row.put("e", null);
        row.put("f", conclusion);
        return row;
    }
}
