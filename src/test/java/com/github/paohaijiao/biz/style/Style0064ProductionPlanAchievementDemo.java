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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景 64：生产计划达成率表（计划排期类，🟢 纯 XML）。
 *
 * <p>业务：按生产工单登记计划数量与实际产量，逐行算「达成率 = 实际产量 / 计划数量」，
 * 末尾一行给出平均达成率；未达成工单的达成率标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0064_style_production-plan-achievement.xml}：
 * FORMULAS 求逐行达成率与平均达成率，STYLE 给合计行高亮、给未达成工单标红；Java 只构造排期数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0064ProductionPlanAchievementDemo {

    private static final String XML = "jquick/biz/style/0064_style_production-plan-achievement.xml";

    /** 导出：纯 XML 完成达成率计算、平均达成率与未达成标红。 */
    @Test
    public void exportProductionPlanAchievement() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e=达成率 留空，由 FORMULAS 计算
        rows.add(plan("WO2601", "智能手环", 5000, 5200, date(2026, 3, 10), date(2026, 3, 9), "达成"));
        rows.add(plan("WO2602", "无线耳机", 4000, 3800, date(2026, 3, 12), date(2026, 3, 13), "未达成"));
        rows.add(plan("WO2603", "扫地机器人", 800, 840, date(2026, 3, 15), date(2026, 3, 15), "达成"));
        rows.add(plan("WO2604", "空气炸锅", 6000, 6300, date(2026, 3, 18), date(2026, 3, 17), "达成"));
        rows.add(plan("WO2605", "破壁机", 2000, 1900, date(2026, 3, 20), date(2026, 3, 22), "未达成"));
        // 合计占位行：数量合计与平均达成率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        total.put("h", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0064_style_production-plan-achievement.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("midnightDark", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0064ProductionPlanAchievementService service = factory.createApi(Style0064ProductionPlanAchievementService.class);
            service.exportProductionPlanAchievement("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("生产计划达成率表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行达成率 = 实际 / 计划 = 5200 / 5000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/C2", e2.getCellFormula());
            Assert.assertEquals(1.04, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 未达成工单（第 2 条明细，Excel 第 3 行）达成率 = 3800 / 4000
            Cell e3 = sheet.getRow(2).getCell(4);
            Assert.assertEquals(0.95, evaluator.evaluate(e3).getNumberValue(), 0.0001);
            // 计划 / 实际 合计
            Cell c7 = sheet.getRow(6).getCell(2);
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals(17800.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            Assert.assertEquals(18040.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 平均达成率 = AVERAGE(E2:E6) = (1.04 + 0.95 + 1.05 + 1.05 + 0.95) / 5
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("AVERAGE(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(1.008, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 两条未达成工单达成率标红加粗（Excel 第 3、6 行）
            XSSFCellStyle first = (XSSFCellStyle) e3.getCellStyle();
            XSSFCellStyle second = (XSSFCellStyle) sheet.getRow(5).getCell(4).getCellStyle();
            Assert.assertTrue("未达成达成率应加粗", first.getFont().getBold());
            Assert.assertEquals("未达成达成率应标红", (int) IndexedColors.RED.getIndex(), (int) first.getFont().getColor());
            Assert.assertEquals("未达成达成率应标红", (int) IndexedColors.RED.getIndex(), (int) second.getFont().getColor());

            System.out.println("【场景64】生产计划达成率表导出: " + out.getAbsolutePath()
                    + "，平均达成率 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的计划达成率表（先重算公式）。 */
    @Test
    public void importProductionPlanAchievement() throws Exception {
        File src = BizKit.outFile("style", "0064_style_production-plan-achievement.xlsx");
        if (!src.exists()) {
            exportProductionPlanAchievement();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0064_style_production-plan-achievement-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0064ProductionPlanAchievementService service = factory.createApi(Style0064ProductionPlanAchievementService.class);
            List<JQuickRow> rows = service.importProductionPlanAchievement("field", "value");

            System.out.println("【场景64】生产计划达成率表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工单号=" + r.get("orderNo") + ", 产品=" + r.get("productName")
                        + ", 计划=" + r.get("planQty") + ", 实际=" + r.get("actualQty")
                        + ", 达成率=" + r.get("achievementRate") + ", 状态=" + r.get("status"));
            }
            // 表头不计入数据行：5 条明细 + 1 行合计
            Assert.assertEquals("5 条明细 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条工单排期（a=工单号，b=产品，c=计划数量，d=实际产量，e=达成率，f=计划完成日，g=实际完成日，h=状态）。 */
    private static JQuickRow plan(String orderNo, String productName, int planQty, int actualQty,
                                  Date planDate, Date actualDate, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", orderNo);
        row.put("b", productName);
        row.put("c", planQty);
        row.put("d", actualQty);
        row.put("e", null);
        row.put("f", planDate);
        row.put("g", actualDate);
        row.put("h", status);
        return row;
    }

    /** 构造日期（月份按自然月 1-12 传入）。 */
    private static Date date(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
