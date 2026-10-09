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
package com.github.paohaijiao.biz.merge;

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
import org.apache.poi.ss.util.CellRangeAddress;
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
 * 场景 55：门店销售日报汇总（自助报表类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按门店归集各品类当日销售额与销售笔数，每个门店后跟一行小计，末尾一行给出全部门店合计；
 * 门店名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按门店分组、插入小计 / 合计占位行、确定合并范围」，
 * 小计 / 合计由 {@code jquick/biz/merge/0055_merge_store-sales-summary.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0055StoreSalesSummaryDemo {

    private static final String XML = "jquick/biz/merge/0055_merge_store-sales-summary.xml";

    /** 导出：Java 分组 + 插小计行，门店列纵向合并，小计与合计由 XML FORMULAS 完成。 */
    @Test
    public void exportStoreSalesSummary() throws Exception {
        // 1. 构造扁平销售明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("中山路店", "生鲜", 52000.00, 320));
        flat.add(line("中山路店", "日化", 38000.00, 210));
        flat.add(line("中山路店", "家电", 96000.00, 85));
        flat.add(line("滨江店", "生鲜", 61000.00, 380));
        flat.add(line("滨江店", "日化", 42000.00, 240));

        // 2. Java 分组并插入小计 / 合计占位行（金额与笔数留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0055_merge_store-sales-summary.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("crimsonRed", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0055StoreSalesSummaryService service = factory.createApi(Merge0055StoreSalesSummaryService.class);
            service.exportStoreSalesSummary("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("门店销售日报汇总");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首个门店小计 = SUM(C2:C4) = 52000 + 38000 + 96000
            Cell c5 = sheet.getRow(4).getCell(2);
            Assert.assertEquals("SUM(C2:C4)", c5.getCellFormula());
            Assert.assertEquals(186000.00, evaluator.evaluate(c5).getNumberValue(), 0.0001);
            // 第二个门店小计 = SUM(C6:C7) = 61000 + 42000
            Cell c8 = sheet.getRow(7).getCell(2);
            Assert.assertEquals("SUM(C6:C7)", c8.getCellFormula());
            Assert.assertEquals(103000.00, evaluator.evaluate(c8).getNumberValue(), 0.0001);
            // 合计 = 两个小计相加 = 186000 + 103000
            Cell c9 = sheet.getRow(8).getCell(2);
            Assert.assertEquals("C5+C8", c9.getCellFormula());
            Assert.assertEquals(289000.00, evaluator.evaluate(c9).getNumberValue(), 0.0001);
            // 销售笔数合计 = 615 + 620
            Cell d9 = sheet.getRow(8).getCell(3);
            Assert.assertEquals("D5+D8", d9.getCellFormula());
            Assert.assertEquals(1235.00, evaluator.evaluate(d9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 门店列纵向合并：中山路店 A2:A4、滨江店 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("中山路店 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("滨江店 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景55】门店销售日报汇总导出: " + out.getAbsolutePath()
                    + "，销售额合计 " + evaluator.evaluate(c9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的门店销售日报汇总（先重算公式）。 */
    @Test
    public void importStoreSalesSummary() throws Exception {
        File src = BizKit.outFile("merge", "0055_merge_store-sales-summary.xlsx");
        if (!src.exists()) {
            exportStoreSalesSummary();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0055_merge_store-sales-summary-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0055StoreSalesSummaryService service = factory.createApi(Merge0055StoreSalesSummaryService.class);
            List<JQuickRow> rows = service.importStoreSalesSummary("field", "value");

            System.out.println("【场景55】门店销售日报汇总导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    门店=" + r.get("store") + ", 品类=" + r.get("category")
                        + ", 销售额=" + r.get("salesAmount") + ", 笔数=" + r.get("orderCount"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按门店分组，门店切换处插入小计行，末尾追加合计行（不做金额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String store = String.valueOf(r.get("a"));
            if (current != null && !current.equals(store)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = store;
        }
        if (current != null) {
            out.add(subtotalRow(current));
        }
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写门店与小计文字，金额与笔数留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String store) {
        JQuickRow row = new JQuickRow();
        row.put("a", store);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        return row;
    }

    /** 构造一条销售明细（a=门店，b=品类，c=销售额，d=销售笔数，作为分段 SUM 的数据源）。 */
    private static JQuickRow line(String store, String category, double salesAmount, int orderCount) {
        JQuickRow row = new JQuickRow();
        row.put("a", store);
        row.put("b", category);
        row.put("c", salesAmount);
        row.put("d", orderCount);
        return row;
    }
}
