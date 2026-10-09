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
 * 场景 66：材料退货单套打（单据套打类，🟢 纯 XML）。
 *
 * <p>业务：按退货明细登记物料，逐行算「退货金额 = 退货数量 × 单价」，末尾一行汇总退货数量与退货金额。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0066_formulas_material-return.xml}：
 * FORMULAS 求逐行退货金额与两列合计，STYLE 给合计行高亮；Java 只构造退货单据数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0066MaterialReturnDemo {

    private static final String XML = "jquick/biz/formulas/0066_formulas_material-return.xml";

    /** 导出：纯 XML 完成退货金额计算与合计。 */
    @Test
    public void exportMaterialReturn() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f=退货金额 留空，由 FORMULAS 计算
        rows.add(line("M2601", "冷轧钢板", "1.2mm", 200, 4.50, "尺寸偏差"));
        rows.add(line("M2602", "铜芯电缆", "2.5mm²", 150, 12.80, "绝缘不合格"));
        rows.add(line("M2603", "铝合金型材", "6063", 300, 8.20, "表面划伤"));
        rows.add(line("M2604", "不锈钢管", "304", 120, 25.00, "壁厚不足"));
        rows.add(line("M2605", "橡胶密封圈", "DN50", 500, 2.30, "硬度不达标"));
        // 合计占位行：退货数量与退货金额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0066_formulas_material-return.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("navyBlue", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0066MaterialReturnService service = factory.createApi(Formulas0066MaterialReturnService.class);
            service.exportMaterialReturn("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("材料退货单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行退货金额 = 退货数量 × 单价 = 200 × 4.50
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2*E2", f2.getCellFormula());
            Assert.assertEquals(900.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 退货数量合计 = SUM(D2:D6) = 200 + 150 + 300 + 120 + 500
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(1270.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 退货金额合计 = SUM(F2:F6) = 900 + 1920 + 2460 + 3000 + 1150
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("SUM(F2:F6)", f7.getCellFormula());
            Assert.assertEquals(9430.00, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景66】材料退货单导出: " + out.getAbsolutePath()
                    + "，退货金额合计 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的材料退货单（先重算公式）。 */
    @Test
    public void importMaterialReturn() throws Exception {
        File src = BizKit.outFile("formulas", "0066_formulas_material-return.xlsx");
        if (!src.exists()) {
            exportMaterialReturn();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0066_formulas_material-return-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0066MaterialReturnService service = factory.createApi(Formulas0066MaterialReturnService.class);
            List<JQuickRow> rows = service.importMaterialReturn("field", "value");

            System.out.println("【场景66】材料退货单导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    物料编码=" + r.get("materialCode") + ", 物料名称=" + r.get("materialName")
                        + ", 规格=" + r.get("spec") + ", 退货数量=" + r.get("returnQty")
                        + ", 退货金额=" + r.get("returnAmount") + ", 原因=" + r.get("returnReason"));
            }
            // 表头不计入数据行：5 条明细 + 1 行合计
            Assert.assertEquals("5 条明细 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条退货明细（a=物料编码，b=物料名称，c=规格，d=退货数量，e=单价，f=退货金额，g=退货原因）。 */
    private static JQuickRow line(String materialCode, String materialName, String spec,
                                  int returnQty, double unitPrice, String returnReason) {
        JQuickRow row = new JQuickRow();
        row.put("a", materialCode);
        row.put("b", materialName);
        row.put("c", spec);
        row.put("d", returnQty);
        row.put("e", unitPrice);
        row.put("f", null);
        row.put("g", returnReason);
        return row;
    }
}
