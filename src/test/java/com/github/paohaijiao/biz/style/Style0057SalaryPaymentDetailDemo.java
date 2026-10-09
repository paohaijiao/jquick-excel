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
 * 场景 57：员工薪资发放明细（台账汇总类，🟢 纯 XML）。
 *
 * <p>业务：按月登记员工应发工资、社保公积金与个人所得税，逐行算
 * 「实发工资 = 应发 - 社保公积金 - 个税」，末尾一行汇总各列，个税最高者标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0057_style_salary-payment-detail.xml}：
 * FORMULAS 做逐行减法与合计 SUM，STYLE 给合计行高亮、给个税最高者标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0057SalaryPaymentDetailDemo {

    private static final String XML = "jquick/biz/style/0057_style_salary-payment-detail.xml";

    /** 导出：纯 XML 完成实发工资、各列合计与个税最高者标红。 */
    @Test
    public void exportSalaryPaymentDetail() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(staff("E2601", "张伟", "研发部", 22000.00, 3500.00, 1800.00));
        rows.add(staff("E2602", "李娜", "市场部", 18000.00, 3000.00, 1200.00));
        rows.add(staff("E2603", "王强", "生产部", 15000.00, 2600.00, 700.00));
        rows.add(staff("E2604", "赵敏", "财务部", 26000.00, 3800.00, 2600.00));
        rows.add(staff("E2605", "陈杰", "研发部", 20000.00, 3300.00, 1400.00));
        // 合计占位行：各列合计与实发工资合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0057_style_salary-payment-detail.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("champagne", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0057SalaryPaymentDetailService service = factory.createApi(Style0057SalaryPaymentDetailService.class);
            service.exportSalaryPaymentDetail("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工薪资发放明细");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行实发工资 = 应发 - 社保公积金 - 个税 = 22000 - 3500 - 1800
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("D2-E2-F2", g2.getCellFormula());
            Assert.assertEquals(16700.00, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 应发工资合计 = SUM(D2:D6) = 101000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(101000.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 个税合计 = SUM(F2:F6) = 7700
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("SUM(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(7700.00, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 实发工资合计 = D7 - E7 - F7 = 101000 - 16200 - 7700
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("D7-E7-F7", g7.getCellFormula());
            Assert.assertEquals(77100.00, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 个税最高者（赵敏，第 4 名）标红
            Assert.assertEquals("个税最高应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) sheet.getRow(3).getCell(5).getCellStyle()).getFont().getColor());

            System.out.println("【场景57】员工薪资发放明细导出: " + out.getAbsolutePath()
                    + "，实发工资合计 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的员工薪资发放明细（先重算公式）。 */
    @Test
    public void importSalaryPaymentDetail() throws Exception {
        File src = BizKit.outFile("style", "0057_style_salary-payment-detail.xlsx");
        if (!src.exists()) {
            exportSalaryPaymentDetail();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0057_style_salary-payment-detail-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0057SalaryPaymentDetailService service = factory.createApi(Style0057SalaryPaymentDetailService.class);
            List<JQuickRow> rows = service.importSalaryPaymentDetail("field", "value");

            System.out.println("【场景57】员工薪资发放明细导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("employeeNo") + ", 姓名=" + r.get("name")
                        + ", 部门=" + r.get("department") + ", 应发=" + r.get("grossPay")
                        + ", 社保=" + r.get("socialSecurity") + ", 个税=" + r.get("incomeTax")
                        + ", 实发=" + r.get("netPay"));
            }
            // 表头不计入数据行：5 名员工 + 1 行合计
            Assert.assertEquals("5 名员工 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条薪资明细（a=工号，b=姓名，c=部门，d=应发，e=社保，f=个税，g 留空由 FORMULAS 计算）。 */
    private static JQuickRow staff(String employeeNo, String name, String department,
                                   double grossPay, double socialSecurity, double incomeTax) {
        JQuickRow row = new JQuickRow();
        row.put("a", employeeNo);
        row.put("b", name);
        row.put("c", department);
        row.put("d", grossPay);
        row.put("e", socialSecurity);
        row.put("f", incomeTax);
        row.put("g", null);
        return row;
    }
}
