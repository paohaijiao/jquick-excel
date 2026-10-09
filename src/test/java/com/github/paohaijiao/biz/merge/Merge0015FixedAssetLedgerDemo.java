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
 * 场景 86：固定资产折旧台账（台账汇总类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按「资产类别 → 资产名称」两级归集固定资产，逐行算「原值 = 数量 × 单价」
 * 「净值 = 原值 - 已提折旧」；每个资产类别后跟一行小计，末尾一行给出全部类别合计；
 * 资产类别列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按资产类别分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行原值 / 净值与小计 / 合计由 {@code jquick/biz/merge/0015_merge_fixed-asset-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0015FixedAssetLedgerDemo {

    private static final String XML = "jquick/biz/merge/0015_merge_fixed-asset-ledger.xml";

    /** 导出：Java 分组 + 插小计行，资产类别列纵向合并，原值 / 净值由 XML FORMULAS 完成。 */
    @Test
    public void exportFixedAssetLedger() throws Exception {
        // 1. 构造扁平资产明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("电子设备", "笔记本电脑", 5, 8000.00, 12000.00));
        flat.add(line("电子设备", "服务器", 2, 50000.00, 40000.00));
        flat.add(line("电子设备", "打印机", 3, 3000.00, 4500.00));
        flat.add(line("办公家具", "办公桌", 10, 1800.00, 6000.00));
        flat.add(line("办公家具", "文件柜", 8, 900.00, 2400.00));

        // 2. Java 分组并插入小计 / 合计占位行（原值 / 净值留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0015_merge_fixed-asset-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("skyBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0015FixedAssetLedgerService service = factory.createApi(Merge0015FixedAssetLedgerService.class);
            service.exportFixedAssetLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("固定资产折旧台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行原值 = 数量 × 单价 = 5 × 8000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(40000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 电子设备小计原值 = SUM(E2:E4) = 40000 + 100000 + 9000
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("SUM(E2:E4)", e5.getCellFormula());
            Assert.assertEquals(149000.00, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 办公家具小计净值 = 原值小计 - 已提折旧小计 = 25200 - 8400
            Cell g8 = sheet.getRow(7).getCell(6);
            Assert.assertEquals("E8-F8", g8.getCellFormula());
            Assert.assertEquals(16800.00, evaluator.evaluate(g8).getNumberValue(), 0.0001);
            // 全部合计原值 = 小计相加 = 149000 + 25200
            Cell e9 = sheet.getRow(8).getCell(4);
            Assert.assertEquals("E5+E8", e9.getCellFormula());
            Assert.assertEquals(174200.00, evaluator.evaluate(e9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 资产类别列纵向合并：电子设备 A2:A4、办公家具 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("电子设备 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("办公家具 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景86】固定资产折旧台账导出: " + out.getAbsolutePath()
                    + "，合计原值 " + evaluator.evaluate(e9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的固定资产折旧台账（先重算公式）。 */
    @Test
    public void importFixedAssetLedger() throws Exception {
        File src = BizKit.outFile("merge", "0015_merge_fixed-asset-ledger.xlsx");
        if (!src.exists()) {
            exportFixedAssetLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0015_merge_fixed-asset-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0015FixedAssetLedgerService service = factory.createApi(Merge0015FixedAssetLedgerService.class);
            List<JQuickRow> rows = service.importFixedAssetLedger("field", "value");

            System.out.println("【场景86】固定资产折旧台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    资产类别=" + r.get("assetCategory") + ", 资产名称=" + r.get("assetName")
                        + ", 数量=" + r.get("quantity") + ", 单价=" + r.get("unitPrice")
                        + ", 原值=" + r.get("originalValue") + ", 已提折旧=" + r.get("accumulatedDepreciation")
                        + ", 净值=" + r.get("netValue"));
            }
            // 表头不计入数据行：5 项资产 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 项资产 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按资产类别分组，类别切换处插入小计行，末尾追加合计行（不做金额计算）。 */
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
        total.put("g", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写资产类别与小计文字，金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String category) {
        JQuickRow row = new JQuickRow();
        row.put("a", category);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }

    /** 构造一项资产（a=类别，b=名称，c=数量，d=单价，e/g 留空，f=已提折旧）。 */
    private static JQuickRow line(String category, String assetName, double quantity,
                                  double unitPrice, double accumulatedDepreciation) {
        JQuickRow row = new JQuickRow();
        row.put("a", category);
        row.put("b", assetName);
        row.put("c", quantity);
        row.put("d", unitPrice);
        row.put("e", null);
        row.put("f", accumulatedDepreciation);
        row.put("g", null);
        return row;
    }
}
