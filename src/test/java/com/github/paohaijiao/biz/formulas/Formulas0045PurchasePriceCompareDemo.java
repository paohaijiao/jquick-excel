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
 * 场景 45：采购价格对比分析表（数据对比差异类，🟢 纯 XML）。
 *
 * <p>业务：按物料比对供应商报价与内部基准价，逐行算「价差 = 报价 - 基准价」与
 * 「价差率 = 价差 / 基准价」，高于基准价的报价标红，末尾一行给出均价与平均价差率。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0045_formulas_purchase-price-compare.xml}：
 * FORMULAS 做逐行减法与除法、均价行 AVERAGE，STYLE 给均价行高亮、给高于基准价的报价标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0045PurchasePriceCompareDemo {

    private static final String XML = "jquick/biz/formulas/0045_formulas_purchase-price-compare.xml";

    /** 导出：纯 XML 完成价差、价差率、均价汇总与高于基准价标红。 */
    @Test
    public void exportPurchasePriceCompare() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(quote("304 不锈钢板", "甲供", 120, 132, "偏高"));
        rows.add(quote("铝型材", "乙供", 85, 80, "偏低"));
        rows.add(quote("轴承钢", "丙供", 60, 66, "偏高"));
        rows.add(quote("铜排", "甲供", 200, 195, "偏低"));
        rows.add(quote("工程塑料", "乙供", 45, 47, "持平"));
        // 均价占位行：两列均价、平均价差与价差率留给 FORMULAS
        JQuickRow avg = new JQuickRow();
        avg.put("a", "均价");
        avg.put("b", null);
        avg.put("c", null);
        avg.put("d", null);
        avg.put("e", null);
        avg.put("f", null);
        avg.put("g", null);
        rows.add(avg);

        File out = BizKit.outFile("formulas", "0045_formulas_purchase-price-compare.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("royalGold", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0045PurchasePriceCompareService service = factory.createApi(Formulas0045PurchasePriceCompareService.class);
            service.exportPurchasePriceCompare("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("采购价格对比分析");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行价差 = 报价 - 基准价 = 132 - 120
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2-C2", e2.getCellFormula());
            Assert.assertEquals(12.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 高于基准价的价差率 = 6 / 60
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals("E4/C4", f4.getCellFormula());
            Assert.assertEquals(0.1, evaluator.evaluate(f4).getNumberValue(), 0.0000001);
            // 均价行基准价 AVERAGE(C2:C6) = 102
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("AVERAGE(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(102.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 均价行报价 AVERAGE(D2:D6) = 104
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("AVERAGE(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(104.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 均价行加粗高亮
            Assert.assertTrue("均价行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 高于基准价的报价标红
            Assert.assertEquals("高于基准价应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) f4.getCellStyle()).getFont().getColor());

            System.out.println("【场景45】采购价格对比分析表导出: " + out.getAbsolutePath()
                    + "，平均价差 " + evaluator.evaluate(sheet.getRow(6).getCell(4)).getNumberValue());
        }
    }

    /** 导入解析：读取导出的采购价格对比分析表（先重算公式）。 */
    @Test
    public void importPurchasePriceCompare() throws Exception {
        File src = BizKit.outFile("formulas", "0045_formulas_purchase-price-compare.xlsx");
        if (!src.exists()) {
            exportPurchasePriceCompare();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0045_formulas_purchase-price-compare-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0045PurchasePriceCompareService service = factory.createApi(Formulas0045PurchasePriceCompareService.class);
            List<JQuickRow> rows = service.importPurchasePriceCompare("field", "value");

            System.out.println("【场景45】采购价格对比分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    物料=" + r.get("materialName") + ", 供应商=" + r.get("supplier")
                        + ", 基准价=" + r.get("basePrice") + ", 报价=" + r.get("quotedPrice")
                        + ", 价差=" + r.get("priceDiff") + ", 价差率=" + r.get("diffRate"));
            }
            // 表头不计入数据行：5 条报价 + 1 行均价
            Assert.assertEquals("5 条报价 + 1 均价行", 6, rows.size());
        }
    }

    /** 构造一条报价行（a=物料，b=供应商，c=基准价，d=报价，g=价格评定）。 */
    private static JQuickRow quote(String materialName, String supplier, double basePrice, double quotedPrice, String rating) {
        JQuickRow row = new JQuickRow();
        row.put("a", materialName);
        row.put("b", supplier);
        row.put("c", basePrice);
        row.put("d", quotedPrice);
        row.put("e", null);
        row.put("f", null);
        row.put("g", rating);
        return row;
    }
}
