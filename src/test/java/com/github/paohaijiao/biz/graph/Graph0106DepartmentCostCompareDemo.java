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
package com.github.paohaijiao.biz.graph;

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
 * 场景 106：部门费用对比（🟢 纯 XML 无代码 + GRAPH 三维条形图）。
 *
 * <p>业务：各部门预算与实际费用对照，行末算差异（实际 - 预算），末行汇总；附三维条形图对比预实。
 * 统计与图表全部写在 {@code jquick/biz/graph/0106_graph_department-cost-compare.xml}：
 * <ul>
 *   <li>FORMULAS：行末差异 {@code C-B}、末行预实汇总与差异；</li>
 *   <li>STYLE：合计行加粗高亮；</li>
 *   <li>GRAPH：三维条形图（BAR3D），图表数据写入独立的「部门费用对比图」sheet。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Graph0106DepartmentCostCompareDemo {

    private static final String XML = "jquick/biz/graph/0106_graph_department-cost-compare.xml";

    /** 导出：4 个部门 + 1 行合计 + 三维条形图，回读校验差异、汇总与图表 sheet。 */
    @Test
    public void exportDepartmentCostCompare() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("销售部", 120000, 135000));
        rows.add(detail("研发部", 180000, 165000));
        rows.add(detail("客服部", 90000, 105000));
        rows.add(detail("运营部", 150000, 142000));
        rows.add(totalRow());

        File out = BizKit.outFile("graph", "0106_graph_department-cost-compare.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sunsetOrange", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0106DepartmentCostCompareService service = factory.createApi(Graph0106DepartmentCostCompareService.class);
            service.exportDepartmentCostCompare("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("部门费用对比");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 行末差异 = C - B
            Assert.assertEquals("C2-B2", sheet.getRow(1).getCell(3).getCellFormula());
            Assert.assertEquals(15000.0, evaluator.evaluate(sheet.getRow(1).getCell(3)).getNumberValue(), 0.0001);
            // 末行预实汇总与差异
            Assert.assertEquals("SUM(B2:B5)", sheet.getRow(5).getCell(1).getCellFormula());
            Assert.assertEquals(540000.0, evaluator.evaluate(sheet.getRow(5).getCell(1)).getNumberValue(), 0.0001);
            Assert.assertEquals("C6-B6", sheet.getRow(5).getCell(3).getCellFormula());
            Assert.assertEquals(7000.0, evaluator.evaluate(sheet.getRow(5).getCell(3)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());
            // 三维条形图数据落在独立的「部门费用对比图」sheet
            Assert.assertNotNull("应生成「部门费用对比图」图表 sheet", wb.getSheet("部门费用对比图"));

            System.out.println("【场景106】部门费用对比导出: " + out.getAbsolutePath()
                    + "，sheet 列表=" + wb.getNumberOfSheets() + " 个");
        }
    }

    /** 导入解析：读取导出的部门费用对比（先重算公式），打印每行字段。 */
    @Test
    public void importDepartmentCostCompare() throws Exception {
        File src = BizKit.outFile("graph", "0106_graph_department-cost-compare.xlsx");
        if (!src.exists()) {
            exportDepartmentCostCompare();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("graph", "0106_graph_department-cost-compare-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0106DepartmentCostCompareService service = factory.createApi(Graph0106DepartmentCostCompareService.class);
            List<JQuickRow> rows = service.importDepartmentCostCompare("field", "value");

            System.out.println("【场景106】部门费用对比导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    部门=" + r.get("department") + ", 预算=" + r.get("budget")
                        + ", 实际=" + r.get("actual") + ", 差异=" + r.get("variance"));
            }
            Assert.assertEquals("4 个部门 + 1 行合计", 5, rows.size());
        }
    }

    /** 构造一条部门明细（d=差异由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String department, double budget, double actual) {
        JQuickRow row = new JQuickRow();
        row.put("a", department);
        row.put("b", budget);
        row.put("c", actual);
        row.put("d", null);
        return row;
    }

    /** 合计行：只写「合计」文字，其余列以空值占位（必须补齐全部映射列）。 */
    private static JQuickRow totalRow() {
        JQuickRow row = new JQuickRow();
        row.put("a", "合计");
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        return row;
    }
}
