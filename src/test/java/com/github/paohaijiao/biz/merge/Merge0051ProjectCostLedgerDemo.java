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
 * 场景 51：项目成本归集台账（台账汇总类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按项目归集各成本科目发生额，每个项目后跟一行成本小计，末尾一行给出全部项目合计；
 * 项目名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按项目分组、插入小计 / 合计占位行、确定合并范围」，
 * 小计 / 合计由 {@code jquick/biz/merge/0051_merge_project-cost-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0051ProjectCostLedgerDemo {

    private static final String XML = "jquick/biz/merge/0051_merge_project-cost-ledger.xml";

    /** 导出：Java 分组 + 插小计行，项目列纵向合并，小计与合计由 XML FORMULAS 完成。 */
    @Test
    public void exportProjectCostLedger() throws Exception {
        // 1. 构造扁平成本明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("智能仓储系统", "设备采购", "2026-01", 300000.00));
        flat.add(line("智能仓储系统", "施工安装", "2026-02", 120000.00));
        flat.add(line("智能仓储系统", "软件授权", "2026-03", 80000.00));
        flat.add(line("产线改造", "设备采购", "2026-02", 260000.00));
        flat.add(line("产线改造", "技术服务", "2026-03", 90000.00));

        // 2. Java 分组并插入小计 / 合计占位行（成本金额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0051_merge_project-cost-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("bronze", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0051ProjectCostLedgerService service = factory.createApi(Merge0051ProjectCostLedgerService.class);
            service.exportProjectCostLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("项目成本归集台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首个项目小计 = SUM(D2:D4) = 300000 + 120000 + 80000
            Cell d5 = sheet.getRow(4).getCell(3);
            Assert.assertEquals("SUM(D2:D4)", d5.getCellFormula());
            Assert.assertEquals(500000.00, evaluator.evaluate(d5).getNumberValue(), 0.0001);
            // 第二个项目小计 = SUM(D6:D7) = 260000 + 90000
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("SUM(D6:D7)", d8.getCellFormula());
            Assert.assertEquals(350000.00, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 合计 = 两个小计相加 = 500000 + 350000
            Cell d9 = sheet.getRow(8).getCell(3);
            Assert.assertEquals("D5+D8", d9.getCellFormula());
            Assert.assertEquals(850000.00, evaluator.evaluate(d9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 项目列纵向合并：智能仓储系统 A2:A4、产线改造 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("智能仓储系统 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("产线改造 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景51】项目成本归集台账导出: " + out.getAbsolutePath()
                    + "，成本合计 " + evaluator.evaluate(d9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的项目成本归集台账（先重算公式）。 */
    @Test
    public void importProjectCostLedger() throws Exception {
        File src = BizKit.outFile("merge", "0051_merge_project-cost-ledger.xlsx");
        if (!src.exists()) {
            exportProjectCostLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0051_merge_project-cost-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0051ProjectCostLedgerService service = factory.createApi(Merge0051ProjectCostLedgerService.class);
            List<JQuickRow> rows = service.importProjectCostLedger("field", "value");

            System.out.println("【场景51】项目成本归集台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    项目=" + r.get("project") + ", 科目=" + r.get("subject")
                        + ", 月份=" + r.get("month") + ", 成本=" + r.get("cost"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按项目分组，项目切换处插入小计行，末尾追加合计行（不做金额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String project = String.valueOf(r.get("a"));
            if (current != null && !current.equals(project)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = project;
        }
        if (current != null) {
            out.add(subtotalRow(current));
        }
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写项目与小计文字，成本金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String project) {
        JQuickRow row = new JQuickRow();
        row.put("a", project);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        return row;
    }

    /** 构造一条成本明细（a=项目，b=科目，c=月份，d=成本金额，作为分段 SUM 的数据源）。 */
    private static JQuickRow line(String project, String subject, String month, double cost) {
        JQuickRow row = new JQuickRow();
        row.put("a", project);
        row.put("b", subject);
        row.put("c", month);
        row.put("d", cost);
        return row;
    }
}
