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
package com.github.paohaijiao.biz.style;

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
 * 场景 80：年度库存盘点差异表（库存盘点类，🟢 纯 XML）。
 *
 * <p>业务：逐物料比对账面数量与实盘数量，算「差异数量 = 实盘 - 账面」与「差异率 = 差异 / 账面」，
 * 末尾一行汇总账面总量、实盘总量、总差异与整体差异率；盘亏最严重的物料标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0080_style_annual-inventory-count.xml}：
 * FORMULAS 求逐行差异 / 差异率、SUM 求各列合计，STYLE 给合计行高亮、给盘亏最严重物料的差异标红；
 * Java 只构造盘点数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0080AnnualInventoryCountDemo {

    private static final String XML = "jquick/biz/style/0080_style_annual-inventory-count.xml";

    /** 导出：纯 XML 完成逐行差异、差异率、合计与盘亏标红。 */
    @Test
    public void exportAnnualInventoryCount() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e/f 留空，由 FORMULAS 计算
        rows.add(material("M001", "螺丝 M4", 10000, 9980, "盘亏"));
        rows.add(material("M002", "轴承 6204", 5000, 5000, "相符"));
        rows.add(material("M003", "电机 Y2", 200, 176, "盘亏"));
        rows.add(material("M004", "齿轮 Z30", 800, 820, "盘盈"));
        rows.add(material("M005", "密封圈", 3000, 2950, "盘亏"));
        rows.add(material("M006", "皮带 A型", 1000, 1005, "盘盈"));
        // 合计占位行：各列合计与整体差异率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0080_style_annual-inventory-count.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("terracotta", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0080AnnualInventoryCountService service = factory.createApi(Style0080AnnualInventoryCountService.class);
            service.exportAnnualInventoryCount("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("年度库存盘点差异表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行差异数量 = 实盘 - 账面 = 9980 - 10000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2-C2", e2.getCellFormula());
            Assert.assertEquals(-20.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 盘亏最严重的「电机 Y2」（Excel 第 4 行）差异 = 176 - 200
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals("D4-C4", e4.getCellFormula());
            Assert.assertEquals(-24.00, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 合计：账面总量 = SUM(C2:C7) = 20000
            Cell c8 = sheet.getRow(7).getCell(2);
            Assert.assertEquals("SUM(C2:C7)", c8.getCellFormula());
            Assert.assertEquals(20000.00, evaluator.evaluate(c8).getNumberValue(), 0.0001);
            // 合计：总差异 = 实盘总量 - 账面总量 = 19931 - 20000
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals("D8-C8", e8.getCellFormula());
            Assert.assertEquals(-69.00, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 盘亏最严重的差异标红加粗
            XSSFCellStyle lossStyle = (XSSFCellStyle) e4.getCellStyle();
            Assert.assertTrue("盘亏最严重应加粗", lossStyle.getFont().getBold());
            Assert.assertEquals("盘亏最严重应标红", (int) IndexedColors.RED.getIndex(), (int) lossStyle.getFont().getColor());

            System.out.println("【场景80】年度库存盘点差异表导出: " + out.getAbsolutePath()
                    + "，总差异数量 " + evaluator.evaluate(e8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的年度库存盘点差异表（先重算公式）。 */
    @Test
    public void importAnnualInventoryCount() throws Exception {
        File src = BizKit.outFile("style", "0080_style_annual-inventory-count.xlsx");
        if (!src.exists()) {
            exportAnnualInventoryCount();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0080_style_annual-inventory-count-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0080AnnualInventoryCountService service = factory.createApi(Style0080AnnualInventoryCountService.class);
            List<JQuickRow> rows = service.importAnnualInventoryCount("field", "value");

            System.out.println("【场景80】年度库存盘点差异表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    物料编码=" + r.get("materialCode") + ", 物料名称=" + r.get("materialName")
                        + ", 账面数量=" + r.get("bookQuantity") + ", 实盘数量=" + r.get("actualQuantity")
                        + ", 差异数量=" + r.get("differenceQuantity") + ", 差异率=" + r.get("differenceRate")
                        + ", 盘点结果=" + r.get("countResult"));
            }
            // 表头不计入数据行：6 种物料 + 1 行合计
            Assert.assertEquals("6 种物料 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一种物料（a=编码，b=名称，c=账面数量，d=实盘数量，e/f 留空，g=盘点结果）。 */
    private static JQuickRow material(String code, String name, double bookQuantity,
                                      double actualQuantity, String countResult) {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", bookQuantity);
        row.put("d", actualQuantity);
        row.put("e", null);
        row.put("f", null);
        row.put("g", countResult);
        return row;
    }
}
