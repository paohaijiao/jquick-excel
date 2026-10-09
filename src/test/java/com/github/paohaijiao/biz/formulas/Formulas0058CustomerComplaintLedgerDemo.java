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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景 58：客户投诉处理台账（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：登记客户投诉单，末尾一行给出平均处理天数，超期投诉的处理天数标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0058_formulas_customer-complaint-ledger.xml}：
 * FORMULAS 用 AVERAGE 求平均处理天数，STYLE 给合计行高亮、给超期处理天数标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0058CustomerComplaintLedgerDemo {

    private static final String XML = "jquick/biz/formulas/0058_formulas_customer-complaint-ledger.xml";

    /** 导出：纯 XML 完成平均处理天数汇总、合计行高亮与超期标红。 */
    @Test
    public void exportCustomerComplaintLedger() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(complaint("TS2601", "恒信商贸", "物流延迟", date(2026, 3, 1), 3, 2.0, "否"));
        rows.add(complaint("TS2602", "华越集团", "质量异议", date(2026, 3, 2), 5, 3.0, "否"));
        rows.add(complaint("TS2603", "广发物流", "服务态度", date(2026, 3, 4), 2, 4.0, "是"));
        rows.add(complaint("TS2604", "中远建材", "交付延期", date(2026, 3, 6), 4, 3.0, "否"));
        rows.add(complaint("TS2605", "蓝天科技", "质量异议", date(2026, 3, 8), 5, 5.0, "否"));
        rows.add(complaint("TS2606", "星辰电子", "物流延迟", date(2026, 3, 10), 3, 6.0, "是"));
        // 合计占位行：平均处理天数留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0058_formulas_customer-complaint-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("pearl", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0058CustomerComplaintLedgerService service = factory.createApi(Formulas0058CustomerComplaintLedgerService.class);
            service.exportCustomerComplaintLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("客户投诉处理台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首条处理天数
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals(2.0, f2.getNumericCellValue(), 0.0001);
            // 平均处理天数 = AVERAGE(F2:F7) = (2 + 3 + 4 + 3 + 5 + 6) / 6
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("AVERAGE(F2:F7)", f8.getCellFormula());
            Assert.assertEquals(23.0 / 6.0, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 超期投诉处理天数标红（第 3 条 Excel 第 4 行、第 6 条 Excel 第 7 行）
            Assert.assertEquals("超期应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) sheet.getRow(3).getCell(5).getCellStyle()).getFont().getColor());
            Assert.assertEquals("超期应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) sheet.getRow(6).getCell(5).getCellStyle()).getFont().getColor());

            System.out.println("【场景58】客户投诉处理台账导出: " + out.getAbsolutePath()
                    + "，平均处理天数 " + evaluator.evaluate(f8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的客户投诉处理台账（先重算公式）。 */
    @Test
    public void importCustomerComplaintLedger() throws Exception {
        File src = BizKit.outFile("formulas", "0058_formulas_customer-complaint-ledger.xlsx");
        if (!src.exists()) {
            exportCustomerComplaintLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0058_formulas_customer-complaint-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0058CustomerComplaintLedgerService service = factory.createApi(Formulas0058CustomerComplaintLedgerService.class);
            List<JQuickRow> rows = service.importCustomerComplaintLedger("field", "value");

            System.out.println("【场景58】客户投诉处理台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    单号=" + r.get("complaintNo") + ", 客户=" + r.get("customer")
                        + ", 类型=" + r.get("complaintType") + ", 时限=" + r.get("timeLimit")
                        + ", 处理天数=" + r.get("actualDays") + ", 是否超期=" + r.get("overdue"));
            }
            // 表头不计入数据行：6 条记录 + 1 行合计
            Assert.assertEquals("6 条记录 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条投诉记录（a=单号，b=客户，c=类型，d=受理日期，e=时限，f=处理天数，g=是否超期）。 */
    private static JQuickRow complaint(String complaintNo, String customer, String complaintType,
                                       Date acceptDate, int timeLimit, double actualDays, String overdue) {
        JQuickRow row = new JQuickRow();
        row.put("a", complaintNo);
        row.put("b", customer);
        row.put("c", complaintType);
        row.put("d", acceptDate);
        row.put("e", timeLimit);
        row.put("f", actualDays);
        row.put("g", overdue);
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
