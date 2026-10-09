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
 * 场景 33：员工月度考勤统计表（人事考勤类，🟢 纯 XML）。
 *
 * <p>业务：按月统计员工考勤，逐行算「请假天数 = 应出勤 - 实出勤」与「出勤率 = 实出勤 / 应出勤」，
 * 末尾一行汇总应出勤、实出勤、请假天数、加班时数并给出整体出勤率。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0033_formulas_attendance-monthly-stat.xml}：
 * FORMULAS 做逐行减法与除法、合计行汇总，STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0033AttendanceMonthlyStatDemo {

    private static final String XML = "jquick/biz/formulas/0033_formulas_attendance-monthly-stat.xml";

    /** 导出：纯 XML 完成请假天数、出勤率与合计行汇总。 */
    @Test
    public void exportAttendanceMonthly() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(record("E1001", "张伟", "研发部", 22, 22, 12));
        rows.add(record("E1002", "李娜", "市场部", 22, 21, 6));
        rows.add(record("E1003", "王强", "财务部", 22, 20, 0));
        rows.add(record("E1004", "赵敏", "研发部", 22, 22, 20));
        rows.add(record("E1005", "陈刚", "生产部", 22, 19, 8));
        // 合计占位行：请假天数、出勤率留给 FORMULAS
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

        File out = BizKit.outFile("formulas", "0033_formulas_attendance-monthly-stat.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("vintageSepia", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0033AttendanceMonthlyStatService service = factory.createApi(Formulas0033AttendanceMonthlyStatService.class);
            service.exportAttendanceMonthly("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工月度考勤统计表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行出勤率 = 实出勤 / 应出勤 = 22 / 22
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("E2/D2", g2.getCellFormula());
            Assert.assertEquals(1.0, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 第4行请假天数 = 应出勤 - 实出勤 = 22 - 20
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals("D4-E4", f4.getCellFormula());
            Assert.assertEquals(2.0, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 合计：应出勤 SUM(D2:D6) = 110
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(110.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计：加班时数 SUM(H2:H6) = 46
            Cell h7 = sheet.getRow(6).getCell(7);
            Assert.assertEquals("SUM(H2:H6)", h7.getCellFormula());
            Assert.assertEquals(46.0, evaluator.evaluate(h7).getNumberValue(), 0.0001);
            // 整体出勤率 = 实出勤合计 / 应出勤合计 = 104 / 110
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("E7/D7", g7.getCellFormula());
            Assert.assertEquals(104.0 / 110.0, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景33】员工月度考勤统计表导出: " + out.getAbsolutePath()
                    + "，整体出勤率 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的员工月度考勤统计表（先重算公式）。 */
    @Test
    public void importAttendanceMonthly() throws Exception {
        File src = BizKit.outFile("formulas", "0033_formulas_attendance-monthly-stat.xlsx");
        if (!src.exists()) {
            exportAttendanceMonthly();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0033_formulas_attendance-monthly-stat-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0033AttendanceMonthlyStatService service = factory.createApi(Formulas0033AttendanceMonthlyStatService.class);
            List<JQuickRow> rows = service.importAttendanceMonthly("field", "value");

            System.out.println("【场景33】员工月度考勤统计表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("empNo") + ", 姓名=" + r.get("name")
                        + ", 实出勤=" + r.get("actualDays") + ", 请假=" + r.get("leaveDays") + ", 出勤率=" + r.get("attendanceRate"));
            }
            // 表头不计入数据行：5 名员工 + 1 行合计
            Assert.assertEquals("5 名员工 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条考勤记录（a=工号，b=姓名，c=部门，d=应出勤，e=实出勤，h=加班时数）。 */
    private static JQuickRow record(String empNo, String name, String dept, int shouldDays, int actualDays, int overtime) {
        JQuickRow row = new JQuickRow();
        row.put("a", empNo);
        row.put("b", name);
        row.put("c", dept);
        row.put("d", shouldDays);
        row.put("e", actualDays);
        row.put("f", null);
        row.put("g", null);
        row.put("h", overtime);
        return row;
    }
}
