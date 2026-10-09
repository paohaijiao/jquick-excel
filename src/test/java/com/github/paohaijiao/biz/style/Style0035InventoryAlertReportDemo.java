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
 * 场景 35：库存预警报表（库存盘点类，🟢 纯 XML）。
 *
 * <p>业务：按物料比对当前库存与安全库存，逐行算「库存差额 = 当前库存 - 安全库存」，
 * 差额为负即触达预警线，末尾一行汇总两列库存数量并给出总差额。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0035_style_inventory-alert-report.xml}：
 * FORMULAS 做逐行相减与合计行汇总，STYLE 给合计行高亮、给低于安全库存的差额标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0035InventoryAlertReportDemo {

    private static final String XML = "jquick/biz/style/0035_style_inventory-alert-report.xml";

    /** 导出：纯 XML 完成逐行差额、合计汇总与预警标红。 */
    @Test
    public void exportInventoryAlert() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(item("M-1001", "内六角螺丝 M6", "一号仓", 800, 500, "库存充足"));
        rows.add(item("M-1002", "深沟球轴承 6204", "一号仓", 120, 300, "低于安全库存"));
        rows.add(item("M-1003", "硅胶密封圈", "二号仓", 2000, 1000, "库存充足"));
        rows.add(item("M-1004", "三相异步电机 1.5kW", "二号仓", 30, 50, "低于安全库存"));
        rows.add(item("M-1005", "PLC 控制板", "三号仓", 450, 200, "库存充足"));
        // 合计占位行：两列库存与差额留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0035_style_inventory-alert-report.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("oceanBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0035InventoryAlertReportService service = factory.createApi(Style0035InventoryAlertReportService.class);
            service.exportInventoryAlert("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("库存预警报表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行差额 = 当前库存 - 安全库存 = 800 - 500
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2-E2", f2.getCellFormula());
            Assert.assertEquals(300.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 低于安全库存：120 - 300 = -180
            Cell f3 = sheet.getRow(2).getCell(5);
            Assert.assertEquals(-180.0, evaluator.evaluate(f3).getNumberValue(), 0.0001);
            // 合计当前库存 SUM(D2:D6) = 3400
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(3400.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 合计差额 = 合计当前库存 - 合计安全库存 = 3400 - 2050
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("D7-E7", f7.getCellFormula());
            Assert.assertEquals(1350.0, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 低于安全库存的两行差额标红
            Assert.assertEquals("低于安全库存应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) f3.getCellStyle()).getFont().getColor());
            Assert.assertEquals("低于安全库存应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) sheet.getRow(4).getCell(5).getCellStyle()).getFont().getColor());

            System.out.println("【场景35】库存预警报表导出: " + out.getAbsolutePath()
                    + "，库存差额合计 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的库存预警报表（先重算公式）。 */
    @Test
    public void importInventoryAlert() throws Exception {
        File src = BizKit.outFile("style", "0035_style_inventory-alert-report.xlsx");
        if (!src.exists()) {
            exportInventoryAlert();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0035_style_inventory-alert-report-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0035InventoryAlertReportService service = factory.createApi(Style0035InventoryAlertReportService.class);
            List<JQuickRow> rows = service.importInventoryAlert("field", "value");

            System.out.println("【场景35】库存预警报表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    物料=" + r.get("materialName") + ", 当前库存=" + r.get("currentStock")
                        + ", 安全库存=" + r.get("safetyStock") + ", 差额=" + r.get("stockDiff") + ", 状态=" + r.get("alertStatus"));
            }
            // 表头不计入数据行：5 项物料 + 1 行合计
            Assert.assertEquals("5 项物料 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条物料行（a=编码，b=名称，c=仓库，d=当前库存，e=安全库存，g=预警状态）。 */
    private static JQuickRow item(String code, String name, String warehouse, double current, double safety, String status) {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", warehouse);
        row.put("d", current);
        row.put("e", safety);
        row.put("f", null);
        row.put("g", status);
        return row;
    }
}
