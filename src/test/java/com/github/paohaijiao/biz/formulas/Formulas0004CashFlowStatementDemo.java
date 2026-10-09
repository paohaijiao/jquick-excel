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
 * 场景 4：现金流量表导出（🟢 纯 XML 无代码）。
 *
 * <p>业务：按经营 / 投资 / 筹资三类活动记录现金流入、流出，逐行算「净额 = 流入 - 流出」，末尾合计。
 * 统计逻辑全部写在 {@code jquick/biz/formulas/0004_formulas_cash-flow-statement.xml}：
 * <ul>
 *   <li>FORMAT：流入 / 流出 / 净额 {@code #,##0.00}；</li>
 *   <li>FORMULAS：逐行净额 {@code C2-D2} 自定义表达式，合计行 {@code SUM} 后相减；</li>
 *   <li>STYLE：合计行加粗 + 浅黄高亮。</li>
 * </ul>
 * Java 仅构造模拟数据。产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0004CashFlowStatementDemo {

    private static final String XML = "jquick/biz/formulas/0004_formulas_cash-flow-statement.xml";

    /** 导出：3 条活动现金流 + 1 行合计，回读校验净额公式与合计。 */
    @Test
    public void exportCashFlow() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("经营活动现金净额", "经营", 800000, 500000));
        rows.add(detail("投资活动现金净额", "投资", 200000, 350000));
        rows.add(detail("筹资活动现金净额", "筹资", 500000, 150000));
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0004_formulas_cash-flow-statement.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mahogany", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0004CashFlowStatementService service = factory.createApi(Formulas0004CashFlowStatementService.class);
            service.exportCashFlow("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("现金流量表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 逐行净额 = 流入 - 流出
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("C2-D2", e2.getCellFormula());
            Assert.assertEquals(300000.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 合计行：流入 / 流出 SUM，净额再相减
            Assert.assertEquals("SUM(C2:C4)", sheet.getRow(4).getCell(2).getCellFormula());
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("C5-D5", e5.getCellFormula());
            Assert.assertEquals(500000.00, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景4】现金流量表导出: " + out.getAbsolutePath());
        }
    }

    /** 导入解析：读取导出的现金流量表（先重算公式）。 */
    @Test
    public void importCashFlow() throws Exception {
        File src = BizKit.outFile("formulas", "0004_formulas_cash-flow-statement.xlsx");
        if (!src.exists()) {
            exportCashFlow();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0004_formulas_cash-flow-statement-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0004CashFlowStatementService service = factory.createApi(Formulas0004CashFlowStatementService.class);
            List<JQuickRow> rows = service.importCashFlow("field", "value");

            System.out.println("【场景4】现金流量表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    项目=" + r.get("item") + ", 类别=" + r.get("category")
                        + ", 流入=" + r.get("inflow") + ", 流出=" + r.get("outflow") + ", 净额=" + r.get("net"));
            }
            Assert.assertEquals("3 条明细 + 1 行合计", 4, rows.size());
        }
    }

    /** 构造一条现金流明细（a=项目，b=类别，c=流入，d=流出；e=净额由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String item, String category, double inflow, double outflow) {
        JQuickRow row = new JQuickRow();
        row.put("a", item);
        row.put("b", category);
        row.put("c", inflow);
        row.put("d", outflow);
        row.put("e", null);
        return row;
    }
}
