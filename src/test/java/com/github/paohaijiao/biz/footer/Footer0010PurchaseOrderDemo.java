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
package com.github.paohaijiao.biz.footer;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
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
 * 场景 10：采购订单套打（🟢 纯 XML 无代码）。
 *
 * <p>业务：采购订单逐行「数量 × 单价 = 金额」，再按税率算「含税金额」，末尾一行合计，
 * 单据底部由 FOOTER 生成一行制单 / 审核签字说明。
 * 统计逻辑全部写在 {@code jquick/biz/footer/0010_footer_purchase-order.xml}：
 * <ul>
 *   <li>FORMAT：税率 {@code 0.00%}、金额列 {@code #,##0.00}、数量 {@code 0}；</li>
 *   <li>FORMULAS：行内 {@code C2*D2}、{@code E2*(1+F2)}，合计行 {@code SUM}；</li>
 *   <li>STYLE：合计行加粗 + 浅黄高亮；FOOTER：底部签字说明。</li>
 * </ul>
 *
 * <p><b>FOOTER 落点约定</b>：框架把 FOOTER 写到数据区最后一行并覆盖它，
 * 因此数据里必须预留一行「尾部占位行」，否则会覆盖掉合计行（本类 {@link #footerPlaceholder()}）。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Footer0010PurchaseOrderDemo {

    private static final String XML = "jquick/biz/footer/0010_footer_purchase-order.xml";

    /** 导出：3 条采购明细 + 合计 + 尾部占位行，回读校验公式、样式与页脚。 */
    @Test
    public void exportPurchaseOrder() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("M001", "无线鼠标", 100, 25.50, 0.13));
        rows.add(detail("M002", "机械键盘", 80, 30.00, 0.13));
        rows.add(detail("M003", "显示器", 50, 12.50, 0.09));
        rows.add(totalRow());
        rows.add(footerPlaceholder());

        File out = BizKit.outFile("footer", "0010_footer_purchase-order.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sage", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Footer0010PurchaseOrderService service = factory.createApi(Footer0010PurchaseOrderService.class);
            service.exportPurchaseOrder("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("采购订单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 行内金额 = 数量 × 单价
            Assert.assertEquals("C2*D2", sheet.getRow(1).getCell(4).getCellFormula());
            Assert.assertEquals(2550.00, evaluator.evaluate(sheet.getRow(1).getCell(4)).getNumberValue(), 0.0001);
            // 行内含税金额 = 金额 × (1 + 税率)
            Assert.assertEquals("E2*(1+F2)", sheet.getRow(1).getCell(6).getCellFormula());
            Assert.assertEquals(2881.50, evaluator.evaluate(sheet.getRow(1).getCell(6)).getNumberValue(), 0.0001);
            // 合计行 SUM 汇总
            Assert.assertEquals("SUM(E2:E4)", sheet.getRow(4).getCell(4).getCellFormula());
            Assert.assertEquals(5575.00, evaluator.evaluate(sheet.getRow(4).getCell(4)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(G2:G4)", sheet.getRow(4).getCell(6).getCellFormula());
            Assert.assertEquals(6274.75, evaluator.evaluate(sheet.getRow(4).getCell(6)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 税率百分比格式
            Assert.assertEquals("0.00%", sheet.getRow(1).getCell(5).getCellStyle().getDataFormatString());
            // 页脚签字说明落在最后一行（被 FOOTER 覆盖写入并横向合并）
            String footer = sheet.getRow(5).getCell(0).getStringCellValue();
            Assert.assertTrue("页脚应包含审核人", footer.contains("审核人"));

            System.out.println("【场景10】采购订单套打导出: " + out.getAbsolutePath() + "，页脚=" + footer);
        }
    }

    /** 导入解析：读取导出的采购订单（先重算公式），打印每行字段。 */
    @Test
    public void importPurchaseOrder() throws Exception {
        File src = BizKit.outFile("footer", "0010_footer_purchase-order.xlsx");
        if (!src.exists()) {
            exportPurchaseOrder();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("footer", "0010_footer_purchase-order-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Footer0010PurchaseOrderService service = factory.createApi(Footer0010PurchaseOrderService.class);
            List<JQuickRow> rows = service.importPurchaseOrder("field", "value");

            System.out.println("【场景10】采购订单套打导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    编码=" + r.get("code") + ", 名称=" + r.get("name")
                        + ", 数量=" + r.get("qty") + ", 单价=" + r.get("price")
                        + ", 金额=" + r.get("amount") + ", 含税=" + r.get("amountWithTax"));
            }
            // 3 条明细 + 1 行合计 + 1 行页脚说明
            Assert.assertEquals("3 条明细 + 合计 + 页脚", 5, rows.size());
        }
    }

    /** 构造一条采购明细（e=金额、g=含税金额由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String code, String name, int qty, double price, double taxRate) {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", qty);
        row.put("d", price);
        row.put("e", null);
        row.put("f", taxRate);
        row.put("g", null);
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
        row.put("f", null);
        row.put("g", null);
        return row;
    }

    /** 尾部占位行：承接 FOOTER 页脚，避免页脚覆盖合计行。 */
    private static JQuickRow footerPlaceholder() {
        JQuickRow row = new JQuickRow();
        row.put("a", null);
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        row.put("g", null);
        return row;
    }
}
