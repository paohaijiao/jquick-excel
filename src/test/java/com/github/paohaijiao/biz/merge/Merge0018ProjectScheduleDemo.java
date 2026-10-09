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
package com.github.paohaijiao.biz.merge;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
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
 * 场景 18：项目进度计划排期表（计划排期类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：项目按阶段排期，逐行算「工期 = 结束日期 - 开始日期」天，每阶段后跟一行工期小计，
 * 末尾一行给出总工期与整体平均完成率；阶段名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按阶段分组、插入小计 / 合计占位行、确定合并范围」，
 * 工期与完成率统计由 {@code jquick/biz/merge/0018_merge_project-schedule.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。小计行完成率留空，合计行以 AVERAGE 忽略空白只统计任务行。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0018ProjectScheduleDemo {

    private static final String XML = "jquick/biz/merge/0018_merge_project-schedule.xml";

    /** 导出：Java 分组 + 插小计行，阶段列纵向合并，工期 / 完成率由 XML FORMULAS 完成。 */
    @Test
    public void exportProjectSchedule() throws Exception {
        // 1. 构造扁平任务明细（Java 不做任何工期计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(task("需求分析", "需求调研", "张伟", date(2026, 3, 1), date(2026, 3, 5), 1.0));
        flat.add(task("需求分析", "需求评审", "李娜", date(2026, 3, 6), date(2026, 3, 8), 1.0));
        flat.add(task("系统设计", "概要设计", "王强", date(2026, 3, 9), date(2026, 3, 13), 1.0));
        flat.add(task("系统设计", "详细设计", "赵敏", date(2026, 3, 14), date(2026, 3, 18), 0.8));
        flat.add(task("开发实现", "后端开发", "陈刚", date(2026, 3, 19), date(2026, 3, 28), 0.5));
        flat.add(task("开发实现", "前端开发", "孙丽", date(2026, 3, 19), date(2026, 3, 26), 0.4));
        flat.add(task("开发实现", "联调测试", "周杰", date(2026, 3, 29), date(2026, 4, 2), 0.1));

        // 2. Java 分组并插入小计 / 合计占位行（工期、完成率留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0018_merge_project-schedule.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("terracotta", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0018ProjectScheduleService service = factory.createApi(Merge0018ProjectScheduleService.class);
            service.exportProjectSchedule("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("项目进度计划排期表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首任务工期 = 结束 - 开始 = 3/5 - 3/1 = 4 天
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("E2-D2", f2.getCellFormula());
            Assert.assertEquals(4.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 需求分析小计工期 = SUM(F2:F3) = 4 + 2 = 6
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals("SUM(F2:F3)", f4.getCellFormula());
            Assert.assertEquals(6.0, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 总工期 = 各阶段小计相加 = 6 + 8 + 20 = 34
            Cell f12 = sheet.getRow(11).getCell(5);
            Assert.assertEquals("F4+F7+F11", f12.getCellFormula());
            Assert.assertEquals(34.0, evaluator.evaluate(f12).getNumberValue(), 0.0001);
            // 整体平均完成率 = AVERAGE(G2:G10)（忽略空白小计行）= 4.8 / 7
            Cell g12 = sheet.getRow(11).getCell(6);
            Assert.assertEquals("AVERAGE(G2:G10)", g12.getCellFormula());
            Assert.assertEquals(4.8 / 7, evaluator.evaluate(g12).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(3).getCell(0).getCellStyle()).getFont().getBold());
            // 阶段列纵向合并：需求分析 A2:A3、系统设计 A5:A6、开发实现 A8:A10
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 3 个合并区块", 3, regions.size());
            Assert.assertTrue("需求分析 A2:A3 应合并", regions.contains(new CellRangeAddress(1, 2, 0, 0)));
            Assert.assertTrue("系统设计 A5:A6 应合并", regions.contains(new CellRangeAddress(4, 5, 0, 0)));
            Assert.assertTrue("开发实现 A8:A10 应合并", regions.contains(new CellRangeAddress(7, 9, 0, 0)));

            System.out.println("【场景18】项目进度计划排期表导出: " + out.getAbsolutePath() + "，合并区块 " + regions);
        }
    }

    /** 导入解析：读取导出的项目进度计划排期表（先重算公式）。 */
    @Test
    public void importProjectSchedule() throws Exception {
        File src = BizKit.outFile("merge", "0018_merge_project-schedule.xlsx");
        if (!src.exists()) {
            exportProjectSchedule();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0018_merge_project-schedule-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0018ProjectScheduleService service = factory.createApi(Merge0018ProjectScheduleService.class);
            List<JQuickRow> rows = service.importProjectSchedule("field", "value");

            System.out.println("【场景18】项目进度计划排期表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    阶段=" + r.get("phase") + ", 任务=" + r.get("task")
                        + ", 工期=" + r.get("duration") + " 天, 完成率=" + r.get("progress"));
            }
            // 7 条任务 + 3 行小计 + 1 行合计
            Assert.assertEquals("7 条任务 + 3 小计 + 1 合计", 11, rows.size());
        }
    }

    /** 按阶段分组，阶段切换处插入小计行，末尾追加合计行（不做工期计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String phase = String.valueOf(r.get("a"));
            if (current != null && !current.equals(phase)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = phase;
        }
        if (current != null) {
            out.add(subtotalRow(current));
        }
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写阶段与小计文字，工期留空由 FORMULAS 计算，完成率留空供合计行平均时忽略。 */
    private static JQuickRow subtotalRow(String phase) {
        JQuickRow row = new JQuickRow();
        row.put("a", phase);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }

    /** 构造一条任务明细（a=阶段，b=任务，c=负责人，d=开始，e=结束，g=完成率）。 */
    private static JQuickRow task(String phase, String name, String owner, Date start, Date end, double progress) {
        JQuickRow row = new JQuickRow();
        row.put("a", phase);
        row.put("b", name);
        row.put("c", owner);
        row.put("d", start);
        row.put("e", end);
        row.put("f", null);
        row.put("g", progress);
        return row;
    }

    /** 构造一个「年-月-日」日期（月份从 1 开始）。 */
    private static Date date(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
