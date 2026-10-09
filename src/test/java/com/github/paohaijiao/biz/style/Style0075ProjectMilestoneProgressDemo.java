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
 * 场景 75：研发项目里程碑进度表（计划排期类，🟢 纯 XML）。
 *
 * <p>业务：逐行登记里程碑的计划工时与实际工时，算「偏差工时 = 计划 - 实际」与
 * 「工时达成率 = 计划 / 实际」，末尾一行汇总总计划工时、总实际工时、总偏差与整体达成率；
 * 达成率最低的里程碑标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0075_style_project-milestone-progress.xml}：
 * FORMULAS 求逐行偏差与达成率、SUM 求合计，STYLE 给合计行高亮、给最低达成率标红；
 * Java 只构造进度数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0075ProjectMilestoneProgressDemo {

    private static final String XML = "jquick/biz/style/0075_style_project-milestone-progress.xml";

    /** 导出：纯 XML 完成偏差工时、达成率、合计与最低达成率标红。 */
    @Test
    public void exportProjectMilestoneProgress() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e/f 留空，由 FORMULAS 计算
        rows.add(milestone("需求分析", "张伟", 120.00, 110.00, "已完成"));
        rows.add(milestone("系统设计", "李娜", 200.00, 230.00, "已完成"));
        rows.add(milestone("编码开发", "王强", 800.00, 850.00, "进行中"));
        rows.add(milestone("测试验证", "赵敏", 300.00, 280.00, "进行中"));
        rows.add(milestone("上线部署", "陈晨", 80.00, 90.00, "未开始"));
        // 合计占位行：各列合计与整体达成率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0075_style_project-milestone-progress.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("jade", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0075ProjectMilestoneProgressService service = factory.createApi(Style0075ProjectMilestoneProgressService.class);
            service.exportProjectMilestoneProgress("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("研发项目里程碑进度表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行偏差工时 = 计划 - 实际 = 120 - 110
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(10.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 首行达成率 = 计划 / 实际 = 120 / 110
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2/D2", f2.getCellFormula());
            Assert.assertEquals(120.00 / 110.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 合计：总计划工时 = SUM(C2:C6) = 1500
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(1500.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计：总实际工时 = SUM(D2:D6) = 1560
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals(1560.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计：总偏差 = 计划合计 - 实际合计 = 1500 - 1560
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("C7-D7", e7.getCellFormula());
            Assert.assertEquals(-60.00, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 达成率最低的「系统设计」（Excel 第 3 行）达成率标红加粗
            XSSFCellStyle lowStyle = (XSSFCellStyle) sheet.getRow(2).getCell(5).getCellStyle();
            Assert.assertTrue("最低达成率应加粗", lowStyle.getFont().getBold());
            Assert.assertEquals("最低达成率应标红", (int) IndexedColors.RED.getIndex(), (int) lowStyle.getFont().getColor());

            System.out.println("【场景75】研发项目里程碑进度表导出: " + out.getAbsolutePath()
                    + "，总偏差工时 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的研发项目里程碑进度表（先重算公式）。 */
    @Test
    public void importProjectMilestoneProgress() throws Exception {
        File src = BizKit.outFile("style", "0075_style_project-milestone-progress.xlsx");
        if (!src.exists()) {
            exportProjectMilestoneProgress();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0075_style_project-milestone-progress-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0075ProjectMilestoneProgressService service = factory.createApi(Style0075ProjectMilestoneProgressService.class);
            List<JQuickRow> rows = service.importProjectMilestoneProgress("field", "value");

            System.out.println("【场景75】研发项目里程碑进度表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    里程碑=" + r.get("milestone") + ", 负责人=" + r.get("owner")
                        + ", 计划工时=" + r.get("plannedHours") + ", 实际工时=" + r.get("actualHours")
                        + ", 偏差工时=" + r.get("deviationHours") + ", 达成率=" + r.get("achievementRate")
                        + ", 状态=" + r.get("status"));
            }
            // 表头不计入数据行：5 个里程碑 + 1 行合计
            Assert.assertEquals("5 个里程碑 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一个里程碑（a=阶段，b=负责人，c=计划工时，d=实际工时，e/f 留空，g=状态）。 */
    private static JQuickRow milestone(String stage, String owner, double plannedHours,
                                       double actualHours, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", stage);
        row.put("b", owner);
        row.put("c", plannedHours);
        row.put("d", actualHours);
        row.put("e", null);
        row.put("f", null);
        row.put("g", status);
        return row;
    }
}
