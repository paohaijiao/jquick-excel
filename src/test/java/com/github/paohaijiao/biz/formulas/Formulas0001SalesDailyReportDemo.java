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
import org.apache.poi.ss.usermodel.CellType;
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
 * 场景 1：销售日报 / 周报 / 月报（🟢 纯 XML 无代码）。
 *
 * <p>业务：某日销售明细，逐行「数量 × 单价 = 金额」，末尾一行合计。
 * 统计逻辑全部写在 {@code jquick/biz/formulas/0001_formulas_sales-daily-report.xml}：
 * <ul>
 *   <li>FORMAT：金额 {@code #,##0.00}、日期 {@code yyyy-MM-dd}、数量 {@code 0}；</li>
 *   <li>FORMULAS：逐行 {@code C2*D2} 自定义表达式，合计行 {@code SUM}；</li>
 *   <li>STYLE：合计行加粗 + 浅黄高亮。</li>
 * </ul>
 * 表头深蓝底白字加粗、隔行浅灰斑马纹由框架默认样式提供。Java 仅构造模拟数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Formulas0001SalesDailyReportDemo {

    private static final String XML = "jquick/biz/formulas/0001_formulas_sales-daily-report.xml";

    /** 导出：写入 3 条销售明细 + 1 行合计，回读校验公式与合计行样式。 */
    @Test
    public void exportSalesDailyReport() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("2026-10-01", "无线鼠标", 100, 25.50));
        rows.add(detail("2026-10-01", "机械键盘", 80, 30.00));
        rows.add(detail("2026-10-01", "显示器", 50, 12.50));
        rows.add(totalRow());

        File out = BizKit.outFile("formulas", "0001_formulas_sales-daily-report.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mintFresh", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0001SalesDailyReportService service = factory.createApi(Formulas0001SalesDailyReportService.class);
            service.exportSalesDailyReport("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("销售日报");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 逐行金额 = 数量 × 单价
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals(CellType.FORMULA, e2.getCellType());
            Assert.assertEquals("C2*D2", e2.getCellFormula());
            Assert.assertEquals(2550.00, evaluator.evaluate(e2).getNumberValue(), 0.0001);

            // 合计行 SUM 汇总
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("SUM(E2:E4)", e5.getCellFormula());
            Assert.assertEquals(5575.00, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 日期格式化 yyyy-MM-dd
            Assert.assertEquals("yyyy-MM-dd", sheet.getRow(1).getCell(0).getCellStyle().getDataFormatString());

            System.out.println("【场景1】销售日报导出: " + out.getAbsolutePath());
        }
    }

    /** 导入解析：读取导出的销售日报（先重算公式），打印每行字段。 */
    @Test
    public void importSalesDailyReport() throws Exception {
        File src = BizKit.outFile("formulas", "0001_formulas_sales-daily-report.xlsx");
        if (!src.exists()) {
            exportSalesDailyReport();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("formulas", "0001_formulas_sales-daily-report-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Formulas0001SalesDailyReportService service = factory.createApi(Formulas0001SalesDailyReportService.class);
            List<JQuickRow> rows = service.importSalesDailyReport("field", "value");

            System.out.println("【场景1】销售日报导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    日期=" + r.get("date") + ", 商品=" + r.get("product")
                        + ", 数量=" + r.get("qty") + ", 单价=" + r.get("price") + ", 金额=" + r.get("amount"));
            }
            Assert.assertEquals("3 条明细 + 1 行合计", 4, rows.size());
        }
    }

    /** 构造一条销售明细（a=日期，b=商品，c=数量，d=单价；e=金额由 FORMULAS 计算，占位保列）。 */
    private static JQuickRow detail(String date, String product, int qty, double price) throws Exception {
        JQuickRow row = new JQuickRow();
        row.put("a", new SimpleDateFormat("yyyy-MM-dd").parse(date));
        row.put("b", product);
        row.put("c", qty);
        row.put("d", price);
        row.put("e", null);
        return row;
    }

    /**
     * 合计行：只写「合计」文字，其余列以空值占位。
     *
     * <p>必须补齐全部映射列，否则框架写行时只会创建已存在的列，
     * 造成样式/公式定位时单元格空洞（JRowStyleStrategy 遍历时空指针）。
     */
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
