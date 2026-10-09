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
 * 场景 20：进项发票台账（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：采购取得的进项增值税发票登记，逐行算「税额 = 不含税金额 × 税率」与「价税合计 = 不含税金额 + 税额」，
 * 末尾一行汇总不含税金额、税额与价税合计。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0020_formulas_input-vat-invoice-ledger.xml}：
 * FORMULAS 做逐行乘加与合计行汇总，STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0020InputVatInvoiceLedgerDemo {

    private static final String XML = "jquick/biz/formulas/0020_formulas_input-vat-invoice-ledger.xml";

    /** 导出：纯 XML 完成税额、价税合计与合计行汇总。 */
    @Test
    public void exportInputVatInvoice() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(invoice("INV2026001", date(2026, 1, 5), "华新电子", 100000, 0.13));
        rows.add(invoice("INV2026002", date(2026, 1, 12), "恒通材料", 80000, 0.13));
        rows.add(invoice("INV2026003", date(2026, 1, 18), "光明物流", 20000, 0.09));
        rows.add(invoice("INV2026004", date(2026, 1, 25), "长江机械", 150000, 0.13));
        rows.add(invoice("INV2026005", date(2026, 2, 2), "佳美包装", 30000, 0.06));
        rows.add(invoice("INV2026006", date(2026, 2, 8), "兴业办公", 10000, 0.13));
        // 合计占位行：金额、税额、价税合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0020_formulas_input-vat-invoice-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("bronze", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0020InputVatInvoiceLedgerService service = factory.createApi(Formulas0020InputVatInvoiceLedgerService.class);
            service.exportInputVatInvoice("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("进项发票台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行税额 = 不含税 × 税率 = 100000 × 13%
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(13000.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 首行价税合计 = 不含税 + 税额 = 100000 + 13000
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("D2+F2", g2.getCellFormula());
            Assert.assertEquals(113000.0, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计：不含税 SUM(D2:D7) = 390000
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("SUM(D2:D7)", d8.getCellFormula());
            Assert.assertEquals(390000.0, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 合计：税额 SUM(F2:F7) = 47800
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F2:F7)", f8.getCellFormula());
            Assert.assertEquals(47800.0, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景20】进项发票台账导出: " + out.getAbsolutePath()
                    + "，合计税额 " + evaluator.evaluate(f8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的进项发票台账（先重算公式）。 */
    @Test
    public void importInputVatInvoice() throws Exception {
        File src = BizKit.outFile("formulas", "0020_formulas_input-vat-invoice-ledger.xlsx");
        if (!src.exists()) {
            exportInputVatInvoice();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0020_formulas_input-vat-invoice-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0020InputVatInvoiceLedgerService service = factory.createApi(Formulas0020InputVatInvoiceLedgerService.class);
            List<JQuickRow> rows = service.importInputVatInvoice("field", "value");

            System.out.println("【场景20】进项发票台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    发票号=" + r.get("invoiceNo") + ", 供应商=" + r.get("supplier")
                        + ", 不含税=" + r.get("amount") + ", 税额=" + r.get("taxAmount") + ", 价税合计=" + r.get("totalAmount"));
            }
            // 6 张发票 + 1 行合计
            Assert.assertEquals("6 张发票 + 1 合计", 7, rows.size());
        }
    }

    /** 构造一条进项发票明细（a=发票号，b=开票日期，c=供应商，d=不含税金额，e=税率）。 */
    private static JQuickRow invoice(String no, Date invoiceDate, String supplier, double amount, double rate) {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", invoiceDate);
        row.put("c", supplier);
        row.put("d", amount);
        row.put("e", rate);
        row.put("f", null);
        row.put("g", null);
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
