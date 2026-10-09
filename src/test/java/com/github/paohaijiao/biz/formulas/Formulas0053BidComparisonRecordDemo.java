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
import java.util.List;

/**
 * 场景 53：招标比价定标记录（数据对比差异类，🟢 纯 XML）。
 *
 * <p>业务：多家投标单位比价，按「技术得分 × 70% + 业绩得分 × 30%」算综合评分，
 * 末尾一行给出平均报价与平均综合评分，报价最高的投标单位标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0053_formulas_bid-comparison-record.xml}：
 * FORMULAS 用带权重算式求综合评分、AVERAGE 求均值，STYLE 给均值行高亮、给最高报价标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0053BidComparisonRecordDemo {

    private static final String XML = "jquick/biz/formulas/0053_formulas_bid-comparison-record.xml";

    /** 导出：纯 XML 完成加权综合评分、均值汇总与最高报价标红。 */
    @Test
    public void exportBidComparisonRecord() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(bidder("甲建工", 1180000.00, 88.0, 90.0, "中标候选"));
        rows.add(bidder("乙工程", 1250000.00, 85.0, 82.0, "未入选"));
        rows.add(bidder("丙建设", 1320000.00, 80.0, 78.0, "未入选"));
        rows.add(bidder("丁安装", 1100000.00, 82.0, 85.0, "备选"));
        rows.add(bidder("戊建筑", 1210000.00, 86.0, 88.0, "中标候选"));
        // 均值占位行：平均报价与平均综合评分留给 FORMULAS
        JQuickRow avg = new JQuickRow();
        avg.put("a", "平均");
        avg.put("b", null);
        avg.put("c", null);
        avg.put("d", null);
        avg.put("e", null);
        avg.put("f", null);
        rows.add(avg);

        File out = BizKit.outFile("formulas", "0053_formulas_bid-comparison-record.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("oliveGreen", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0053BidComparisonRecordService service = factory.createApi(Formulas0053BidComparisonRecordService.class);
            service.exportBidComparisonRecord("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("招标比价定标记录");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行综合评分 = 技术得分 × 0.7 + 业绩得分 × 0.3 = 88 × 0.7 + 90 × 0.3
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*0.7+D2*0.3", e2.getCellFormula());
            Assert.assertEquals(88.6, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 平均报价 = AVERAGE(B2:B6) = 1212000
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("AVERAGE(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(1212000.00, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 平均综合评分 = AVERAGE(E2:E6) = 84.32
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("AVERAGE(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(84.32, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 均值行加粗高亮
            Assert.assertTrue("均值行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 最高报价（丙建设，第 3 家）标红
            Assert.assertEquals("最高报价应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) sheet.getRow(3).getCell(1).getCellStyle()).getFont().getColor());

            System.out.println("【场景53】招标比价定标记录导出: " + out.getAbsolutePath()
                    + "，平均综合评分 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的招标比价定标记录（先重算公式）。 */
    @Test
    public void importBidComparisonRecord() throws Exception {
        File src = BizKit.outFile("formulas", "0053_formulas_bid-comparison-record.xlsx");
        if (!src.exists()) {
            exportBidComparisonRecord();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0053_formulas_bid-comparison-record-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0053BidComparisonRecordService service = factory.createApi(Formulas0053BidComparisonRecordService.class);
            List<JQuickRow> rows = service.importBidComparisonRecord("field", "value");

            System.out.println("【场景53】招标比价定标记录导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    投标单位=" + r.get("bidder") + ", 报价=" + r.get("quote")
                        + ", 技术分=" + r.get("techScore") + ", 业绩分=" + r.get("perfScore")
                        + ", 综合评分=" + r.get("totalScore") + ", 结果=" + r.get("result"));
            }
            // 表头不计入数据行：5 家投标单位 + 1 行均值
            Assert.assertEquals("5 家投标单位 + 1 均值行", 6, rows.size());
        }
    }

    /** 构造一家投标单位（a=单位，b=报价，c=技术分，d=业绩分，f=结果，e 留空由 FORMULAS 计算）。 */
    private static JQuickRow bidder(String bidder, double quote, double techScore, double perfScore, String result) {
        JQuickRow row = new JQuickRow();
        row.put("a", bidder);
        row.put("b", quote);
        row.put("c", techScore);
        row.put("d", perfScore);
        row.put("e", null);
        row.put("f", result);
        return row;
    }
}
