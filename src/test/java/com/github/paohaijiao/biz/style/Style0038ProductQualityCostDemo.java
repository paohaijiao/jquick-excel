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
 * 场景 38：产品质量成本分析表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>业务：按产品统计检验结果，逐行算「不良率 = 不良数量 / 检验数量」与
 * 「质量成本 = 不良数量 × 单件返工成本」，不良率超标的产品标红预警，末尾一行给出整体不良率。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0038_style_product-quality-cost.xml}：
 * FORMULAS 做逐行除法与乘法、合计行汇总，STYLE 给合计行高亮、给超标产品的不良率标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0038ProductQualityCostDemo {

    private static final String XML = "jquick/biz/style/0038_style_product-quality-cost.xml";

    /** 导出：纯 XML 完成不良率、质量成本、合计汇总与超标标红。 */
    @Test
    public void exportProductQualityCost() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(product("手机外壳", 5000, 120, 8.5, "受控"));
        rows.add(product("显示屏模组", 3000, 240, 25, "超标"));
        rows.add(product("锂电池", 8000, 96, 12, "受控"));
        rows.add(product("连接器", 6000, 180, 5.5, "受控"));
        rows.add(product("摄像头模组", 2500, 75, 18, "受控"));
        // 合计占位行：数量、整体不良率与质量成本留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0038_style_product-quality-cost.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("espresso", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0038ProductQualityCostService service = factory.createApi(Style0038ProductQualityCostService.class);
            service.exportProductQualityCost("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("产品质量成本分析");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行不良率 = 120 / 5000
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals("C2/B2", d2.getCellFormula());
            Assert.assertEquals(0.024, evaluator.evaluate(d2).getNumberValue(), 0.0000001);
            // 首行质量成本 = 120 × 8.5
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2*E2", f2.getCellFormula());
            Assert.assertEquals(1020.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 超标产品不良率 = 240 / 3000
            Cell d3 = sheet.getRow(2).getCell(3);
            Assert.assertEquals(0.08, evaluator.evaluate(d3).getNumberValue(), 0.0000001);
            // 合计检验数量 SUM(B2:B6) = 24500
            Cell b7 = sheet.getRow(6).getCell(1);
            Assert.assertEquals("SUM(B2:B6)", b7.getCellFormula());
            Assert.assertEquals(24500.0, evaluator.evaluate(b7).getNumberValue(), 0.0001);
            // 合计质量成本 SUM(F2:F6) = 10512
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("SUM(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(10512.0, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 超标产品不良率标红
            Assert.assertEquals("超标产品应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) d3.getCellStyle()).getFont().getColor());

            System.out.println("【场景38】产品质量成本分析表导出: " + out.getAbsolutePath()
                    + "，质量成本合计 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的产品质量成本分析表（先重算公式）。 */
    @Test
    public void importProductQualityCost() throws Exception {
        File src = BizKit.outFile("style", "0038_style_product-quality-cost.xlsx");
        if (!src.exists()) {
            exportProductQualityCost();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0038_style_product-quality-cost-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0038ProductQualityCostService service = factory.createApi(Style0038ProductQualityCostService.class);
            List<JQuickRow> rows = service.importProductQualityCost("field", "value");

            System.out.println("【场景38】产品质量成本分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    产品=" + r.get("product") + ", 检验=" + r.get("inspectQty")
                        + ", 不良=" + r.get("defectQty") + ", 不良率=" + r.get("defectRate")
                        + ", 质量成本=" + r.get("qualityCost") + ", 状态=" + r.get("controlStatus"));
            }
            // 表头不计入数据行：5 种产品 + 1 行合计
            Assert.assertEquals("5 种产品 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一个产品行（a=产品名称，b=检验数量，c=不良数量，e=单件返工成本，g=控制状态）。 */
    private static JQuickRow product(String name, double inspectQty, double defectQty, double reworkUnitCost, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", name);
        row.put("b", inspectQty);
        row.put("c", defectQty);
        row.put("d", null);
        row.put("e", reworkUnitCost);
        row.put("f", null);
        row.put("g", status);
        return row;
    }
}
