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
 * 场景 87：生产排产计划表（计划排期类，🟢 纯 XML）。
 *
 * <p>业务：逐产线登记计划产量与日产能，算「需求天数 = 计划产量 / 日产能」
 * 「计划投入工时 = 需求天数 × 8」，末尾一行汇总计划产量与投入工时；排期最长的产品标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0087_style_production-scheduling-plan.xml}：
 * FORMULAS 求逐行需求天数 / 投入工时、SUM 求合计，STYLE 给合计行高亮、给最长排期标红；
 * Java 只构造排产数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0087ProductionSchedulingPlanDemo {

    private static final String XML = "jquick/biz/style/0087_style_production-scheduling-plan.xml";

    /** 导出：纯 XML 完成逐行需求天数、投入工时、合计与最长排期标红。 */
    @Test
    public void exportProductionSchedulingPlan() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e/f 留空，由 FORMULAS 计算
        rows.add(task("一号线", "A产品", 1200, 200, "已排产"));
        rows.add(task("一号线", "B产品", 900, 150, "已排产"));
        rows.add(task("二号线", "C产品", 800, 100, "待排产"));
        rows.add(task("二号线", "D产品", 600, 120, "已排产"));
        rows.add(task("三号线", "E产品", 1000, 250, "已排产"));
        // 合计占位行：计划产量与投入工时合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0087_style_production-scheduling-plan.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mintFresh", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0087ProductionSchedulingPlanService service = factory.createApi(Style0087ProductionSchedulingPlanService.class);
            service.exportProductionSchedulingPlan("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("生产排产计划表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行需求天数 = 计划产量 / 日产能 = 1200 / 200
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2/D2", e2.getCellFormula());
            Assert.assertEquals(6.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 首行投入工时 = 需求天数 × 8 = 6 × 8
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("E2*8", f2.getCellFormula());
            Assert.assertEquals(48.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 排期最长的「C产品」（Excel 第 4 行）需求天数 = 800 / 100
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals("C4/D4", e4.getCellFormula());
            Assert.assertEquals(8.0, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 合计：计划产量 = SUM(C2:C6) = 4500
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(4500.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计：投入工时 = SUM(F2:F6) = 232
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("SUM(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(232.0, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 最长排期标红加粗
            XSSFCellStyle longStyle = (XSSFCellStyle) e4.getCellStyle();
            Assert.assertTrue("最长排期应加粗", longStyle.getFont().getBold());
            Assert.assertEquals("最长排期应标红", (int) IndexedColors.RED.getIndex(), (int) longStyle.getFont().getColor());

            System.out.println("【场景87】生产排产计划表导出: " + out.getAbsolutePath()
                    + "，合计投入工时 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的生产排产计划表（先重算公式）。 */
    @Test
    public void importProductionSchedulingPlan() throws Exception {
        File src = BizKit.outFile("style", "0087_style_production-scheduling-plan.xlsx");
        if (!src.exists()) {
            exportProductionSchedulingPlan();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0087_style_production-scheduling-plan-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0087ProductionSchedulingPlanService service = factory.createApi(Style0087ProductionSchedulingPlanService.class);
            List<JQuickRow> rows = service.importProductionSchedulingPlan("field", "value");

            System.out.println("【场景87】生产排产计划表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    产线=" + r.get("productionLine") + ", 产品名称=" + r.get("productName")
                        + ", 计划产量=" + r.get("plannedOutput") + ", 日产能=" + r.get("dailyCapacity")
                        + ", 需求天数=" + r.get("requiredDays") + ", 计划投入工时=" + r.get("plannedHours")
                        + ", 排产状态=" + r.get("scheduleStatus"));
            }
            // 表头不计入数据行：5 个排产任务 + 1 行合计
            Assert.assertEquals("5 个排产任务 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条排产任务（a=产线，b=产品，c=计划产量，d=日产能，e/f 留空，g=排产状态）。 */
    private static JQuickRow task(String productionLine, String productName, double plannedOutput,
                                  double dailyCapacity, String scheduleStatus) {
        JQuickRow row = new JQuickRow();
        row.put("a", productionLine);
        row.put("b", productName);
        row.put("c", plannedOutput);
        row.put("d", dailyCapacity);
        row.put("e", null);
        row.put("f", null);
        row.put("g", scheduleStatus);
        return row;
    }
}
