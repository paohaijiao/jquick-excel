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
import java.util.List;

/**
 * 场景 37：项目工时统计表（计划排期类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按项目归集成员投入工时，逐行算「工时费 = 工时 × 费率」，每个项目后跟一行工时费小计，
 * 末尾一行给出全部项目合计；项目名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按项目分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行乘法与小计 / 合计由 {@code jquick/biz/merge/0037_merge_project-timesheet.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0037ProjectTimesheetDemo {

    private static final String XML = "jquick/biz/merge/0037_merge_project-timesheet.xml";

    /** 导出：Java 分组 + 插小计行，项目列纵向合并，工时费由 XML FORMULAS 完成。 */
    @Test
    public void exportProjectTimesheet() throws Exception {
        // 1. 构造扁平工时明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("ERP 系统升级", "张伟", "架构师", 120, 180));
        flat.add(line("ERP 系统升级", "李娜", "开发工程师", 160, 120));
        flat.add(line("ERP 系统升级", "王强", "测试工程师", 80, 100));
        flat.add(line("移动端改版", "赵敏", "开发工程师", 140, 120));
        flat.add(line("移动端改版", "陈刚", "UI 设计", 60, 130));

        // 2. Java 分组并插入小计 / 合计占位行（工时费留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0037_merge_project-timesheet.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mahogany", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0037ProjectTimesheetService service = factory.createApi(Merge0037ProjectTimesheetService.class);
            service.exportProjectTimesheet("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("项目工时统计表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行工时费 = 工时 × 费率 = 120 × 180
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(21600.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // ERP 项目小计 = SUM(F2:F4) = 21600 + 19200 + 8000
            Cell f5 = sheet.getRow(4).getCell(5);
            Assert.assertEquals("SUM(F2:F4)", f5.getCellFormula());
            Assert.assertEquals(48800.0, evaluator.evaluate(f5).getNumberValue(), 0.0001);
            // 移动端项目小计 = SUM(F6:F7) = 16800 + 7800
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F6:F7)", f8.getCellFormula());
            Assert.assertEquals(24600.0, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 合计 = 两个小计相加 = 48800 + 24600
            Cell f9 = sheet.getRow(8).getCell(5);
            Assert.assertEquals("F5+F8", f9.getCellFormula());
            Assert.assertEquals(73400.0, evaluator.evaluate(f9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 项目列纵向合并：ERP A2:A4、移动端 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("ERP A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("移动端 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景37】项目工时统计表导出: " + out.getAbsolutePath()
                    + "，工时费合计 " + evaluator.evaluate(f9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的项目工时统计表（先重算公式）。 */
    @Test
    public void importProjectTimesheet() throws Exception {
        File src = BizKit.outFile("merge", "0037_merge_project-timesheet.xlsx");
        if (!src.exists()) {
            exportProjectTimesheet();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0037_merge_project-timesheet-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0037ProjectTimesheetService service = factory.createApi(Merge0037ProjectTimesheetService.class);
            List<JQuickRow> rows = service.importProjectTimesheet("field", "value");

            System.out.println("【场景37】项目工时统计表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    项目=" + r.get("project") + ", 成员=" + r.get("member")
                        + ", 角色=" + r.get("role") + ", 工时=" + r.get("hours") + ", 工时费=" + r.get("laborCost"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按项目分组，项目切换处插入小计行，末尾追加合计行（不做金额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String project = String.valueOf(r.get("a"));
            if (current != null && !current.equals(project)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = project;
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
        out.add(total);
        return out;
    }

    /** 小计占位行：只写项目与小计文字，工时费留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String project) {
        JQuickRow row = new JQuickRow();
        row.put("a", project);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条工时明细（a=项目，b=成员，c=角色，d=工时，e=费率，f=工时费留空）。 */
    private static JQuickRow line(String project, String member, String role, double hours, double rate) {
        JQuickRow row = new JQuickRow();
        row.put("a", project);
        row.put("b", member);
        row.put("c", role);
        row.put("d", hours);
        row.put("e", rate);
        row.put("f", null);
        return row;
    }
}
