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
 * 场景 32：供应商往来对账表（审计对账类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按供应商归集逐月往来对账，逐行算「差异金额 = 我方应付 - 供方对账」，
 * 每个供应商后跟一行差异小计，末尾一行合计；供应商名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按供应商分组、插入小计 / 合计占位行、确定合并范围」，
 * 差异金额与小计由 {@code jquick/biz/merge/0032_merge_supplier-reconciliation.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0032SupplierReconciliationDemo {

    private static final String XML = "jquick/biz/merge/0032_merge_supplier-reconciliation.xml";

    /** 导出：Java 分组 + 插小计行，供应商列纵向合并，差异金额由 XML FORMULAS 完成。 */
    @Test
    public void exportSupplierReconciliation() throws Exception {
        // 1. 构造扁平对账明细（Java 不做任何差异计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("华新电子", "2026-01", 150000, 150000, "本月无差异"));
        flat.add(line("华新电子", "2026-02", 120000, 118000, "运费差异 2000"));
        flat.add(line("恒通材料", "2026-01", 320000, 320000, "本月无差异"));
        flat.add(line("恒通材料", "2026-02", 280000, 275000, "退货未入账 5000"));
        flat.add(line("恒通材料", "2026-03", 90000, 90000, "本月无差异"));
        flat.add(line("佳美包装", "2026-01", 45000, 45000, "本月无差异"));
        flat.add(line("佳美包装", "2026-02", 38000, 40000, "重复开票 -2000"));

        // 2. Java 分组并插入小计 / 合计占位行（差异金额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0032_merge_supplier-reconciliation.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mintFresh", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0032SupplierReconciliationService service = factory.createApi(Merge0032SupplierReconciliationService.class);
            service.exportSupplierReconciliation("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("供应商往来对账表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行差异 = 我方应付 - 供方对账 = 150000 - 150000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(0.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 华新电子小计 = SUM(E2:E3) = 0 + 2000
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals("SUM(E2:E3)", e4.getCellFormula());
            Assert.assertEquals(2000.0, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 恒通材料小计 = SUM(E5:E7) = 0 + 5000 + 0
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals("SUM(E5:E7)", e8.getCellFormula());
            Assert.assertEquals(5000.0, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 合计 = 各供应商小计相加 = 2000 + 5000 - 2000
            Cell e12 = sheet.getRow(11).getCell(4);
            Assert.assertEquals("E4+E8+E11", e12.getCellFormula());
            Assert.assertEquals(5000.0, evaluator.evaluate(e12).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(3).getCell(0).getCellStyle()).getFont().getBold());
            // 供应商列纵向合并：华新电子 A2:A3、恒通材料 A5:A7、佳美包装 A9:A10
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 3 个合并区块", 3, regions.size());
            Assert.assertTrue("华新电子 A2:A3 应合并", regions.contains(new CellRangeAddress(1, 2, 0, 0)));
            Assert.assertTrue("恒通材料 A5:A7 应合并", regions.contains(new CellRangeAddress(4, 6, 0, 0)));
            Assert.assertTrue("佳美包装 A9:A10 应合并", regions.contains(new CellRangeAddress(8, 9, 0, 0)));

            System.out.println("【场景32】供应商往来对账表导出: " + out.getAbsolutePath()
                    + "，合计差异金额 " + evaluator.evaluate(e12).getNumberValue());
        }
    }

    /** 导入解析：读取导出的供应商往来对账表（先重算公式）。 */
    @Test
    public void importSupplierReconciliation() throws Exception {
        File src = BizKit.outFile("merge", "0032_merge_supplier-reconciliation.xlsx");
        if (!src.exists()) {
            exportSupplierReconciliation();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0032_merge_supplier-reconciliation-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0032SupplierReconciliationService service = factory.createApi(Merge0032SupplierReconciliationService.class);
            List<JQuickRow> rows = service.importSupplierReconciliation("field", "value");

            System.out.println("【场景32】供应商往来对账表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    供应商=" + r.get("supplier") + ", 月份=" + r.get("period")
                        + ", 我方应付=" + r.get("ourPayable") + ", 差异=" + r.get("diffAmount") + ", 说明=" + r.get("diffRemark"));
            }
            // 表头不计入数据行：7 条对账明细 + 3 行小计 + 1 行合计
            Assert.assertEquals("7 条明细 + 3 小计 + 1 合计", 11, rows.size());
        }
    }

    /** 按供应商分组，供应商切换处插入小计行，末尾追加合计行（不做差异计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String supplier = String.valueOf(r.get("a"));
            if (current != null && !current.equals(supplier)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = supplier;
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

    /** 小计占位行：只写供应商与小计文字，差异金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String supplier) {
        JQuickRow row = new JQuickRow();
        row.put("a", supplier);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条对账明细（a=供应商，b=对账月份，c=我方应付，d=供方对账，f=差异说明）。 */
    private static JQuickRow line(String supplier, String period, double ourPayable, double statement, String remark) {
        JQuickRow row = new JQuickRow();
        row.put("a", supplier);
        row.put("b", period);
        row.put("c", ourPayable);
        row.put("d", statement);
        row.put("e", null);
        row.put("f", remark);
        return row;
    }
}
