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
 * 场景 23：票据登记台账（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：企业持有的应收 / 应付票据登记，逐行算「票据期限 = 到期日期 - 出票日期」天，末尾一行汇总票面金额。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0023_formulas_bill-register-ledger.xml}：
 * FORMULAS 用两个日期单元格直接相减求天数，并 SUM 汇总票面金额；STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0023BillRegisterLedgerDemo {

    private static final String XML = "jquick/biz/formulas/0023_formulas_bill-register-ledger.xml";

    /** 导出：纯 XML 完成票据期限计算与票面金额合计。 */
    @Test
    public void exportBillRegister() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(bill("BIL2026001", date(2026, 1, 5), date(2026, 4, 5), 100000, "银行承兑"));
        rows.add(bill("BIL2026002", date(2026, 2, 1), date(2026, 5, 1), 150000, "银行承兑"));
        rows.add(bill("BIL2026003", date(2026, 3, 10), date(2026, 6, 10), 80000, "商业承兑"));
        rows.add(bill("BIL2026004", date(2026, 4, 1), date(2026, 7, 1), 120000, "银行承兑"));
        rows.add(bill("BIL2026005", date(2026, 5, 15), date(2026, 8, 15), 60000, "商业承兑"));
        // 合计占位行：票面金额留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0023_formulas_bill-register-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("crimsonRed", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0023BillRegisterLedgerService service = factory.createApi(Formulas0023BillRegisterLedgerService.class);
            service.exportBillRegister("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("票据登记台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首张票据期限 = 到期 - 出票 = 4/5 - 1/5 = 90 天
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-B2", e2.getCellFormula());
            Assert.assertEquals(90.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 合计：票面金额 SUM(D2:D6) = 510000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(510000.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景23】票据登记台账导出: " + out.getAbsolutePath()
                    + "，票面合计 " + evaluator.evaluate(d7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的票据登记台账（先重算公式）。 */
    @Test
    public void importBillRegister() throws Exception {
        File src = BizKit.outFile("formulas", "0023_formulas_bill-register-ledger.xlsx");
        if (!src.exists()) {
            exportBillRegister();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0023_formulas_bill-register-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0023BillRegisterLedgerService service = factory.createApi(Formulas0023BillRegisterLedgerService.class);
            List<JQuickRow> rows = service.importBillRegister("field", "value");

            System.out.println("【场景23】票据登记台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    票据号=" + r.get("billNo") + ", 类型=" + r.get("billType")
                        + ", 票面=" + r.get("faceValue") + ", 期限=" + r.get("termDays") + " 天");
            }
            // 5 张票据 + 1 行合计
            Assert.assertEquals("5 张票据 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条票据明细（a=票据号，b=出票日期，c=到期日期，d=票面金额，f=票据类型）。 */
    private static JQuickRow bill(String no, Date issueDate, Date dueDate, double faceValue, String type) {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", issueDate);
        row.put("c", dueDate);
        row.put("d", faceValue);
        row.put("e", null);
        row.put("f", type);
        return row;
    }

    /** 构造一个「年-月-日」日期（月份从 1 开始）。 */
    private static Date date(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
