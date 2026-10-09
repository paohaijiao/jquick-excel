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
package com.github.paohaijiao.biz.validation;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 场景 12：员工信息填报表（🟡 Java 预处理 + VALIDATION 导入校验）。
 *
 * <p>业务：员工填报信息（工号 / 姓名 / 部门 / 年龄 / 入职日期 / 月薪 / 邮箱 / 手机号），
 * 末行给出平均年龄与月薪合计；导入时逐项校验填报内容是否合规。
 *
 * <p>🟡 标记含义：Java 只负责「构造填报数据 + 追加统计占位行」，统计仍由
 * {@code jquick/biz/validation/0012_validation_employee-info-report.xml} 的 FORMULAS 计算（AVERAGE / SUM）；
 * 导入校验规则全部写在模板的 VALIDATION 块里。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Validation0012EmployeeInfoReportDemo {

    private static final String XML = "jquick/biz/validation/0012_validation_employee-info-report.xml";

    /** 导出：3 名员工 + 1 行统计，回读校验统计公式与统计行样式。 */
    @Test
    public void exportEmployeeInfo() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(employee("EMP001", "张三", "研发部", 26, "2023-03-15", 12000.00, "zhang@example.com", "13800000001"));
        rows.add(employee("EMP002", "李四", "市场部", 31, "2021-07-01", 15000.00, "li@example.com", "13900000002"));
        rows.add(employee("EMP003", "王五", "财务部", 29, "2022-11-20", 13000.00, "wang@example.com", "13700000003"));
        rows.add(statRow());

        File out = BizKit.outFile("validation", "0012_validation_employee-info-report.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("bronze", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0012EmployeeInfoReportService service = factory.createApi(Validation0012EmployeeInfoReportService.class);
            service.exportEmployeeInfo("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工信息填报表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 统计行：平均年龄 AVERAGE = (26 + 31 + 29) / 3
            Assert.assertEquals("AVERAGE(D2:D4)", sheet.getRow(4).getCell(3).getCellFormula());
            Assert.assertEquals(86.0 / 3.0, evaluator.evaluate(sheet.getRow(4).getCell(3)).getNumberValue(), 0.0001);
            // 统计行：薪资合计 SUM
            Assert.assertEquals("SUM(F2:F4)", sheet.getRow(4).getCell(5).getCellFormula());
            Assert.assertEquals(40000.00, evaluator.evaluate(sheet.getRow(4).getCell(5)).getNumberValue(), 0.0001);
            // 统计行加粗高亮
            Assert.assertTrue("统计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 入职日期格式化
            Assert.assertEquals("yyyy-MM-dd", sheet.getRow(1).getCell(4).getCellStyle().getDataFormatString());

            System.out.println("【场景12】员工信息填报表导出: " + out.getAbsolutePath());
        }
    }

    /** 导入解析：读取导出的填报表（先重算公式），VALIDATION 全部通过后返回数据行。 */
    @Test
    public void importEmployeeInfo() throws Exception {
        File src = BizKit.outFile("validation", "0012_validation_employee-info-report.xlsx");
        if (!src.exists()) {
            exportEmployeeInfo();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("validation", "0012_validation_employee-info-report-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0012EmployeeInfoReportService service = factory.createApi(Validation0012EmployeeInfoReportService.class);
            List<JQuickRow> rows = service.importEmployeeInfo("field", "value");

            System.out.println("【场景12】员工信息填报表导入解析（校验通过）: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("code") + ", 姓名=" + r.get("name")
                        + ", 部门=" + r.get("dept") + ", 年龄=" + r.get("age")
                        + ", 入职=" + r.get("hireDate") + ", 月薪=" + r.get("salary"));
            }
            Assert.assertEquals("3 名员工 + 1 行统计", 4, rows.size());
        }
    }

    /** 构造一名员工填报行（含全部映射列）。 */
    private static JQuickRow employee(String code, String name, String dept, int age,
                                      String hireDate, double salary, String email, String phone) throws Exception {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", dept);
        row.put("d", age);
        row.put("e", new SimpleDateFormat("yyyy-MM-dd").parse(hireDate));
        row.put("f", salary);
        row.put("g", email);
        row.put("h", phone);
        return row;
    }

    /** 统计占位行：只写「统计」文字，人数 / 平均年龄 / 薪资合计三列留给 FORMULAS。 */
    private static JQuickRow statRow() {
        JQuickRow row = new JQuickRow();
        row.put("a", "统计");
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        row.put("h", null);
        return row;
    }
}
