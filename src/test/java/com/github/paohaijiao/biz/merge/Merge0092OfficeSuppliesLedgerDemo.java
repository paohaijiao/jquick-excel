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
 * 场景 92：办公用品领用台账（台账汇总类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按「领用部门 → 用品名称」归集办公用品领用，逐行算「金额 = 数量 × 单价」；
 * 每个部门后跟一行小计，末尾一行给出全部门合计；领用部门列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按领用部门分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行金额与分段小计 / 合计由 {@code jquick/biz/merge/0092_merge_office-supplies-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0092OfficeSuppliesLedgerDemo {

    private static final String XML = "jquick/biz/merge/0092_merge_office-supplies-ledger.xml";

    /** 导出：Java 分组 + 插小计行，领用部门列纵向合并，金额由 XML FORMULAS 完成。 */
    @Test
    public void exportOfficeSuppliesLedger() throws Exception {
        // 1. 构造扁平领用明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("研发部", "陈晨", "签字笔", 30, 3.50));
        flat.add(line("研发部", "陈晨", "A4复印纸", 10, 25.00));
        flat.add(line("研发部", "林静", "硒鼓", 2, 380.00));
        flat.add(line("市场部", "刘洋", "笔记本", 15, 12.00));
        flat.add(line("市场部", "刘洋", "文件夹", 20, 8.00));

        // 2. Java 分组并插入小计 / 合计占位行（金额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0092_merge_office-supplies-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("forestGreen", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0092OfficeSuppliesLedgerService service = factory.createApi(Merge0092OfficeSuppliesLedgerService.class);
            service.exportOfficeSuppliesLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("办公用品领用台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行金额 = 数量 × 单价 = 30 × 3.5
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(105.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 研发部小计金额 = SUM(F2:F4) = 105 + 250 + 760
            Cell f5 = sheet.getRow(4).getCell(5);
            Assert.assertEquals("SUM(F2:F4)", f5.getCellFormula());
            Assert.assertEquals(1115.00, evaluator.evaluate(f5).getNumberValue(), 0.0001);
            // 市场部小计金额 = SUM(F6:F7) = 180 + 160
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F6:F7)", f8.getCellFormula());
            Assert.assertEquals(340.00, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 全部合计金额 = 小计相加 = 1115 + 340
            Cell f9 = sheet.getRow(8).getCell(5);
            Assert.assertEquals("F5+F8", f9.getCellFormula());
            Assert.assertEquals(1455.00, evaluator.evaluate(f9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 领用部门列纵向合并：研发部 A2:A4、市场部 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("研发部 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("市场部 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景92】办公用品领用台账导出: " + out.getAbsolutePath()
                    + "，合计金额 " + evaluator.evaluate(f9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的办公用品领用台账（先重算公式）。 */
    @Test
    public void importOfficeSuppliesLedger() throws Exception {
        File src = BizKit.outFile("merge", "0092_merge_office-supplies-ledger.xlsx");
        if (!src.exists()) {
            exportOfficeSuppliesLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0092_merge_office-supplies-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0092OfficeSuppliesLedgerService service = factory.createApi(Merge0092OfficeSuppliesLedgerService.class);
            List<JQuickRow> rows = service.importOfficeSuppliesLedger("field", "value");

            System.out.println("【场景92】办公用品领用台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    领用部门=" + r.get("department") + ", 领用人=" + r.get("receiver")
                        + ", 用品名称=" + r.get("suppliesName") + ", 数量=" + r.get("quantity")
                        + ", 单价=" + r.get("unitPrice") + ", 金额=" + r.get("amount"));
            }
            // 表头不计入数据行：5 项领用 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 项领用 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按领用部门分组，部门切换处插入小计行，末尾追加合计行（不做金额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String department = String.valueOf(r.get("a"));
            if (current != null && !current.equals(department)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = department;
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

    /** 小计占位行：只写领用部门与小计文字，金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String department) {
        JQuickRow row = new JQuickRow();
        row.put("a", department);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条领用明细（a=部门，b=领用人，c=用品名称，d=数量，e=单价，f 留空）。 */
    private static JQuickRow line(String department, String receiver, String suppliesName,
                                  double quantity, double unitPrice) {
        JQuickRow row = new JQuickRow();
        row.put("a", department);
        row.put("b", receiver);
        row.put("c", suppliesName);
        row.put("d", quantity);
        row.put("e", unitPrice);
        row.put("f", null);
        return row;
    }
}
