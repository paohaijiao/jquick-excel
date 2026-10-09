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
 * 场景 19：预算与实际差异分析（数据对比差异类，🟢 纯 XML）。
 *
 * <p>业务：按费用科目对比预算与实际，逐行算「差异 = 实际 - 预算」与「差异率 = 差异 / 预算」，
 * 末尾一行合计；结论为「超支」的科目结论单元格字体标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0019_style_budget-variance.xml}：
 * FORMULAS 做逐行相减、相除与合计；STYLE 给合计行高亮并给超支科目结论标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0019BudgetVarianceDemo {

    private static final String XML = "jquick/biz/style/0019_style_budget-variance.xml";

    /** 导出：纯 XML 完成差异、差异率、合计与超支标红。 */
    @Test
    public void exportBudgetVariance() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(subject("人力成本", 800000, 780000, "结余"));
        rows.add(subject("市场推广", 300000, 350000, "超支"));
        rows.add(subject("研发投入", 500000, 480000, "结余"));
        rows.add(subject("差旅费用", 80000, 95000, "超支"));
        rows.add(subject("办公费用", 60000, 55000, "结余"));
        rows.add(subject("招待费用", 40000, 52000, "超支"));
        // 合计占位行：金额、差异、差异率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0019_style_budget-variance.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("slate", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0019BudgetVarianceService service = factory.createApi(Style0019BudgetVarianceService.class);
            service.exportBudgetVariance("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("预算实际差异分析");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首科目差异 = 实际 - 预算 = 780000 - 800000 = -20000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("C2-B2", d2.getCellFormula());
            Assert.assertEquals(-20000.0, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 市场推广差异 = 350000 - 300000 = 50000
            Cell d3 = sheet.getRow(2).getCell(3);
            Assert.assertEquals(50000.0, evaluator.evaluate(d3).getNumberValue(), 0.0001);
            // 合计预算 = SUM(B2:B7) = 1780000
            Cell b8 = sheet.getRow(7).getCell(1);
            Assert.assertEquals("SUM(B2:B7)", b8.getCellFormula());
            Assert.assertEquals(1780000.0, evaluator.evaluate(b8).getNumberValue(), 0.0001);
            // 合计差异 = 合计实际 - 合计预算 = 32000
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("C8-B8", d8.getCellFormula());
            Assert.assertEquals(32000.0, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 超支科目（第3行「市场推广」「超支」）结论单元格标红加粗
            XSSFCellStyle over = (XSSFCellStyle) sheet.getRow(2).getCell(5).getCellStyle();
            Assert.assertEquals("超支结论应标红", (int) IndexedColors.RED.getIndex(), (int) over.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, over.getFont().getBold());
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景19】预算实际差异分析导出: " + out.getAbsolutePath()
                    + "，合计差异 " + evaluator.evaluate(d8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的预算与实际差异分析（先重算公式）。 */
    @Test
    public void importBudgetVariance() throws Exception {
        File src = BizKit.outFile("style", "0019_style_budget-variance.xlsx");
        if (!src.exists()) {
            exportBudgetVariance();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0019_style_budget-variance-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0019BudgetVarianceService service = factory.createApi(Style0019BudgetVarianceService.class);
            List<JQuickRow> rows = service.importBudgetVariance("field", "value");

            System.out.println("【场景19】预算实际差异分析导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    科目=" + r.get("subject") + ", 预算=" + r.get("budget")
                        + ", 实际=" + r.get("actual") + ", 差异=" + r.get("variance")
                        + ", 结论=" + r.get("conclusion"));
            }
            // 6 个科目 + 1 行合计
            Assert.assertEquals("6 科目 + 1 合计", 7, rows.size());
        }
    }

    /** 构造一条科目差异明细（a=科目，b=预算，c=实际，f=结论）。 */
    private static JQuickRow subject(String name, double budget, double actual, String conclusion) {
        JQuickRow row = new JQuickRow();
        row.put("a", name);
        row.put("b", budget);
        row.put("c", actual);
        row.put("d", null);
        row.put("e", null);
        row.put("f", conclusion);
        return row;
    }
}
