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
 * 场景 41：应收账款催收台账（审计对账类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按客户归集各账期的应收与回款，逐行算「未收余额 = 应收金额 - 已收金额」，
 * 每个客户后跟一行余额小计，末尾一行给出全部客户合计；客户名称列纵向合并成一个区块，
 * 逾期超过 90 天的明细在逾期天数列标红。
 *
 * <p>🟡 标记含义：Java 只负责「按客户分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行减法与小计 / 合计由 {@code jquick/biz/merge/0041_merge_ar-collection-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0041ArCollectionLedgerDemo {

    private static final String XML = "jquick/biz/merge/0041_merge_ar-collection-ledger.xml";

    /** 导出：Java 分组 + 插小计行，客户列纵向合并，未收余额由 XML FORMULAS 完成。 */
    @Test
    public void exportArCollection() throws Exception {
        // 1. 构造扁平应收明细（Java 不做任何余额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("华越集团", "2026-01", 300000, 100000, 120));
        flat.add(line("华越集团", "2026-02", 250000, 250000, 0));
        flat.add(line("华越集团", "2026-03", 180000, 120000, 30));
        flat.add(line("恒信商贸", "2026-02", 200000, 150000, 45));
        flat.add(line("恒信商贸", "2026-03", 150000, 150000, 0));

        // 2. Java 分组并插入小计 / 合计占位行（未收余额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0041_merge_ar-collection-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("lavenderPurple", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0041ArCollectionLedgerService service = factory.createApi(Merge0041ArCollectionLedgerService.class);
            service.exportArCollection("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("应收账款催收台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行未收余额 = 应收 - 已收 = 300000 - 100000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(200000.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 华越集团小计余额 = SUM(C2:C4) - SUM(D2:D4) = 730000 - 470000
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("C5-D5", e5.getCellFormula());
            Assert.assertEquals(260000.0, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 全客户合计余额 = 合计应收 - 合计已收 = 1080000 - 770000
            Cell e9 = sheet.getRow(8).getCell(4);
            Assert.assertEquals("C9-D9", e9.getCellFormula());
            Assert.assertEquals(310000.0, evaluator.evaluate(e9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 逾期 120 天的明细标红
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("逾期超 90 天应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) f2.getCellStyle()).getFont().getColor());
            // 客户列纵向合并：华越集团 A2:A4、恒信商贸 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("华越集团 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("恒信商贸 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景41】应收账款催收台账导出: " + out.getAbsolutePath()
                    + "，未收余额合计 " + evaluator.evaluate(e9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的应收账款催收台账（先重算公式）。 */
    @Test
    public void importArCollection() throws Exception {
        File src = BizKit.outFile("merge", "0041_merge_ar-collection-ledger.xlsx");
        if (!src.exists()) {
            exportArCollection();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0041_merge_ar-collection-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0041ArCollectionLedgerService service = factory.createApi(Merge0041ArCollectionLedgerService.class);
            List<JQuickRow> rows = service.importArCollection("field", "value");

            System.out.println("【场景41】应收账款催收台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    客户=" + r.get("customer") + ", 账期=" + r.get("period")
                        + ", 应收=" + r.get("receivable") + ", 已收=" + r.get("received")
                        + ", 未收=" + r.get("balance") + ", 逾期=" + r.get("overdueDays"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按客户分组，客户切换处插入小计行，末尾追加合计行（不做余额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String customer = String.valueOf(r.get("a"));
            if (current != null && !current.equals(customer)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = customer;
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

    /** 小计占位行：只写客户与小计文字，未收余额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String customer) {
        JQuickRow row = new JQuickRow();
        row.put("a", customer);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条应收明细（a=客户，b=账期，c=应收，d=已收，f=逾期天数，e 留空）。 */
    private static JQuickRow line(String customer, String period, double receivable, double received, double overdueDays) {
        JQuickRow row = new JQuickRow();
        row.put("a", customer);
        row.put("b", period);
        row.put("c", receivable);
        row.put("d", received);
        row.put("e", null);
        row.put("f", overdueDays);
        return row;
    }
}
