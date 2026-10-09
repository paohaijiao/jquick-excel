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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 场景 14：银行流水对账单（🟢 纯 XML 无代码 + GRAPH 折线图）。
 *
 * <p>业务：账户流水逐笔登记借方 / 贷方，并滚动计算余额
 * （余额 = 上一行余额 + 本行贷方 - 本行借方），末尾一行合计；附余额趋势折线图。
 * 统计与图表全部写在 {@code jquick/biz/graph/0014_graph_bank-statement.xml}：
 * <ul>
 *   <li>FORMULAS：行内滚动余额引用上一行（{@code E3='E2+D3-C3'}）、合计行 {@code SUM}；</li>
 *   <li>STYLE：合计行加粗高亮；</li>
 *   <li>GRAPH：折线图「余额趋势」，图表数据写入独立的「余额趋势」sheet。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Graph0014BankStatementDemo {

    private static final String XML = "jquick/biz/graph/0014_graph_bank-statement.xml";

    /** 导出：4 笔流水 + 1 行合计 + 折线图，回读校验滚动余额、合计与图表 sheet。 */
    @Test
    public void exportBankStatement() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("2026-09-01", "销售收入", 0.00, 50000.00));
        rows.add(detail("2026-09-05", "采购支出", 20000.00, 0.00));
        rows.add(detail("2026-09-12", "销售收入", 0.00, 35000.00));
        rows.add(detail("2026-09-20", "支付工资", 45000.00, 0.00));
        rows.add(totalRow());

        File out = BizKit.outFile("graph", "0014_graph_bank-statement.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("midnightDark", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0014BankStatementService service = factory.createApi(Graph0014BankStatementService.class);
            service.exportBankStatement("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("银行流水对账单");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行余额 = 贷方 - 借方
            Assert.assertEquals("D2-C2", sheet.getRow(1).getCell(4).getCellFormula());
            Assert.assertEquals(50000.00, evaluator.evaluate(sheet.getRow(1).getCell(4)).getNumberValue(), 0.0001);
            // 滚动余额：引用上一行
            Assert.assertEquals("E2+D3-C3", sheet.getRow(2).getCell(4).getCellFormula());
            Assert.assertEquals(30000.00, evaluator.evaluate(sheet.getRow(2).getCell(4)).getNumberValue(), 0.0001);
            Assert.assertEquals("E4+D5-C5", sheet.getRow(4).getCell(4).getCellFormula());
            Assert.assertEquals(20000.00, evaluator.evaluate(sheet.getRow(4).getCell(4)).getNumberValue(), 0.0001);
            // 合计行：借方合计 / 贷方合计 / 期末余额
            Assert.assertEquals("SUM(C2:C5)", sheet.getRow(5).getCell(2).getCellFormula());
            Assert.assertEquals(65000.00, evaluator.evaluate(sheet.getRow(5).getCell(2)).getNumberValue(), 0.0001);
            Assert.assertEquals("SUM(D2:D5)", sheet.getRow(5).getCell(3).getCellFormula());
            Assert.assertEquals(85000.00, evaluator.evaluate(sheet.getRow(5).getCell(3)).getNumberValue(), 0.0001);
            Assert.assertEquals("D6-C6", sheet.getRow(5).getCell(4).getCellFormula());
            Assert.assertEquals(20000.00, evaluator.evaluate(sheet.getRow(5).getCell(4)).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());
            // 折线图数据落在独立的「余额趋势」sheet
            Assert.assertNotNull("应生成「余额趋势」图表 sheet", wb.getSheet("余额趋势"));

            System.out.println("【场景14】银行流水对账单导出: " + out.getAbsolutePath()
                    + "，sheet 列表=" + wb.getNumberOfSheets() + " 个");
        }
    }

    /** 导入解析：读取导出的银行流水对账单（先重算公式），打印每行字段。 */
    @Test
    public void importBankStatement() throws Exception {
        File src = BizKit.outFile("graph", "0014_graph_bank-statement.xlsx");
        if (!src.exists()) {
            exportBankStatement();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("graph", "0014_graph_bank-statement-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0014BankStatementService service = factory.createApi(Graph0014BankStatementService.class);
            List<JQuickRow> rows = service.importBankStatement("field", "value");

            System.out.println("【场景14】银行流水对账单导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    日期=" + r.get("tradeDate") + ", 摘要=" + r.get("summary")
                        + ", 借方=" + r.get("debit") + ", 贷方=" + r.get("credit") + ", 余额=" + r.get("balance"));
            }
            Assert.assertEquals("4 笔流水 + 1 行合计", 5, rows.size());
        }
    }

    /** 构造一条流水（e=余额由 FORMULAS 滚动计算，占位保列）。 */
    private static JQuickRow detail(String date, String summary, double debit, double credit) throws Exception {
        JQuickRow row = new JQuickRow();
        row.put("a", new SimpleDateFormat("yyyy-MM-dd").parse(date));
        row.put("b", summary);
        row.put("c", debit);
        row.put("d", credit);
        row.put("e", null);
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
        return row;
    }
}
