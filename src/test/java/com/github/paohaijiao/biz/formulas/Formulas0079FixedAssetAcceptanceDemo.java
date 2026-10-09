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
 * 场景 79：固定资产验收单（单据套打类，🟢 纯 XML）。
 *
 * <p>业务：逐项登记验收资产的规格、数量与单价，算「金额 = 数量 × 单价」，
 * 末尾一行汇总验收总数量与总金额。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0079_formulas_fixed-asset-acceptance.xml}：
 * FORMULAS 求逐行金额、SUM 求数量与金额合计，STYLE 给合计行加粗高亮；Java 只构造验收明细。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0079FixedAssetAcceptanceDemo {

    private static final String XML = "jquick/biz/formulas/0079_formulas_fixed-asset-acceptance.xml";

    /** 导出：纯 XML 完成逐行金额、数量与金额合计。 */
    @Test
    public void exportFixedAssetAcceptance() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // e 留空，由 FORMULAS 计算
        rows.add(asset("笔记本电脑", "ThinkPad X1 Carbon", 5, 12000.00, "研发部", "A区工位"));
        rows.add(asset("打印机", "HP M405dn", 3, 2500.00, "行政部", "办公区"));
        rows.add(asset("办公桌", "1.6m 实木", 10, 1800.00, "行政部", "办公区"));
        rows.add(asset("空调", "格力 3匹柜机", 2, 6500.00, "行政部", "会议室"));
        rows.add(asset("服务器", "戴尔 R750", 1, 48000.00, "研发部", "机房"));
        // 合计占位行：数量与金额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "验收合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0079_formulas_fixed-asset-acceptance.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("wineRed", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0079FixedAssetAcceptanceService service = factory.createApi(Formulas0079FixedAssetAcceptanceService.class);
            service.exportFixedAssetAcceptance("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("固定资产验收单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行金额 = 数量 × 单价 = 5 × 12000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(60000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 验收总数量 = SUM(C2:C6) = 21
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(21.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 验收总金额 = SUM(E2:E6) = 146500
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("SUM(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(146500.00, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景79】固定资产验收单导出: " + out.getAbsolutePath()
                    + "，验收总金额 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的固定资产验收单（先重算公式）。 */
    @Test
    public void importFixedAssetAcceptance() throws Exception {
        File src = BizKit.outFile("formulas", "0079_formulas_fixed-asset-acceptance.xlsx");
        if (!src.exists()) {
            exportFixedAssetAcceptance();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0079_formulas_fixed-asset-acceptance-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0079FixedAssetAcceptanceService service = factory.createApi(Formulas0079FixedAssetAcceptanceService.class);
            List<JQuickRow> rows = service.importFixedAssetAcceptance("field", "value");

            System.out.println("【场景79】固定资产验收单导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    资产名称=" + r.get("assetName") + ", 规格型号=" + r.get("specification")
                        + ", 数量=" + r.get("quantity") + ", 单价=" + r.get("unitPrice")
                        + ", 金额=" + r.get("amount") + ", 使用部门=" + r.get("useDepartment")
                        + ", 存放地点=" + r.get("location"));
            }
            // 表头不计入数据行：5 项资产 + 1 行合计
            Assert.assertEquals("5 项资产 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一项验收资产（a=资产名称，b=规格型号，c=数量，d=单价，e 留空，f=使用部门，g=存放地点）。 */
    private static JQuickRow asset(String assetName, String specification, double quantity,
                                   double unitPrice, String useDepartment, String location) {
        JQuickRow row = new JQuickRow();
        row.put("a", assetName);
        row.put("b", specification);
        row.put("c", quantity);
        row.put("d", unitPrice);
        row.put("e", null);
        row.put("f", useDepartment);
        row.put("g", location);
        return row;
    }
}
