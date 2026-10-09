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
 * 场景 81：部门季度绩效考核汇总表（绩效考核类，🟢 纯 XML）。
 *
 * <p>业务：逐部门登记目标完成分 / 工作质量分 / 团队协作分，按权重算
 * 「综合得分 = 完成分×0.5 + 质量分×0.3 + 协作分×0.2」，末尾一行给出各项平均分与平均综合得分；
 * 综合得分最低的部门标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0081_formulas_department-performance-review.xml}：
 * FORMULAS 用三项加权算式求综合得分、AVERAGE 求各列平均分，STYLE 给合计行高亮、给最低综合得分标红；
 * Java 只构造考核数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0081DepartmentPerformanceReviewDemo {

    private static final String XML = "jquick/biz/formulas/0081_formulas_department-performance-review.xml";

    /** 导出：纯 XML 完成加权综合得分、平均分与最低分标红。 */
    @Test
    public void exportDepartmentPerformanceReview() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f 留空，由 FORMULAS 计算
        rows.add(dept("研发部", "张伟", 95.00, 90.00, 88.00, "优秀"));
        rows.add(dept("市场部", "李娜", 88.00, 85.00, 92.00, "良好"));
        rows.add(dept("生产部", "王强", 82.00, 78.00, 80.00, "合格"));
        rows.add(dept("财务部", "赵敏", 90.00, 88.00, 85.00, "良好"));
        rows.add(dept("客服部", "陈晨", 85.00, 80.00, 78.00, "合格"));
        // 平均占位行：各项平均分留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "平均分");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0081_formulas_department-performance-review.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("indigo", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0081DepartmentPerformanceReviewService service = factory.createApi(Formulas0081DepartmentPerformanceReviewService.class);
            service.exportDepartmentPerformanceReview("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("部门季度绩效考核汇总表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 研发部综合得分 = 95×0.5 + 90×0.3 + 88×0.2 = 92.1
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2*0.5+D2*0.3+E2*0.2", f2.getCellFormula());
            Assert.assertEquals(92.10, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 综合得分最低的「生产部」（Excel 第 4 行）= 82×0.5 + 78×0.3 + 80×0.2 = 80.4
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals(80.40, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 平均：目标完成分 = AVERAGE(C2:C6) = 88
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("AVERAGE(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(88.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 平均：综合得分 = AVERAGE(F2:F6) = 86.18
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("AVERAGE(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(86.18, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 平均行加粗高亮
            Assert.assertTrue("平均行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 最低综合得分标红加粗
            XSSFCellStyle lowStyle = (XSSFCellStyle) f4.getCellStyle();
            Assert.assertTrue("最低综合得分应加粗", lowStyle.getFont().getBold());
            Assert.assertEquals("最低综合得分应标红", (int) IndexedColors.RED.getIndex(), (int) lowStyle.getFont().getColor());

            System.out.println("【场景81】部门季度绩效考核汇总表导出: " + out.getAbsolutePath()
                    + "，平均综合得分 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的部门季度绩效考核汇总表（先重算公式）。 */
    @Test
    public void importDepartmentPerformanceReview() throws Exception {
        File src = BizKit.outFile("formulas", "0081_formulas_department-performance-review.xlsx");
        if (!src.exists()) {
            exportDepartmentPerformanceReview();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0081_formulas_department-performance-review-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0081DepartmentPerformanceReviewService service = factory.createApi(Formulas0081DepartmentPerformanceReviewService.class);
            List<JQuickRow> rows = service.importDepartmentPerformanceReview("field", "value");

            System.out.println("【场景81】部门季度绩效考核汇总表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    部门=" + r.get("department") + ", 负责人=" + r.get("reviewer")
                        + ", 目标完成分=" + r.get("goalScore") + ", 工作质量分=" + r.get("qualityScore")
                        + ", 团队协作分=" + r.get("teamScore") + ", 综合得分=" + r.get("totalScore")
                        + ", 考核等级=" + r.get("grade"));
            }
            // 表头不计入数据行：5 个部门 + 1 行平均
            Assert.assertEquals("5 个部门 + 1 平均行", 6, rows.size());
        }
    }

    /** 构造一个部门（a=部门，b=负责人，c/d/e=三项得分，f 留空，g=考核等级）。 */
    private static JQuickRow dept(String department, String reviewer, double goalScore,
                                  double qualityScore, double teamScore, String grade) {
        JQuickRow row = new JQuickRow();
        row.put("a", department);
        row.put("b", reviewer);
        row.put("c", goalScore);
        row.put("d", qualityScore);
        row.put("e", teamScore);
        row.put("f", null);
        row.put("g", grade);
        return row;
    }
}
