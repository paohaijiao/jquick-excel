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
import org.apache.poi.ss.usermodel.IndexedColors;
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
 * 场景 39：部门预算执行台账（台账汇总类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按部门归集各费用科目的年度预算与实际支出，逐行算「预算余额 = 预算 - 实际」与
 * 「执行率 = 实际 / 预算」，每个部门后跟一行小计，末尾一行给出全公司合计；
 * 部门名称列纵向合并成一个区块，超支科目的执行率标红。
 *
 * <p>🟡 标记含义：Java 只负责「按部门分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行减法与除法、小计 / 合计由 {@code jquick/biz/merge/0039_merge_department-budget-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0039DepartmentBudgetLedgerDemo {

    private static final String XML = "jquick/biz/merge/0039_merge_department-budget-ledger.xml";

    /** 导出：Java 分组 + 插小计行，部门列纵向合并，余额 / 执行率由 XML FORMULAS 完成。 */
    @Test
    public void exportDepartmentBudget() throws Exception {
        // 1. 构造扁平预算明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("研发部", "人力成本", 1200000, 1150000));
        flat.add(line("研发部", "设备采购", 500000, 520000));
        flat.add(line("研发部", "云服务", 200000, 150000));
        flat.add(line("市场部", "广告投放", 800000, 720000));
        flat.add(line("市场部", "展会费用", 300000, 340000));
        flat.add(line("市场部", "差旅费", 150000, 120000));

        // 2. Java 分组并插入小计 / 合计占位行（余额、执行率留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0039_merge_department-budget-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("amethyst", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0039DepartmentBudgetLedgerService service = factory.createApi(Merge0039DepartmentBudgetLedgerService.class);
            service.exportDepartmentBudget("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("部门预算执行台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行余额 = 预算 - 实际 = 1200000 - 1150000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(50000.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 超支科目执行率 = 520000 / 500000 = 1.04
            Cell f3 = sheet.getRow(2).getCell(5);
            Assert.assertEquals("D3/C3", f3.getCellFormula());
            Assert.assertEquals(1.04, evaluator.evaluate(f3).getNumberValue(), 0.0000001);
            // 研发部小计余额 = SUM(C2:C4) - SUM(D2:D4) = 1900000 - 1820000
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("C5-D5", e5.getCellFormula());
            Assert.assertEquals(80000.0, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 全公司合计余额 = 两个小计相加 = 80000 + 70000
            Cell e10 = sheet.getRow(9).getCell(4);
            Assert.assertEquals("E5+E9", e10.getCellFormula());
            Assert.assertEquals(150000.0, evaluator.evaluate(e10).getNumberValue(), 0.0001);
            // 合计执行率 = 合计实际 / 合计预算 = 3000000 / 3150000
            Cell f10 = sheet.getRow(9).getCell(5);
            Assert.assertEquals("D10/C10", f10.getCellFormula());
            Assert.assertEquals(3000000d / 3150000d, evaluator.evaluate(f10).getNumberValue(), 0.0000001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 超支科目执行率标红
            Assert.assertEquals("超支科目应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) f3.getCellStyle()).getFont().getColor());
            // 部门列纵向合并：研发部 A2:A4、市场部 A6:A8
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("研发部 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("市场部 A6:A8 应合并", regions.contains(new CellRangeAddress(5, 7, 0, 0)));

            System.out.println("【场景39】部门预算执行台账导出: " + out.getAbsolutePath()
                    + "，预算余额合计 " + evaluator.evaluate(e10).getNumberValue());
        }
    }

    /** 导入解析：读取导出的部门预算执行台账（先重算公式）。 */
    @Test
    public void importDepartmentBudget() throws Exception {
        File src = BizKit.outFile("merge", "0039_merge_department-budget-ledger.xlsx");
        if (!src.exists()) {
            exportDepartmentBudget();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0039_merge_department-budget-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0039DepartmentBudgetLedgerService service = factory.createApi(Merge0039DepartmentBudgetLedgerService.class);
            List<JQuickRow> rows = service.importDepartmentBudget("field", "value");

            System.out.println("【场景39】部门预算执行台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    部门=" + r.get("dept") + ", 科目=" + r.get("subject")
                        + ", 预算=" + r.get("budget") + ", 实际=" + r.get("actual")
                        + ", 余额=" + r.get("balance") + ", 执行率=" + r.get("executionRate"));
            }
            // 表头不计入数据行：6 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("6 条明细 + 2 小计 + 1 合计", 9, rows.size());
        }
    }

    /** 按部门分组，部门切换处插入小计行，末尾追加合计行（不做余额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String dept = String.valueOf(r.get("a"));
            if (current != null && !current.equals(dept)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = dept;
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

    /** 小计占位行：只写部门与小计文字，余额、执行率留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String dept) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条预算明细（a=部门，b=费用科目，c=年度预算，d=实际支出，e/f 留空）。 */
    private static JQuickRow line(String dept, String subject, double budget, double actual) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", subject);
        row.put("c", budget);
        row.put("d", actual);
        row.put("e", null);
        row.put("f", null);
        return row;
    }
}
