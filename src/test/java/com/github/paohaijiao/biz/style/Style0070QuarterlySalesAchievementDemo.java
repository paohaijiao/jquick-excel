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
import java.util.List;

/**
 * 场景 70：季度销售目标达成对比表（数据对比差异类，🟢 纯 XML）。
 *
 * <p>业务：按区域登记季度销售目标与实际销售额，逐行算「目标差额 = 实际 - 目标」与
 * 「达成率 = 实际 / 目标」，末尾一行给出全区域汇总；未达标区域标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0070_style_quarterly-sales-achievement.xml}：
 * FORMULAS 求逐行差额 / 达成率与汇总，STYLE 给合计行高亮、给未达标区域标红；Java 只构造数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0070QuarterlySalesAchievementDemo {

    private static final String XML = "jquick/biz/style/0070_style_quarterly-sales-achievement.xml";

    /** 导出：纯 XML 完成目标差额、达成率、汇总与未达标标红。 */
    @Test
    public void exportQuarterlySalesAchievement() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // d=目标差额、e=达成率 留空，由 FORMULAS 计算
        rows.add(region("华东", 2000000.00, 2200000.00, "良好"));
        rows.add(region("华南", 1500000.00, 1650000.00, "良好"));
        rows.add(region("华北", 1800000.00, 1620000.00, "未达标"));
        rows.add(region("西南", 1200000.00, 1380000.00, "优秀"));
        rows.add(region("东北", 800000.00, 760000.00, "未达标"));
        // 汇总占位行：目标 / 实际 / 差额 / 达成率 留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0070_style_quarterly-sales-achievement.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mustard", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0070QuarterlySalesAchievementService service = factory.createApi(Style0070QuarterlySalesAchievementService.class);
            service.exportQuarterlySalesAchievement("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("季度销售目标达成对比表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行目标差额 = 实际 - 目标 = 2200000 - 2000000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("C2-B2", d2.getCellFormula());
            Assert.assertEquals(200000.00, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 华南达成率 = 1650000 / 1500000
            Cell e3 = sheet.getRow(2).getCell(4);
            Assert.assertEquals("C3/B3", e3.getCellFormula());
            Assert.assertEquals(1.10, evaluator.evaluate(e3).getNumberValue(), 0.0001);
            // 目标合计 = SUM(B2:B6) = 7300000
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(7300000.00, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 未达标区域（第 3 个区域，Excel 第 4 行）达成率 = 1620000 / 1800000
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals(0.90, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 未达标区域达成率标红加粗
            XSSFCellStyle lowStyle = (XSSFCellStyle) e4.getCellStyle();
            Assert.assertTrue("未达标达成率应加粗", lowStyle.getFont().getBold());
            Assert.assertEquals("未达标达成率应标红", (int) IndexedColors.RED.getIndex(), (int) lowStyle.getFont().getColor());

            System.out.println("【场景70】季度销售目标达成对比表导出: " + out.getAbsolutePath()
                    + "，整体达成率 " + evaluator.evaluate(sheet.getRow(6).getCell(4)).getNumberValue());
        }
    }

    /** 导入解析：读取导出的达成对比表（先重算公式）。 */
    @Test
    public void importQuarterlySalesAchievement() throws Exception {
        File src = BizKit.outFile("style", "0070_style_quarterly-sales-achievement.xlsx");
        if (!src.exists()) {
            exportQuarterlySalesAchievement();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0070_style_quarterly-sales-achievement-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0070QuarterlySalesAchievementService service = factory.createApi(Style0070QuarterlySalesAchievementService.class);
            List<JQuickRow> rows = service.importQuarterlySalesAchievement("field", "value");

            System.out.println("【场景70】季度销售目标达成对比表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    区域=" + r.get("region") + ", 目标=" + r.get("target")
                        + ", 实际=" + r.get("actual") + ", 差额=" + r.get("difference")
                        + ", 达成率=" + r.get("achievementRate") + ", 评级=" + r.get("rating"));
            }
            // 表头不计入数据行：5 个区域 + 1 行汇总
            Assert.assertEquals("5 个区域 + 1 汇总行", 6, rows.size());
        }
    }

    /** 构造一条区域达成（a=区域，b=季度目标，c=实际销售，d=目标差额，e=达成率，f=评级）。 */
    private static JQuickRow region(String region, double target, double actual, String rating) {
        JQuickRow row = new JQuickRow();
        row.put("a", region);
        row.put("b", target);
        row.put("c", actual);
        row.put("d", null);
        row.put("e", null);
        row.put("f", rating);
        return row;
    }
}
