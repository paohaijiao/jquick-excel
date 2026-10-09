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
 * 场景 84：应收账款账龄分析表（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：按客户拆分应收账龄区间（30 天内 / 31-60 天 / 61 天以上），
 * 算「逾期金额 = 31-60 天 + 61 天以上」「逾期占比 = 逾期金额 / 应收金额」，
 * 末尾一行汇总各账龄区间与整体逾期占比；逾期占比最高的客户标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0084_style_accounts-receivable-aging.xml}：
 * FORMULAS 求逐行逾期金额 / 占比、SUM 求各区间合计，STYLE 给合计行高亮、给最高逾期占比标红；
 * Java 只构造账龄数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0084AccountsReceivableAgingDemo {

    private static final String XML = "jquick/biz/style/0084_style_accounts-receivable-aging.xml";

    /** 导出：纯 XML 完成逐行逾期金额、占比、合计与最高占比标红。 */
    @Test
    public void exportAccountsReceivableAging() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f/g 留空，由 FORMULAS 计算
        rows.add(customer("华宇集团", 500000.00, 400000.00, 60000.00, 40000.00));
        rows.add(customer("东方贸易", 320000.00, 320000.00, 0.00, 0.00));
        rows.add(customer("金泰实业", 280000.00, 180000.00, 50000.00, 50000.00));
        rows.add(customer("恒信科技", 150000.00, 150000.00, 0.00, 0.00));
        rows.add(customer("瑞丰商贸", 260000.00, 100000.00, 90000.00, 70000.00));
        // 合计占位行：各区间合计与整体逾期占比留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0084_style_accounts-receivable-aging.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("turquoise", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0084AccountsReceivableAgingService service = factory.createApi(Style0084AccountsReceivableAgingService.class);
            service.exportAccountsReceivableAging("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("应收账款账龄分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行逾期金额 = 31-60 天 + 61 天以上 = 60000 + 40000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2+E2", f2.getCellFormula());
            Assert.assertEquals(100000.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 首行逾期占比 = 逾期金额 / 应收金额 = 100000 / 500000
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("F2/B2", g2.getCellFormula());
            Assert.assertEquals(0.20, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计：应收金额 = SUM(B2:B6) = 1510000
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(1510000.00, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 合计：整体逾期占比 = 逾期合计 / 应收合计 = 360000 / 1510000
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("F7/B7", g7.getCellFormula());
            Assert.assertEquals(360000.00 / 1510000.00, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 逾期占比最高的「瑞丰商贸」（Excel 第 6 行）逾期占比标红加粗
            Cell g6 = sheet.getRow(5).getCell(6);
            XSSFCellStyle highStyle = (XSSFCellStyle) g6.getCellStyle();
            Assert.assertTrue("最高逾期占比应加粗", highStyle.getFont().getBold());
            Assert.assertEquals("最高逾期占比应标红", (int) IndexedColors.RED.getIndex(), (int) highStyle.getFont().getColor());

            System.out.println("【场景84】应收账款账龄分析表导出: " + out.getAbsolutePath()
                    + "，整体逾期占比 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的应收账款账龄分析表（先重算公式）。 */
    @Test
    public void importAccountsReceivableAging() throws Exception {
        File src = BizKit.outFile("style", "0084_style_accounts-receivable-aging.xlsx");
        if (!src.exists()) {
            exportAccountsReceivableAging();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0084_style_accounts-receivable-aging-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0084AccountsReceivableAgingService service = factory.createApi(Style0084AccountsReceivableAgingService.class);
            List<JQuickRow> rows = service.importAccountsReceivableAging("field", "value");

            System.out.println("【场景84】应收账款账龄分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    客户=" + r.get("customer") + ", 应收金额=" + r.get("receivableAmount")
                        + ", 30天内=" + r.get("within30") + ", 31-60天=" + r.get("days31to60")
                        + ", 61天以上=" + r.get("over60") + ", 逾期金额=" + r.get("overdueAmount")
                        + ", 逾期占比=" + r.get("overdueRatio"));
            }
            // 表头不计入数据行：5 个客户 + 1 行合计
            Assert.assertEquals("5 个客户 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一个客户（a=客户名称，b=应收金额，c=30天内，d=31-60天，e=61天以上，f/g 留空）。 */
    private static JQuickRow customer(String customer, double receivableAmount, double within30,
                                      double days31to60, double over60) {
        JQuickRow row = new JQuickRow();
        row.put("a", customer);
        row.put("b", receivableAmount);
        row.put("c", within30);
        row.put("d", days31to60);
        row.put("e", over60);
        row.put("f", null);
        row.put("g", null);
        return row;
    }
}
