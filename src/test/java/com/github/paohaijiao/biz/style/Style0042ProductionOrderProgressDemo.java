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
 * 场景 42：生产工单进度看板（计划排期类，🟢 纯 XML）。
 *
 * <p>业务：按生产工单跟踪计划数量与完成数量，逐行算「完成率 = 完成数量 / 计划数量」，
 * 逾期未完成的工单完成率标红，末尾一行给出整体完成率。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0042_style_production-order-progress.xml}：
 * FORMULAS 做逐行除法与合计行汇总，STYLE 给合计行高亮、给逾期工单的完成率标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0042ProductionOrderProgressDemo {

    private static final String XML = "jquick/biz/style/0042_style_production-order-progress.xml";

    /** 导出：纯 XML 完成完成率、合计汇总与逾期标红。 */
    @Test
    public void exportProductionOrder() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(order("WO260301", "智能手表", 2000, 2000, date(2026, 3, 25), "已完工"));
        rows.add(order("WO260302", "蓝牙耳机", 5000, 5000, date(2026, 3, 28), "已完工"));
        rows.add(order("WO260303", "无线充电器", 3000, 1800, date(2026, 3, 30), "生产中"));
        rows.add(order("WO260304", "智能音箱", 1500, 750, date(2026, 3, 22), "逾期"));
        rows.add(order("WO260305", "运动手环", 4000, 1600, date(2026, 4, 5), "生产中"));
        // 合计占位行：数量与整体完成率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0042_style_production-order-progress.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("minimalistGrey", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0042ProductionOrderProgressService service = factory.createApi(Style0042ProductionOrderProgressService.class);
            service.exportProductionOrder("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("生产工单进度看板");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行完成率 = 2000 / 2000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/C2", e2.getCellFormula());
            Assert.assertEquals(1.0, evaluator.evaluate(e2).getNumberValue(), 0.0000001);
            // 逾期工单完成率 = 750 / 1500
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals(0.5, evaluator.evaluate(e5).getNumberValue(), 0.0000001);
            // 合计计划数量 SUM(C2:C6) = 15500
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(15500.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计完成数量 SUM(D2:D6) = 11150
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(11150.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计整体完成率 = 11150 / 15500
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("D7/C7", e7.getCellFormula());
            Assert.assertEquals(11150d / 15500d, evaluator.evaluate(e7).getNumberValue(), 0.0000001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 逾期工单完成率标红
            Assert.assertEquals("逾期工单应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) e5.getCellStyle()).getFont().getColor());

            System.out.println("【场景42】生产工单进度看板导出: " + out.getAbsolutePath()
                    + "，整体完成率 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的生产工单进度看板（先重算公式）。 */
    @Test
    public void importProductionOrder() throws Exception {
        File src = BizKit.outFile("style", "0042_style_production-order-progress.xlsx");
        if (!src.exists()) {
            exportProductionOrder();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0042_style_production-order-progress-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0042ProductionOrderProgressService service = factory.createApi(Style0042ProductionOrderProgressService.class);
            List<JQuickRow> rows = service.importProductionOrder("field", "value");

            System.out.println("【场景42】生产工单进度看板导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工单=" + r.get("orderNo") + ", 产品=" + r.get("product")
                        + ", 计划=" + r.get("planQty") + ", 完成=" + r.get("doneQty")
                        + ", 完成率=" + r.get("doneRate") + ", 状态=" + r.get("progressStatus"));
            }
            // 表头不计入数据行：5 张工单 + 1 行合计
            Assert.assertEquals("5 张工单 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一张工单行（a=工单号，b=产品，c=计划数量，d=完成数量，f=交期，e 留空）。 */
    private static JQuickRow order(String orderNo, String product, double planQty, double doneQty, Date dueDate, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", orderNo);
        row.put("b", product);
        row.put("c", planQty);
        row.put("d", doneQty);
        row.put("e", null);
        row.put("f", dueDate);
        row.put("g", status);
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
