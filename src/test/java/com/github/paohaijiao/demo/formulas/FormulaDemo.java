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
package com.github.paohaijiao.demo.formulas;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类：<b>FORMULAS</b> —— 单元格公式写入。
 *
 * <p>规则文件 {@code demo/formulas/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory}，不使用任何自封装方法。
 * 覆盖三类目标：{@code E2:'...'} 单格、{@code ROW 6:'...'} 整行、{@code COL F:'...'} 整列。
 * 写公式命中 {@code needsRandomRowAccess} 会强制关闭 SXSSF 流式。产物目录：{@code D:\test\excel}。
 */
public class FormulaDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/formulas/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导出：单格 SUM + 整行公式 + 整列公式，回读校验公式文本、目标列位与计算结果。 */
    @Test
    public void exportFormula() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "项目A");
        r1.put("b", 10);
        r1.put("c", 20);
        r1.put("d", 30);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "项目B");
        r2.put("b", 40);
        r2.put("c", 50);
        r2.put("d", 60);
        rows.add(r2);
        JQuickRow r3 = new JQuickRow();
        r3.put("a", "项目C");
        r3.put("b", 70);
        r3.put("c", 80);
        r3.put("d", 90);
        rows.add(r3);

        File out = new File(OUT_DIR, "formulas-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            FormulaService service = factory.createApi(FormulaService.class);
            service.exportFormula("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("公式");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 单元格目标：E2 = SUM(B2:D2)
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals(CellType.FORMULA, e2.getCellType());
            Assert.assertEquals("SUM(B2:D2)", e2.getCellFormula());
            Assert.assertEquals(60.0, evaluator.evaluate(e2).getNumberValue(), 0.0001);

            // 整行目标：ROW 6 横向铺满映射列，A6 = SUM(B2:D4)
            Cell a6 = sheet.getRow(5).getCell(0);
            Assert.assertEquals(CellType.FORMULA, a6.getCellType());
            Assert.assertEquals("SUM(B2:D4)", a6.getCellFormula());
            Assert.assertEquals(450.0, evaluator.evaluate(a6).getNumberValue(), 0.0001);

            // 整列目标：COL F 写入 F 列（不是 G 列），F2 = SUM(B2:B4)
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals(CellType.FORMULA, f2.getCellType());
            Assert.assertEquals("SUM(B2:B4)", f2.getCellFormula());
            Assert.assertEquals(120.0, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            Assert.assertNull("COL F 不应偏移到 G 列", sheet.getRow(1).getCell(6));

            System.out.println("【FORMULAS】E2=" + e2.getCellFormula()
                    + "，A6=" + a6.getCellFormula()
                    + "，F2=" + f2.getCellFormula());
        }
    }
}
