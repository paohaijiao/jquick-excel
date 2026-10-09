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
 * 场景 78：成品出厂检验缺陷统计表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>业务：按检验批次登记抽检数量与缺陷数量，逐行算「缺陷率 = 缺陷数量 / 抽检数量」，
 * 末尾一行汇总总抽检量、总缺陷量与整体缺陷率；缺陷率超标的批次标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0078_style_finished-goods-defect.xml}：
 * FORMULAS 求逐行缺陷率、SUM 求各列合计与整体缺陷率，STYLE 给合计行高亮、给超标批次标红；
 * Java 只构造检验数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0078FinishedGoodsDefectDemo {

    private static final String XML = "jquick/biz/style/0078_style_finished-goods-defect.xml";

    /** 导出：纯 XML 完成逐行缺陷率、合计、整体缺陷率与超标标红。 */
    @Test
    public void exportFinishedGoodsDefect() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e 留空，由 FORMULAS 计算
        rows.add(batch("A100", "B2026-001", 500, 5, "合格", "张伟"));
        rows.add(batch("A100", "B2026-002", 600, 12, "合格", "张伟"));
        rows.add(batch("B200", "B2026-003", 400, 36, "不合格", "李娜"));
        rows.add(batch("B200", "B2026-004", 800, 16, "合格", "李娜"));
        rows.add(batch("C300", "B2026-005", 1000, 30, "合格", "王强"));
        rows.add(batch("C300", "B2026-006", 500, 11, "合格", "王强"));
        // 合计占位行：各列合计与整体缺陷率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0078_style_finished-goods-defect.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("skyBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0078FinishedGoodsDefectService service = factory.createApi(Style0078FinishedGoodsDefectService.class);
            service.exportFinishedGoodsDefect("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("成品出厂检验缺陷统计表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首批次缺陷率 = 缺陷数量 / 抽检数量 = 5 / 500
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/C2", e2.getCellFormula());
            Assert.assertEquals(0.01, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 超标批次（第 3 批次，Excel 第 4 行）缺陷率 = 36 / 400
            Cell e4 = sheet.getRow(3).getCell(4);
            Assert.assertEquals("D4/C4", e4.getCellFormula());
            Assert.assertEquals(0.09, evaluator.evaluate(e4).getNumberValue(), 0.0001);
            // 合计：总缺陷数量 = SUM(D2:D7) = 110
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("SUM(D2:D7)", d8.getCellFormula());
            Assert.assertEquals(110.00, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 合计：总抽检数量 = SUM(C2:C7) = 3800
            Cell c8 = sheet.getRow(7).getCell(2);
            Assert.assertEquals(3800.00, evaluator.evaluate(c8).getNumberValue(), 0.0001);
            // 合计：整体缺陷率 = 缺陷合计 / 抽检合计 = 110 / 3800
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals("D8/C8", e8.getCellFormula());
            Assert.assertEquals(110.00 / 3800.00, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 超标批次缺陷率标红加粗
            XSSFCellStyle overStyle = (XSSFCellStyle) e4.getCellStyle();
            Assert.assertTrue("超标缺陷率应加粗", overStyle.getFont().getBold());
            Assert.assertEquals("超标缺陷率应标红", (int) IndexedColors.RED.getIndex(), (int) overStyle.getFont().getColor());

            System.out.println("【场景78】成品出厂检验缺陷统计表导出: " + out.getAbsolutePath()
                    + "，整体缺陷率 " + evaluator.evaluate(e8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的成品出厂检验缺陷统计表（先重算公式）。 */
    @Test
    public void importFinishedGoodsDefect() throws Exception {
        File src = BizKit.outFile("style", "0078_style_finished-goods-defect.xlsx");
        if (!src.exists()) {
            exportFinishedGoodsDefect();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0078_style_finished-goods-defect-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0078FinishedGoodsDefectService service = factory.createApi(Style0078FinishedGoodsDefectService.class);
            List<JQuickRow> rows = service.importFinishedGoodsDefect("field", "value");

            System.out.println("【场景78】成品出厂检验缺陷统计表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    型号=" + r.get("productModel") + ", 批次=" + r.get("inspectionBatch")
                        + ", 抽检数量=" + r.get("inspectionQuantity") + ", 缺陷数量=" + r.get("defectQuantity")
                        + ", 缺陷率=" + r.get("defectRate") + ", 判定=" + r.get("verdict")
                        + ", 检验员=" + r.get("inspector"));
            }
            // 表头不计入数据行：6 个批次 + 1 行合计
            Assert.assertEquals("6 个批次 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一个检验批次（a=型号，b=批次，c=抽检数量，d=缺陷数量，e 留空，f=判定，g=检验员）。 */
    private static JQuickRow batch(String model, String batchNo, double inspectionQuantity,
                                   double defectQuantity, String verdict, String inspector) {
        JQuickRow row = new JQuickRow();
        row.put("a", model);
        row.put("b", batchNo);
        row.put("c", inspectionQuantity);
        row.put("d", defectQuantity);
        row.put("e", null);
        row.put("f", verdict);
        row.put("g", inspector);
        return row;
    }
}
