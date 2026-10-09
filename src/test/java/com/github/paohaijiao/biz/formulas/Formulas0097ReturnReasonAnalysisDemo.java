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
 * 场景 97：客户退货原因分析表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>业务：逐退货原因登记退货数量与金额，算「数量占比 = 该项退货数量 / 退货数量合计」，
 * 末尾一行汇总退货数量、金额与占比；占比最高的原因标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0097_formulas_return-reason-analysis.xml}：
 * FORMULAS 求逐行数量占比、SUM 求各列合计，占比公式直接引用合计单元格，
 * STYLE 给合计行高亮、给占比最高的原因标红；Java 只构造数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0097ReturnReasonAnalysisDemo {

    private static final String XML = "jquick/biz/formulas/0097_formulas_return-reason-analysis.xml";

    /** 导出：纯 XML 完成逐行数量占比、各列汇总与占比最高原因标红。 */
    @Test
    public void exportReturnReasonAnalysis() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // d 留空，由 FORMULAS 计算
        rows.add(reason("质量问题", 80, 24000.00, "生产部"));
        rows.add(reason("包装破损", 45, 6750.00, "物流部"));
        rows.add(reason("尺寸不符", 120, 36000.00, "研发部"));
        rows.add(reason("交期延误", 30, 4500.00, "计划部"));
        rows.add(reason("客户误购", 25, 2500.00, "销售部"));
        // 合计占位行：数量 / 金额 / 占比汇总留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0097_formulas_return-reason-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("emerald", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0097ReturnReasonAnalysisService service = factory.createApi(Formulas0097ReturnReasonAnalysisService.class);
            service.exportReturnReasonAnalysis("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("客户退货原因分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行数量占比 = 该项退货数量 / 退货数量合计 = 80 / 300
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("B2/B7", d2.getCellFormula());
            Assert.assertEquals(80.00 / 300.00, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 占比最高的「尺寸不符」（Excel 第 4 行）= 120 / 300
            Cell d4 = sheet.getRow(3).getCell(3);
            Assert.assertEquals("B4/B7", d4.getCellFormula());
            Assert.assertEquals(0.40, evaluator.evaluate(d4).getNumberValue(), 0.0001);
            // 合计：退货数量 = SUM(B2:B6) = 300
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(300.00, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 合计：退货金额 = SUM(C2:C6) = 73750
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(73750.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 占比最高原因标红加粗
            XSSFCellStyle ratioStyle = (XSSFCellStyle) d4.getCellStyle();
            Assert.assertTrue("最高占比应加粗", ratioStyle.getFont().getBold());
            Assert.assertEquals("最高占比应标红", (int) IndexedColors.RED.getIndex(), (int) ratioStyle.getFont().getColor());

            System.out.println("【场景97】客户退货原因分析表导出: " + out.getAbsolutePath()
                    + "，退货数量合计 " + evaluator.evaluate(b7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的客户退货原因分析表（先重算公式）。 */
    @Test
    public void importReturnReasonAnalysis() throws Exception {
        File src = BizKit.outFile("formulas", "0097_formulas_return-reason-analysis.xlsx");
        if (!src.exists()) {
            exportReturnReasonAnalysis();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0097_formulas_return-reason-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0097ReturnReasonAnalysisService service = factory.createApi(Formulas0097ReturnReasonAnalysisService.class);
            List<JQuickRow> rows = service.importReturnReasonAnalysis("field", "value");

            System.out.println("【场景97】客户退货原因分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    退货原因=" + r.get("returnReason") + ", 退货数量=" + r.get("returnQuantity")
                        + ", 退货金额=" + r.get("returnAmount") + ", 数量占比=" + r.get("quantityRatio")
                        + ", 责任部门=" + r.get("responsibleDepartment"));
            }
            // 表头不计入数据行：5 类原因 + 1 行合计
            Assert.assertEquals("5 类原因 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一类退货原因（a=原因，b=数量，c=金额，d 留空，e=责任部门）。 */
    private static JQuickRow reason(String returnReason, double returnQuantity, double returnAmount,
                                    String responsibleDepartment) {
        JQuickRow row = new JQuickRow();
        row.put("a", returnReason);
        row.put("b", returnQuantity);
        row.put("c", returnAmount);
        row.put("d", null);
        row.put("e", responsibleDepartment);
        return row;
    }
}
