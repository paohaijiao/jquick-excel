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
 * 场景 91：差旅补贴发放明细表（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：逐条出差记录登记天数与日补贴标准，算「补贴合计 = 出差天数 × 日补贴标准 + 交通补贴」，
 * 末尾一行汇总天数、交通补贴与补贴合计。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0091_formulas_travel-allowance-detail.xml}：
 * FORMULAS 求逐行补贴合计、SUM 求各列合计，STYLE 给合计行高亮；Java 只构造差旅数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0091TravelAllowanceDetailDemo {

    private static final String XML = "jquick/biz/formulas/0091_formulas_travel-allowance-detail.xml";

    /** 导出：纯 XML 完成逐行补贴合计、各列汇总与合计行高亮。 */
    @Test
    public void exportTravelAllowanceDetail() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // g 留空，由 FORMULAS 计算
        rows.add(record("张伟", "研发部", "北京", 5, 300.00, 1500.00));
        rows.add(record("李娜", "市场部", "上海", 4, 280.00, 1200.00));
        rows.add(record("王强", "生产部", "广州", 6, 260.00, 1800.00));
        rows.add(record("赵敏", "财务部", "深圳", 3, 300.00, 900.00));
        rows.add(record("孙磊", "研发部", "杭州", 5, 280.00, 1500.00));
        rows.add(record("周芳", "客服部", "成都", 4, 240.00, 1200.00));
        // 合计占位行：天数 / 交通补贴 / 补贴合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0091_formulas_travel-allowance-detail.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("champagne", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0091TravelAllowanceDetailService service = factory.createApi(Formulas0091TravelAllowanceDetailService.class);
            service.exportTravelAllowanceDetail("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("差旅补贴发放明细表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行补贴合计 = 天数 × 日补贴标准 + 交通补贴 = 5 × 300 + 1500
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("D2*E2+F2", g2.getCellFormula());
            Assert.assertEquals(3000.00, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计：出差天数 = SUM(D2:D7) = 27
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("SUM(D2:D7)", d8.getCellFormula());
            Assert.assertEquals(27.00, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 合计：补贴合计 = SUM(G2:G7) = 15540
            Cell g8 = sheet.getRow(7).getCell(6);
            Assert.assertEquals("SUM(G2:G7)", g8.getCellFormula());
            Assert.assertEquals(15540.00, evaluator.evaluate(g8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景91】差旅补贴发放明细表导出: " + out.getAbsolutePath()
                    + "，合计补贴 " + evaluator.evaluate(g8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的差旅补贴发放明细表（先重算公式）。 */
    @Test
    public void importTravelAllowanceDetail() throws Exception {
        File src = BizKit.outFile("formulas", "0091_formulas_travel-allowance-detail.xlsx");
        if (!src.exists()) {
            exportTravelAllowanceDetail();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0091_formulas_travel-allowance-detail-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0091TravelAllowanceDetailService service = factory.createApi(Formulas0091TravelAllowanceDetailService.class);
            List<JQuickRow> rows = service.importTravelAllowanceDetail("field", "value");

            System.out.println("【场景91】差旅补贴发放明细表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    姓名=" + r.get("employeeName") + ", 部门=" + r.get("department")
                        + ", 出差地点=" + r.get("destination") + ", 出差天数=" + r.get("travelDays")
                        + ", 日补贴标准=" + r.get("dailyAllowance") + ", 交通补贴=" + r.get("transportAllowance")
                        + ", 补贴合计=" + r.get("totalAllowance"));
            }
            // 表头不计入数据行：6 条明细 + 1 行合计
            Assert.assertEquals("6 条明细 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条差旅记录（a=姓名，b=部门，c=地点，d=天数，e=日标准，f=交通补贴，g 留空）。 */
    private static JQuickRow record(String employeeName, String department, String destination,
                                    double travelDays, double dailyAllowance, double transportAllowance) {
        JQuickRow row = new JQuickRow();
        row.put("a", employeeName);
        row.put("b", department);
        row.put("c", destination);
        row.put("d", travelDays);
        row.put("e", dailyAllowance);
        row.put("f", transportAllowance);
        row.put("g", null);
        return row;
    }
}
