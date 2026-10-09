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
 * 场景 98：呆滞物料统计表（库存盘点类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按「仓库 → 物料」归集呆滞物料，逐行算「呆滞金额 = 库存数量 × 单价」；
 * 每个仓库后跟一行小计，末尾一行给出全仓合计；仓库列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按仓库分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行呆滞金额与分段小计 / 合计由 {@code jquick/biz/merge/0098_merge_slow-moving-material.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0098SlowMovingMaterialDemo {

    private static final String XML = "jquick/biz/merge/0098_merge_slow-moving-material.xml";

    /** 导出：Java 分组 + 插小计行，仓库列纵向合并，呆滞金额由 XML FORMULAS 完成。 */
    @Test
    public void exportSlowMovingMaterial() throws Exception {
        // 1. 构造扁平呆滞物料明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("一号仓", "M1001", "轴承", 500, 45.00, 210));
        flat.add(line("一号仓", "M1002", "密封圈", 800, 12.00, 180));
        flat.add(line("一号仓", "M1003", "皮带", 300, 80.00, 365));
        flat.add(line("二号仓", "M2001", "齿轮", 200, 150.00, 400));
        flat.add(line("二号仓", "M2002", "弹簧", 600, 5.00, 150));

        // 2. Java 分组并插入小计 / 合计占位行（呆滞金额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0098_merge_slow-moving-material.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("royalGold", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0098SlowMovingMaterialService service = factory.createApi(Merge0098SlowMovingMaterialService.class);
            service.exportSlowMovingMaterial("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("呆滞物料统计表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行呆滞金额 = 库存数量 × 单价 = 500 × 45
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(22500.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 一号仓小计呆滞金额 = SUM(F2:F4) = 22500 + 9600 + 24000
            Cell f5 = sheet.getRow(4).getCell(5);
            Assert.assertEquals("SUM(F2:F4)", f5.getCellFormula());
            Assert.assertEquals(56100.00, evaluator.evaluate(f5).getNumberValue(), 0.0001);
            // 二号仓小计呆滞金额 = SUM(F6:F7) = 30000 + 3000
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F6:F7)", f8.getCellFormula());
            Assert.assertEquals(33000.00, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 全部合计呆滞金额 = 小计相加 = 56100 + 33000
            Cell f9 = sheet.getRow(8).getCell(5);
            Assert.assertEquals("F5+F8", f9.getCellFormula());
            Assert.assertEquals(89100.00, evaluator.evaluate(f9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 仓库列纵向合并：一号仓 A2:A4、二号仓 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("一号仓 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("二号仓 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景98】呆滞物料统计表导出: " + out.getAbsolutePath()
                    + "，合计呆滞金额 " + evaluator.evaluate(f9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的呆滞物料统计表（先重算公式）。 */
    @Test
    public void importSlowMovingMaterial() throws Exception {
        File src = BizKit.outFile("merge", "0098_merge_slow-moving-material.xlsx");
        if (!src.exists()) {
            exportSlowMovingMaterial();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0098_merge_slow-moving-material-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0098SlowMovingMaterialService service = factory.createApi(Merge0098SlowMovingMaterialService.class);
            List<JQuickRow> rows = service.importSlowMovingMaterial("field", "value");

            System.out.println("【场景98】呆滞物料统计表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    仓库=" + r.get("warehouse") + ", 物料编码=" + r.get("materialCode")
                        + ", 物料名称=" + r.get("materialName") + ", 库存数量=" + r.get("stockQuantity")
                        + ", 单价=" + r.get("unitPrice") + ", 呆滞金额=" + r.get("slowMovingAmount")
                        + ", 库龄天数=" + r.get("agingDays"));
            }
            // 表头不计入数据行：5 项物料 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 项物料 + 2 小计 + 1 合计", 8, rows.size());
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
        total.put("g", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写仓库与小计文字，呆滞金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String warehouse) {
        JQuickRow row = new JQuickRow();
        row.put("a", warehouse);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }

    /** 构造一项呆滞物料（a=仓库，b=编码，c=名称，d=数量，e=单价，f 留空，g=库龄天数）。 */
    private static JQuickRow line(String warehouse, String materialCode, String materialName,
                                  double stockQuantity, double unitPrice, double agingDays) {
        JQuickRow row = new JQuickRow();
        row.put("a", warehouse);
        row.put("b", materialCode);
        row.put("c", materialName);
        row.put("d", stockQuantity);
        row.put("e", unitPrice);
        row.put("f", null);
        row.put("g", agingDays);
        return row;
    }
}
