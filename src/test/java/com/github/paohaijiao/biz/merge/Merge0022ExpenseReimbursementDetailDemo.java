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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 场景 22：费用报销明细表（费用票据类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：按部门归集员工费用报销明细，每个部门后跟一行金额小计，末尾一行合计；部门名称列纵向合并成一个区块。
 *
 * <p>🟡 标记含义：Java 只负责「按部门分组、插入小计 / 合计占位行、确定合并范围」，
 * 金额小计与合计由 {@code jquick/biz/merge/0022_merge_expense-reimbursement-detail.xml} 的 FORMULAS 计算，
 * 样式与 {@code MERGE} 合并范围由模板声明。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0022ExpenseReimbursementDetailDemo {

    private static final String XML = "jquick/biz/merge/0022_merge_expense-reimbursement-detail.xml";

    /** 导出：Java 分组 + 插小计行，部门列纵向合并，金额统计由 XML FORMULAS 完成。 */
    @Test
    public void exportExpenseReimbursementDetail() throws Exception {
        // 1. 构造扁平报销明细（Java 不做任何金额汇总）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(detail("研发部", "张伟", "差旅费", date(2026, 1, 8), 3200));
        flat.add(detail("研发部", "李娜", "办公用品", date(2026, 1, 12), 850));
        flat.add(detail("研发部", "王强", "培训费", date(2026, 1, 20), 5000));
        flat.add(detail("市场部", "赵敏", "招待费", date(2026, 1, 15), 2800));
        flat.add(detail("市场部", "陈刚", "差旅费", date(2026, 1, 22), 4500));
        flat.add(detail("财务部", "孙丽", "办公用品", date(2026, 1, 9), 600));
        flat.add(detail("财务部", "周杰", "会议费", date(2026, 1, 18), 1500));
        flat.add(detail("财务部", "吴敏", "差旅费", date(2026, 1, 26), 2200));

        // 2. Java 分组并插入小计 / 合计占位行（金额留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0022_merge_expense-reimbursement-detail.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sunsetOrange", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0022ExpenseReimbursementDetailService service = factory.createApi(Merge0022ExpenseReimbursementDetailService.class);
            service.exportExpenseReimbursementDetail("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("费用报销明细表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 研发部小计 = SUM(E2:E4) = 3200 + 850 + 5000 = 9050
            Cell e5 = sheet.getRow(4).getCell(4);
            Assert.assertEquals("SUM(E2:E4)", e5.getCellFormula());
            Assert.assertEquals(9050.0, evaluator.evaluate(e5).getNumberValue(), 0.0001);
            // 合计 = 各部门小计相加 = 9050 + 7300 + 4300
            Cell e13 = sheet.getRow(12).getCell(4);
            Assert.assertEquals("E5+E8+E12", e13.getCellFormula());
            Assert.assertEquals(20650.0, evaluator.evaluate(e13).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());
            // 部门列纵向合并：研发部 A2:A4、市场部 A6:A7、财务部 A9:A11
            List<CellRangeAddress> regions = sheet.getMergedRegions();
            Assert.assertEquals("应有 3 个合并区块", 3, regions.size());
            Assert.assertTrue("研发部 A2:A4 应合并", regions.contains(new CellRangeAddress(1, 3, 0, 0)));
            Assert.assertTrue("市场部 A6:A7 应合并", regions.contains(new CellRangeAddress(5, 6, 0, 0)));
            Assert.assertTrue("财务部 A9:A11 应合并", regions.contains(new CellRangeAddress(8, 10, 0, 0)));

            System.out.println("【场景22】费用报销明细表导出: " + out.getAbsolutePath()
                    + "，合计金额 " + evaluator.evaluate(e13).getNumberValue());
        }
    }

    /** 导入解析：读取导出的费用报销明细表（先重算公式）。 */
    @Test
    public void importExpenseReimbursementDetail() throws Exception {
        File src = BizKit.outFile("merge", "0022_merge_expense-reimbursement-detail.xlsx");
        if (!src.exists()) {
            exportExpenseReimbursementDetail();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0022_merge_expense-reimbursement-detail-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0022ExpenseReimbursementDetailService service = factory.createApi(Merge0022ExpenseReimbursementDetailService.class);
            List<JQuickRow> rows = service.importExpenseReimbursementDetail("field", "value");

            System.out.println("【场景22】费用报销明细表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    部门=" + r.get("dept") + ", 报销人=" + r.get("applicant")
                        + ", 类型=" + r.get("expenseType") + ", 金额=" + r.get("amount"));
            }
            // 表头不计入数据行：8 笔明细 + 3 行小计 + 1 行合计
            Assert.assertEquals("8 笔明细 + 3 小计 + 1 合计", 12, rows.size());
        }
    }

    /** 按部门分组，部门切换处插入小计行，末尾追加合计行（不做金额汇总）。 */
    private static List<JQuickRow> groupWithSubtotal(List<JQuickRow> flat) {
        List<JQuickRow> out = new ArrayList<>();
        String current = null;
        for (JQuickRow r : flat) {
            String dept = String.valueOf(r.get("a"));
            if (current != null && !current.equals(dept)) {
                out.add(subtotalRow(current));
            }
            out.add(r);
            current = dept;
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
        out.add(total);
        return out;
    }

    /** 小计占位行：只写部门与小计文字，金额留空由 FORMULAS 计算。 */
    private static JQuickRow subtotalRow(String dept) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", "小计");
        row.put("c", null);
        row.put("d", null);
        row.put("e", null);
        return row;
    }

    /** 构造一条报销明细（a=部门，b=报销人，c=费用类型，d=申请日期，e=金额）。 */
    private static JQuickRow detail(String dept, String applicant, String type, Date applyDate, double amount) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", applicant);
        row.put("c", type);
        row.put("d", applyDate);
        row.put("e", amount);
        return row;
    }

    /** 构造一个「年-月-日」日期（月份从 1 开始）。 */
    private static Date date(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
