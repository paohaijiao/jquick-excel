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
 * 场景 16：应收账款账龄台账（台账汇总类，🟢 纯 XML）。
 *
 * <p>业务：按客户登记应收、已收、未收余额与账龄天数，逐行算「未收余额 = 应收 - 已收」与未收占比，
 * 末尾一行合计；账龄超长的异常客户单元格字体标红，提示重点催收。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0016_style_ar-aging-ledger.xml}：
 * FORMULAS 做逐行相减、占比与合计；STYLE 给合计行高亮，并给异常客户的余额、账龄单元格标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0016ArAgingLedgerDemo {

    private static final String XML = "jquick/biz/style/0016_style_ar-aging-ledger.xml";

    /** 导出：纯 XML 完成未收余额、占比、合计与异常标红。 */
    @Test
    public void exportArAgingLedger() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(customer("华东电子", 500000, 300000, 45));
        rows.add(customer("南方机械", 320000, 320000, 10));
        rows.add(customer("西部建设", 280000, 100000, 75));
        rows.add(customer("北方物流", 150000, 150000, 5));
        // 第 6 行：账龄 120 天、未收 34 万，为异常客户，模板中 D6/E6 标红
        rows.add(customer("东尚科技", 420000, 80000, 120));
        // 合计占位行：金额与占比留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0016_style_ar-aging-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("steelBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0016ArAgingLedgerService service = factory.createApi(Style0016ArAgingLedgerService.class);
            service.exportArAgingLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("应收账款账龄台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首客户未收余额 = 应收 - 已收 = 500000 - 300000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("B2-C2", d2.getCellFormula());
            Assert.assertEquals(200000.0, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 合计：应收 SUM(B2:B6) = 1670000
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(1670000.0, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 合计：未收 SUM(D2:D6) = 720000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(720000.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 异常客户（第6行）未收余额、账龄字体标红
            XSSFCellStyle overdue = (XSSFCellStyle) sheet.getRow(5).getCell(3).getCellStyle();
            Assert.assertEquals("异常客户未收余额应标红", (int) IndexedColors.RED.getIndex(), (int) overdue.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, overdue.getFont().getBold());
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景16】应收账款账龄台账导出: " + out.getAbsolutePath()
                    + "，合计未收 " + evaluator.evaluate(d7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的应收账款账龄台账（先重算公式）。 */
    @Test
    public void importArAgingLedger() throws Exception {
        File src = BizKit.outFile("style", "0016_style_ar-aging-ledger.xlsx");
        if (!src.exists()) {
            exportArAgingLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0016_style_ar-aging-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0016ArAgingLedgerService service = factory.createApi(Style0016ArAgingLedgerService.class);
            List<JQuickRow> rows = service.importArAgingLedger("field", "value");

            System.out.println("【场景16】应收账款账龄台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    客户=" + r.get("customer") + ", 应收=" + r.get("receivable")
                        + ", 未收=" + r.get("unpaid") + ", 账龄=" + r.get("agingDays") + " 天");
            }
            // 5 个客户 + 1 行合计
            Assert.assertEquals("5 客户 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条客户应收明细（a=客户，b=应收，c=已收，e=账龄天数）。 */
    private static JQuickRow customer(String name, double receivable, double received, int agingDays) {
        JQuickRow row = new JQuickRow();
        row.put("a", name);
        row.put("b", receivable);
        row.put("c", received);
        row.put("d", null);
        row.put("e", agingDays);
        row.put("f", null);
        return row;
    }
}
