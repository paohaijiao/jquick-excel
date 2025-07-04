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
package com.github.paohaijiao.css;

import com.github.paohaijiao.factory.JExcelFormulaFactory;
import com.github.paohaijiao.formula.impl.JVLookupFormula;
import com.github.paohaijiao.formula.impl.judge.JIfFormula;
import com.github.paohaijiao.formula.impl.math.JAverageFormula;
import com.github.paohaijiao.formula.impl.math.JSumFormula;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class JFormulaTest {

    @Test
    public void fieldMapping1() {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaFactory factory = new JExcelFormulaFactory(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JSumFormula sumFormula = factory.createSumFormula("A1:A10");
        JAverageFormula avgFormula = factory.createAverageFormula("B1:B10");
        JIfFormula ifFormula = factory.createIfFormula("A1>5", "\"Large\"", "\"Small\"");
        JVLookupFormula vlookup = factory.createVLookupFormula("A1", "A1:B10", 2, false);
        factory.applyFormula(sheet, 10, 0, sumFormula);  // A11
        factory.applyFormula(sheet, 10, 1, avgFormula);  // B11
        factory.applyFormula(sheet, "C1", ifFormula);  // C1
        factory.applyFormula(sheet, 0, 3, vlookup);  // D1
        factory.evaluateAll();
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("SUM结果: " + sumResult);
    }

}
