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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景 43：员工培训档案登记（档案名册类，🟢 纯 XML）。
 *
 * <p>业务：登记员工培训档案，逐行记录课程、培训日期、学时与考核成绩，
 * 考核不及格的记录标红，末尾一行汇总总学时与平均成绩。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0043_formulas_training-record.xml}：
 * FORMULAS 在合计行做 SUM 与 AVERAGE，STYLE 给合计行高亮、给不及格的考核成绩标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0043TrainingRecordDemo {

    private static final String XML = "jquick/biz/formulas/0043_formulas_training-record.xml";

    /** 导出：纯 XML 完成学时汇总、平均成绩与不及格标红。 */
    @Test
    public void exportTrainingRecord() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(record("E1001", "张伟", "研发部", "安全生产", date(2026, 3, 5), 8, 92, "通过"));
        rows.add(record("E1002", "李娜", "市场部", "客户沟通", date(2026, 3, 8), 6, 85, "通过"));
        rows.add(record("E1003", "王强", "生产部", "设备操作", date(2026, 3, 10), 12, 58, "未通过"));
        rows.add(record("E1004", "赵敏", "财务部", "税务合规", date(2026, 3, 12), 10, 88, "通过"));
        rows.add(record("E1005", "陈刚", "研发部", "项目管理", date(2026, 3, 15), 16, 79, "通过"));
        // 合计占位行：总学时与平均成绩留给 FORMULAS
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

        File out = BizKit.outFile("formulas", "0043_formulas_training-record.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("forestGreen", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0043TrainingRecordService service = factory.createApi(Formulas0043TrainingRecordService.class);
            service.exportTrainingRecord("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工培训档案登记");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 不及格记录成绩 = 58
            Cell g4 = sheet.getRow(3).getCell(6);
            Assert.assertEquals(58.0, evaluator.evaluate(g4).getNumberValue(), 0.0001);
            // 总学时 SUM(F2:F6) = 8 + 6 + 12 + 10 + 16
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("SUM(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(52.0, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 平均成绩 AVERAGE(G2:G6) = (92 + 85 + 58 + 88 + 79) / 5
            Cell g7 = sheet.getRow(6).getCell(6);
            Assert.assertEquals("AVERAGE(G2:G6)", g7.getCellFormula());
            Assert.assertEquals(80.4, evaluator.evaluate(g7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 不及格记录成绩标红
            Assert.assertEquals("不及格记录应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) g4.getCellStyle()).getFont().getColor());

            System.out.println("【场景43】员工培训档案登记导出: " + out.getAbsolutePath()
                    + "，平均成绩 " + evaluator.evaluate(g7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的员工培训档案登记（先重算公式）。 */
    @Test
    public void importTrainingRecord() throws Exception {
        File src = BizKit.outFile("formulas", "0043_formulas_training-record.xlsx");
        if (!src.exists()) {
            exportTrainingRecord();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0043_formulas_training-record-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0043TrainingRecordService service = factory.createApi(Formulas0043TrainingRecordService.class);
            List<JQuickRow> rows = service.importTrainingRecord("field", "value");

            System.out.println("【场景43】员工培训档案登记导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("empNo") + ", 姓名=" + r.get("name")
                        + ", 课程=" + r.get("course") + ", 学时=" + r.get("hours")
                        + ", 成绩=" + r.get("score") + ", 是否通过=" + r.get("passed"));
            }
            // 表头不计入数据行：5 条培训记录 + 1 行合计
            Assert.assertEquals("5 条记录 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条培训记录（a=工号，b=姓名，c=部门，d=课程，e=日期，f=学时，g=成绩，h=是否通过）。 */
    private static JQuickRow record(String empNo, String name, String dept, String course, Date trainDate,
                                    double hours, double score, String passed) {
        JQuickRow row = new JQuickRow();
        row.put("a", empNo);
        row.put("b", name);
        row.put("c", dept);
        row.put("d", course);
        row.put("e", trainDate);
        row.put("f", hours);
        row.put("g", score);
        row.put("h", passed);
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
