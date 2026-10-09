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
 * 场景 44：固定资产盘点表（库存盘点类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按使用部门盘点固定资产，逐行算「盘盈盘亏 = 实盘数量 - 账面数量」，
 * 每个部门后跟一行数量小计，末尾一行给出全公司合计；部门名称列纵向合并成一个区块，
 * 盘亏（差额为负）的明细标红。
 *
 * <p>🟡 标记含义：Java 只负责「按使用部门分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行减法与小计 / 合计由 {@code jquick/biz/merge/0044_merge_fixed-asset-inventory.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0044FixedAssetInventoryDemo {

    private static final String XML = "jquick/biz/merge/0044_merge_fixed-asset-inventory.xml";

    /** 导出：Java 分组 + 插小计行，部门列纵向合并，盘盈盘亏由 XML FORMULAS 完成。 */
    @Test
    public void exportFixedAssetInventory() throws Exception {
        // 1. 构造扁平资产明细（Java 不做任何差额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(asset("研发部", "FA-001", "服务器", 10, 10, 80000));
        flat.add(asset("研发部", "FA-002", "开发工作站", 20, 18, 45000));
        flat.add(asset("研发部", "FA-003", "测试设备", 5, 6, 30000));
        flat.add(asset("行政部", "FA-004", "办公家具", 50, 50, 25000));
        flat.add(asset("行政部", "FA-005", "激光打印机", 8, 7, 12000));

        // 2. Java 分组并插入小计 / 合计占位行（盘盈盘亏留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0044_merge_fixed-asset-inventory.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("charcoal", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0044FixedAssetInventoryService service = factory.createApi(Merge0044FixedAssetInventoryService.class);
            service.exportFixedAssetInventory("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("固定资产盘点表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 盘亏明细：实盘 - 账面 = 18 - 20
            Cell f3 = sheet.getRow(2).getCell(5);
            Assert.assertEquals("E3-D3", f3.getCellFormula());
            Assert.assertEquals(-2.0, evaluator.evaluate(f3).getNumberValue(), 0.0001);
            // 研发部小计盘亏 = 合计实盘 - 合计账面 = 34 - 35
            Cell f5 = sheet.getRow(4).getCell(5);
            Assert.assertEquals("E5-D5", f5.getCellFormula());
            Assert.assertEquals(-1.0, evaluator.evaluate(f5).getNumberValue(), 0.0001);
            // 全公司合计盘亏 = 合计实盘 - 合计账面 = 91 - 93
            Cell f9 = sheet.getRow(8).getCell(5);
            Assert.assertEquals("E9-D9", f9.getCellFormula());
            Assert.assertEquals(-2.0, evaluator.evaluate(f9).getNumberValue(), 0.0001);
            // 合计资产原值 = 两个小计相加 = 155000 + 37000
            Cell g9 = sheet.getRow(8).getCell(6);
            Assert.assertEquals("G5+G8", g9.getCellFormula());
            Assert.assertEquals(192000.0, evaluator.evaluate(g9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 盘亏明细标红
            Assert.assertEquals("盘亏明细应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) f3.getCellStyle()).getFont().getColor());
            // 部门列纵向合并：研发部 A2:A4、行政部 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("研发部 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("行政部 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景44】固定资产盘点表导出: " + out.getAbsolutePath()
                    + "，盘盈盘亏合计 " + evaluator.evaluate(f9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的固定资产盘点表（先重算公式）。 */
    @Test
    public void importFixedAssetInventory() throws Exception {
        File src = BizKit.outFile("merge", "0044_merge_fixed-asset-inventory.xlsx");
        if (!src.exists()) {
            exportFixedAssetInventory();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0044_merge_fixed-asset-inventory-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0044FixedAssetInventoryService service = factory.createApi(Merge0044FixedAssetInventoryService.class);
            List<JQuickRow> rows = service.importFixedAssetInventory("field", "value");

            System.out.println("【场景44】固定资产盘点表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    部门=" + r.get("dept") + ", 资产=" + r.get("assetName")
                        + ", 账面=" + r.get("bookQty") + ", 实盘=" + r.get("actualQty")
                        + ", 盘盈盘亏=" + r.get("diffQty") + ", 原值=" + r.get("originalValue"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按使用部门分组，部门切换处插入小计行，末尾追加合计行（不做差额计算）。 */
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
        total.put("g", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写部门与小计文字，盘盈盘亏、原值留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String dept) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }

    /** 构造一条资产明细（a=部门，b=编号，c=名称，d=账面，e=实盘，g=原值，f 留空）。 */
    private static JQuickRow asset(String dept, String assetNo, String assetName, double bookQty, double actualQty, double originalValue) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", assetNo);
        row.put("c", assetName);
        row.put("d", bookQty);
        row.put("e", actualQty);
        row.put("f", null);
        row.put("g", originalValue);
        return row;
    }
}
