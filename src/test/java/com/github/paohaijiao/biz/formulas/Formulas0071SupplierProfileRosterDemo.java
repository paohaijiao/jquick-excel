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
 * 场景 71：供应商档案名册（档案名册类，🟢 纯 XML）。
 *
 * <p>业务：登记合格供应商档案（编码 / 名称 / 类别 / 合作年限 / 联系人 / 电话 / 年供货额 / 评级），
 * 末尾一行汇总年供货额。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/formulas/0071_formulas_supplier-profile-roster.xml}：
 * FORMULAS 求年供货额合计，STYLE 给合计行高亮；Java 只构造供应商档案数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0071SupplierProfileRosterDemo {

    private static final String XML = "jquick/biz/formulas/0071_formulas_supplier-profile-roster.xml";

    /** 导出：纯 XML 完成年供货额汇总与合计行高亮。 */
    @Test
    public void exportSupplierProfileRoster() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(supplier("S2601", "华新电子有限公司", "电子元件", 8, "张明", "13800138001", 2400000.00, "战略供应商"));
        rows.add(supplier("S2602", "恒通材料有限公司", "金属材料", 5, "李强", "13900139002", 1800000.00, "核心供应商"));
        rows.add(supplier("S2603", "佳美包装有限公司", "包装耗材", 3, "王芳", "13700137003", 760000.00, "一般供应商"));
        rows.add(supplier("S2604", "中兴物流有限公司", "物流服务", 6, "赵敏", "13600136004", 540000.00, "核心供应商"));
        rows.add(supplier("S2605", "新元五金有限公司", "五金配件", 2, "陈杰", "13500135005", 320000.00, "一般供应商"));
        rows.add(supplier("S2606", "天海电子有限公司", "电子元件", 4, "周涛", "13400134006", 460000.00, "一般供应商"));
        // 合计占位行：年供货额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        total.put("h", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0071_formulas_supplier-profile-roster.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("azure", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0071SupplierProfileRosterService service = factory.createApi(Formulas0071SupplierProfileRosterService.class);
            service.exportSupplierProfileRoster("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("供应商档案名册");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 年供货额合计 = SUM(G2:G7)
            Cell g8 = sheet.getRow(7).getCell(6);
            Assert.assertEquals("SUM(G2:G7)", g8.getCellFormula());
            Assert.assertEquals(6280000.00, evaluator.evaluate(g8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景71】供应商档案名册导出: " + out.getAbsolutePath()
                    + "，年供货额合计 " + evaluator.evaluate(g8).getNumberValue());
        }
    }

    /** 导入解析：读取导出的供应商档案名册（先重算公式）。 */
    @Test
    public void importSupplierProfileRoster() throws Exception {
        File src = BizKit.outFile("formulas", "0071_formulas_supplier-profile-roster.xlsx");
        if (!src.exists()) {
            exportSupplierProfileRoster();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0071_formulas_supplier-profile-roster-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0071SupplierProfileRosterService service = factory.createApi(Formulas0071SupplierProfileRosterService.class);
            List<JQuickRow> rows = service.importSupplierProfileRoster("field", "value");

            System.out.println("【场景71】供应商档案名册导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    供应商编码=" + r.get("supplierNo") + ", 名称=" + r.get("supplierName")
                        + ", 类别=" + r.get("category") + ", 合作年限=" + r.get("cooperationYears")
                        + ", 电话=" + r.get("phone") + ", 年供货额=" + r.get("annualAmount")
                        + ", 评级=" + r.get("rating"));
            }
            // 表头不计入数据行：6 家供应商 + 1 行合计
            Assert.assertEquals("6 家供应商 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一家供应商档案（a=编码，b=名称，c=类别，d=合作年限，e=联系人，f=电话，g=年供货额，h=评级）。 */
    private static JQuickRow supplier(String supplierNo, String supplierName, String category, int cooperationYears,
                                      String contact, String phone, double annualAmount, String rating) {
        JQuickRow row = new JQuickRow();
        row.put("a", supplierNo);
        row.put("b", supplierName);
        row.put("c", category);
        row.put("d", cooperationYears);
        row.put("e", contact);
        row.put("f", phone);
        row.put("g", annualAmount);
        row.put("h", rating);
        return row;
    }
}
