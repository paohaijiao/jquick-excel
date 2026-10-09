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
 * 场景 34：销售订单执行跟踪表（其他拓展类，🟢 纯 XML）。
 *
 * <p>业务：跟踪销售订单的发货执行情况，逐行算「未发货金额 = 订单金额 - 已发货金额」与
 * 「执行率 = 已发货金额 / 订单金额」，末尾一行汇总；仍未发货的订单执行率单元格标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0034_style_sales-order-tracking.xml}：
 * FORMULAS 做逐行减法与除法、合计行汇总，STYLE 给合计行高亮并给未发货订单标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0034SalesOrderTrackingDemo {

    private static final String XML = "jquick/biz/style/0034_style_sales-order-tracking.xml";

    /** 导出：纯 XML 完成未发货金额、执行率、合计行汇总与未发货标红。 */
    @Test
    public void exportSalesOrderTracking() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(order("SO2026001", "华东电子", 500000, 500000, "已完成"));
        rows.add(order("SO2026002", "南方机械", 320000, 200000, "执行中"));
        rows.add(order("SO2026003", "西部建设", 180000, 180000, "已完成"));
        rows.add(order("SO2026004", "北方物流", 260000, 100000, "执行中"));
        // 第 6 行：东尚科技订单尚未发货，执行率 0%，模板中 F6 标红
        rows.add(order("SO2026005", "东尚科技", 400000, 0, "未开始"));
        // 合计占位行：未发货金额、执行率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0034_style_sales-order-tracking.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("denim", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0034SalesOrderTrackingService service = factory.createApi(Style0034SalesOrderTrackingService.class);
            service.exportSalesOrderTracking("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("销售订单执行跟踪");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行未发货金额 = 订单 - 已发货 = 500000 - 500000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(0.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 第3行执行率 = 已发货 / 订单 = 200000 / 320000
            Cell f3 = sheet.getRow(2).getCell(5);
            Assert.assertEquals("D3/C3", f3.getCellFormula());
            Assert.assertEquals(0.625, evaluator.evaluate(f3).getNumberValue(), 0.0001);
            // 合计：订单金额 SUM(C2:C6) = 1660000
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(1660000.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计：未发货金额 = 订单合计 - 已发货合计 = 1660000 - 980000
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("C7-D7", e7.getCellFormula());
            Assert.assertEquals(680000.0, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 整体执行率 = 已发货合计 / 订单合计 = 980000 / 1660000
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("D7/C7", f7.getCellFormula());
            Assert.assertEquals(980000.0 / 1660000.0, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 未发货订单（第6行）执行率字体标红
            XSSFCellStyle pending = (XSSFCellStyle) sheet.getRow(5).getCell(5).getCellStyle();
            Assert.assertEquals("未发货订单执行率应标红", (int) IndexedColors.RED.getIndex(), (int) pending.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, pending.getFont().getBold());
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景34】销售订单执行跟踪表导出: " + out.getAbsolutePath()
                    + "，整体执行率 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的销售订单执行跟踪表（先重算公式）。 */
    @Test
    public void importSalesOrderTracking() throws Exception {
        File src = BizKit.outFile("style", "0034_style_sales-order-tracking.xlsx");
        if (!src.exists()) {
            exportSalesOrderTracking();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0034_style_sales-order-tracking-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0034SalesOrderTrackingService service = factory.createApi(Style0034SalesOrderTrackingService.class);
            List<JQuickRow> rows = service.importSalesOrderTracking("field", "value");

            System.out.println("【场景34】销售订单执行跟踪表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    订单=" + r.get("orderNo") + ", 客户=" + r.get("customer")
                        + ", 订单金额=" + r.get("orderAmount") + ", 执行率=" + r.get("executionRate") + ", 状态=" + r.get("orderStatus"));
            }
            // 表头不计入数据行：5 张订单 + 1 行合计
            Assert.assertEquals("5 张订单 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条销售订单（a=订单号，b=客户，c=订单金额，d=已发货金额，g=订单状态）。 */
    private static JQuickRow order(String orderNo, String customer, double orderAmount, double shipped, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", orderNo);
        row.put("b", customer);
        row.put("c", orderAmount);
        row.put("d", shipped);
        row.put("e", null);
        row.put("f", null);
        row.put("g", status);
        return row;
    }
}
