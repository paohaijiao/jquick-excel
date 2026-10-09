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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景 63：物业水电费收缴台账（费用票据类，🟢 纯 XML）。
 *
 * <p>业务：按房号登记物业费 / 水费 / 电费应收与已收金额，逐行算「欠缴金额 = 应收 - 已收」，
 * 末尾一行汇总应收、已收与欠缴；欠缴户的欠缴金额标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0063_style_utility-fee-ledger.xml}：
 * FORMULAS 求逐行欠缴与三项合计，STYLE 给合计行高亮、给欠缴户标红；Java 只构造收缴数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0063UtilityFeeLedgerDemo {

    private static final String XML = "jquick/biz/style/0063_style_utility-fee-ledger.xml";

    /** 导出：纯 XML 完成欠缴金额计算、合计与欠缴户标红。 */
    @Test
    public void exportUtilityFeeLedger() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f=欠缴金额 留空，由 FORMULAS 计算
        rows.add(line("A-1801", "张伟", "物业费", 2400.00, 2400.00, "已缴清", date(2026, 3, 5)));
        rows.add(line("A-1802", "李娜", "物业费", 2400.00, 1200.00, "部分缴纳", date(2026, 3, 8)));
        rows.add(line("B-0903", "王强", "水费", 680.00, 680.00, "已缴清", date(2026, 3, 10)));
        rows.add(line("B-0904", "赵敏", "电费", 1560.00, 0.00, "欠缴", null));
        rows.add(line("C-0605", "陈杰", "物业费", 3200.00, 3200.00, "已缴清", date(2026, 3, 15)));
        // 合计占位行：应收 / 已收 / 欠缴 合计留给 FORMULAS
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

        File out = BizKit.outFile("style", "0063_style_utility-fee-ledger.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("emerald", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0063UtilityFeeLedgerService service = factory.createApi(Style0063UtilityFeeLedgerService.class);
            service.exportUtilityFeeLedger("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("物业水电费收缴台账");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行欠缴 = 应收 - 已收 = 2400 - 2400
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("D2-E2", f2.getCellFormula());
            Assert.assertEquals(0.00, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 第二行欠缴 = 2400 - 1200
            Cell f3 = sheet.getRow(2).getCell(5);
            Assert.assertEquals(1200.00, evaluator.evaluate(f3).getNumberValue(), 0.0001);
            // 欠缴户（第 4 条明细，Excel 第 5 行）欠缴 = 1560 - 0
            Cell f5 = sheet.getRow(4).getCell(5);
            Assert.assertEquals(1560.00, evaluator.evaluate(f5).getNumberValue(), 0.0001);
            // 应收 / 已收 / 欠缴 合计
            Cell d7 = sheet.getRow(6).getCell(3);
            Cell e7 = sheet.getRow(6).getCell(4);
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(10240.00, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            Assert.assertEquals(7480.00, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            Assert.assertEquals(2760.00, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // 欠缴户欠缴金额标红加粗
            XSSFCellStyle unpaidStyle = (XSSFCellStyle) f5.getCellStyle();
            Assert.assertTrue("欠缴金额应加粗", unpaidStyle.getFont().getBold());
            Assert.assertEquals("欠缴金额应标红", (int) IndexedColors.RED.getIndex(), (int) unpaidStyle.getFont().getColor());

            System.out.println("【场景63】物业水电费收缴台账导出: " + out.getAbsolutePath()
                    + "，欠缴合计 " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的收缴台账（先重算公式）。 */
    @Test
    public void importUtilityFeeLedger() throws Exception {
        File src = BizKit.outFile("style", "0063_style_utility-fee-ledger.xlsx");
        if (!src.exists()) {
            exportUtilityFeeLedger();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0063_style_utility-fee-ledger-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0063UtilityFeeLedgerService service = factory.createApi(Style0063UtilityFeeLedgerService.class);
            List<JQuickRow> rows = service.importUtilityFeeLedger("field", "value");

            System.out.println("【场景63】物业水电费收缴台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    房号=" + r.get("roomNo") + ", 户主=" + r.get("owner")
                        + ", 费用类型=" + r.get("feeType") + ", 应收=" + r.get("receivable")
                        + ", 已收=" + r.get("received") + ", 欠缴=" + r.get("unpaid")
                        + ", 状态=" + r.get("payStatus"));
            }
            // 表头不计入数据行：5 条明细 + 1 行合计
            Assert.assertEquals("5 条明细 + 1 合计行", 6, rows.size());
        }
    }

    /** 构造一条收缴明细（a=房号，b=户主，c=费用类型，d=应收，e=已收，f=欠缴，g=状态，h=缴费日期）。 */
    private static JQuickRow line(String roomNo, String owner, String feeType,
                                  double receivable, double received, String payStatus, Date payDate) {
        JQuickRow row = new JQuickRow();
        row.put("a", roomNo);
        row.put("b", owner);
        row.put("c", feeType);
        row.put("d", receivable);
        row.put("e", received);
        row.put("f", null);
        row.put("g", payStatus);
        row.put("h", payDate);
        return row;
    }

    /** 构造日期（月份按自然月 1-12 传入）。 */
    private static Date date(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
