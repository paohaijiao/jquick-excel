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
 * 场景 6：客户分层报表（🟢 纯 XML 无代码）。
 *
 * <p>业务：A/B/C 分层客户的年度成交额与贡献占比，末尾一行合计。
 * 统计逻辑全部写在 {@code jquick/biz/formulas/0006_formulas_customer-tier-report.xml}：
 * <ul>
 *   <li>FORMAT：成交额 {@code #,##0.00}，贡献占比 {@code 0.00%}；</li>
 *   <li>FORMULAS：逐行占比 {@code D2/SUM(D2:D6)}，合计行 {@code SUM}；</li>
 *   <li>STYLE：合计行加粗 + 浅黄高亮。</li>
 * </ul>
 * Java 仅构造模拟数据。产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0006CustomerTierReportDemo {

    private static final String XML = "jquick/biz/formulas/0006_formulas_customer-tier-report.xml";

    /** 导出：5 个分层客户 + 1 行合计，回读校验占比公式与合计行样式。 */
    @Test
    public void exportCustomerTier() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("C001", "华兴科技", "A", 1200000));
        rows.add(detail("C002", "中远集团", "A", 980000));
        rows.add(detail("C003", "联创电子", "B", 560000));
        rows.add(detail("C004", "恒力机械", "B", 430000));
        rows.add(detail("C005", "小微商贸", "C", 120000));
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        rows.add(total);

        File out = BizKit.outFile("formulas", "0006_formulas_customer-tier-report.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("lavenderPurple", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0006CustomerTierReportService service = factory.createApi(Formulas0006CustomerTierReportService.class);
            service.exportCustomerTier("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("客户分层报表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 逐行贡献占比 = 本行成交额 / 全部成交额
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/SUM(D2:D6)", e2.getCellFormula());
            Assert.assertEquals(1200000.0 / 3290000.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 合计行 SUM 汇总
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(3290000.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 占比合计应为 100%
            Cell e7 = sheet.getRow(6).getCell(4);
            Assert.assertEquals(1.0, evaluator.evaluate(e7).getNumberValue(), 0.0001);
            // 合计行加粗高亮 + 占比列百分比格式
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            Assert.assertEquals("0.00%", sheet.getRow(1).getCell(4).getCellStyle().getDataFormatString());

            System.out.println("【场景6】客户分层报表导出: " + out.getAbsolutePath());
        }
    }

    /** 导入解析：读取导出的客户分层报表（先重算公式）。 */
    @Test
    public void importCustomerTier() throws Exception {
        File src = BizKit.outFile("formulas", "0006_formulas_customer-tier-report.xlsx");
        if (!src.exists()) {
            exportCustomerTier();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0006_formulas_customer-tier-report-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0006CustomerTierReportService service = factory.createApi(Formulas0006CustomerTierReportService.class);
            List<JQuickRow> rows = service.importCustomerTier("field", "value");

            System.out.println("【场景6】客户分层报表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    编码=" + r.get("code") + ", 名称=" + r.get("name")
                        + ", 等级=" + r.get("tier") + ", 成交额=" + r.get("amount") + ", 占比=" + r.get("ratio"));
            }
            Assert.assertEquals("5 条明细 + 1 行合计", 6, rows.size());
        }
    }

    /** 构造一条客户分层明细（a=客户编码，b=客户名称，c=客户等级，d=年度成交额；e=贡献占比由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String code, String name, String tier, double amount) {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", tier);
        row.put("d", amount);
        row.put("e", null);
        return row;
    }
}
