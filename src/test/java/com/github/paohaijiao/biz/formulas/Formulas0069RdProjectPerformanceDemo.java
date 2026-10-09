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
 * 场景 69：研发项目绩效考核评分表（绩效考核类，🟢 纯 XML）。
 *
 * <p>业务：按研发项目登记「进度达成 / 质量 / 成本控制」三项得分，逐行算加权综合得分
 * （进度 × 0.4 + 质量 × 0.35 + 成本 × 0.25），末尾一行给出平均综合得分；低分项目（综合得分最低）标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0069_formulas_rd-project-performance.xml}：
 * FORMULAS 求加权综合得分与平均分，STYLE 给均分行高亮、给低分项目标红；Java 只构造评分数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0069RdProjectPerformanceDemo {

    private static final String XML = "jquick/biz/formulas/0069_formulas_rd-project-performance.xml";

    /** 导出：纯 XML 完成加权综合得分、平均分与低分标红。 */
    @Test
    public void exportRdProjectPerformance() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f=综合得分 留空，由 FORMULAS 计算
        rows.add(project("智能仓储系统", "张伟", 95.0, 90.0, 85.0, "优秀"));
        rows.add(project("产线改造项目", "李娜", 88.0, 92.0, 80.0, "良好"));
        rows.add(project("数据中台建设", "王强", 70.0, 75.0, 68.0, "待改进"));
        rows.add(project("移动端重构", "赵敏", 85.0, 88.0, 90.0, "良好"));
        rows.add(project("客户关系系统", "陈杰", 92.0, 85.0, 82.0, "良好"));
        // 均分占位行：平均综合得分留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "平均分");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0069_formulas_rd-project-performance.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("espresso", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0069RdProjectPerformanceService service = factory.createApi(Formulas0069RdProjectPerformanceService.class);
            service.exportRdProjectPerformance("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("研发项目绩效考核评分表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行综合得分 = 95*0.4 + 90*0.35 + 85*0.25
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2*0.4+D2*0.35+E2*0.25", f2.getCellFormula());
            Assert.assertEquals(90.75, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 低分项目（第 3 个项目，Excel 第 4 行）= 70*0.4 + 75*0.35 + 68*0.25
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals(71.25, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 平均综合得分 = AVERAGE(F2:F6)
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("AVERAGE(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(84.75, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 均分行加粗高亮
            Assert.assertTrue("均分行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 低分项目综合得分标红加粗
            XSSFCellStyle lowStyle = (XSSFCellStyle) f4.getCellStyle();
            Assert.assertTrue("低分项目综合得分应加粗", lowStyle.getFont().getBold());
            Assert.assertEquals("低分项目综合得分应标红", (int) IndexedColors.RED.getIndex(), (int) lowStyle.getFont().getColor());

            System.out.println("【场景69】研发项目绩效考核评分表导出: " + out.getAbsolutePath()
                    + "，平均综合得分 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的绩效考核评分表（先重算公式）。 */
    @Test
    public void importRdProjectPerformance() throws Exception {
        File src = BizKit.outFile("formulas", "0069_formulas_rd-project-performance.xlsx");
        if (!src.exists()) {
            exportRdProjectPerformance();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0069_formulas_rd-project-performance-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0069RdProjectPerformanceService service = factory.createApi(Formulas0069RdProjectPerformanceService.class);
            List<JQuickRow> rows = service.importRdProjectPerformance("field", "value");

            System.out.println("【场景69】研发项目绩效考核评分表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    项目=" + r.get("projectName") + ", 负责人=" + r.get("owner")
                        + ", 进度=" + r.get("scheduleScore") + ", 质量=" + r.get("qualityScore")
                        + ", 成本=" + r.get("costScore") + ", 综合=" + r.get("totalScore")
                        + ", 评级=" + r.get("rating"));
            }
            // 表头不计入数据行：5 个项目 + 1 行均分
            Assert.assertEquals("5 个项目 + 1 均分行", 6, rows.size());
        }
    }

    /** 构造一条项目评分（a=项目名称，b=负责人，c=进度达成，d=质量得分，e=成本控制得分，f=综合得分，g=评级）。 */
    private static JQuickRow project(String projectName, String owner, double scheduleScore,
                                     double qualityScore, double costScore, String rating) {
        JQuickRow row = new JQuickRow();
        row.put("a", projectName);
        row.put("b", owner);
        row.put("c", scheduleScore);
        row.put("d", qualityScore);
        row.put("e", costScore);
        row.put("f", null);
        row.put("g", rating);
        return row;
    }
}
