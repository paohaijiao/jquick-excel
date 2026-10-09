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
 * 场景 28：库存库龄分析表（库存盘点类，🟢 纯 XML）。
 *
 * <p>业务：把库存按库龄区间分层，统计各区间物料种类数、库存金额与金额占比，末尾一行汇总；
 * 库龄最长的区间单元格标红，提示呆滞库存风险。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0028_style_inventory-aging-analysis.xml}：
 * FORMULAS 做逐行占比 = 本区间金额 / 库存金额合计，合计行 SUM 汇总；
 * STYLE 给合计行高亮并给呆滞区间标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0028InventoryAgingAnalysisDemo {

    private static final String XML = "jquick/biz/style/0028_style_inventory-aging-analysis.xml";

    /** 导出：纯 XML 完成金额占比、合计行汇总与呆滞区间标红。 */
    @Test
    public void exportInventoryAging() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(bucket("0-30 天", 45, 850000, "低"));
        rows.add(bucket("31-90 天", 28, 420000, "低"));
        rows.add(bucket("91-180 天", 12, 180000, "中"));
        // 第 5 行：180 天以上为呆滞库存，模板中 C5 / D5 标红
        rows.add(bucket("180 天以上", 5, 95000, "高"));
        // 合计占位行：占比留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0028_style_inventory-aging-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("pearl", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0028InventoryAgingAnalysisService service = factory.createApi(Style0028InventoryAgingAnalysisService.class);
            service.exportInventoryAging("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("库存库龄分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首个区间占比 = 本区间金额 / 库存金额合计 = 850000 / 1545000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("C2/C6", d2.getCellFormula());
            Assert.assertEquals(850000.0 / 1545000.0, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 合计：物料种类数 SUM(B2:B5) = 90
            Cell b6 = sheet.getRow(5).getCell(1);
            Assert.assertEquals("SUM(B2:B5)", b6.getCellFormula());
            Assert.assertEquals(90.0, evaluator.evaluate(b6).getNumberValue(), 0.0001);
            // 合计：库存金额 SUM(C2:C5) = 1545000
            Cell c6 = sheet.getRow(5).getCell(2);
            Assert.assertEquals("SUM(C2:C5)", c6.getCellFormula());
            Assert.assertEquals(1545000.0, evaluator.evaluate(c6).getNumberValue(), 0.0001);
            // 合计：占比 SUM(D2:D5) = 1
            Cell d6 = sheet.getRow(5).getCell(3);
            Assert.assertEquals("SUM(D2:D5)", d6.getCellFormula());
            Assert.assertEquals(1.0, evaluator.evaluate(d6).getNumberValue(), 0.0001);
            // 呆滞区间（第5行 180 天以上）库存金额与占比字体标红
            XSSFCellStyle slow = (XSSFCellStyle) sheet.getRow(4).getCell(2).getCellStyle();
            Assert.assertEquals("呆滞区间金额应标红", (int) IndexedColors.RED.getIndex(), (int) slow.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, slow.getFont().getBold());
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景28】库存库龄分析表导出: " + out.getAbsolutePath()
                    + "，库存金额合计 " + evaluator.evaluate(c6).getNumberValue());
        }
    }

    /** 导入解析：读取导出的库存库龄分析表（先重算公式）。 */
    @Test
    public void importInventoryAging() throws Exception {
        File src = BizKit.outFile("style", "0028_style_inventory-aging-analysis.xlsx");
        if (!src.exists()) {
            exportInventoryAging();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0028_style_inventory-aging-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0028InventoryAgingAnalysisService service = factory.createApi(Style0028InventoryAgingAnalysisService.class);
            List<JQuickRow> rows = service.importInventoryAging("field", "value");

            System.out.println("【场景28】库存库龄分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    区间=" + r.get("agingBucket") + ", 种类=" + r.get("skuCount")
                        + ", 金额=" + r.get("stockAmount") + ", 占比=" + r.get("amountRatio"));
            }
            // 表头不计入数据行：4 个库龄区间 + 1 行合计
            Assert.assertEquals("4 个区间 + 1 合计", 5, rows.size());
        }
    }

    /** 构造一条库龄区间（a=区间，b=种类数，c=库存金额，e=风险等级）。 */
    private static JQuickRow bucket(String agingBucket, int skuCount, double amount, String riskLevel) {
        JQuickRow row = new JQuickRow();
        row.put("a", agingBucket);
        row.put("b", skuCount);
        row.put("c", amount);
        row.put("d", null);
        row.put("e", riskLevel);
        return row;
    }
}
