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
package com.github.paohaijiao.biz.merge;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 场景 13：费用报销单（🟢 纯 XML 无代码）。
 *
 * <p>业务：报销明细逐行「金额 - 自付金额 = 可报销金额」，末尾一行合计。
 * 统计与版式全部写在 {@code jquick/biz/merge/0013_merge_expense-reimbursement.xml}：
 * <ul>
 *   <li>FORMULAS：行内 {@code C2-D2}，合计行 {@code SUM} 汇总三列金额；</li>
 *   <li>FORMAT：发生日期 {@code yyyy-MM-dd}，金额列 {@code #,##0.00}；</li>
 *   <li>STYLE：合计行加粗高亮；MERGE：合计行 {@code A5:B5} 横向合并。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0013ExpenseReimbursementDemo {

    private static final String XML = "jquick/biz/merge/0013_merge_expense-reimbursement.xml";

    /** 导出：3 条报销明细 + 1 行合计，回读校验公式、合计行样式与横向合并。 */
    @Test
    public void exportExpenseReimbursement() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("差旅费", "2026-09-10", 3200.00, 200.00));
        rows.add(detail("招待费", "2026-09-12", 1800.00, 0.00));
        rows.add(detail("办公用品", "2026-09-15", 600.00, 100.00));
        rows.add(totalRow());

        File out = BizKit.outFile("merge", "0013_merge_expense-reimbursement.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("plum", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0013ExpenseReimbursementService service = factory.createApi(Merge0013ExpenseReimbursementService.class);
            service.exportExpenseReimbursement("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("费用报销单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 行内可报销金额 = 金额 - 自付金额
            Assert.assertEquals("C2-D2", sheet.getRow(1).getCell(4).getCellFormula());
            Assert.assertEquals(3000.00, evaluator.evaluate(sheet.getRow(1).getCell(4)).getNumberValue(), 0.0001);
            // 合计行：金额 / 自付 / 可报销
            Assert.assertEquals("SUM(C2:C4)", sheet.getRow(4).getCell(2).getCellFormula());
            Assert.assertEquals(5600.00, evaluator.evaluate(sheet.getRow(4).getCell(2)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(D2:D4)", sheet.getRow(4).getCell(3).getCellFormula());
            Assert.assertEquals(300.00, evaluator.evaluate(sheet.getRow(4).getCell(3)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(E2:E4)", sheet.getRow(4).getCell(4).getCellFormula());
            Assert.assertEquals(5300.00, evaluator.evaluate(sheet.getRow(4).getCell(4)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 合计行 A5:B5 横向合并
            Assert.assertTrue("A5:B5 应合并", sheet.getMergedRegions().contains(new CellRangeAddress(4, 4, 0, 1)));
            // 日期格式化
            Assert.assertEquals("yyyy-MM-dd", sheet.getRow(1).getCell(1).getCellStyle().getDataFormatString());

            System.out.println("【场景13】费用报销单导出: " + out.getAbsolutePath() + "，合并区块 " + sheet.getMergedRegions());
        }
    }

    /** 导入解析：读取导出的费用报销单（先重算公式），打印每行字段。 */
    @Test
    public void importExpenseReimbursement() throws Exception {
        File src = BizKit.outFile("merge", "0013_merge_expense-reimbursement.xlsx");
        if (!src.exists()) {
            exportExpenseReimbursement();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0013_merge_expense-reimbursement-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0013ExpenseReimbursementService service = factory.createApi(Merge0013ExpenseReimbursementService.class);
            List<JQuickRow> rows = service.importExpenseReimbursement("field", "value");

            System.out.println("【场景13】费用报销单导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    费用类型=" + r.get("category") + ", 日期=" + r.get("expenseDate")
                        + ", 金额=" + r.get("amount") + ", 自付=" + r.get("selfPaid") + ", 可报销=" + r.get("reimbursable"));
            }
            Assert.assertEquals("3 条明细 + 1 行合计", 4, rows.size());
        }
    }

    /** 构造一条报销明细（e=可报销金额由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String category, String date, double amount, double selfPaid) throws Exception {
        JQuickRow row = new JQuickRow();
        row.put("a", category);
        row.put("b", new SimpleDateFormat("yyyy-MM-dd").parse(date));
        row.put("c", amount);
        row.put("d", selfPaid);
        row.put("e", null);
        return row;
    }

    /** 合计行：只写「合计」文字，其余列以空值占位（必须补齐全部映射列）。 */
    private static JQuickRow totalRow() {
        JQuickRow row = new JQuickRow();
        row.put("a", "合计");
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        return row;
    }
}
