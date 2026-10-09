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
 * 场景 17：员工绩效考核评分表（绩效考核类，🟢 纯 XML）。
 *
 * <p>业务：按员工录入业绩分 / 能力分 / 态度分，按权重算综合得分，末尾给出各维度平均分；
 * 综合得分偏低、等级为「待改进」的员工，其等级单元格字体标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0017_formulas_performance-appraisal.xml}：
 * FORMULAS 做逐行加权（业绩*0.5 + 能力*0.3 + 态度*0.2）与平均行；STYLE 给平均行高亮并给待改进等级标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0017PerformanceAppraisalDemo {

    private static final String XML = "jquick/biz/formulas/0017_formulas_performance-appraisal.xml";

    /** 导出：纯 XML 完成加权得分、平均分与待改进标红。 */
    @Test
    public void exportPerformanceAppraisal() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(employee("E1001", "张伟", "研发部", 92, 88, 90, "优秀"));
        rows.add(employee("E1002", "李娜", "市场部", 85, 90, 88, "良好"));
        rows.add(employee("E1003", "王强", "财务部", 78, 82, 85, "良好"));
        rows.add(employee("E1004", "赵敏", "研发部", 95, 93, 92, "优秀"));
        // 第 6 行：综合得分 65.6，等级「待改进」，模板中 H6 标红
        rows.add(employee("E1005", "陈刚", "生产部", 62, 70, 68, "待改进"));
        // 平均行占位：得分留给 FORMULAS
        JQuickRow avg = new JQuickRow();
        avg.put("a", "平均");
        avg.put("b", null);
        avg.put("c", null);
        avg.put("d", null);
        avg.put("e", null);
        avg.put("f", null);
        avg.put("g", null);
        avg.put("h", null);
        rows.add(avg);

        File out = BizKit.outFile("formulas", "0017_formulas_performance-appraisal.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("periwinkle", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0017PerformanceAppraisalService service = factory.createApi(Formulas0017PerformanceAppraisalService.class);
            service.exportPerformanceAppraisal("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工绩效考核评分表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首员工综合得分 = 92*0.5 + 88*0.3 + 90*0.2 = 90.4
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("D2*0.5+E2*0.3+F2*0.2", g2.getCellFormula());
            Assert.assertEquals(90.4, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 业绩平均分 = AVERAGE(D2:D6) = 82.4
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("AVERAGE(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(82.4, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 综合得分平均 = AVERAGE(G2:G6) = 83.5
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("AVERAGE(G2:G6)", g7.getCellFormula());
            Assert.assertEquals(83.5, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 待改进员工（第6行）等级单元格标红加粗
            XSSFCellStyle grade = (XSSFCellStyle) sheet.getRow(5).getCell(7).getCellStyle();
            Assert.assertEquals("待改进等级应标红", (int) IndexedColors.RED.getIndex(), (int) grade.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, grade.getFont().getBold());
            // 平均行加粗高亮
            Assert.assertTrue("平均行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景17】员工绩效考核评分表导出: " + out.getAbsolutePath()
                    + "，综合平均 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的员工绩效考核评分表（先重算公式）。 */
    @Test
    public void importPerformanceAppraisal() throws Exception {
        File src = BizKit.outFile("formulas", "0017_formulas_performance-appraisal.xlsx");
        if (!src.exists()) {
            exportPerformanceAppraisal();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0017_formulas_performance-appraisal-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0017PerformanceAppraisalService service = factory.createApi(Formulas0017PerformanceAppraisalService.class);
            List<JQuickRow> rows = service.importPerformanceAppraisal("field", "value");

            System.out.println("【场景17】员工绩效考核评分表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("code") + ", 姓名=" + r.get("name")
                        + ", 综合得分=" + r.get("total") + ", 等级=" + r.get("grade"));
            }
            // 5 名员工 + 1 行平均
            Assert.assertEquals("5 员工 + 1 平均", 6, rows.size());
        }
    }

    /** 构造一条员工考核明细（a=工号，b=姓名，c=部门，d/e/f=三项得分，h=等级）。 */
    private static JQuickRow employee(String code, String name, String dept,
                                      double performance, double ability, double attitude, String grade) {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", dept);
        row.put("d", performance);
        row.put("e", ability);
        row.put("f", attitude);
        row.put("g", null);
        row.put("h", grade);
        return row;
    }
}
