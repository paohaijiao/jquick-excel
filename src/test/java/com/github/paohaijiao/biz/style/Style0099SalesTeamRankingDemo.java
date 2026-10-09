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
 * 场景 99：销售团队业绩排行榜（绩效考核类，🟢 纯 XML）。
 *
 * <p>业务：逐销售员登记销售目标与实际业绩，算「完成率 = 实际业绩 / 销售目标」，
 * 末尾一行汇总目标与业绩并给出整体完成率；完成率最低的销售员标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0099_style_sales-team-ranking.xml}：
 * FORMULAS 求逐行完成率、SUM 求目标 / 业绩合计、E7/D7 求整体完成率，
 * STYLE 给合计行高亮、给完成率最低者标红；Java 只构造数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0099SalesTeamRankingDemo {

    private static final String XML = "jquick/biz/style/0099_style_sales-team-ranking.xml";

    /** 导出：纯 XML 完成逐行完成率、目标 / 业绩合计与整体完成率。 */
    @Test
    public void exportSalesTeamRanking() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f 留空，由 FORMULAS 计算
        rows.add(salesperson(1, "张明", "华东", 500000.00, 620000.00, "优秀"));
        rows.add(salesperson(2, "李强", "华南", 400000.00, 460000.00, "优秀"));
        rows.add(salesperson(3, "王芳", "华北", 350000.00, 330000.00, "达标"));
        rows.add(salesperson(4, "赵磊", "西南", 300000.00, 360000.00, "优秀"));
        rows.add(salesperson(5, "陈静", "华中", 250000.00, 200000.00, "待改进"));
        // 合计占位行：目标 / 业绩 / 完成率汇总留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0099_style_sales-team-ranking.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("periwinkle", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0099SalesTeamRankingService service = factory.createApi(Style0099SalesTeamRankingService.class);
            service.exportSalesTeamRanking("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("销售团队业绩排行榜");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行完成率 = 实际业绩 / 销售目标 = 620000 / 500000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("E2/D2", f2.getCellFormula());
            Assert.assertEquals(1.24, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 完成率最低的「陈静」（Excel 第 6 行）= 200000 / 250000
            Cell f6 = sheet.getRow(5).getCell(5);
            Assert.assertEquals("E6/D6", f6.getCellFormula());
            Assert.assertEquals(0.80, evaluator.evaluate(f6).getNumberValue(), 0.0001);
            // 合计：销售目标 = SUM(D2:D6) = 1800000
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(1800000.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计：实际业绩 = SUM(E2:E6) = 1970000
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("SUM(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(1970000.00, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计：整体完成率 = E7 / D7
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("E7/D7", f7.getCellFormula());
            Assert.assertEquals(1970000.00 / 1800000.00, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 最低完成率标红加粗
            XSSFCellStyle rateStyle = (XSSFCellStyle) f6.getCellStyle();
            Assert.assertTrue("最低完成率应加粗", rateStyle.getFont().getBold());
            Assert.assertEquals("最低完成率应标红", (int) IndexedColors.RED.getIndex(), (int) rateStyle.getFont().getColor());

            System.out.println("【场景99】销售团队业绩排行榜导出: " + out.getAbsolutePath()
                    + "，整体完成率 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的销售团队业绩排行榜（先重算公式）。 */
    @Test
    public void importSalesTeamRanking() throws Exception {
        File src = BizKit.outFile("style", "0099_style_sales-team-ranking.xlsx");
        if (!src.exists()) {
            exportSalesTeamRanking();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0099_style_sales-team-ranking-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0099SalesTeamRankingService service = factory.createApi(Style0099SalesTeamRankingService.class);
            List<JQuickRow> rows = service.importSalesTeamRanking("field", "value");

            System.out.println("【场景99】销售团队业绩排行榜导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    排名=" + r.get("ranking") + ", 销售员=" + r.get("salesperson")
                        + ", 所属区域=" + r.get("region") + ", 销售目标=" + r.get("salesTarget")
                        + ", 实际业绩=" + r.get("actualPerformance") + ", 完成率=" + r.get("completionRate")
                        + ", 业绩等级=" + r.get("performanceGrade"));
            }
            // 表头不计入数据行：5 名销售员 + 1 行合计
            Assert.assertEquals("5 名销售员 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一名销售员（a=排名，b=销售员，c=区域，d=目标，e=业绩，f 留空，g=等级）。 */
    private static JQuickRow salesperson(int ranking, String salesperson, String region,
                                         double salesTarget, double actualPerformance, String performanceGrade) {
        JQuickRow row = new JQuickRow();
        row.put("a", ranking);
        row.put("b", salesperson);
        row.put("c", region);
        row.put("d", salesTarget);
        row.put("e", actualPerformance);
        row.put("f", null);
        row.put("g", performanceGrade);
        return row;
    }
}
