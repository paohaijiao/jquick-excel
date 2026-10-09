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
 * 场景 8：产品销量 Top N 排行（🟢 纯 XML 无代码）。
 *
 * <p>业务：销量前 N 名的产品排行，逐行给出销售占比，末尾一行合计。
 * 统计逻辑全部写在 {@code jquick/biz/formulas/0008_formulas_product-sales-topn.xml}：
 * <ul>
 *   <li>FORMAT：销量 {@code #,##0}，销售额 {@code #,##0.00}，销售占比 {@code 0.00%}；</li>
 *   <li>FORMULAS：逐行占比 {@code D2/SUM(D2:D6)}，合计行 {@code SUM} 汇总销量 / 销售额 / 占比；</li>
 *   <li>STYLE：合计行加粗 + 浅黄高亮。</li>
 * </ul>
 * 排名为 Java 构造的静态序号（非聚合）。产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0008ProductSalesTopNDemo {

    private static final String XML = "jquick/biz/formulas/0008_formulas_product-sales-topn.xml";

    /** 导出：Top 5 产品 + 1 行合计，回读校验占比公式与合计行样式。 */
    @Test
    public void exportProductSalesTopN() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail(1, "智能手表", 1200, 360000));
        rows.add(detail(2, "无线耳机", 980, 294000));
        rows.add(detail(3, "蓝牙音箱", 760, 228000));
        rows.add(detail(4, "平板电脑", 520, 208000));
        rows.add(detail(5, "智能手环", 410, 123000));
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0008_formulas_product-sales-topn.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("charcoal", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0008ProductSalesTopNService service = factory.createApi(Formulas0008ProductSalesTopNService.class);
            service.exportProductSalesTopN("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("产品销量TopN");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 逐行销售占比 = 本行销售额 / 全部销售额
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/SUM(D2:D6)", e2.getCellFormula());
            Assert.assertEquals(360000.0 / 1213000.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 合计行汇总销量与销售额
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(3870.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals(1213000.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景8】产品销量 Top N 排行导出: " + out.getAbsolutePath());
        }
    }

    /** 导入解析：读取导出的产品销量排行（先重算公式）。 */
    @Test
    public void importProductSalesTopN() throws Exception {
        File src = BizKit.outFile("formulas", "0008_formulas_product-sales-topn.xlsx");
        if (!src.exists()) {
            exportProductSalesTopN();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0008_formulas_product-sales-topn-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0008ProductSalesTopNService service = factory.createApi(Formulas0008ProductSalesTopNService.class);
            List<JQuickRow> rows = service.importProductSalesTopN("field", "value");

            System.out.println("【场景8】产品销量 Top N 导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    排名=" + r.get("rank") + ", 产品=" + r.get("name")
                        + ", 销量=" + r.get("qty") + ", 销售额=" + r.get("amount") + ", 占比=" + r.get("ratio"));
            }
            Assert.assertEquals("Top 5 明细 + 1 行合计", 6, rows.size());
        }
    }

    /** 构造一条产品销量明细（a=排名，b=产品名称，c=销量，d=销售额；e=销售占比由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(int rank, String name, int qty, double amount) {
        JQuickRow row = new JQuickRow();
        row.put("a", rank);
        row.put("b", name);
        row.put("c", qty);
        row.put("d", amount);
        row.put("e", null);
        return row;
    }
}
