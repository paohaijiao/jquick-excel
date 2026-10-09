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
 * 场景 60：电商平台销售对账表（审计对账类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按平台归集各月平台结算额与我方账面额，逐行算「差异 = 平台结算 - 我方账面」，
 * 每个平台后跟一行小计，末尾一行给出全部平台合计；平台名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按平台分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行差异与小计 / 合计由 {@code jquick/biz/merge/0060_merge_ecommerce-reconciliation.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0060EcommerceReconciliationDemo {

    private static final String XML = "jquick/biz/merge/0060_merge_ecommerce-reconciliation.xml";

    /** 导出：Java 分组 + 插小计行，平台列纵向合并，差异与小计由 XML FORMULAS 完成。 */
    @Test
    public void exportEcommerceReconciliation() throws Exception {
        // 1. 构造扁平对账明细（Java 不做任何差异计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("天猫", "2026-01", 520000.00, 518000.00));
        flat.add(line("天猫", "2026-02", 480000.00, 483000.00));
        flat.add(line("天猫", "2026-03", 610000.00, 609500.00));
        flat.add(line("京东", "2026-01", 320000.00, 318000.00));
        flat.add(line("京东", "2026-02", 295000.00, 296000.00));

        // 2. Java 分组并插入小计 / 合计占位行（差异留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0060_merge_ecommerce-reconciliation.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("coral", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0060EcommerceReconciliationService service = factory.createApi(Merge0060EcommerceReconciliationService.class);
            service.exportEcommerceReconciliation("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("电商平台销售对账表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行差异 = 平台结算 - 我方账面 = 520000 - 518000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(2000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 天猫小计差异 = 小计结算 - 小计账面 = 1610000 - 1610500
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("C5-D5", e5.getCellFormula());
            Assert.assertEquals(-500.00, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 全平台合计差异 = 合计结算 - 合计账面 = 2225000 - 2224500
            Cell e9 = sheet.getRow(8).getCell(4);
            Assert.assertEquals("C9-D9", e9.getCellFormula());
            Assert.assertEquals(500.00, evaluator.evaluate(e9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 平台列纵向合并：天猫 A2:A4、京东 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("天猫 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("京东 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景60】电商平台销售对账表导出: " + out.getAbsolutePath()
                    + "，合计差异 " + evaluator.evaluate(e9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的电商平台销售对账表（先重算公式）。 */
    @Test
    public void importEcommerceReconciliation() throws Exception {
        File src = BizKit.outFile("merge", "0060_merge_ecommerce-reconciliation.xlsx");
        if (!src.exists()) {
            exportEcommerceReconciliation();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0060_merge_ecommerce-reconciliation-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0060EcommerceReconciliationService service = factory.createApi(Merge0060EcommerceReconciliationService.class);
            List<JQuickRow> rows = service.importEcommerceReconciliation("field", "value");

            System.out.println("【场景60】电商平台销售对账表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    平台=" + r.get("platform") + ", 月份=" + r.get("month")
                        + ", 平台结算=" + r.get("platformAmount") + ", 我方账面=" + r.get("bookAmount")
                        + ", 差异=" + r.get("difference"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按平台分组，平台切换处插入小计行，末尾追加合计行（不做差异计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String platform = String.valueOf(r.get("a"));
            if (current != null && !current.equals(platform)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = platform;
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
        out.add(total);
        return out;
    }

    /** 小计占位行：只写平台与小计文字，差异留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String platform) {
        JQuickRow row = new JQuickRow();
        row.put("a", platform);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        return row;
    }

    /** 构造一条对账明细（a=平台，b=月份，c=平台结算，d=我方账面，e 留空由 FORMULAS 计算）。 */
    private static JQuickRow line(String platform, String month, double platformAmount, double bookAmount) {
        JQuickRow row = new JQuickRow();
        row.put("a", platform);
        row.put("b", month);
        row.put("c", platformAmount);
        row.put("d", bookAmount);
        row.put("e", null);
        return row;
    }
}
