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
 * 场景 90：销售退货分析表（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：逐产品登记销售数量与退货数量，算「退货金额 = 退货数量 × 单价」
 * 「退货率 = 退货数量 / 销售数量」，末尾一行汇总销量、退货量与退货金额；退货率最高的产品标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0090_style_sales-return-analysis.xml}：
 * FORMULAS 求逐行退货金额 / 退货率、SUM 求各列合计、C8/B8 求整体退货率，
 * STYLE 给合计行高亮、给退货率最高的产品标红；Java 只构造退货数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0090SalesReturnAnalysisDemo {

    private static final String XML = "jquick/biz/style/0090_style_sales-return-analysis.xml";

    /** 导出：纯 XML 完成逐行退货金额、退货率、合计与最高退货率标红。 */
    @Test
    public void exportSalesReturnAnalysis() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e/f 留空，由 FORMULAS 计算
        rows.add(product("A型电机", 2000, 40, 120.00, "返修"));
        rows.add(product("B型电机", 1500, 45, 95.00, "返修"));
        rows.add(product("C型水泵", 800, 64, 260.00, "报废"));
        rows.add(product("D型水泵", 1200, 18, 310.00, "返修"));
        rows.add(product("E型风机", 600, 30, 450.00, "折价处理"));
        rows.add(product("F型风机", 1000, 15, 280.00, "返修"));
        // 合计占位行：销量 / 退货量 / 退货金额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0090_style_sales-return-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("amethyst", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0090SalesReturnAnalysisService service = factory.createApi(Style0090SalesReturnAnalysisService.class);
            service.exportSalesReturnAnalysis("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("销售退货分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行退货金额 = 退货数量 × 单价 = 40 × 120
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(4800.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 首行退货率 = 退货数量 / 销售数量 = 40 / 2000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2/B2", f2.getCellFormula());
            Assert.assertEquals(0.02, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 退货率最高的「C型水泵」（Excel 第 4 行）= 64 / 800
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals("C4/B4", f4.getCellFormula());
            Assert.assertEquals(0.08, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 合计：销量 = SUM(B2:B7) = 7100
            Cell b8 = sheet.getRow(7).getCell(1);
            Assert.assertEquals("SUM(B2:B7)", b8.getCellFormula());
            Assert.assertEquals(7100.00, evaluator.evaluate(b8).getNumberValue(), 0.0001);
            // 合计：退货金额 = SUM(E2:E7) = 48995
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals("SUM(E2:E7)", e8.getCellFormula());
            Assert.assertEquals(48995.00, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 最高退货率标红加粗
            XSSFCellStyle rateStyle = (XSSFCellStyle) f4.getCellStyle();
            Assert.assertTrue("最高退货率应加粗", rateStyle.getFont().getBold());
            Assert.assertEquals("最高退货率应标红", (int) IndexedColors.RED.getIndex(), (int) rateStyle.getFont().getColor());

            System.out.println("【场景90】销售退货分析表导出: " + out.getAbsolutePath()
                    + "，合计退货金额 " + evaluator.evaluate(e8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的销售退货分析表（先重算公式）。 */
    @Test
    public void importSalesReturnAnalysis() throws Exception {
        File src = BizKit.outFile("style", "0090_style_sales-return-analysis.xlsx");
        if (!src.exists()) {
            exportSalesReturnAnalysis();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0090_style_sales-return-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0090SalesReturnAnalysisService service = factory.createApi(Style0090SalesReturnAnalysisService.class);
            List<JQuickRow> rows = service.importSalesReturnAnalysis("field", "value");

            System.out.println("【场景90】销售退货分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    产品名称=" + r.get("productName") + ", 销售数量=" + r.get("salesQuantity")
                        + ", 退货数量=" + r.get("returnQuantity") + ", 单价=" + r.get("unitPrice")
                        + ", 退货金额=" + r.get("returnAmount") + ", 退货率=" + r.get("returnRate")
                        + ", 处理方式=" + r.get("handleType"));
            }
            // 表头不计入数据行：6 款产品 + 1 行合计
            Assert.assertEquals("6 款产品 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条退货明细（a=产品，b=销售数量，c=退货数量，d=单价，e/f 留空，g=处理方式）。 */
    private static JQuickRow product(String productName, double salesQuantity, double returnQuantity,
                                     double unitPrice, String handleType) {
        JQuickRow row = new JQuickRow();
        row.put("a", productName);
        row.put("b", salesQuantity);
        row.put("c", returnQuantity);
        row.put("d", unitPrice);
        row.put("e", null);
        row.put("f", null);
        row.put("g", handleType);
        return row;
    }
}
