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
 * 场景 24：增值税税负分析表（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：按季度分析增值税税负，逐行算「应纳增值税 = 销项税额 - 进项税额」与「税负率 = 应纳增值税 / 销售收入」，
 * 末尾一行汇总；税负最高的季度税负率单元格标红，提示重点关注。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0024_style_vat-burden-analysis.xml}：
 * FORMULAS 做逐行减法与除法、合计行汇总，STYLE 给合计行高亮并给高税负单元格标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0024VatBurdenAnalysisDemo {

    private static final String XML = "jquick/biz/style/0024_style_vat-burden-analysis.xml";

    /** 导出：纯 XML 完成应纳增值税、税负率、合计行汇总与高税负标红。 */
    @Test
    public void exportVatBurden() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(quarter("2026Q1", 2000000, 260000, 180000));
        rows.add(quarter("2026Q2", 2000000, 260000, 200000));
        // 第 4 行：2026Q3 税负最高（4.25%），模板中 F4 标红
        rows.add(quarter("2026Q3", 2400000, 312000, 210000));
        rows.add(quarter("2026Q4", 2600000, 338000, 240000));
        // 合计占位行：应纳增值税、税负率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0024_style_vat-burden-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("oliveGreen", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0024VatBurdenAnalysisService service = factory.createApi(Style0024VatBurdenAnalysisService.class);
            service.exportVatBurden("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("增值税税负分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // Q1 应纳增值税 = 销项 - 进项 = 260000 - 180000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(80000.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // Q1 税负率 = 应纳 / 销售收入 = 80000 / 2000000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("E2/B2", f2.getCellFormula());
            Assert.assertEquals(0.04, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 合计：销售收入 SUM(B2:B5) = 9000000
            Cell b6 = sheet.getRow(5).getCell(1);
            Assert.assertEquals("SUM(B2:B5)", b6.getCellFormula());
            Assert.assertEquals(9000000.0, evaluator.evaluate(b6).getNumberValue(), 0.0001);
            // 合计：应纳增值税 = 销项合计 - 进项合计 = 1170000 - 830000
            Cell e6 = sheet.getRow(5).getCell(4);
            Assert.assertEquals("C6-D6", e6.getCellFormula());
            Assert.assertEquals(340000.0, evaluator.evaluate(e6).getNumberValue(), 0.0001);
            // 高税负季度（第4行 2026Q3）税负率字体标红
            XSSFCellStyle high = (XSSFCellStyle) sheet.getRow(3).getCell(5).getCellStyle();
            Assert.assertEquals("高税负应标红", (int) IndexedColors.RED.getIndex(), (int) high.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, high.getFont().getBold());
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景24】增值税税负分析表导出: " + out.getAbsolutePath()
                    + "，合计应纳增值税 " + evaluator.evaluate(e6).getNumberValue());
        }
    }

    /** 导入解析：读取导出的增值税税负分析表（先重算公式）。 */
    @Test
    public void importVatBurden() throws Exception {
        File src = BizKit.outFile("style", "0024_style_vat-burden-analysis.xlsx");
        if (!src.exists()) {
            exportVatBurden();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0024_style_vat-burden-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0024VatBurdenAnalysisService service = factory.createApi(Style0024VatBurdenAnalysisService.class);
            List<JQuickRow> rows = service.importVatBurden("field", "value");

            System.out.println("【场景24】增值税税负分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    期间=" + r.get("period") + ", 销售收入=" + r.get("salesRevenue")
                        + ", 应纳增值税=" + r.get("payableTax") + ", 税负率=" + r.get("taxBurdenRate"));
            }
            // 4 个季度 + 1 行合计
            Assert.assertEquals("4 季度 + 1 合计", 5, rows.size());
        }
    }

    /** 构造一条季度税负明细（a=期间，b=销售收入，c=销项税额，d=进项税额）。 */
    private static JQuickRow quarter(String period, double revenue, double outputTax, double inputTax) {
        JQuickRow row = new JQuickRow();
        row.put("a", period);
        row.put("b", revenue);
        row.put("c", outputTax);
        row.put("d", inputTax);
        row.put("e", null);
        row.put("f", null);
        return row;
    }
}
