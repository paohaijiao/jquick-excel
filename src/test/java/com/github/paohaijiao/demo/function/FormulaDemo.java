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
package com.github.paohaijiao.demo.function;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * function 子包 demo：FORMULAS 四种目标，回读单元格公式字符串验证原样写入。
 *
 * <p>独立规则文件：{@code demo/function/jquick-excel.xml}。
 *
 * <p><b>实测注意（3.7.0）：</b>COL 目标在框架内部按 {@code col+1} 落位——
 * DSL 写 COL E，公式实际写进 F 列；遍历行还会包含表头行（F1 也会被写入）。
 */
public class FormulaDemo {

    private static final String XML = "demo/function/jquick-excel.xml";

    /** D5 单元格公式 + ROW 6 整行公式 + COL E 整列公式。 */
    @Test
    public void exportFormulas() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.formulaRows());
        File out = DemoKit.out("function-formulas.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            FunctionService service = DemoKit.exportApi(XML, rows, os, FunctionService.class);
            service.exportFormulas("field", "value");
        }

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("公式");
            // POI 行/列都是 0 基：D5 -> (row 4, col 3)
            Cell d5 = sheet.getRow(4).getCell(3);
            // ROW 6 -> 第 6 行 B 列
            Cell b6 = sheet.getRow(5).getCell(1);
            // COL E 实际落在 F 列（框架按 col+1 写），数据行第 2 行 -> F2
            Cell f2 = sheet.getRow(1).getCell(5);
            System.out.println("D5=" + d5.getCellFormula()
                    + ", B6=" + b6.getCellFormula() + ", F2=" + f2.getCellFormula());
            Assert.assertEquals("SUM(D2:D4)", d5.getCellFormula());
            Assert.assertEquals("SUM(B2:C2)", b6.getCellFormula());
            // 实测（3.7.0）COL 目标按 col+1 落位：写 COL E 实际落在 F 列，
            // 且遍历行包含表头行（F1 也会写入同一条公式）。
            Assert.assertEquals("D2*0.1", f2.getCellFormula());
        }
    }

    /** ROW 5..10：第 5~10 行的映射列都写入同一条公式。 */
    @Test
    public void exportFormulasRowRange() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.formulaRows());
        File out = DemoKit.out("function-row-range.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            FunctionService service = DemoKit.exportApi(XML, rows, os, FunctionService.class);
            service.exportFormulasRowRange("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("公式行区间");
            Row row5 = sheet.getRow(4);
            Assert.assertEquals("SUM(B2:C2)", row5.getCell(1).getCellFormula());
            Assert.assertEquals("SUM(B2:C2)", row5.getCell(2).getCellFormula());
            System.out.println("ROW 5 公式: B5=" + row5.getCell(1).getCellFormula());
        }
    }
}
