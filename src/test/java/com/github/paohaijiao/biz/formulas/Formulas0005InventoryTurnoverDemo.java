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
 * 场景 5：库存周转报表（🟢 纯 XML 无代码）。
 *
 * <p>业务：物料入库次数、出库次数、期末库存、库龄，逐行算「周转率 = 出库次数 / 期末库存」，
 * 末尾合计行汇总次数与库存、求平均库龄。统计逻辑全部写在 {@code jquick/biz/formulas/0005_formulas_inventory-turnover.xml}：
 * <ul>
 *   <li>FORMAT：次数 / 库存 / 库龄 {@code 0}，周转率 {@code 0.00}；</li>
 *   <li>FORMULAS：逐行 {@code D2/E2} 自定义表达式；合计行 {@code SUM} 与 {@code AVERAGE}；
 *       整体周转率 {@code D5/E5}；</li>
 *   <li>STYLE：合计行加粗 + 浅黄高亮。</li>
 * </ul>
 * Java 仅构造模拟数据。产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0005InventoryTurnoverDemo {

    private static final String XML = "jquick/biz/formulas/0005_formulas_inventory-turnover.xml";

    /** 导出：3 条物料周转明细 + 1 行合计，回读校验周转率与汇总公式。 */
    @Test
    public void exportInventoryTurnover() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("M001", "螺丝", 20, 50, 100, 45));
        rows.add(detail("M002", "扳手", 10, 30, 60, 30));
        rows.add(detail("M003", "电缆", 6, 24, 80, 60));
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0005_formulas_inventory-turnover.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("amethyst", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0005InventoryTurnoverService service = factory.createApi(Formulas0005InventoryTurnoverService.class);
            service.exportInventoryTurnover("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("库存周转报表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 逐行周转率 = 出库次数 / 期末库存
            Cell g2 = sheet.getRow(1).getCell(6);
            Assert.assertEquals("D2/E2", g2.getCellFormula());
            Assert.assertEquals(0.50, evaluator.evaluate(g2).getNumberValue(), 0.0001);
            // 合计行：次数 / 库存 SUM、库龄 AVERAGE、整体周转率
            Assert.assertEquals("SUM(C2:C4)", sheet.getRow(4).getCell(2).getCellFormula());
            Cell f5 = sheet.getRow(4).getCell(5);
            Assert.assertEquals("AVERAGE(F2:F4)", f5.getCellFormula());
            Assert.assertEquals(45.00, evaluator.evaluate(f5).getNumberValue(), 0.0001);
            Cell g5 = sheet.getRow(4).getCell(6);
            Assert.assertEquals("D5/E5", g5.getCellFormula());
            Assert.assertEquals(104.0 / 240.0, evaluator.evaluate(g5).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景5】库存周转报表导出: " + out.getAbsolutePath());
        }
    }

    /** 导入解析：读取导出的库存周转报表（先重算公式）。 */
    @Test
    public void importInventoryTurnover() throws Exception {
        File src = BizKit.outFile("formulas", "0005_formulas_inventory-turnover.xlsx");
        if (!src.exists()) {
            exportInventoryTurnover();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0005_formulas_inventory-turnover-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0005InventoryTurnoverService service = factory.createApi(Formulas0005InventoryTurnoverService.class);
            List<JQuickRow> rows = service.importInventoryTurnover("field", "value");

            System.out.println("【场景5】库存周转报表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    物料编码=" + r.get("code") + ", 名称=" + r.get("name")
                        + ", 入库=" + r.get("inCount") + ", 出库=" + r.get("outCount")
                        + ", 库存=" + r.get("stock") + ", 库龄=" + r.get("ageDays")
                        + ", 周转率=" + r.get("turnover"));
            }
            Assert.assertEquals("3 条明细 + 1 行合计", 4, rows.size());
        }
    }

    /** 构造一条物料周转明细（a=编码，b=名称，c=入库次数，d=出库次数，e=期末库存，f=库龄；g=周转率由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String code, String name, int inCount, int outCount, int stock, int ageDays) {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", inCount);
        row.put("d", outCount);
        row.put("e", stock);
        row.put("f", ageDays);
        row.put("g", null);
        return row;
    }
}
