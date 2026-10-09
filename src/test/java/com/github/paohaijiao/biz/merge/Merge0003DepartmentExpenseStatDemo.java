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
 * 场景 3：部门费用统计表（🟡 Java 仅分组 + 插小计行）。
 *
 * <p>业务：各部门费用明细，每个部门后跟一行小计，末尾一行合计。
 *
 * <p>🟡 标记含义：Java 只负责「按部门分组、插入小计 / 合计占位行」，
 * 不做任何金额求和；小计与合计的金额由 {@code jquick/biz/merge/0003_merge_department-expense-stat.xml}
 * 的 FORMULAS 计算，样式与格式同样交给模板。
 *
 * <p>Java 侧确定的分组布局（每部门 3 条明细 + 1 行小计）：
 * 表头(1)、研发部(2-4)+小计(5)、市场部(6-8)+小计(9)、财务部(10-12)+小计(13)、合计(14)。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Merge0003DepartmentExpenseStatDemo {

    private static final String XML = "jquick/biz/merge/0003_merge_department-expense-stat.xml";

    /** 导出：Java 分组 + 插小计行，金额求和由 XML FORMULAS 完成。 */
    @Test
    public void exportDepartmentExpense() throws Exception {
        // 1. 构造扁平明细（Java 不做任何金额聚合）
        List<JQuickRow> flat = new ArrayList<>();
        flat.add(detail("研发部", "差旅费", 12000));
        flat.add(detail("研发部", "办公费", 8000));
        flat.add(detail("研发部", "招待费", 5000));
        flat.add(detail("市场部", "广告费", 30000));
        flat.add(detail("市场部", "差旅费", 15000));
        flat.add(detail("市场部", "招待费", 10000));
        flat.add(detail("财务部", "办公费", 6000));
        flat.add(detail("财务部", "审计费", 20000));
        flat.add(detail("财务部", "培训费", 4000));

        // 2. Java 分组并插入小计 / 合计占位行（金额列留给 FORMULAS）
        List<JQuickRow> rows = groupWithSubtotal(flat);

        File out = BizKit.outFile("merge", "0003_merge_department-expense-stat.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("mustard", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0003DepartmentExpenseStatService service = factory.createApi(Merge0003DepartmentExpenseStatService.class);
            service.exportDepartmentExpense("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("部门费用统计");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 研发部小计 = SUM(C2:C4)
            Cell c5 = sheet.getRow(4).getCell(2);
            Assert.assertEquals("SUM(C2:C4)", c5.getCellFormula());
            Assert.assertEquals(25000.00, evaluator.evaluate(c5).getNumberValue(), 0.0001);
            // 市场部小计 = SUM(C6:C8)
            Assert.assertEquals("SUM(C6:C8)", sheet.getRow(8).getCell(2).getCellFormula());
            // 合计 = 三个小计之和
            Cell c14 = sheet.getRow(13).getCell(2);
            Assert.assertEquals("C5+C9+C13", c14.getCellFormula());
            Assert.assertEquals(110000.00, evaluator.evaluate(c14).getNumberValue(), 0.0001);
            // 小计行加粗高亮
            Assert.assertTrue("小计行应加粗", ((XSSFCellStyle) sheet.getRow(4).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景3】部门费用统计表导出: " + out.getAbsolutePath());
        }
    }

    /** 导入解析：读取导出的部门费用统计表（先重算公式）。 */
    @Test
    public void importDepartmentExpense() throws Exception {
        File src = BizKit.outFile("merge", "0003_merge_department-expense-stat.xlsx");
        if (!src.exists()) {
            exportDepartmentExpense();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("merge", "0003_merge_department-expense-stat-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Merge0003DepartmentExpenseStatService service = factory.createApi(Merge0003DepartmentExpenseStatService.class);
            List<JQuickRow> rows = service.importDepartmentExpense("field", "value");

            System.out.println("【场景3】部门费用统计导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    部门=" + r.get("dept") + ", 费用类型=" + r.get("item")
                        + ", 金额=" + r.get("amount"));
            }
            Assert.assertEquals("9 条明细 + 3 小计 + 1 合计", 13, rows.size());
        }
    }

    /** 按部门分组，部门切换处插入小计行，末尾追加合计行（不做金额求和）。 */
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
        out.add(total);
        return out;
    }

    /** 小计占位行：只写部门与小计文字，金额列以空值占位（值由 XML FORMULAS 计算）。 */
    private static JQuickRow subtotalRow(String dept) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", "小计");
        row.put("c", null);
        return row;
    }

    /** 构造一条费用明细（a=部门，b=费用类型，c=金额）。 */
    private static JQuickRow detail(String dept, String item, double amount) {
        JQuickRow row = new JQuickRow();
        row.put("a", dept);
        row.put("b", item);
        row.put("c", amount);
        return row;
    }
}
