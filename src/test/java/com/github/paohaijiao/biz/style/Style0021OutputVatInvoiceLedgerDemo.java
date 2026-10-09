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
 * 场景 21：销项发票台账（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：对外开具的销项增值税发票登记，逐行算「税额 = 不含税金额 × 税率」与「价税合计 = 不含税金额 + 税额」，
 * 末尾一行汇总；作废发票的开票状态单元格标红，提示不可计入当期销项。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0021_style_output-vat-invoice-ledger.xml}：
 * FORMULAS 做逐行乘加与合计行汇总，STYLE 给合计行高亮并给作废状态标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0021OutputVatInvoiceLedgerDemo {

    private static final String XML = "jquick/biz/style/0021_style_output-vat-invoice-ledger.xml";

    /** 导出：纯 XML 完成税额、价税合计、合计行汇总与作废状态标红。 */
    @Test
    public void exportOutputVatInvoice() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(invoice("SO2026001", date(2026, 1, 6), "华东电子", 200000, 0.13, "正常"));
        rows.add(invoice("SO2026002", date(2026, 1, 14), "南方机械", 120000, 0.13, "正常"));
        rows.add(invoice("SO2026003", date(2026, 1, 20), "西部建设", 60000, 0.09, "正常"));
        rows.add(invoice("SO2026004", date(2026, 1, 27), "北方物流", 50000, 0.13, "正常"));
        // 第 6 行：东尚科技发票作废，模板中 H6 标红
        rows.add(invoice("SO2026005", date(2026, 2, 3), "东尚科技", 80000, 0.13, "作废"));
        // 合计占位行：金额、税额、价税合计留给 FORMULAS
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

        File out = BizKit.outFile("style", "0021_style_output-vat-invoice-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("roseQuartz", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0021OutputVatInvoiceLedgerService service = factory.createApi(Style0021OutputVatInvoiceLedgerService.class);
            service.exportOutputVatInvoice("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("销项发票台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行税额 = 不含税 × 税率 = 200000 × 13%
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(26000.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 首行价税合计 = 不含税 + 税额 = 200000 + 26000
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("D2+F2", g2.getCellFormula());
            Assert.assertEquals(226000.0, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计：不含税 SUM(D2:D6) = 510000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(510000.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计：价税合计 = 不含税合计 + 税额合计 = 510000 + 63900
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("D7+F7", g7.getCellFormula());
            Assert.assertEquals(573900.0, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 作废发票（第6行）开票状态字体标红
            XSSFCellStyle voided = (XSSFCellStyle) sheet.getRow(5).getCell(7).getCellStyle();
            Assert.assertEquals("作废状态应标红", (int) IndexedColors.RED.getIndex(), (int) voided.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, voided.getFont().getBold());
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景21】销项发票台账导出: " + out.getAbsolutePath()
                    + "，合计价税 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的销项发票台账（先重算公式）。 */
    @Test
    public void importOutputVatInvoice() throws Exception {
        File src = BizKit.outFile("style", "0021_style_output-vat-invoice-ledger.xlsx");
        if (!src.exists()) {
            exportOutputVatInvoice();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0021_style_output-vat-invoice-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0021OutputVatInvoiceLedgerService service = factory.createApi(Style0021OutputVatInvoiceLedgerService.class);
            List<JQuickRow> rows = service.importOutputVatInvoice("field", "value");

            System.out.println("【场景21】销项发票台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    发票号=" + r.get("invoiceNo") + ", 客户=" + r.get("customer")
                        + ", 不含税=" + r.get("amount") + ", 价税合计=" + r.get("totalAmount") + ", 状态=" + r.get("status"));
            }
            // 5 张发票 + 1 行合计
            Assert.assertEquals("5 张发票 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条销项发票明细（a=发票号，b=开票日期，c=客户，d=不含税金额，e=税率，h=开票状态）。 */
    private static JQuickRow invoice(String no, Date invoiceDate, String customer, double amount, double rate, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", invoiceDate);
        row.put("c", customer);
        row.put("d", amount);
        row.put("e", rate);
        row.put("f", null);
        row.put("g", null);
        row.put("h", status);
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
