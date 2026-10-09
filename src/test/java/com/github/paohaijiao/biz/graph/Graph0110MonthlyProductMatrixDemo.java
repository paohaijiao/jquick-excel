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
 * 场景 110：月度销量矩阵（🟢 纯 XML 无代码 + GRAPH 曲面图）。
 *
 * <p>业务：三个产品一至三月的销量矩阵，末行按月份汇总；附曲面图展示二维（产品 × 月份）分布。
 * 统计与图表全部写在 {@code jquick/biz/graph/0110_graph_monthly-product-matrix.xml}：
 * <ul>
 *   <li>FORMULAS：末行按月份 {@code SUM} 汇总；</li>
 *   <li>STYLE：合计行加粗高亮；</li>
 *   <li>GRAPH：曲面图（SURFACE），图表数据写入独立的「月度销量曲面图」sheet。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Graph0110MonthlyProductMatrixDemo {

    private static final String XML = "jquick/biz/graph/0110_graph_monthly-product-matrix.xml";

    /** 导出：3 个产品 + 1 行合计 + 曲面图，回读校验末行汇总与图表 sheet。 */
    @Test
    public void exportMonthlyProductMatrix() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("产品A", 120, 140, 160));
        rows.add(detail("产品B", 150, 160, 180));
        rows.add(detail("产品C", 180, 200, 220));
        rows.add(totalRow());

        File out = BizKit.outFile("graph", "0110_graph_monthly-product-matrix.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("turquoise", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0110MonthlyProductMatrixService service = factory.createApi(Graph0110MonthlyProductMatrixService.class);
            service.exportMonthlyProductMatrix("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("月度销量矩阵");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 末行按月份汇总
            Assert.assertEquals("SUM(B2:B4)", sheet.getRow(4).getCell(1).getCellFormula());
            Assert.assertEquals(450.0, evaluator.evaluate(sheet.getRow(4).getCell(1)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(D2:D4)", sheet.getRow(4).getCell(3).getCellFormula());
            Assert.assertEquals(560.0, evaluator.evaluate(sheet.getRow(4).getCell(3)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 曲面图数据落在独立的「月度销量曲面图」sheet
            Assert.assertNotNull("应生成「月度销量曲面图」图表 sheet", wb.getSheet("月度销量曲面图"));

            System.out.println("【场景110】月度销量矩阵导出: " + out.getAbsolutePath()
                    + "，sheet 列表=" + wb.getNumberOfSheets() + " 个");
        }
    }

    /** 导入解析：读取导出的月度销量矩阵（先重算公式），打印每行字段。 */
    @Test
    public void importMonthlyProductMatrix() throws Exception {
        File src = BizKit.outFile("graph", "0110_graph_monthly-product-matrix.xlsx");
        if (!src.exists()) {
            exportMonthlyProductMatrix();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("graph", "0110_graph_monthly-product-matrix-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0110MonthlyProductMatrixService service = factory.createApi(Graph0110MonthlyProductMatrixService.class);
            List<JQuickRow> rows = service.importMonthlyProductMatrix("field", "value");

            System.out.println("【场景110】月度销量矩阵导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    产品=" + r.get("product") + ", 1月=" + r.get("jan")
                        + ", 2月=" + r.get("feb") + ", 3月=" + r.get("mar"));
            }
            Assert.assertEquals("3 个产品 + 1 行合计", 4, rows.size());
        }
    }

    /** 构造一条产品明细（三列为直接值，末行汇总由 FORMULAS 计算）。 */
    private static JQuickRow detail(String product, double jan, double feb, double mar) {
        JQuickRow row = new JQuickRow();
        row.put("a", product);
        row.put("b", jan);
        row.put("c", feb);
        row.put("d", mar);
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
