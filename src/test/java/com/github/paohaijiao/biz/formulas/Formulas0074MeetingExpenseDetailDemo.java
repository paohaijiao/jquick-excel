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
 * 场景 74：会议费用报销明细表（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：按会议登记场地费 / 餐饮费 / 住宿费，逐行算「合计金额 = 场地费 + 餐饮费 + 住宿费」，
 * 末尾一行汇总各项费用与合计；费用最高的会议标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0074_formulas_meeting-expense-detail.xml}：
 * FORMULAS 求逐行合计与各列汇总，STYLE 给合计行高亮、给最高费用会议标红；Java 只构造报销数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0074MeetingExpenseDetailDemo {

    private static final String XML = "jquick/biz/formulas/0074_formulas_meeting-expense-detail.xml";

    /** 导出：纯 XML 完成合计金额、各列汇总与最高费用标红。 */
    @Test
    public void exportMeetingExpenseDetail() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f=合计金额 留空，由 FORMULAS 计算
        rows.add(meeting("年度供应商大会", date(2026, 3, 5), 20000.00, 15000.00, 12000.00, "已审批"));
        rows.add(meeting("新品发布会", date(2026, 3, 12), 35000.00, 18000.00, 8000.00, "已审批"));
        rows.add(meeting("技术研讨会", date(2026, 3, 18), 12000.00, 9000.00, 0.00, "已审批"));
        rows.add(meeting("销售季度总结会", date(2026, 3, 25), 8000.00, 12000.00, 0.00, "已审批"));
        rows.add(meeting("客户答谢晚宴", date(2026, 3, 30), 15000.00, 22000.00, 0.00, "待审批"));
        // 合计占位行：各列费用与合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0074_formulas_meeting-expense-detail.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("minimalistGrey", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0074MeetingExpenseDetailService service = factory.createApi(Formulas0074MeetingExpenseDetailService.class);
            service.exportMeetingExpenseDetail("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("会议费用报销明细表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首场合计 = 场地 + 餐饮 + 住宿 = 20000 + 15000 + 12000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2+D2+E2", f2.getCellFormula());
            Assert.assertEquals(47000.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 最高费用会议（第 2 场，Excel 第 3 行）合计 = 35000 + 18000 + 8000
            Cell f3 = sheet.getRow(2).getCell(5);
            Assert.assertEquals(61000.00, evaluator.evaluate(f3).getNumberValue(), 0.0001);
            // 各列汇总：场地费合计 = SUM(C2:C6) = 90000
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(90000.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计行合计金额 = 各列合计相加 = 90000 + 76000 + 20000
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("C7+D7+E7", f7.getCellFormula());
            Assert.assertEquals(186000.00, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 最高费用会议合计标红加粗
            XSSFCellStyle highStyle = (XSSFCellStyle) f3.getCellStyle();
            Assert.assertTrue("最高费用合计应加粗", highStyle.getFont().getBold());
            Assert.assertEquals("最高费用合计应标红", (int) IndexedColors.RED.getIndex(), (int) highStyle.getFont().getColor());

            System.out.println("【场景74】会议费用报销明细表导出: " + out.getAbsolutePath()
                    + "，费用合计 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的会议费用报销明细表（先重算公式）。 */
    @Test
    public void importMeetingExpenseDetail() throws Exception {
        File src = BizKit.outFile("formulas", "0074_formulas_meeting-expense-detail.xlsx");
        if (!src.exists()) {
            exportMeetingExpenseDetail();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0074_formulas_meeting-expense-detail-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0074MeetingExpenseDetailService service = factory.createApi(Formulas0074MeetingExpenseDetailService.class);
            List<JQuickRow> rows = service.importMeetingExpenseDetail("field", "value");

            System.out.println("【场景74】会议费用报销明细表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    会议=" + r.get("meetingName") + ", 日期=" + r.get("meetingDate")
                        + ", 场地费=" + r.get("venueFee") + ", 餐饮费=" + r.get("cateringFee")
                        + ", 住宿费=" + r.get("accommodationFee") + ", 合计=" + r.get("totalAmount")
                        + ", 状态=" + r.get("approvalStatus"));
            }
            // 表头不计入数据行：5 场会议 + 1 行合计
            Assert.assertEquals("5 场会议 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一场会议报销（a=会议名称，b=日期，c=场地费，d=餐饮费，e=住宿费，f=合计金额，g=审批状态）。 */
    private static JQuickRow meeting(String meetingName, Date meetingDate, double venueFee,
                                     double cateringFee, double accommodationFee, String approvalStatus) {
        JQuickRow row = new JQuickRow();
        row.put("a", meetingName);
        row.put("b", meetingDate);
        row.put("c", venueFee);
        row.put("d", cateringFee);
        row.put("e", accommodationFee);
        row.put("f", null);
        row.put("g", approvalStatus);
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
