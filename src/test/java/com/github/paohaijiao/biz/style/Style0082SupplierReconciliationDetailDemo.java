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
 * 场景 82：供应商往来对账明细表（审计对账类，🟢 纯 XML）。
 *
 * <p>业务：逐供应商比对「我方应付」与「对方应收」，算「差异 = 我方应付 - 对方应收」，
 * 末尾一行汇总两边金额与总差异；有差异的供应商标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0082_style_supplier-reconciliation-detail.xml}：
 * FORMULAS 求逐行差异与合计，STYLE 给合计行高亮、给差异行标红；Java 只构造对账数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0082SupplierReconciliationDetailDemo {

    private static final String XML = "jquick/biz/style/0082_style_supplier-reconciliation-detail.xml";

    /** 导出：纯 XML 完成逐行差异、合计与差异标红。 */
    @Test
    public void exportSupplierReconciliationDetail() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e 留空，由 FORMULAS 计算
        rows.add(supplier("华强电子", "DZ-2026-001", 128000.00, 128000.00, "相符", date(2026, 3, 31)));
        rows.add(supplier("长江物流", "DZ-2026-002", 65000.00, 63500.00, "存在差异", date(2026, 3, 31)));
        rows.add(supplier("恒昌五金", "DZ-2026-003", 43200.00, 43200.00, "相符", date(2026, 3, 31)));
        rows.add(supplier("明达包装", "DZ-2026-004", 27600.00, 28400.00, "存在差异", date(2026, 3, 31)));
        rows.add(supplier("天成化工", "DZ-2026-005", 98000.00, 98000.00, "相符", date(2026, 3, 31)));
        // 合计占位行：两边金额与总差异留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0082_style_supplier-reconciliation-detail.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sunsetOrange", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0082SupplierReconciliationDetailService service = factory.createApi(Style0082SupplierReconciliationDetailService.class);
            service.exportSupplierReconciliationDetail("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("供应商往来对账明细表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行差异 = 我方应付 - 对方应收 = 128000 - 128000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(0.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 长江物流（Excel 第 3 行）差异 = 65000 - 63500
            Cell e3 = sheet.getRow(2).getCell(4);
            Assert.assertEquals("C3-D3", e3.getCellFormula());
            Assert.assertEquals(1500.00, evaluator.evaluate(e3).getNumberValue(), 0.0001);
            // 合计：我方应付合计 = SUM(C2:C6) = 361800
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(361800.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计：总差异 = 应付合计 - 应收合计 = 361800 - 361100
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("C7-D7", e7.getCellFormula());
            Assert.assertEquals(700.00, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 存在差异的行标红加粗
            XSSFCellStyle diffStyle = (XSSFCellStyle) e3.getCellStyle();
            Assert.assertTrue("差异行应加粗", diffStyle.getFont().getBold());
            Assert.assertEquals("差异行应标红", (int) IndexedColors.RED.getIndex(), (int) diffStyle.getFont().getColor());

            System.out.println("【场景82】供应商往来对账明细表导出: " + out.getAbsolutePath()
                    + "，总差异 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的供应商往来对账明细表（先重算公式）。 */
    @Test
    public void importSupplierReconciliationDetail() throws Exception {
        File src = BizKit.outFile("style", "0082_style_supplier-reconciliation-detail.xlsx");
        if (!src.exists()) {
            exportSupplierReconciliationDetail();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0082_style_supplier-reconciliation-detail-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0082SupplierReconciliationDetailService service = factory.createApi(Style0082SupplierReconciliationDetailService.class);
            List<JQuickRow> rows = service.importSupplierReconciliationDetail("field", "value");

            System.out.println("【场景82】供应商往来对账明细表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    供应商=" + r.get("supplier") + ", 对账单号=" + r.get("statementNo")
                        + ", 我方应付=" + r.get("ourPayable") + ", 对方应收=" + r.get("theirReceivable")
                        + ", 差异=" + r.get("difference") + ", 对账结论=" + r.get("conclusion")
                        + ", 对账日期=" + r.get("reconciliationDate"));
            }
            // 表头不计入数据行：5 家供应商 + 1 行合计
            Assert.assertEquals("5 家供应商 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条对账明细（a=供应商，b=对账单号，c=我方应付，d=对方应收，e 留空，f=结论，g=日期）。 */
    private static JQuickRow supplier(String supplier, String statementNo, double ourPayable,
                                      double theirReceivable, String conclusion, Date reconciliationDate) {
        JQuickRow row = new JQuickRow();
        row.put("a", supplier);
        row.put("b", statementNo);
        row.put("c", ourPayable);
        row.put("d", theirReceivable);
        row.put("e", null);
        row.put("f", conclusion);
        row.put("g", reconciliationDate);
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
