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
 * 场景 40：销售提成计算表（绩效考核类，🟢 纯 XML）。
 *
 * <p>业务：按销售员核算目标达成情况，逐行算「达成率 = 实际销售额 / 销售目标」与
 * 「提成金额 = 实际销售额 × 提成比例」，未达成目标的销售员达成率标红，末尾一行给出整体达成率。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0040_style_sales-commission.xml}：
 * FORMULAS 做逐行除法与乘法、合计行汇总，STYLE 给合计行高亮、给未达成销售员的达成率标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0040SalesCommissionDemo {

    private static final String XML = "jquick/biz/style/0040_style_sales-commission.xml";

    /** 导出：纯 XML 完成达成率、提成、合计汇总与未达成标红。 */
    @Test
    public void exportSalesCommission() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(salesman("张伟", "华东", 800000, 920000, 0.03));
        rows.add(salesman("李娜", "华南", 600000, 480000, 0.025));
        rows.add(salesman("王强", "华北", 700000, 735000, 0.03));
        rows.add(salesman("赵敏", "西南", 500000, 560000, 0.028));
        rows.add(salesman("陈刚", "东北", 400000, 380000, 0.02));
        // 合计占位行：目标、实际销售额与提成金额留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0040_style_sales-commission.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("azure", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0040SalesCommissionService service = factory.createApi(Style0040SalesCommissionService.class);
            service.exportSalesCommission("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("销售提成计算表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行达成率 = 920000 / 800000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/C2", e2.getCellFormula());
            Assert.assertEquals(1.15, evaluator.evaluate(e2).getNumberValue(), 0.0000001);
            // 首行提成金额 = 920000 × 0.03
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("D2*F2", g2.getCellFormula());
            Assert.assertEquals(27600.0, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 未达成销售员达成率 = 480000 / 600000
            Cell e3 = sheet.getRow(2).getCell(4);
            Assert.assertEquals(0.8, evaluator.evaluate(e3).getNumberValue(), 0.0000001);
            // 合计销售目标 SUM(C2:C6) = 3000000
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(3000000.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计提成金额 SUM(G2:G6) = 84930
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("SUM(G2:G6)", g7.getCellFormula());
            Assert.assertEquals(84930.0, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 未达成销售员达成率标红
            Assert.assertEquals("未达成销售员应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) e3.getCellStyle()).getFont().getColor());

            System.out.println("【场景40】销售提成计算表导出: " + out.getAbsolutePath()
                    + "，提成金额合计 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的销售提成计算表（先重算公式）。 */
    @Test
    public void importSalesCommission() throws Exception {
        File src = BizKit.outFile("style", "0040_style_sales-commission.xlsx");
        if (!src.exists()) {
            exportSalesCommission();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0040_style_sales-commission-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0040SalesCommissionService service = factory.createApi(Style0040SalesCommissionService.class);
            List<JQuickRow> rows = service.importSalesCommission("field", "value");

            System.out.println("【场景40】销售提成计算表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    销售员=" + r.get("salesman") + ", 区域=" + r.get("region")
                        + ", 目标=" + r.get("target") + ", 实际=" + r.get("actualSales")
                        + ", 达成率=" + r.get("achieveRate") + ", 提成=" + r.get("commissionAmount"));
            }
            // 表头不计入数据行：5 名销售员 + 1 行合计
            Assert.assertEquals("5 名销售员 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一名销售员行（a=销售员，b=区域，c=目标，d=实际，f=提成比例，e/g 留空）。 */
    private static JQuickRow salesman(String name, String region, double target, double actualSales, double commissionRate) {
        JQuickRow row = new JQuickRow();
        row.put("a", name);
        row.put("b", region);
        row.put("c", target);
        row.put("d", actualSales);
        row.put("e", null);
        row.put("f", commissionRate);
        row.put("g", null);
        return row;
    }
}
