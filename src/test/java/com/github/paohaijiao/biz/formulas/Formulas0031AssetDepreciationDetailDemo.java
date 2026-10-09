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
 * 场景 31：固定资产折旧明细表（台账汇总类，🟢 纯 XML）。
 *
 * <p>业务：按直线法计算固定资产年折旧额与月折旧额，逐行算
 * 「年折旧额 = 原值 × (1 - 残值率) / 使用年限」与「月折旧额 = 年折旧额 / 12」，
 * 末尾一行汇总原值、年折旧与月折旧。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0031_formulas_asset-depreciation-detail.xml}：
 * FORMULAS 做带括号的乘除复合算式与合计行汇总，STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0031AssetDepreciationDetailDemo {

    private static final String XML = "jquick/biz/formulas/0031_formulas_asset-depreciation-detail.xml";

    /** 导出：纯 XML 完成年 / 月折旧额计算与合计行汇总。 */
    @Test
    public void exportAssetDepreciation() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(asset("FA001", "服务器", 80000, 0.05, 5));
        rows.add(asset("FA002", "开发工作站", 45000, 0.05, 3));
        rows.add(asset("FA003", "测试设备", 30000, 0.05, 5));
        rows.add(asset("FA004", "办公家具", 25000, 0.05, 8));
        rows.add(asset("FA005", "激光打印机", 12000, 0.05, 5));
        rows.add(asset("FA006", "生产线设备", 200000, 0.05, 10));
        // 合计占位行：折旧额留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0031_formulas_asset-depreciation-detail.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("amber", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0031AssetDepreciationDetailService service = factory.createApi(Formulas0031AssetDepreciationDetailService.class);
            service.exportAssetDepreciation("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("固定资产折旧明细表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行年折旧额 = 原值 × (1 - 残值率) / 年限 = 80000 × 0.95 / 5
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2*(1-D2)/E2", f2.getCellFormula());
            Assert.assertEquals(15200.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 首行月折旧额 = 年折旧额 / 12
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("F2/12", g2.getCellFormula());
            Assert.assertEquals(15200.0 / 12, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计：原值 SUM(C2:C7) = 392000
            Cell c8 = sheet.getRow(7).getCell(2);
            Assert.assertEquals("SUM(C2:C7)", c8.getCellFormula());
            Assert.assertEquals(392000.0, evaluator.evaluate(c8).getNumberValue(), 0.0001);
            // 合计：年折旧额 SUM(F2:F7) = 59398.75
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F2:F7)", f8.getCellFormula());
            Assert.assertEquals(59398.75, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景31】固定资产折旧明细表导出: " + out.getAbsolutePath()
                    + "，年折旧合计 " + evaluator.evaluate(f8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的固定资产折旧明细表（先重算公式）。 */
    @Test
    public void importAssetDepreciation() throws Exception {
        File src = BizKit.outFile("formulas", "0031_formulas_asset-depreciation-detail.xlsx");
        if (!src.exists()) {
            exportAssetDepreciation();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0031_formulas_asset-depreciation-detail-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0031AssetDepreciationDetailService service = factory.createApi(Formulas0031AssetDepreciationDetailService.class);
            List<JQuickRow> rows = service.importAssetDepreciation("field", "value");

            System.out.println("【场景31】固定资产折旧明细表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    资产=" + r.get("assetName") + ", 原值=" + r.get("originalValue")
                        + ", 年折旧=" + r.get("annualDepreciation") + ", 月折旧=" + r.get("monthlyDepreciation"));
            }
            // 表头不计入数据行：6 项资产 + 1 行合计
            Assert.assertEquals("6 项资产 + 1 合计", 7, rows.size());
        }
    }

    /** 构造一条固定资产（a=编号，b=名称，c=原值，d=残值率，e=使用年限）。 */
    private static JQuickRow asset(String no, String name, double originalValue, double residualRate, int life) {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", name);
        row.put("c", originalValue);
        row.put("d", residualRate);
        row.put("e", life);
        row.put("f", null);
        row.put("g", null);
        return row;
    }
}
