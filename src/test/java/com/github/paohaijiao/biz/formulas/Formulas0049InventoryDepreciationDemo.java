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
package com.github.paohaijiao.biz.formulas;

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
 * 场景 49：存货跌价准备计提表（台账汇总类，🟢 纯 XML）。
 *
 * <p>业务：期末逐项测算存货跌价，逐行算「账面成本 = 数量 × 单位成本」与
 * 「跌价准备 = 账面成本 - 可变现净值」，末尾一行汇总，跌价金额最高的存货标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0049_formulas_inventory-depreciation.xml}：
 * FORMULAS 做逐行乘法与减法、合计 SUM，STYLE 给合计行高亮、给跌价金额最高的存货标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0049InventoryDepreciationDemo {

    private static final String XML = "jquick/biz/formulas/0049_formulas_inventory-depreciation.xml";

    /** 导出：纯 XML 完成账面成本、跌价准备、合计汇总与跌价金额最高项标红。 */
    @Test
    public void exportInventoryDepreciation() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(item("IC2601", "成品A", 200, 120, 20000.00));
        rows.add(item("IC2602", "成品B", 150, 80, 10500.00));
        rows.add(item("IC2603", "半成品C", 300, 50, 9000.00));
        rows.add(item("IC2604", "原材料D", 500, 30, 14500.00));
        rows.add(item("IC2605", "原材料E", 400, 25, 9800.00));
        // 合计占位行：账面成本与跌价准备合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0049_formulas_inventory-depreciation.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("slate", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0049InventoryDepreciationService service = factory.createApi(Formulas0049InventoryDepreciationService.class);
            service.exportInventoryDepreciation("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("存货跌价准备计提表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首项账面成本 = 数量 × 单位成本 = 200 × 120
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(24000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 首项跌价准备 = 账面成本 - 可变现净值 = 24000 - 20000
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("E2-F2", g2.getCellFormula());
            Assert.assertEquals(4000.00, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 跌价金额最高项（半成品C）= 15000 - 9000
            Cell g4 = sheet.getRow(3).getCell(6);
            Assert.assertEquals("E4-F4", g4.getCellFormula());
            Assert.assertEquals(6000.00, evaluator.evaluate(g4).getNumberValue(), 0.0001);
            // 合计行账面成本 = SUM(E2:E6) = 76000
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("SUM(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(76000.00, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行跌价准备 = SUM(G2:G6) = 12200
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("SUM(G2:G6)", g7.getCellFormula());
            Assert.assertEquals(12200.00, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 跌价金额最高项标红
            Assert.assertEquals("跌价金额最高项应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) g4.getCellStyle()).getFont().getColor());

            System.out.println("【场景49】存货跌价准备计提表导出: " + out.getAbsolutePath()
                    + "，跌价准备合计 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的存货跌价准备计提表（先重算公式）。 */
    @Test
    public void importInventoryDepreciation() throws Exception {
        File src = BizKit.outFile("formulas", "0049_formulas_inventory-depreciation.xlsx");
        if (!src.exists()) {
            exportInventoryDepreciation();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0049_formulas_inventory-depreciation-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0049InventoryDepreciationService service = factory.createApi(Formulas0049InventoryDepreciationService.class);
            List<JQuickRow> rows = service.importInventoryDepreciation("field", "value");

            System.out.println("【场景49】存货跌价准备计提表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    编码=" + r.get("itemCode") + ", 名称=" + r.get("itemName")
                        + ", 数量=" + r.get("quantity") + ", 单位成本=" + r.get("unitCost")
                        + ", 账面成本=" + r.get("bookCost") + ", 跌价准备=" + r.get("depreciation"));
            }
            // 表头不计入数据行：5 项存货 + 1 行合计
            Assert.assertEquals("5 项存货 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一项存货（a=编码，b=名称，c=数量，d=单位成本，f=可变现净值，e/g 留空由 FORMULAS 计算）。 */
    private static JQuickRow item(String itemCode, String itemName, int quantity, double unitCost, double netRealizableValue) {
        JQuickRow row = new JQuickRow();
        row.put("a", itemCode);
        row.put("b", itemName);
        row.put("c", quantity);
        row.put("d", unitCost);
        row.put("e", null);
        row.put("f", netRealizableValue);
        row.put("g", null);
        return row;
    }
}
