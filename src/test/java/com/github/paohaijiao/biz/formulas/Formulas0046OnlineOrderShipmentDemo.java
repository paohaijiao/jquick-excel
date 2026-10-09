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
 * 场景 46：电商订单发货明细（单据套打类，🟢 纯 XML）。
 *
 * <p>业务：按订单登记商品明细，逐行算「金额 = 数量 × 单价」，末尾一行汇总发货数量与订单金额，
 * 适合直接套打成发货单。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0046_formulas_online-order-shipment.xml}：
 * FORMULAS 做逐行乘法与合计行汇总，STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0046OnlineOrderShipmentDemo {

    private static final String XML = "jquick/biz/formulas/0046_formulas_online-order-shipment.xml";

    /** 导出：纯 XML 完成逐行金额与合计汇总。 */
    @Test
    public void exportOnlineOrderShipment() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(item("SO20260301", "无线鼠标", 2, 129.00, "张伟", "已发货"));
        rows.add(item("SO20260302", "机械键盘", 1, 499.00, "李娜", "已发货"));
        rows.add(item("SO20260303", "显示器", 2, 1299.00, "王强", "待发货"));
        rows.add(item("SO20260304", "移动硬盘", 3, 359.00, "赵敏", "已发货"));
        rows.add(item("SO20260305", "蓝牙耳机", 4, 199.00, "陈刚", "待发货"));
        // 合计占位行：数量与金额留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0046_formulas_online-order-shipment.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("steelBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0046OnlineOrderShipmentService service = factory.createApi(Formulas0046OnlineOrderShipmentService.class);
            service.exportOnlineOrderShipment("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("电商订单发货明细");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行金额 = 数量 × 单价 = 2 × 129
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(258.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 合计发货数量 SUM(C2:C6) = 12
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(12.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计订单金额 SUM(E2:E6) = 5228
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("SUM(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(5228.0, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景46】电商订单发货明细导出: " + out.getAbsolutePath()
                    + "，订单金额合计 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的电商订单发货明细（先重算公式）。 */
    @Test
    public void importOnlineOrderShipment() throws Exception {
        File src = BizKit.outFile("formulas", "0046_formulas_online-order-shipment.xlsx");
        if (!src.exists()) {
            exportOnlineOrderShipment();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0046_formulas_online-order-shipment-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0046OnlineOrderShipmentService service = factory.createApi(Formulas0046OnlineOrderShipmentService.class);
            List<JQuickRow> rows = service.importOnlineOrderShipment("field", "value");

            System.out.println("【场景46】电商订单发货明细导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    订单=" + r.get("orderNo") + ", 商品=" + r.get("productName")
                        + ", 数量=" + r.get("quantity") + ", 单价=" + r.get("unitPrice")
                        + ", 金额=" + r.get("amount") + ", 状态=" + r.get("shipStatus"));
            }
            // 表头不计入数据行：5 条明细 + 1 行合计
            Assert.assertEquals("5 条明细 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条发货明细（a=订单号，b=商品，c=数量，d=单价，f=收货人，g=状态，e 留空）。 */
    private static JQuickRow item(String orderNo, String productName, double quantity, double unitPrice, String receiver, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", orderNo);
        row.put("b", productName);
        row.put("c", quantity);
        row.put("d", unitPrice);
        row.put("e", null);
        row.put("f", receiver);
        row.put("g", status);
        return row;
    }
}
