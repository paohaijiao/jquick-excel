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
 * 场景 61：员工请假申请汇总表（数据校验填报类，🟢 纯 XML + 导入校验）。
 *
 * <p>业务：汇总各员工的请假申请，末尾一行汇总请假总天数；导入时对填报内容做规则校验
 * （工号 / 姓名长度、部门与请假类型字典、请假天数数值范围）。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/validation/0061_validation_leave-application-summary.xml}：
 * FORMULAS 求请假总天数，STYLE 给合计行高亮；导入侧的 VALIDATION 规则块负责校验。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Validation0061LeaveApplicationSummaryDemo {

    private static final String XML = "jquick/biz/validation/0061_validation_leave-application-summary.xml";

    /** 导出：纯 XML 完成请假总天数汇总与合计行高亮。 */
    @Test
    public void exportLeaveApplicationSummary() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(leave("E2601", "张伟", "研发部", "年假", date(2026, 3, 2), date(2026, 3, 4), 3, "已批准"));
        rows.add(leave("E2602", "李娜", "市场部", "事假", date(2026, 3, 5), date(2026, 3, 5), 1, "已批准"));
        rows.add(leave("E2603", "王强", "生产部", "病假", date(2026, 3, 9), date(2026, 3, 11), 3, "已批准"));
        rows.add(leave("E2604", "赵敏", "财务部", "年假", date(2026, 3, 12), date(2026, 3, 16), 5, "待审批"));
        rows.add(leave("E2605", "陈杰", "研发部", "调休", date(2026, 3, 18), date(2026, 3, 19), 2, "已批准"));
        rows.add(leave("E2606", "周涛", "市场部", "事假", date(2026, 3, 23), date(2026, 3, 23), 1, "已批准"));
        // 合计占位行：请假总天数留给 FORMULAS
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

        File out = BizKit.outFile("validation", "0061_validation_leave-application-summary.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("coral", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0061LeaveApplicationSummaryService service = factory.createApi(Validation0061LeaveApplicationSummaryService.class);
            service.exportLeaveApplicationSummary("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工请假申请汇总表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首条请假天数
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals(3.0, g2.getNumericCellValue(), 0.0001);
            // 请假总天数 = SUM(G2:G7) = 3 + 1 + 3 + 5 + 2 + 1
            Cell g8 = sheet.getRow(7).getCell(6);
            Assert.assertEquals("SUM(G2:G7)", g8.getCellFormula());
            Assert.assertEquals(15.00, evaluator.evaluate(g8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景61】员工请假申请汇总表导出: " + out.getAbsolutePath()
                    + "，请假总天数 " + evaluator.evaluate(g8).getNumberValue());
        }
    }

    /** 导入解析：读取填报的请假申请并逐项校验（先重算公式）。 */
    @Test
    public void importLeaveApplicationSummary() throws Exception {
        File src = BizKit.outFile("validation", "0061_validation_leave-application-summary.xlsx");
        if (!src.exists()) {
            exportLeaveApplicationSummary();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("validation", "0061_validation_leave-application-summary-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0061LeaveApplicationSummaryService service = factory.createApi(Validation0061LeaveApplicationSummaryService.class);
            List<JQuickRow> rows = service.importLeaveApplicationSummary("field", "value");

            System.out.println("【场景61】员工请假申请汇总表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("employeeNo") + ", 姓名=" + r.get("name")
                        + ", 部门=" + r.get("department") + ", 类型=" + r.get("leaveType")
                        + ", 天数=" + r.get("leaveDays") + ", 状态=" + r.get("approvalStatus"));
            }
            // 表头不计入数据行：6 条申请 + 1 行合计
            Assert.assertEquals("6 条申请 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条请假申请（a=工号，b=姓名，c=部门，d=类型，e=开始，f=结束，g=天数，h=状态）。 */
    private static JQuickRow leave(String employeeNo, String name, String department, String leaveType,
                                   Date startDate, Date endDate, int leaveDays, String approvalStatus) {
        JQuickRow row = new JQuickRow();
        row.put("a", employeeNo);
        row.put("b", name);
        row.put("c", department);
        row.put("d", leaveType);
        row.put("e", startDate);
        row.put("f", endDate);
        row.put("g", leaveDays);
        row.put("h", approvalStatus);
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
