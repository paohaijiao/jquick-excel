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
package com.github.paohaijiao.biz.merge;

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
import org.apache.poi.ss.util.CellRangeAddress;
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
 * 场景 68：合同履行台账（台账汇总类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按合同类型归集各合同金额与已履行金额，逐行算「履行率 = 已履行 / 合同金额」，
 * 每类合同后跟一行小计，末尾一行给出全部合同合计；合同类型列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按合同类型分组、插入小计 / 合计占位行、确定合并范围」，
 * 逐行履行率与小计 / 合计由 {@code jquick/biz/merge/0068_merge_contract-performance-ledger.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0068ContractPerformanceLedgerDemo {

    private static final String XML = "jquick/biz/merge/0068_merge_contract-performance-ledger.xml";

    /** 导出：Java 分组 + 插小计行，合同类型列纵向合并，履行率与小计由 XML FORMULAS 完成。 */
    @Test
    public void exportContractPerformanceLedger() throws Exception {
        // 1. 构造扁平合同明细（Java 不做任何履行率计算）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(line("采购合同", "HT2601", "华新电子", 500000.00, 500000.00));
        flat.add(line("采购合同", "HT2602", "恒通材料", 300000.00, 180000.00));
        flat.add(line("采购合同", "HT2603", "佳美包装", 200000.00, 200000.00));
        flat.add(line("销售合同", "HT2604", "中远集团", 800000.00, 640000.00));
        flat.add(line("销售合同", "HT2605", "城建集团", 600000.00, 600000.00));

        // 2. Java 分组并插入小计 / 合计占位行（履行率留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0068_merge_contract-performance-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("tropicalTeal", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0068ContractPerformanceLedgerService service = factory.createApi(Merge0068ContractPerformanceLedgerService.class);
            service.exportContractPerformanceLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("合同履行台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行履行率 = 已履行 / 合同金额 = 500000 / 500000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("E2/D2", f2.getCellFormula());
            Assert.assertEquals(1.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 采购合同小计 = SUM = 1000000
            Cell d5 = sheet.getRow(4).getCell(3);
            Assert.assertEquals("SUM(D2:D4)", d5.getCellFormula());
            Assert.assertEquals(1000000.00, evaluator.evaluate(d5).getNumberValue(), 0.0001);
            // 销售合同小计已履行 = 640000 + 600000
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals(1240000.00, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 全部合同合计 = 1000000 + 1400000
            Cell d9 = sheet.getRow(8).getCell(3);
            Assert.assertEquals("D5+D8", d9.getCellFormula());
            Assert.assertEquals(2400000.00, evaluator.evaluate(d9).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 合同类型列纵向合并：采购合同 A2:A4、销售合同 A6:A7
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 2 个合并区块", 2, regions.size());
            Assert.assertTrue("采购合同 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("销售合同 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));

            System.out.println("【场景68】合同履行台账导出: " + out.getAbsolutePath()
                    + "，合同金额合计 " + evaluator.evaluate(d9).getNumberValue());
        }
    }

    /** 导入解析：读取导出的合同履行台账（先重算公式）。 */
    @Test
    public void importContractPerformanceLedger() throws Exception {
        File src = BizKit.outFile("merge", "0068_merge_contract-performance-ledger.xlsx");
        if (!src.exists()) {
            exportContractPerformanceLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0068_merge_contract-performance-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0068ContractPerformanceLedgerService service = factory.createApi(Merge0068ContractPerformanceLedgerService.class);
            List<JQuickRow> rows = service.importContractPerformanceLedger("field", "value");

            System.out.println("【场景68】合同履行台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    合同类型=" + r.get("contractType") + ", 合同编号=" + r.get("contractNo")
                        + ", 签约对方=" + r.get("counterparty") + ", 合同金额=" + r.get("contractAmount")
                        + ", 已履行=" + r.get("performedAmount") + ", 履行率=" + r.get("performanceRate"));
            }
            // 表头不计入数据行：5 条明细 + 2 行小计 + 1 行合计
            Assert.assertEquals("5 条明细 + 2 小计 + 1 合计", 8, rows.size());
        }
    }

    /** 按合同类型分组，类型切换处插入小计行，末尾追加合计行（不做履行率计算）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String type = String.valueOf(r.get("a"));
            if (current != null && !current.equals(type)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = type;
        }
        if (current != null) {
            out.add(subtotalRow(current));
        }
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        out.add(total);
        return out;
    }

    /** 小计占位行：只写合同类型与小计文字，金额与履行率留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String contractType) {
        JQuickRow row = new JQuickRow();
        row.put("a", contractType);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        row.put("f", null);
        return row;
    }

    /** 构造一条合同明细（a=合同类型，b=合同编号，c=签约对方，d=合同金额，e=已履行金额，f 留空由 FORMULAS 计算）。 */
    private static JQuickRow line(String contractType, String contractNo, String counterparty,
                                  double contractAmount, double performedAmount) {
        JQuickRow row = new JQuickRow();
        row.put("a", contractType);
        row.put("b", contractNo);
        row.put("c", counterparty);
        row.put("d", contractAmount);
        row.put("e", performedAmount);
        row.put("f", null);
        return row;
    }
}
