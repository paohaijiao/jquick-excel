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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景 25：员工档案名册（档案名册类，🟢 纯 XML）。
 *
 * <p>业务：人事维护的员工档案名册，登记工号、姓名、部门、职位、入职日期与薪酬标准，
 * 逐行算「年薪 = 月薪 × 12」，末尾一行汇总月薪与年薪总额。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0025_formulas_employee-profile-roster.xml}：
 * FORMULAS 做逐行乘法与合计行汇总，STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0025EmployeeProfileRosterDemo {

    private static final String XML = "jquick/biz/formulas/0025_formulas_employee-profile-roster.xml";

    /** 导出：纯 XML 完成年薪计算与月薪 / 年薪汇总。 */
    @Test
    public void exportEmployeeProfile() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(employee("E1001", "张伟", "研发部", "高级工程师", date(2020, 3, 15), 32000));
        rows.add(employee("E1002", "李娜", "市场部", "市场专员", date(2021, 7, 1), 18000));
        rows.add(employee("E1003", "王强", "财务部", "会计", date(2019, 5, 20), 15000));
        rows.add(employee("E1004", "赵敏", "研发部", "技术专家", date(2018, 9, 10), 45000));
        rows.add(employee("E1005", "陈刚", "生产部", "车间主管", date(2022, 2, 8), 12000));
        rows.add(employee("E1006", "孙丽", "人力部", "招聘专员", date(2023, 6, 12), 11000));
        // 合计占位行：薪酬留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0025_formulas_employee-profile-roster.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("coral", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0025EmployeeProfileRosterService service = factory.createApi(Formulas0025EmployeeProfileRosterService.class);
            service.exportEmployeeProfile("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工档案名册");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行年薪 = 月薪 × 12 = 32000 × 12
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("F2*12", g2.getCellFormula());
            Assert.assertEquals(384000.0, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计：月薪 SUM(F2:F7) = 133000
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F2:F7)", f8.getCellFormula());
            Assert.assertEquals(133000.0, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 合计：年薪 SUM(G2:G7) = 1596000
            Cell g8 = sheet.getRow(7).getCell(6);
            Assert.assertEquals("SUM(G2:G7)", g8.getCellFormula());
            Assert.assertEquals(1596000.0, evaluator.evaluate(g8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景25】员工档案名册导出: " + out.getAbsolutePath()
                    + "，年薪合计 " + evaluator.evaluate(g8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的员工档案名册（先重算公式）。 */
    @Test
    public void importEmployeeProfile() throws Exception {
        File src = BizKit.outFile("formulas", "0025_formulas_employee-profile-roster.xlsx");
        if (!src.exists()) {
            exportEmployeeProfile();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0025_formulas_employee-profile-roster-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0025EmployeeProfileRosterService service = factory.createApi(Formulas0025EmployeeProfileRosterService.class);
            List<JQuickRow> rows = service.importEmployeeProfile("field", "value");

            System.out.println("【场景25】员工档案名册导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("empNo") + ", 姓名=" + r.get("name")
                        + ", 部门=" + r.get("dept") + ", 月薪=" + r.get("monthlySalary") + ", 年薪=" + r.get("annualSalary"));
            }
            // 表头不计入数据行：6 名员工 + 1 行合计
            Assert.assertEquals("6 名员工 + 1 合计", 7, rows.size());
        }
    }

    /** 构造一条员工档案（a=工号，b=姓名，c=部门，d=职位，e=入职日期，f=月薪）。 */
    private static JQuickRow employee(String no, String name, String dept, String position, Date hireDate, double salary) {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", name);
        row.put("c", dept);
        row.put("d", position);
        row.put("e", hireDate);
        row.put("f", salary);
        row.put("g", null);
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
