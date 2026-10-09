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
 * 场景 88：售后客诉质量分析表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>业务：逐产品登记销售量与客诉数量，算「客诉率 = 客诉数量 / 销售量」
 * 「严重客诉占比 = 严重客诉 / 客诉数量」，末尾一行汇总销售量、客诉量、严重客诉量与整体客诉率；
 * 客诉率最高的产品标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0088_style_after-sales-complaint-analysis.xml}：
 * FORMULAS 求逐行客诉率 / 严重占比、SUM 求合计，STYLE 给合计行高亮、给最高客诉率标红；
 * Java 只构造客诉数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0088AfterSalesComplaintAnalysisDemo {

    private static final String XML = "jquick/biz/style/0088_style_after-sales-complaint-analysis.xml";

    /** 导出：纯 XML 完成逐行客诉率、严重占比、合计与最高客诉率标红。 */
    @Test
    public void exportAfterSalesComplaintAnalysis() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e/f 留空，由 FORMULAS 计算
        rows.add(product("X100", 5000, 25, 3, "生产部"));
        rows.add(product("X200", 4200, 18, 1, "生产部"));
        rows.add(product("X300", 3000, 45, 9, "质检部"));
        rows.add(product("Y100", 6000, 30, 4, "生产部"));
        rows.add(product("Y200", 3500, 14, 1, "质检部"));
        rows.add(product("Z100", 2500, 20, 2, "生产部"));
        // 合计占位行：各列合计与整体客诉率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0088_style_after-sales-complaint-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mahogany", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0088AfterSalesComplaintAnalysisService service = factory.createApi(Style0088AfterSalesComplaintAnalysisService.class);
            service.exportAfterSalesComplaintAnalysis("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("售后客诉质量分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行客诉率 = 客诉数量 / 销售量 = 25 / 5000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2/B2", e2.getCellFormula());
            Assert.assertEquals(0.005, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 客诉率最高的「X300」（Excel 第 4 行）= 45 / 3000
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals("C4/B4", e4.getCellFormula());
            Assert.assertEquals(0.015, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 合计：客诉数量 = SUM(C2:C7) = 152
            Cell c8 = sheet.getRow(7).getCell(2);
            Assert.assertEquals("SUM(C2:C7)", c8.getCellFormula());
            Assert.assertEquals(152.0, evaluator.evaluate(c8).getNumberValue(), 0.0001);
            // 合计：整体客诉率 = 客诉合计 / 销售合计 = 152 / 24200
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals("C8/B8", e8.getCellFormula());
            Assert.assertEquals(152.0 / 24200.0, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 最高客诉率标红加粗
            XSSFCellStyle highStyle = (XSSFCellStyle) e4.getCellStyle();
            Assert.assertTrue("最高客诉率应加粗", highStyle.getFont().getBold());
            Assert.assertEquals("最高客诉率应标红", (int) IndexedColors.RED.getIndex(), (int) highStyle.getFont().getColor());

            System.out.println("【场景88】售后客诉质量分析表导出: " + out.getAbsolutePath()
                    + "，整体客诉率 " + evaluator.evaluate(e8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的售后客诉质量分析表（先重算公式）。 */
    @Test
    public void importAfterSalesComplaintAnalysis() throws Exception {
        File src = BizKit.outFile("style", "0088_style_after-sales-complaint-analysis.xlsx");
        if (!src.exists()) {
            exportAfterSalesComplaintAnalysis();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0088_style_after-sales-complaint-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0088AfterSalesComplaintAnalysisService service = factory.createApi(Style0088AfterSalesComplaintAnalysisService.class);
            List<JQuickRow> rows = service.importAfterSalesComplaintAnalysis("field", "value");

            System.out.println("【场景88】售后客诉质量分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    产品型号=" + r.get("productModel") + ", 销售量=" + r.get("salesQuantity")
                        + ", 客诉数量=" + r.get("complaintQuantity") + ", 严重客诉=" + r.get("seriousComplaint")
                        + ", 客诉率=" + r.get("complaintRate") + ", 严重客诉占比=" + r.get("seriousRatio")
                        + ", 责任部门=" + r.get("responsibleDepartment"));
            }
            // 表头不计入数据行：6 个产品 + 1 行合计
            Assert.assertEquals("6 个产品 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一个产品的客诉数据（a=型号，b=销售量，c=客诉数量，d=严重客诉，e/f 留空，g=责任部门）。 */
    private static JQuickRow product(String model, double salesQuantity, double complaintQuantity,
                                     double seriousComplaint, String responsibleDepartment) {
        JQuickRow row = new JQuickRow();
        row.put("a", model);
        row.put("b", salesQuantity);
        row.put("c", complaintQuantity);
        row.put("d", seriousComplaint);
        row.put("e", null);
        row.put("f", null);
        row.put("g", responsibleDepartment);
        return row;
    }
}
