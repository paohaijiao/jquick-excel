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
 * 场景 89：材料领用单套打（单据套打类，🟢 纯 XML）。
 *
 * <p>业务：逐项登记领用材料的规格、数量与单价，算「金额 = 领用数量 × 单价」，
 * 末尾一行汇总领用总数量与总金额。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0050_formulas_material-requisition.xml}：
 * FORMULAS 求逐行金额、SUM 求数量与金额合计，STYLE 给合计行加粗高亮；Java 只构造领用明细。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0050MaterialRequisitionDemo {

    private static final String XML = "jquick/biz/formulas/0050_formulas_material-requisition.xml";

    /** 导出：纯 XML 完成逐行金额、数量与金额合计。 */
    @Test
    public void exportMaterialRequisition() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f 留空，由 FORMULAS 计算
        rows.add(material("钢板", "Q235 δ3mm", 20, "张", 380.00, "机架制作"));
        rows.add(material("角钢", "50×50×5", 40, "根", 65.00, "机架制作"));
        rows.add(material("焊条", "J422 φ3.2", 100, "kg", 8.50, "焊接工艺"));
        rows.add(material("螺栓", "M12×60", 500, "套", 1.20, "整机装配"));
        rows.add(material("防锈漆", "红丹底漆", 30, "kg", 22.00, "表面处理"));
        rows.add(material("砂纸", "240目", 200, "张", 1.50, "表面处理"));
        // 合计占位行：数量与金额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "领用合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0050_formulas_material-requisition.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("roseQuartz", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0050MaterialRequisitionService service = factory.createApi(Formulas0050MaterialRequisitionService.class);
            service.exportMaterialRequisition("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("材料领用单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行金额 = 领用数量 × 单价 = 20 × 380
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2*E2", f2.getCellFormula());
            Assert.assertEquals(7600.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 领用总数量 = SUM(C2:C7) = 890
            Cell c8 = sheet.getRow(7).getCell(2);
            Assert.assertEquals("SUM(C2:C7)", c8.getCellFormula());
            Assert.assertEquals(890.00, evaluator.evaluate(c8).getNumberValue(), 0.0001);
            // 领用总金额 = SUM(F2:F7) = 12610
            Cell f8 = sheet.getRow(7).getCell(5);
            Assert.assertEquals("SUM(F2:F7)", f8.getCellFormula());
            Assert.assertEquals(12610.00, evaluator.evaluate(f8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景89】材料领用单导出: " + out.getAbsolutePath()
                    + "，领用总金额 " + evaluator.evaluate(f8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的材料领用单（先重算公式）。 */
    @Test
    public void importMaterialRequisition() throws Exception {
        File src = BizKit.outFile("formulas", "0050_formulas_material-requisition.xlsx");
        if (!src.exists()) {
            exportMaterialRequisition();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0050_formulas_material-requisition-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0050MaterialRequisitionService service = factory.createApi(Formulas0050MaterialRequisitionService.class);
            List<JQuickRow> rows = service.importMaterialRequisition("field", "value");

            System.out.println("【场景89】材料领用单导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    材料名称=" + r.get("materialName") + ", 规格型号=" + r.get("specification")
                        + ", 领用数量=" + r.get("requisitionQuantity") + ", 计量单位=" + r.get("unit")
                        + ", 单价=" + r.get("unitPrice") + ", 金额=" + r.get("amount")
                        + ", 用途=" + r.get("purpose"));
            }
            // 表头不计入数据行：6 项材料 + 1 行合计
            Assert.assertEquals("6 项材料 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一項领用材料（a=名称，b=规格，c=数量，d=单位，e=单价，f 留空，g=用途）。 */
    private static JQuickRow material(String materialName, String specification, double requisitionQuantity,
                                      String unit, double unitPrice, String purpose) {
        JQuickRow row = new JQuickRow();
        row.put("a", materialName);
        row.put("b", specification);
        row.put("c", requisitionQuantity);
        row.put("d", unit);
        row.put("e", unitPrice);
        row.put("f", null);
        row.put("g", purpose);
        return row;
    }
}
