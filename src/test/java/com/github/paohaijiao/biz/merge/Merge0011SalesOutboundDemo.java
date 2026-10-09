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
 * 场景 11：销售出库单套打（🟢 纯 XML 无代码）。
 *
 * <p>业务：一张出库单下列多行商品明细，「数量 × 单价 = 金额」，末尾一行合计。
 * 统计与版式全部写在 {@code jquick/biz/merge/0011_merge_sales-outbound.xml}：
 * <ul>
 *   <li>FORMULAS：行内 {@code C2*D2}，合计行 {@code SUM} 汇总数量与金额；</li>
 *   <li>STYLE：合计行加粗 + 浅黄高亮；</li>
 *   <li>MERGE：合计行 {@code A5:B5} 横向合并「合计」标签。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0011SalesOutboundDemo {

    private static final String XML = "jquick/biz/merge/0011_merge_sales-outbound.xml";

    /** 导出：3 条出库明细 + 1 行合计，回读校验公式、合计行样式与横向合并。 */
    @Test
    public void exportSalesOutbound() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("SO20260901001", "无线鼠标", 100, 25.50));
        rows.add(detail("SO20260901002", "机械键盘", 80, 30.00));
        rows.add(detail("SO20260901003", "显示器", 50, 12.50));
        rows.add(totalRow());

        File out = BizKit.outFile("merge", "0011_merge_sales-outbound.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("jade", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0011SalesOutboundService service = factory.createApi(Merge0011SalesOutboundService.class);
            service.exportSalesOutbound("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("销售出库单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 行内金额 = 数量 × 单价
            Assert.assertEquals("C2*D2", sheet.getRow(1).getCell(4).getCellFormula());
            Assert.assertEquals(2550.00, evaluator.evaluate(sheet.getRow(1).getCell(4)).getNumberValue(), 0.0001);
            // 合计行：数量合计与金额合计
            Assert.assertEquals("SUM(C2:C4)", sheet.getRow(4).getCell(2).getCellFormula());
            Assert.assertEquals(230.00, evaluator.evaluate(sheet.getRow(4).getCell(2)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(E2:E4)", sheet.getRow(4).getCell(4).getCellFormula());
            Assert.assertEquals(5575.00, evaluator.evaluate(sheet.getRow(4).getCell(4)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 合计行 A5:B5 横向合并（0 基行 4、列 0..1）
            Assert.assertTrue("A5:B5 应合并", sheet.getMergedRegions().contains(new CellRangeAddress(4, 4, 0, 1)));

            System.out.println("【场景11】销售出库单套打导出: " + out.getAbsolutePath() + "，合并区块 " + sheet.getMergedRegions());
        }
    }

    /** 导入解析：读取导出的销售出库单（先重算公式），打印每行字段。 */
    @Test
    public void importSalesOutbound() throws Exception {
        File src = BizKit.outFile("merge", "0011_merge_sales-outbound.xlsx");
        if (!src.exists()) {
            exportSalesOutbound();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0011_merge_sales-outbound-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0011SalesOutboundService service = factory.createApi(Merge0011SalesOutboundService.class);
            List<JQuickRow> rows = service.importSalesOutbound("field", "value");

            System.out.println("【场景11】销售出库单套打导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    单号=" + r.get("orderNo") + ", 商品=" + r.get("goods")
                        + ", 数量=" + r.get("qty") + ", 单价=" + r.get("price") + ", 金额=" + r.get("amount"));
            }
            Assert.assertEquals("3 条明细 + 1 行合计", 4, rows.size());
        }
    }

    /** 构造一条出库明细（e=金额由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String orderNo, String goods, int qty, double price) {
        JQuickRow row = new JQuickRow();
        row.put("a", orderNo);
        row.put("b", goods);
        row.put("c", qty);
        row.put("d", price);
        row.put("e", null);
        return row;
    }

    /** 合计行：只写「合计」文字，其余列以空值占位（必须补齐全部映射列）。 */
    private static JQuickRow totalRow() {
        JQuickRow row = new JQuickRow();
        row.put("a", "合计");
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        return row;
    }
}
