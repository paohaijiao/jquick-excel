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
 * 场景 30：差旅费报销明细单（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：员工出差后按行程登记交通费、住宿费与补贴，逐行算「小计 = 交通费 + 住宿费 + 补贴」，
 * 末尾一行汇总各费用项与报销总额。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0030_formulas_travel-expense-detail.xml}：
 * FORMULAS 做逐行三列相加与合计行汇总，STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0030TravelExpenseDetailDemo {

    private static final String XML = "jquick/biz/formulas/0030_formulas_travel-expense-detail.xml";

    /** 导出：纯 XML 完成逐行小计与合计行汇总。 */
    @Test
    public void exportTravelExpense() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(trip("张伟", "客户拜访", date(2026, 3, 2), date(2026, 3, 5), 1200, 800, 450));
        rows.add(trip("李娜", "展会参展", date(2026, 3, 8), date(2026, 3, 11), 2200, 1600, 600));
        rows.add(trip("王强", "供应商审核", date(2026, 3, 10), date(2026, 3, 12), 900, 600, 300));
        rows.add(trip("赵敏", "技术交流", date(2026, 3, 15), date(2026, 3, 18), 1800, 1200, 500));
        rows.add(trip("陈刚", "项目验收", date(2026, 3, 20), date(2026, 3, 22), 700, 400, 200));
        // 合计占位行：各费用项与小计留给 FORMULAS
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

        File out = BizKit.outFile("formulas", "0030_formulas_travel-expense-detail.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("tropicalTeal", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0030TravelExpenseDetailService service = factory.createApi(Formulas0030TravelExpenseDetailService.class);
            service.exportTravelExpense("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("差旅费用报销明细");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行小计 = 交通 + 住宿 + 补贴 = 1200 + 800 + 450
            Cell h2 = sheet.getRow(1).getCell(7);
            Assert.assertEquals("E2+F2+G2", h2.getCellFormula());
            Assert.assertEquals(2450.0, evaluator.evaluate(h2).getNumberValue(), 0.0001);
            // 合计：交通费 SUM(E2:E6) = 6800
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("SUM(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(6800.0, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计：小计 SUM(H2:H6) = 13450
            Cell h7 = sheet.getRow(6).getCell(7);
            Assert.assertEquals("SUM(H2:H6)", h7.getCellFormula());
            Assert.assertEquals(13450.0, evaluator.evaluate(h7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景30】差旅费报销明细单导出: " + out.getAbsolutePath()
                    + "，报销总额 " + evaluator.evaluate(h7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的差旅费报销明细单（先重算公式）。 */
    @Test
    public void importTravelExpense() throws Exception {
        File src = BizKit.outFile("formulas", "0030_formulas_travel-expense-detail.xlsx");
        if (!src.exists()) {
            exportTravelExpense();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0030_formulas_travel-expense-detail-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0030TravelExpenseDetailService service = factory.createApi(Formulas0030TravelExpenseDetailService.class);
            List<JQuickRow> rows = service.importTravelExpense("field", "value");

            System.out.println("【场景30】差旅费报销明细单导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    出差人=" + r.get("traveler") + ", 事由=" + r.get("reason")
                        + ", 交通费=" + r.get("transportFee") + ", 住宿费=" + r.get("hotelFee") + ", 小计=" + r.get("subtotal"));
            }
            // 表头不计入数据行：5 段行程 + 1 行合计
            Assert.assertEquals("5 段行程 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条出差行程（a=出差人，b=事由，c=出发，d=返回，e=交通费，f=住宿费，g=补贴）。 */
    private static JQuickRow trip(String traveler, String reason, Date start, Date end, double transport, double hotel, double allowance) {
        JQuickRow row = new JQuickRow();
        row.put("a", traveler);
        row.put("b", reason);
        row.put("c", start);
        row.put("d", end);
        row.put("e", transport);
        row.put("f", hotel);
        row.put("g", allowance);
        row.put("h", null);
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
