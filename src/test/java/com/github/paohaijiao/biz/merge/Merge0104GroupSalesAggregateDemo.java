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
 * 场景 104：分组销售聚合（大区切片 + 季度矩阵，🟡 Java 仅分组）。
 *
 * <p>业务：按大区切片列出各门店一至四季度销售额，大区名称列纵向合并成一个区块，
 * 视觉上把同区门店归为一个分组。
 *
 * <p>🟡 标记含义：Java 只负责「按大区排序分组并补齐列」，
 * 数值格式化与 {@code MERGE} 合并范围均由
 * {@code jquick/biz/merge/0104_merge_group-sales-aggregate.xml} 的模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0104GroupSalesAggregateDemo {

    private static final String XML = "jquick/biz/merge/0104_merge_group-sales-aggregate.xml";

    /** 导出：按大区分组建表，大区列纵向合并，回读校验合并区块与表头样式。 */
    @Test
    public void exportGroupSalesAggregate() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(line("华东", "上海店", 400, 205, 210, 190));
        rows.add(line("华东", "杭州店", 250, 230, 200, 260));
        rows.add(line("华南", "广州店", 300, 260, 290, 320));
        rows.add(line("华南", "深圳店", 280, 260, 270, 250));
        rows.add(line("华北", "北京店", 400, 360, 380, 410));
        rows.add(line("华北", "天津店", 360, 340, 350, 370));

        File out = BizKit.outFile("merge", "0104_merge_group-sales-aggregate.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("terracotta", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0104GroupSalesAggregateService service = factory.createApi(Merge0104GroupSalesAggregateService.class);
            service.exportGroupSalesAggregate("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("分组销售聚合");
            // 大区列纵向合并：华东 A2:A3、华南 A4:A5、华北 A6:A7（0 基行号 1..2 / 3..4 / 5..6）
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 3 个合并区块", 3, regions.size());
            Assert.assertTrue("华东 A2:A3 应合并", regions.contains(new CellRangeAddress(1, 2, 0, 0)));
            Assert.assertTrue("华南 A4:A5 应合并", regions.contains(new CellRangeAddress(3, 4, 0, 0)));
            Assert.assertTrue("华北 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));
            // 表头加粗高亮
            Assert.assertTrue("表头应加粗", ((XSSFCellStyle) sheet.getRow(0).getCell(0).getCellStyle()).getFont().getBold());
            // 季度列千分位整数格式
            Assert.assertEquals("#,##0", sheet.getRow(1).getCell(2).getCellStyle().getDataFormatString());

            System.out.println("【场景104】分组销售聚合导出: " + out.getAbsolutePath() + "，合并区块 " + regions);
        }
    }

    /** 导入解析：读取导出的分组销售聚合。 */
    @Test
    public void importGroupSalesAggregate() throws Exception {
        File src = BizKit.outFile("merge", "0104_merge_group-sales-aggregate.xlsx");
        if (!src.exists()) {
            exportGroupSalesAggregate();
        }
        try (InputStream in = new FileInputStream(src)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0104GroupSalesAggregateService service = factory.createApi(Merge0104GroupSalesAggregateService.class);
            List<JQuickRow> rows = service.importGroupSalesAggregate("field", "value");

            System.out.println("【场景104】分组销售聚合导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    大区=" + r.get("region") + ", 门店=" + r.get("store")
                        + ", 一季度=" + r.get("q1") + ", 二季度=" + r.get("q2"));
            }
            Assert.assertEquals(6, rows.size());
            Assert.assertEquals("上海店", rows.get(0).get("store"));
            Assert.assertEquals(400.0, ((Number) rows.get(0).get("q1")).doubleValue(), 0.0001);
        }
    }

    /** 构造一条门店季度明细（a=大区，b=门店，c..f=一至四季度）。 */
    private static JQuickRow line(String region, String store, int q1, int q2, int q3, int q4) {
        JQuickRow row = new JQuickRow();
        row.put("a", region);
        row.put("b", store);
        row.put("c", q1);
        row.put("d", q2);
        row.put("e", q3);
        row.put("f", q4);
        return row;
    }
}
