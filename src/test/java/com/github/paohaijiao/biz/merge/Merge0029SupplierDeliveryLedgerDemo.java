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
 * 场景 29：供应商供货台账（其他拓展类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按供应商归集采购供货明细，逐行算「供货金额 = 供货数量 × 单价」，
 * 每个供应商后跟一行金额小计，末尾一行合计；供应商名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按供应商分组、插入小计 / 合计占位行、确定合并范围」，
 * 供货金额与小计由 {@code jquick/biz/merge/0029_merge_supplier-delivery-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0029SupplierDeliveryLedgerDemo {

    private static final String XML = "jquick/biz/merge/0029_merge_supplier-delivery-ledger.xml";

    /** 导出：Java 分组 + 插小计行，供应商列纵向合并，供货金额由 XML FORMULAS 完成。 */
    @Test
    public void exportSupplierDelivery() throws Exception {
        // 1. 构造扁平供货明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(delivery("华新电子", "PO001", "电容", 2000, 1.5, "已到货"));
        flat.add(delivery("华新电子", "PO002", "电阻", 5000, 0.2, "已到货"));
        flat.add(delivery("恒通材料", "PO003", "钢板", 100, 320, "已到货"));
        flat.add(delivery("恒通材料", "PO004", "铝材", 200, 85, "部分到货"));
        flat.add(delivery("恒通材料", "PO005", "铜管", 50, 210, "未到货"));
        flat.add(delivery("佳美包装", "PO006", "纸箱", 3000, 2.8, "已到货"));
        flat.add(delivery("佳美包装", "PO007", "泡沫", 1500, 1.2, "已到货"));

        // 2. Java 分组并插入小计 / 合计占位行（供货金额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0029_merge_supplier-delivery-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sage", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0029SupplierDeliveryLedgerService service = factory.createApi(Merge0029SupplierDeliveryLedgerService.class);
            service.exportSupplierDelivery("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("供应商供货台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行供货金额 = 数量 × 单价 = 2000 × 1.5
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(3000.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 华新电子小计 = SUM(F2:F3) = 3000 + 1000
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals("SUM(F2:F3)", f4.getCellFormula());
            Assert.assertEquals(4000.0, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 合计 = 各供应商小计相加 = 4000 + 59500 + 10200
            Cell f12 = sheet.getRow(11).getCell(5);
            Assert.assertEquals("F4+F8+F11", f12.getCellFormula());
            Assert.assertEquals(73700.0, evaluator.evaluate(f12).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(3).getCell(0).getCellStyle()).getFont().getBold());
            // 供应商列纵向合并：华新电子 A2:A3、恒通材料 A5:A7、佳美包装 A9:A10
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 3 个合并区块", 3, regions.size());
            Assert.assertTrue("华新电子 A2:A3 应合并", regions.contains(new CellRangeAddress(1, 2, 0, 0)));
            Assert.assertTrue("恒通材料 A5:A7 应合并", regions.contains(new CellRangeAddress(4, 6, 0, 0)));
            Assert.assertTrue("佳美包装 A9:A10 应合并", regions.contains(new CellRangeAddress(8, 9, 0, 0)));

            System.out.println("【场景29】供应商供货台账导出: " + out.getAbsolutePath()
                    + "，合计供货金额 " + evaluator.evaluate(f12).getNumberValue());
        }
    }

    /** 导入解析：读取导出的供应商供货台账（先重算公式）。 */
    @Test
    public void importSupplierDelivery() throws Exception {
        File src = BizKit.outFile("merge", "0029_merge_supplier-delivery-ledger.xlsx");
        if (!src.exists()) {
            exportSupplierDelivery();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0029_merge_supplier-delivery-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0029SupplierDeliveryLedgerService service = factory.createApi(Merge0029SupplierDeliveryLedgerService.class);
            List<JQuickRow> rows = service.importSupplierDelivery("field", "value");

            System.out.println("【场景29】供应商供货台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    供应商=" + r.get("supplier") + ", 采购单=" + r.get("poNo")
                        + ", 数量=" + r.get("supplyQty") + ", 供货金额=" + r.get("supplyAmount") + ", 状态=" + r.get("arrivalStatus"));
            }
            // 表头不计入数据行：7 条供货明细 + 3 行小计 + 1 行合计
            Assert.assertEquals("7 条明细 + 3 小计 + 1 合计", 11, rows.size());
        }
    }

    /** 按供应商分组，供应商切换处插入小计行，末尾追加合计行（不做金额计算）。 */
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
        total.put("g", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写供应商与小计文字，供货金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String supplier) {
        JQuickRow row = new JQuickRow();
        row.put("a", supplier);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }

    /** 构造一条供货明细（a=供应商，b=采购单号，c=物料名称，d=供货数量，e=单价，g=到货状态）。 */
    private static JQuickRow delivery(String supplier, String poNo, String material, int qty, double price, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", supplier);
        row.put("b", poNo);
        row.put("c", material);
        row.put("d", qty);
        row.put("e", price);
        row.put("f", null);
        row.put("g", status);
        return row;
    }
}
