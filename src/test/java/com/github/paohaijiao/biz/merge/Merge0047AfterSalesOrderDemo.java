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
 * 场景 47：售后服务工单台账（其他拓展类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按服务工程师归集售后工单，逐行算「服务费 = 服务时长 × 上门费单价」，
 * 每位工程师后跟一行服务费小计，末尾一行给出全部工单合计；工程师列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按服务工程师分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行乘法与小计 / 合计由 {@code jquick/biz/merge/0047_merge_after-sales-order.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0047AfterSalesOrderDemo {

    private static final String XML = "jquick/biz/merge/0047_merge_after-sales-order.xml";

    /** 导出：Java 分组 + 插小计行，工程师列纵向合并，服务费由 XML FORMULAS 完成。 */
    @Test
    public void exportAfterSalesOrder() throws Exception {
        // 1. 构造扁平工单明细（Java 不做任何金额计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("刘工", "SV260301", "恒信商贸", 2.0, 150, 5));
        flat.add(line("刘工", "SV260302", "华越集团", 3.5, 150, 4));
        flat.add(line("陈工", "SV260303", "广发物流", 1.5, 180, 5));
        flat.add(line("陈工", "SV260304", "中远建材", 2.5, 180, 5));
        flat.add(line("陈工", "SV260305", "蓝天科技", 4.0, 180, 3));

        // 2. Java 分组并插入小计 / 合计占位行（服务费留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0047_merge_after-sales-order.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("periwinkle", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0047AfterSalesOrderService service = factory.createApi(Merge0047AfterSalesOrderService.class);
            service.exportAfterSalesOrder("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("售后服务工单台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行服务费 = 服务时长 × 上门费单价 = 2.0 × 150
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(300.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 刘工小计 = SUM(F2:F3) = 300 + 525
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals("SUM(F2:F3)", f4.getCellFormula());
            Assert.assertEquals(825.0, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 陈工小计 = SUM(F5:F7) = 270 + 450 + 720
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F5:F7)", f8.getCellFormula());
            Assert.assertEquals(1440.0, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 合计 = 两个小计相加 = 825 + 1440
            Cell f9 = sheet.getRow(8).getCell(5);
            Assert.assertEquals("F4+F8", f9.getCellFormula());
            Assert.assertEquals(2265.0, evaluator.evaluate(f9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(3).getCell(0).getCellStyle()).getFont().getBold());
            // 工程师列纵向合并：刘工 A2:A3、陈工 A5:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("刘工 A2:A3 应合并", regions.contains(new CellRangeAddress(1, 2, 0, 0)));
            Assert.assertTrue("陈工 A5:A7 应合并", regions.contains(new CellRangeAddress(4, 6, 0, 0)));

            System.out.println("【场景47】售后服务工单台账导出: " + out.getAbsolutePath()
                    + "，服务费合计 " + evaluator.evaluate(f9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的售后服务工单台账（先重算公式）。 */
    @Test
    public void importAfterSalesOrder() throws Exception {
        File src = BizKit.outFile("merge", "0047_merge_after-sales-order.xlsx");
        if (!src.exists()) {
            exportAfterSalesOrder();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0047_merge_after-sales-order-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0047AfterSalesOrderService service = factory.createApi(Merge0047AfterSalesOrderService.class);
            List<JQuickRow> rows = service.importAfterSalesOrder("field", "value");

            System.out.println("【场景47】售后服务工单台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工程师=" + r.get("engineer") + ", 工单=" + r.get("orderNo")
                        + ", 客户=" + r.get("customer") + ", 时长=" + r.get("workHours")
                        + ", 服务费=" + r.get("serviceFee") + ", 满意度=" + r.get("satisfaction"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按服务工程师分组，工程师切换处插入小计行，末尾追加合计行（不做金额计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String engineer = String.valueOf(r.get("a"));
            if (current != null && !current.equals(engineer)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = engineer;
        }
        if (current != null) {
            out.add(subtotalRow(current));
        }
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写工程师与小计文字，服务费留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String engineer) {
        JQuickRow row = new JQuickRow();
        row.put("a", engineer);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }

    /** 构造一条工单明细（a=工程师，b=工单号，c=客户，d=时长，e=单价，g=满意度，f 留空）。 */
    private static JQuickRow line(String engineer, String orderNo, String customer, double workHours, double serviceRate, double satisfaction) {
        JQuickRow row = new JQuickRow();
        row.put("a", engineer);
        row.put("b", orderNo);
        row.put("c", customer);
        row.put("d", workHours);
        row.put("e", serviceRate);
        row.put("f", null);
        row.put("g", satisfaction);
        return row;
    }
}
