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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景 94：工程项目进度款支付计划表（计划排期类，🟢 纯 XML）。
 *
 * <p>业务：逐工程节点登记合同金额与完成进度，算「应付进度款 = 合同金额 × 完成进度」
 * 「待付金额 = 应付进度款 - 已付金额」，末尾一行汇总合同、应付与已付并给出待付合计；
 * 待付金额最大的节点标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0094_style_project-payment-plan.xml}：
 * FORMULAS 求逐行应付进度款 / 待付、SUM 求各列合计、E8-F8 求待付合计，
 * STYLE 给合计行高亮、给待付最大节点标红；Java 只构造节点数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0094ProjectPaymentPlanDemo {

    private static final String XML = "jquick/biz/style/0094_style_project-payment-plan.xml";

    /** 导出：纯 XML 完成逐行应付进度款、待付、合计与待付最大节点标红。 */
    @Test
    public void exportProjectPaymentPlan() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e/g 留空，由 FORMULAS 计算
        rows.add(node("开工准备", date(2026, 3, 1), 200000.00, 0.10, 20000.00));
        rows.add(node("基础施工", date(2026, 5, 1), 200000.00, 0.30, 55000.00));
        rows.add(node("主体结构", date(2026, 8, 1), 200000.00, 0.50, 60000.00));
        rows.add(node("装饰装修", date(2026, 10, 1), 200000.00, 0.70, 110000.00));
        rows.add(node("设备安装", date(2026, 12, 1), 200000.00, 0.90, 160000.00));
        rows.add(node("竣工验收", date(2027, 2, 1), 200000.00, 1.00, 195000.00));
        // 合计占位行：合同、应付、已付与待付合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0094_style_project-payment-plan.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("charcoal", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0094ProjectPaymentPlanService service = factory.createApi(Style0094ProjectPaymentPlanService.class);
            service.exportProjectPaymentPlan("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("工程项目进度款支付计划表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行应付进度款 = 合同金额 × 完成进度 = 200000 × 0.10
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(20000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 首行待付 = 应付 - 已付 = 20000 - 20000
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("E2-F2", g2.getCellFormula());
            Assert.assertEquals(0.00, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 待付最大的「主体结构」（Excel 第 4 行）= 100000 - 60000
            Cell g4 = sheet.getRow(3).getCell(6);
            Assert.assertEquals("E4-F4", g4.getCellFormula());
            Assert.assertEquals(40000.00, evaluator.evaluate(g4).getNumberValue(), 0.0001);
            // 合计：应付进度款 = SUM(E2:E7) = 700000
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals("SUM(E2:E7)", e8.getCellFormula());
            Assert.assertEquals(700000.00, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 合计：待付金额 = E8 - F8 = 700000 - 600000
            Cell g8 = sheet.getRow(7).getCell(6);
            Assert.assertEquals("E8-F8", g8.getCellFormula());
            Assert.assertEquals(100000.00, evaluator.evaluate(g8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 待付最大节点标红加粗
            XSSFCellStyle pendingStyle = (XSSFCellStyle) g4.getCellStyle();
            Assert.assertTrue("待付最大节点应加粗", pendingStyle.getFont().getBold());
            Assert.assertEquals("待付最大节点应标红", (int) IndexedColors.RED.getIndex(), (int) pendingStyle.getFont().getColor());

            System.out.println("【场景94】工程项目进度款支付计划表导出: " + out.getAbsolutePath()
                    + "，合计待付 " + evaluator.evaluate(g8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的工程项目进度款支付计划表（先重算公式）。 */
    @Test
    public void importProjectPaymentPlan() throws Exception {
        File src = BizKit.outFile("style", "0094_style_project-payment-plan.xlsx");
        if (!src.exists()) {
            exportProjectPaymentPlan();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0094_style_project-payment-plan-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0094ProjectPaymentPlanService service = factory.createApi(Style0094ProjectPaymentPlanService.class);
            List<JQuickRow> rows = service.importProjectPaymentPlan("field", "value");

            System.out.println("【场景94】工程项目进度款支付计划表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工程节点=" + r.get("projectNode") + ", 计划完成日=" + r.get("planDate")
                        + ", 合同金额=" + r.get("contractAmount") + ", 完成进度=" + r.get("progress")
                        + ", 应付进度款=" + r.get("payableAmount") + ", 已付金额=" + r.get("paidAmount")
                        + ", 待付金额=" + r.get("pendingAmount"));
            }
            // 表头不计入数据行：6 个节点 + 1 行合计
            Assert.assertEquals("6 个节点 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一个工程节点（a=节点，b=计划完成日，c=合同金额，d=完成进度，e/g 留空，f=已付金额）。 */
    private static JQuickRow node(String projectNode, Date planDate, double contractAmount,
                                  double progress, double paidAmount) {
        JQuickRow row = new JQuickRow();
        row.put("a", projectNode);
        row.put("b", planDate);
        row.put("c", contractAmount);
        row.put("d", progress);
        row.put("e", null);
        row.put("f", paidAmount);
        row.put("g", null);
        return row;
    }

    /** 构造日期（月份按自然月 1-12 传入）。 */
    private static Date date(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
