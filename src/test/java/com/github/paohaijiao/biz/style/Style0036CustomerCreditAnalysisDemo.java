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
 * 场景 36：客户信用额度分析表（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：按客户核算信用额度使用情况，逐行算「可用额度 = 信用额度 - 已用额度」与
 * 「额度使用率 = 已用额度 / 信用额度」，超出额度的客户使用率标红预警，末尾一行给出整体使用率。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0036_style_customer-credit-analysis.xml}：
 * FORMULAS 做逐行减法与除法、合计行汇总，STYLE 给合计行高亮、给超额客户的使用率标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0036CustomerCreditAnalysisDemo {

    private static final String XML = "jquick/biz/style/0036_style_customer-credit-analysis.xml";

    /** 导出：纯 XML 完成可用额度、使用率、合计汇总与超额标红。 */
    @Test
    public void exportCustomerCredit() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(client("鸿达贸易", 500000, 320000, "正常"));
        rows.add(client("鑫源实业", 300000, 285000, "预警"));
        rows.add(client("蓝天科技", 200000, 210000, "超额"));
        rows.add(client("广发物流", 150000, 90000, "正常"));
        rows.add(client("中远建材", 400000, 260000, "正常"));
        // 合计占位行：三列金额与整体使用率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0036_style_customer-credit-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("navyBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0036CustomerCreditAnalysisService service = factory.createApi(Style0036CustomerCreditAnalysisService.class);
            service.exportCustomerCredit("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("客户信用额度分析");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行可用额度 = 信用额度 - 已用额度 = 500000 - 320000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("B2-C2", d2.getCellFormula());
            Assert.assertEquals(180000.0, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 首行使用率 = 320000 / 500000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2/B2", e2.getCellFormula());
            Assert.assertEquals(0.64, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 超额客户：可用额度为负 200000 - 210000
            Cell d4 = sheet.getRow(3).getCell(3);
            Assert.assertEquals(-10000.0, evaluator.evaluate(d4).getNumberValue(), 0.0001);
            // 超额客户使用率 > 1
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals(1.05, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 合计信用额度 SUM(B2:B6) = 1550000
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(1550000.0, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 合计可用额度 = 合计信用额度 - 合计已用额度 = 1550000 - 1165000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("B7-C7", d7.getCellFormula());
            Assert.assertEquals(385000.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 超额客户使用率标红
            Assert.assertEquals("超额客户应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) e4.getCellStyle()).getFont().getColor());

            System.out.println("【场景36】客户信用额度分析表导出: " + out.getAbsolutePath()
                    + "，整体额度使用率 " + evaluator.evaluate(sheet.getRow(6).getCell(4)).getNumberValue());
        }
    }

    /** 导入解析：读取导出的客户信用额度分析表（先重算公式）。 */
    @Test
    public void importCustomerCredit() throws Exception {
        File src = BizKit.outFile("style", "0036_style_customer-credit-analysis.xlsx");
        if (!src.exists()) {
            exportCustomerCredit();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0036_style_customer-credit-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0036CustomerCreditAnalysisService service = factory.createApi(Style0036CustomerCreditAnalysisService.class);
            List<JQuickRow> rows = service.importCustomerCredit("field", "value");

            System.out.println("【场景36】客户信用额度分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    客户=" + r.get("customer") + ", 额度=" + r.get("creditLimit")
                        + ", 已用=" + r.get("usedCredit") + ", 可用=" + r.get("availableCredit")
                        + ", 使用率=" + r.get("usageRate") + ", 状态=" + r.get("creditStatus"));
            }
            // 表头不计入数据行：5 个客户 + 1 行合计
            Assert.assertEquals("5 个客户 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一个客户行（a=客户名称，b=信用额度，c=已用额度，f=信用状态）。 */
    private static JQuickRow client(String customer, double creditLimit, double usedCredit, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", customer);
        row.put("b", creditLimit);
        row.put("c", usedCredit);
        row.put("d", null);
        row.put("e", null);
        row.put("f", status);
        return row;
    }
}
