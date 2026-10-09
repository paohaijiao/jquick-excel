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
 * 场景 56：采购入库单（单据套打类，🟢 纯 XML）。
 *
 * <p>业务：仓库按采购到货登记入库明细，逐行算「入库金额 = 入库数量 × 采购单价」，末尾一行汇总合计。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/formulas/0056_formulas_purchase-inbound.xml}：
 * FORMULAS 做逐行乘法与合计 SUM，STYLE 给合计行高亮。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0056PurchaseInboundDemo {

    private static final String XML = "jquick/biz/formulas/0056_formulas_purchase-inbound.xml";

    /** 导出：纯 XML 完成入库金额、数量合计与金额合计。 */
    @Test
    public void exportPurchaseInbound() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(line("RM2601", "冷轧板", 100, 3.20, "宝钢供应"));
        rows.add(line("RM2602", "铜线", 200, 12.00, "江铜集团"));
        rows.add(line("RM2603", "塑料粒子", 500, 8.50, "中石化"));
        rows.add(line("RM2604", "轴承钢", 80, 15.00, "兴澄特钢"));
        rows.add(line("RM2605", "铝锭", 300, 18.00, "中国铝业"));
        // 合计占位行：数量与金额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0056_formulas_purchase-inbound.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("cyan", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0056PurchaseInboundService service = factory.createApi(Formulas0056PurchaseInboundService.class);
            service.exportPurchaseInbound("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("采购入库单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行入库金额 = 入库数量 × 采购单价 = 100 × 3.2
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(320.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 入库数量合计 = SUM(C2:C6) = 1180
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(1180.00, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 入库金额合计 = SUM(E2:E6) = 13570
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals("SUM(E2:E6)", e7.getCellFormula());
            Assert.assertEquals(13570.00, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景56】采购入库单导出: " + out.getAbsolutePath()
                    + "，入库金额合计 " + evaluator.evaluate(e7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的采购入库单（先重算公式）。 */
    @Test
    public void importPurchaseInbound() throws Exception {
        File src = BizKit.outFile("formulas", "0056_formulas_purchase-inbound.xlsx");
        if (!src.exists()) {
            exportPurchaseInbound();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0056_formulas_purchase-inbound-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0056PurchaseInboundService service = factory.createApi(Formulas0056PurchaseInboundService.class);
            List<JQuickRow> rows = service.importPurchaseInbound("field", "value");

            System.out.println("【场景56】采购入库单导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    编码=" + r.get("materialCode") + ", 名称=" + r.get("materialName")
                        + ", 数量=" + r.get("quantity") + ", 单价=" + r.get("unitPrice")
                        + ", 金额=" + r.get("amount") + ", 供应商=" + r.get("supplier"));
            }
            // 表头不计入数据行：5 条明细 + 1 行合计
            Assert.assertEquals("5 条明细 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条入库明细（a=编码，b=名称，c=数量，d=单价，f=供应商，e 留空由 FORMULAS 计算）。 */
    private static JQuickRow line(String materialCode, String materialName, int quantity, double unitPrice, String supplier) {
        JQuickRow row = new JQuickRow();
        row.put("a", materialCode);
        row.put("b", materialName);
        row.put("c", quantity);
        row.put("d", unitPrice);
        row.put("e", null);
        row.put("f", supplier);
        return row;
    }
}
