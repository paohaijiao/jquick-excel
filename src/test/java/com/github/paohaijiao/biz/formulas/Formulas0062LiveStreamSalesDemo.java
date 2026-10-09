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
 * 场景 62：直播带货销售明细（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：按直播场次登记带货商品，逐行算「销售额 = 销售数量 × 单价」与「佣金 = 销售额 × 佣金比例」，
 * 末尾一行汇总销售额与佣金。
 *
 * <p>导出侧逐行乘法与合计全部在 {@code jquick/biz/formulas/0062_formulas_live-stream-sales.xml}：
 * FORMULAS 用 D*E 求销售额、F*G 求佣金、SUM 求合计，STYLE 给合计行高亮；Java 只构造明细数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0062LiveStreamSalesDemo {

    private static final String XML = "jquick/biz/formulas/0062_formulas_live-stream-sales.xml";

    /** 导出：纯 XML 完成逐行销售额 / 佣金与合计行高亮。 */
    @Test
    public void exportLiveStreamSales() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f=销售额、h=佣金 两列留空，由 FORMULAS 计算
        rows.add(line("S2601", "张伟", "智能手环", 1200, 199.00, 0.15));
        rows.add(line("S2601", "张伟", "无线耳机", 800, 299.00, 0.15));
        rows.add(line("S2602", "李娜", "扫地机器人", 300, 1299.00, 0.20));
        rows.add(line("S2603", "王强", "空气炸锅", 1500, 399.00, 0.12));
        rows.add(line("S2603", "王强", "破壁机", 600, 499.00, 0.12));
        // 合计占位行：销售额与佣金合计留给 FORMULAS
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

        File out = BizKit.outFile("formulas", "0062_formulas_live-stream-sales.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("denim", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0062LiveStreamSalesService service = factory.createApi(Formulas0062LiveStreamSalesService.class);
            service.exportLiveStreamSales("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("直播带货销售明细");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行销售额 = 销售数量 × 单价 = 1200 × 199
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(238800.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 第二行佣金 = 销售额 × 佣金比例 = 239200 × 0.15
            Cell h3 = sheet.getRow(2).getCell(7);
            Assert.assertEquals("F3*G3", h3.getCellFormula());
            Assert.assertEquals(35880.00, evaluator.evaluate(h3).getNumberValue(), 0.0001);
            // 销售额合计 = SUM(F2:F6) = 238800 + 239200 + 389700 + 598500 + 299400
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("SUM(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(1765600.00, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 佣金合计 = SUM(H2:H6) = 35820 + 35880 + 77940 + 71820 + 35928
            Cell h7 = sheet.getRow(6).getCell(7);
            Assert.assertEquals("SUM(H2:H6)", h7.getCellFormula());
            Assert.assertEquals(257388.00, evaluator.evaluate(h7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景62】直播带货销售明细导出: " + out.getAbsolutePath()
                    + "，销售额合计 " + evaluator.evaluate(f7).getNumberValue()
                    + "，佣金合计 " + evaluator.evaluate(h7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的直播带货销售明细（先重算公式）。 */
    @Test
    public void importLiveStreamSales() throws Exception {
        File src = BizKit.outFile("formulas", "0062_formulas_live-stream-sales.xlsx");
        if (!src.exists()) {
            exportLiveStreamSales();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0062_formulas_live-stream-sales-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0062LiveStreamSalesService service = factory.createApi(Formulas0062LiveStreamSalesService.class);
            List<JQuickRow> rows = service.importLiveStreamSales("field", "value");

            System.out.println("【场景62】直播带货销售明细导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    场次=" + r.get("session") + ", 主播=" + r.get("anchor")
                        + ", 商品=" + r.get("productName") + ", 数量=" + r.get("quantity")
                        + ", 单价=" + r.get("unitPrice") + ", 销售额=" + r.get("salesAmount")
                        + ", 佣金=" + r.get("commission"));
            }
            // 表头不计入数据行：5 条明细 + 1 行合计
            Assert.assertEquals("5 条明细 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条带货明细（a=场次，b=主播，c=商品，d=数量，e=单价，f=销售额，g=佣金比例，h=佣金）。 */
    private static JQuickRow line(String session, String anchor, String productName,
                                  int quantity, double unitPrice, double commissionRate) {
        JQuickRow row = new JQuickRow();
        row.put("a", session);
        row.put("b", anchor);
        row.put("c", productName);
        row.put("d", quantity);
        row.put("e", unitPrice);
        row.put("f", null);
        row.put("g", commissionRate);
        row.put("h", null);
        return row;
    }
}
