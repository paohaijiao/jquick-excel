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
 * 场景 48：银行承兑汇票台账（台账汇总类，🟢 纯 XML）。
 *
 * <p>业务：登记持票企业名下的银承汇票，末尾一行用 SUM 汇总票面金额，逾期汇票的剩余天数标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0048_style_bank-acceptance-ledger.xml}：
 * FORMULAS 求票面金额合计，STYLE 给合计行高亮、给逾期行的剩余天数标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0048BankAcceptanceLedgerDemo {

    private static final String XML = "jquick/biz/style/0048_style_bank-acceptance-ledger.xml";

    /** 导出：纯 XML 完成票面金额合计、合计行高亮与逾期剩余天数标红。 */
    @Test
    public void exportBankAcceptanceLedger() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(bill("BA2601", "恒信商贸", "工商银行", 500000.00, date(2025, 12, 1), date(2026, 6, 1), 245, "正常"));
        rows.add(bill("BA2602", "华越集团", "建设银行", 320000.00, date(2026, 1, 15), date(2026, 7, 15), 150, "正常"));
        rows.add(bill("BA2603", "广发物流", "中国银行", 180000.00, date(2025, 6, 20), date(2026, 1, 10), -12, "逾期"));
        rows.add(bill("BA2604", "中远建材", "农业银行", 260000.00, date(2026, 2, 1), date(2026, 8, 1), 210, "正常"));
        rows.add(bill("BA2605", "蓝天科技", "交通银行", 150000.00, date(2026, 3, 10), date(2026, 9, 10), 280, "正常"));
        // 合计占位行：票面金额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        total.put("h", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0048_style_bank-acceptance-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("wineRed", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0048BankAcceptanceLedgerService service = factory.createApi(Style0048BankAcceptanceLedgerService.class);
            service.exportBankAcceptanceLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("银行承兑汇票台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首张汇票票面金额 500000.00
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals(500000.00, d2.getNumericCellValue(), 0.0001);
            // 票面金额合计 = SUM(D2:D6) = 1410000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(1410000.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 逾期汇票（第 3 张，Excel 第 4 行）剩余天数为负
            Cell g4 = sheet.getRow(3).getCell(6);
            Assert.assertEquals(-12, g4.getNumericCellValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 逾期汇票剩余天数标红
            Assert.assertEquals("逾期应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) g4.getCellStyle()).getFont().getColor());

            System.out.println("【场景48】银行承兑汇票台账导出: " + out.getAbsolutePath()
                    + "，票面金额合计 " + evaluator.evaluate(d7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的银行承兑汇票台账（先重算公式）。 */
    @Test
    public void importBankAcceptanceLedger() throws Exception {
        File src = BizKit.outFile("style", "0048_style_bank-acceptance-ledger.xlsx");
        if (!src.exists()) {
            exportBankAcceptanceLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0048_style_bank-acceptance-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0048BankAcceptanceLedgerService service = factory.createApi(Style0048BankAcceptanceLedgerService.class);
            List<JQuickRow> rows = service.importBankAcceptanceLedger("field", "value");

            System.out.println("【场景48】银行承兑汇票台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    汇票号=" + r.get("billNo") + ", 出票人=" + r.get("drawer")
                        + ", 承兑银行=" + r.get("acceptorBank") + ", 票面金额=" + r.get("faceAmount")
                        + ", 剩余天数=" + r.get("remainingDays") + ", 状态=" + r.get("billStatus"));
            }
            // 表头不计入数据行：5 张汇票 + 1 行合计
            Assert.assertEquals("5 张汇票 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一张汇票（a=汇票号，b=出票人，c=承兑银行，d=票面金额，e=出票日，f=到期日，g=剩余天数，h=状态）。 */
    private static JQuickRow bill(String billNo, String drawer, String acceptorBank, double faceAmount,
                                  Date issueDate, Date maturityDate, int remainingDays, String billStatus) {
        JQuickRow row = new JQuickRow();
        row.put("a", billNo);
        row.put("b", drawer);
        row.put("c", acceptorBank);
        row.put("d", faceAmount);
        row.put("e", issueDate);
        row.put("f", maturityDate);
        row.put("g", remainingDays);
        row.put("h", billStatus);
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
