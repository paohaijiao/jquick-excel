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
import org.apache.poi.ss.usermodel.Cell;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 场景 77：月度成本费用台账（台账汇总类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按「费用大类 → 费用明细」两级归集预算与实际，逐行算「差异 = 实际 - 预算」
 * 「执行率 = 实际 / 预算」；每个费用大类后跟一行小计，末尾一行给出全部门类合计；
 * 费用大类列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按费用大类分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行差异 / 执行率与小计 / 合计由 {@code jquick/biz/merge/0077_merge_monthly-cost-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0077MonthlyCostLedgerDemo {

    private static final String XML = "jquick/biz/merge/0077_merge_monthly-cost-ledger.xml";

    /** 导出：Java 分组 + 插小计行，费用大类列纵向合并，差异与执行率由 XML FORMULAS 完成。 */
    @Test
    public void exportMonthlyCostLedger() throws Exception {
        // 1. 构造扁平费用明细（Java 不做任何差异计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("人力成本", "工资", 500000.00, 520000.00));
        flat.add(line("人力成本", "社保", 120000.00, 125000.00));
        flat.add(line("人力成本", "奖金", 80000.00, 70000.00));
        flat.add(line("运营成本", "租金", 200000.00, 200000.00));
        flat.add(line("运营成本", "水电", 50000.00, 58000.00));

        // 2. Java 分组并插入小计 / 合计占位行（差异留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0077_merge_monthly-cost-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("vintageSepia", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0077MonthlyCostLedgerService service = factory.createApi(Merge0077MonthlyCostLedgerService.class);
            service.exportMonthlyCostLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("月度成本费用台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行差异 = 实际 - 预算 = 520000 - 500000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2-C2", e2.getCellFormula());
            Assert.assertEquals(20000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 首行执行率 = 实际 / 预算 = 520000 / 500000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2/C2", f2.getCellFormula());
            Assert.assertEquals(1.04, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 人力成本小计差异 = 实际小计 - 预算小计 = 715000 - 700000
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("D5-C5", e5.getCellFormula());
            Assert.assertEquals(15000.00, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 运营成本小计实际 = SUM(D6:D7) = 200000 + 58000
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("SUM(D6:D7)", d8.getCellFormula());
            Assert.assertEquals(258000.00, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 全部门类合计差异 = 实际合计 - 预算合计 = 973000 - 950000
            Cell e9 = sheet.getRow(8).getCell(4);
            Assert.assertEquals("D9-C9", e9.getCellFormula());
            Assert.assertEquals(23000.00, evaluator.evaluate(e9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 费用大类列纵向合并：人力成本 A2:A4、运营成本 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("人力成本 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("运营成本 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景77】月度成本费用台账导出: " + out.getAbsolutePath()
                    + "，合计差异 " + evaluator.evaluate(e9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的月度成本费用台账（先重算公式）。 */
    @Test
    public void importMonthlyCostLedger() throws Exception {
        File src = BizKit.outFile("merge", "0077_merge_monthly-cost-ledger.xlsx");
        if (!src.exists()) {
            exportMonthlyCostLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0077_merge_monthly-cost-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0077MonthlyCostLedgerService service = factory.createApi(Merge0077MonthlyCostLedgerService.class);
            List<JQuickRow> rows = service.importMonthlyCostLedger("field", "value");

            System.out.println("【场景77】月度成本费用台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    费用大类=" + r.get("costCategory") + ", 费用明细=" + r.get("costItem")
                        + ", 预算金额=" + r.get("budgetAmount") + ", 实际金额=" + r.get("actualAmount")
                        + ", 差异=" + r.get("difference") + ", 执行率=" + r.get("executionRate"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按费用大类分组，大类切换处插入小计行，末尾追加合计行（不做差异计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String category = String.valueOf(r.get("a"));
            if (current != null && !current.equals(category)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = category;
        }
        if (current != null) {
            out.add(subtotalRow(current));
        }
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写费用大类与小计文字，差异留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String category) {
        JQuickRow row = new JQuickRow();
        row.put("a", category);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条费用明细（a=费用大类，b=费用明细，c=预算，d=实际，e/f 留空由 FORMULAS 计算）。 */
    private static JQuickRow line(String category, String item, double budgetAmount, double actualAmount) {
        JQuickRow row = new JQuickRow();
        row.put("a", category);
        row.put("b", item);
        row.put("c", budgetAmount);
        row.put("d", actualAmount);
        row.put("e", null);
        row.put("f", null);
        return row;
    }
}
