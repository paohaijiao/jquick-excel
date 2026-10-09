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
 * 场景 73：仓库库位库存明细表（库存盘点类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按仓库归集各库位的库存数量与单位成本，逐行算「库存金额 = 数量 × 单位成本」，
 * 每个仓库后跟一行小计，末尾一行给出全部仓库合计；仓库名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按仓库分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行金额与小计 / 合计由 {@code jquick/biz/merge/0073_merge_warehouse-location-inventory.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0073WarehouseLocationInventoryDemo {

    private static final String XML = "jquick/biz/merge/0073_merge_warehouse-location-inventory.xml";

    /** 导出：Java 分组 + 插小计行，仓库列纵向合并，金额与小计由 XML FORMULAS 完成。 */
    @Test
    public void exportWarehouseLocationInventory() throws Exception {
        // 1. 构造扁平库存明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("一号仓", "A-01", "冷轧钢板", 500, 4.50));
        flat.add(line("一号仓", "A-02", "铜芯电缆", 300, 12.80));
        flat.add(line("一号仓", "A-03", "铝合金型材", 200, 8.20));
        flat.add(line("二号仓", "B-01", "不锈钢管", 150, 25.00));
        flat.add(line("二号仓", "B-02", "橡胶密封圈", 400, 2.30));

        // 2. Java 分组并插入小计 / 合计占位行（金额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0073_merge_warehouse-location-inventory.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("amber", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0073WarehouseLocationInventoryService service = factory.createApi(Merge0073WarehouseLocationInventoryService.class);
            service.exportWarehouseLocationInventory("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("仓库库位库存明细表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行库存金额 = 数量 × 单位成本 = 500 × 4.50
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(2250.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 一号仓小计数量 = SUM(D2:D4) = 500 + 300 + 200
            Cell d5 = sheet.getRow(4).getCell(3);
            Assert.assertEquals("SUM(D2:D4)", d5.getCellFormula());
            Assert.assertEquals(1000.00, evaluator.evaluate(d5).getNumberValue(), 0.0001);
            // 二号仓小计金额 = 150 × 25.00 + 400 × 2.30 = 3750 + 920
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals(4670.00, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 全部仓库合计金额 = 7730 + 4670
            Cell f9 = sheet.getRow(8).getCell(5);
            Assert.assertEquals("F5+F8", f9.getCellFormula());
            Assert.assertEquals(12400.00, evaluator.evaluate(f9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 仓库列纵向合并：一号仓 A2:A4、二号仓 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("一号仓 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("二号仓 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景73】仓库库位库存明细表导出: " + out.getAbsolutePath()
                    + "，库存金额合计 " + evaluator.evaluate(f9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的库存明细表（先重算公式）。 */
    @Test
    public void importWarehouseLocationInventory() throws Exception {
        File src = BizKit.outFile("merge", "0073_merge_warehouse-location-inventory.xlsx");
        if (!src.exists()) {
            exportWarehouseLocationInventory();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0073_merge_warehouse-location-inventory-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0073WarehouseLocationInventoryService service = factory.createApi(Merge0073WarehouseLocationInventoryService.class);
            List<JQuickRow> rows = service.importWarehouseLocationInventory("field", "value");

            System.out.println("【场景73】仓库库位库存明细表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    仓库=" + r.get("warehouse") + ", 库位=" + r.get("location")
                        + ", 物料=" + r.get("materialName") + ", 数量=" + r.get("stockQty")
                        + ", 单位成本=" + r.get("unitCost") + ", 金额=" + r.get("stockAmount"));
            }
            // 表头不计入数据行：5 个库位 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 个库位 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按仓库分组，仓库切换处插入小计行，末尾追加合计行（不做金额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String warehouse = String.valueOf(r.get("a"));
            if (current != null && !current.equals(warehouse)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = warehouse;
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

    /** 小计占位行：只写仓库与小计文字，金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String warehouse) {
        JQuickRow row = new JQuickRow();
        row.put("a", warehouse);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条库位库存（a=仓库，b=库位，c=物料名称，d=库存数量，e=单位成本，f 留空由 FORMULAS 计算）。 */
    private static JQuickRow line(String warehouse, String location, String materialName,
                                  int stockQty, double unitCost) {
        JQuickRow row = new JQuickRow();
        row.put("a", warehouse);
        row.put("b", location);
        row.put("c", materialName);
        row.put("d", stockQty);
        row.put("e", unitCost);
        row.put("f", null);
        return row;
    }
}
