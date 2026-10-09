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
 * 场景 52：客户拜访记录台账（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：登记客户拜访记录，末尾一行汇总拜访总时长，「待跟进」状态的客户标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0052_style_customer-visit-record.xml}：
 * FORMULAS 求拜访总时长，STYLE 给合计行高亮、给待跟进状态标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0052CustomerVisitRecordDemo {

    private static final String XML = "jquick/biz/style/0052_style_customer-visit-record.xml";

    /** 导出：纯 XML 完成拜访时长汇总、合计行高亮与待跟进标红。 */
    @Test
    public void exportCustomerVisitRecord() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(visit(date(2026, 3, 1), "恒信商贸", "张伟", "上门", 1.5, "A", "已成交"));
        rows.add(visit(date(2026, 3, 3), "华越集团", "张伟", "电话", 0.5, "B", "跟进中"));
        rows.add(visit(date(2026, 3, 5), "广发物流", "李娜", "上门", 2.0, "A", "已成交"));
        rows.add(visit(date(2026, 3, 8), "中远建材", "李娜", "视频", 1.0, "C", "待跟进"));
        rows.add(visit(date(2026, 3, 12), "蓝天科技", "王强", "上门", 1.5, "B", "跟进中"));
        rows.add(visit(date(2026, 3, 15), "星辰电子", "王强", "电话", 0.5, "C", "待跟进"));
        // 合计占位行：拜访总时长留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0052_style_customer-visit-record.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("indigo", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0052CustomerVisitRecordService service = factory.createApi(Style0052CustomerVisitRecordService.class);
            service.exportCustomerVisitRecord("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("客户拜访记录台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首条拜访时长
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals(1.5, e2.getNumericCellValue(), 0.0001);
            // 拜访总时长 = SUM(E2:E7) = 1.5 + 0.5 + 2.0 + 1.0 + 1.5 + 0.5
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals("SUM(E2:E7)", e8.getCellFormula());
            Assert.assertEquals(7.0, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 待跟进状态标红（中远建材 Excel 第 5 行、星辰电子 Excel 第 7 行）
            Assert.assertEquals("待跟进应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) sheet.getRow(4).getCell(6).getCellStyle()).getFont().getColor());
            Assert.assertEquals("待跟进应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) sheet.getRow(6).getCell(6).getCellStyle()).getFont().getColor());

            System.out.println("【场景52】客户拜访记录台账导出: " + out.getAbsolutePath()
                    + "，拜访总时长 " + evaluator.evaluate(e8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的客户拜访记录台账（先重算公式）。 */
    @Test
    public void importCustomerVisitRecord() throws Exception {
        File src = BizKit.outFile("style", "0052_style_customer-visit-record.xlsx");
        if (!src.exists()) {
            exportCustomerVisitRecord();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0052_style_customer-visit-record-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0052CustomerVisitRecordService service = factory.createApi(Style0052CustomerVisitRecordService.class);
            List<JQuickRow> rows = service.importCustomerVisitRecord("field", "value");

            System.out.println("【场景52】客户拜访记录台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    日期=" + r.get("visitDate") + ", 客户=" + r.get("customer")
                        + ", 拜访人=" + r.get("visitor") + ", 方式=" + r.get("visitType")
                        + ", 时长=" + r.get("visitHours") + ", 状态=" + r.get("followStatus"));
            }
            // 表头不计入数据行：6 条记录 + 1 行合计
            Assert.assertEquals("6 条记录 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条拜访记录（a=日期，b=客户，c=拜访人，d=方式，e=时长，f=等级，g=状态）。 */
    private static JQuickRow visit(Date visitDate, String customer, String visitor, String visitType,
                                   double visitHours, String customerLevel, String followStatus) {
        JQuickRow row = new JQuickRow();
        row.put("a", visitDate);
        row.put("b", customer);
        row.put("c", visitor);
        row.put("d", visitType);
        row.put("e", visitHours);
        row.put("f", customerLevel);
        row.put("g", followStatus);
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
