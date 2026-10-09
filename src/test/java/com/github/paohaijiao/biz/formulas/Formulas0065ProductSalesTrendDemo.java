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
 * 场景 65：产品销售月度趋势表（自助报表类，🟢 纯 XML）。
 *
 * <p>业务：按月份登记单一产品的销售数量与单价，逐行算「销售额 = 数量 × 单价」，
 * 并逐月算「环比增长率 = (本期销售额 - 上期销售额) / 上期销售额」，末尾一行汇总数量与销售额。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0065_formulas_product-sales-trend.xml}：
 * FORMULAS 求逐行销售额、环比与合计，STYLE 给合计行高亮；Java 只构造月度趋势数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0065ProductSalesTrendDemo {

    private static final String XML = "jquick/biz/formulas/0065_formulas_product-sales-trend.xml";

    /** 导出：纯 XML 完成销售额、环比增长率与合计。 */
    @Test
    public void exportProductSalesTrend() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // d=销售额、e=环比增长率 留空，由 FORMULAS 计算（首月无环比）
        rows.add(month("2026-01", 1200, 199.00));
        rows.add(month("2026-02", 1500, 199.00));
        rows.add(month("2026-03", 1350, 205.00));
        rows.add(month("2026-04", 1800, 205.00));
        rows.add(month("2026-05", 2000, 210.00));
        // 合计占位行：数量与销售额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0065_formulas_product-sales-trend.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("oceanBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0065ProductSalesTrendService service = factory.createApi(Formulas0065ProductSalesTrendService.class);
            service.exportProductSalesTrend("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("产品销售月度趋势表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首月销售额 = 数量 × 单价 = 1200 × 199
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("B2*C2", d2.getCellFormula());
            Assert.assertEquals(238800.00, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 次月环比 = (本期 - 上期) / 上期 = (298500 - 238800) / 238800
            Cell e3 = sheet.getRow(2).getCell(4);
            Assert.assertEquals("(D3-D2)/D2", e3.getCellFormula());
            Assert.assertEquals(0.25, evaluator.evaluate(e3).getNumberValue(), 0.0001);
            // 销售额合计 = SUM(D2:D6)
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(1603050.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景65】产品销售月度趋势表导出: " + out.getAbsolutePath()
                    + "，销售额合计 " + evaluator.evaluate(d7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的月度趋势表（先重算公式）。 */
    @Test
    public void importProductSalesTrend() throws Exception {
        File src = BizKit.outFile("formulas", "0065_formulas_product-sales-trend.xlsx");
        if (!src.exists()) {
            exportProductSalesTrend();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0065_formulas_product-sales-trend-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0065ProductSalesTrendService service = factory.createApi(Formulas0065ProductSalesTrendService.class);
            List<JQuickRow> rows = service.importProductSalesTrend("field", "value");

            System.out.println("【场景65】产品销售月度趋势表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    月份=" + r.get("month") + ", 数量=" + r.get("quantity")
                        + ", 单价=" + r.get("unitPrice") + ", 销售额=" + r.get("salesAmount")
                        + ", 环比=" + r.get("growthRate"));
            }
            // 表头不计入数据行：5 个月度明细 + 1 行合计
            Assert.assertEquals("5 条明细 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条月度明细（a=月份，b=数量，c=单价，d=销售额，e=环比增长率）。 */
    private static JQuickRow month(String month, int quantity, double unitPrice) {
        JQuickRow row = new JQuickRow();
        row.put("a", month);
        row.put("b", quantity);
        row.put("c", unitPrice);
        row.put("d", null);
        row.put("e", null);
        return row;
    }
}
