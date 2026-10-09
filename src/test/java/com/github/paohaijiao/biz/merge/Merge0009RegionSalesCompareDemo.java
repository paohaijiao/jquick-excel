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
 * 场景 9：区域销售对比（省 / 市维度切片，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按大区切片列出各城市销售额，每个大区后跟一行小计，末尾一行合计；
 * 大区名称列纵向合并，视觉上把同区城市归为一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按大区分组、插入小计 / 合计占位行、确定合并范围」，
 * 金额求和由 {@code jquick/biz/merge/0009_merge_region-sales-compare.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0009RegionSalesCompareDemo {

    private static final String XML = "jquick/biz/merge/0009_merge_region-sales-compare.xml";

    /** 导出：Java 分组 + 插小计行，大区列纵向合并，求和由 XML FORMULAS 完成。 */
    @Test
    public void exportRegionSales() throws Exception {
        // 1. 构造扁平明细（Java 不做任何金额聚合）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(detail("华东", "上海", 3200000));
        flat.add(detail("华东", "杭州", 2100000));
        flat.add(detail("华东", "南京", 1800000));
        flat.add(detail("华南", "广州", 2600000));
        flat.add(detail("华南", "深圳", 3400000));
        flat.add(detail("华北", "北京", 4200000));
        flat.add(detail("华北", "天津", 1500000));

        // 2. Java 分组并插入小计 / 合计占位行（金额列留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0009_merge_region-sales-compare.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("peach", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0009RegionSalesCompareService service = factory.createApi(Merge0009RegionSalesCompareService.class);
            service.exportRegionSales("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("区域销售对比");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 华东小计 = SUM(C2:C4)
            Cell c5 = sheet.getRow(4).getCell(2);
            Assert.assertEquals("SUM(C2:C4)", c5.getCellFormula());
            Assert.assertEquals(7100000.0, evaluator.evaluate(c5).getNumberValue(), 0.0001);
            // 合计 = 三个小计之和
            Cell c12 = sheet.getRow(11).getCell(2);
            Assert.assertEquals("C5+C8+C11", c12.getCellFormula());
            Assert.assertEquals(18800000.0, evaluator.evaluate(c12).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 大区列纵向合并：A2:A4、A6:A7、A9:A10（0 基行号 1..3 / 5..6 / 8..9）
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 3 个合并区块", 3, regions.size());
            Assert.assertTrue("华东 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("华南 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));
            Assert.assertTrue("华北 A9:A10 应合并", regions.contains(new CellRangeAddress(8, 9, 0, 0)));

            System.out.println("【场景9】区域销售对比导出: " + out.getAbsolutePath() + "，合并区块 " + regions);
        }
    }

    /** 导入解析：读取导出的区域销售对比（先重算公式）。 */
    @Test
    public void importRegionSales() throws Exception {
        File src = BizKit.outFile("merge", "0009_merge_region-sales-compare.xlsx");
        if (!src.exists()) {
            exportRegionSales();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0009_merge_region-sales-compare-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0009RegionSalesCompareService service = factory.createApi(Merge0009RegionSalesCompareService.class);
            List<JQuickRow> rows = service.importRegionSales("field", "value");

            System.out.println("【场景9】区域销售对比导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    大区=" + r.get("region") + ", 城市=" + r.get("city") + ", 销售额=" + r.get("amount"));
            }
            Assert.assertEquals("7 条城市明细 + 3 小计 + 1 合计", 11, rows.size());
        }
    }

    /** 按大区分组，大区切换处插入小计行，末尾追加合计行（不做金额求和）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String region = String.valueOf(r.get("a"));
            if (current != null && !current.equals(region)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = region;
        }
        if (current != null) {
            out.add(subtotalRow(current));
        }
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写大区与小计文字，金额列以空值占位（值由 XML FORMULAS 计算）。 */
    private static JQuickRow subtotalRow(String region) {
        JQuickRow row = new JQuickRow();
        row.put("a", region);
        row.put("b", "小计");
        row.put("c", null);
        return row;
    }

    /** 构造一条城市销售明细（a=大区，b=城市，c=销售额）。 */
    private static JQuickRow detail(String region, String city, double amount) {
        JQuickRow row = new JQuickRow();
        row.put("a", region);
        row.put("b", city);
        row.put("c", amount);
        return row;
    }
}
