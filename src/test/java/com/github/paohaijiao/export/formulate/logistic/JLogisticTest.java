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
package com.github.paohaijiao.export.formulate.logistic;

import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import com.github.paohaijiao.formula.impl.judge.JAndFormula;
import com.github.paohaijiao.formula.impl.judge.JIfFormula;
import com.github.paohaijiao.formula.impl.judge.JORFormula;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * packageName com.github.paohaijiao.export.formulate.logistic
 *
 * @author Martin
 * @version 1.0.0
 * @className JLogisticTest
 * @date 2025/7/5
 * @description
 */
public class JLogisticTest {
    private static final String TEST_OUTPUT_DIR = "d://test/";

    @Test
    public void testIfFormulaCreation() {
        JIfFormula formula = new JIfFormula("A1>0", "\"Positive\"", "\"Non-positive\"");
        System.out.println(formula.getFormula());
    }

    @Test
    public void testIfFormulaEvaluation() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue(5);  // A1 = 5
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        JIfFormula trueFormula = new JIfFormula("A1>0", "\"Positive\"", "\"Negative\"");
        factory.applyFormula(sheet, 1, 0, trueFormula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        JIfFormula falseFormula = new JIfFormula("A1<0", "\"Positive\"", "\"Negative\"");
        factory.applyFormula(sheet, 2, 0, falseFormula);
        Cell resultCell1 = sheet.getRow(2).getCell(0);
        Object result1 = factory.evaluateFormula(resultCell1);
        System.out.println(result1);
        workbook.write(new FileOutputStream(TEST_OUTPUT_DIR + "/if_test.xlsx"));
    }

    @Test
    public void testNestedIfFormula() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue(75);  // A1 = 75
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        String nestedIf = "IF(A1>=90,\"A\",IF(A1>=80,\"B\",IF(A1>=70,\"C\",\"D\")))";
        JIfFormula formula = new JIfFormula("A1>=90", "\"A\"",
                "IF(A1>=80,\"B\",IF(A1>=70,\"C\",\"D\"))");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(TEST_OUTPUT_DIR + "/nested_if_test.xlsx"));

    }
    @Test
    public void testOrFormulaCreation() {
        JORFormula formula = new JORFormula("A1>10", "B1<5");
        System.out.println(formula.getFormula());
    }



    @Test
    public void testOrFormulaEvaluation() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue(5);  // A1 = 5
        row.createCell(1).setCellValue(8);  // B1 = 8
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        JORFormula trueFormula = new JORFormula("A1>10", "B1<10");
        factory.applyFormula(sheet, 1, 0, trueFormula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        JORFormula falseFormula = new JORFormula("A1>10", "B1<5");
        factory.applyFormula(sheet, 2, 0, falseFormula);
        Cell resultCell1 = sheet.getRow(2).getCell(0);
        Object result1 = factory.evaluateFormula(resultCell1);
        System.out.println(result1);
        workbook.write(new FileOutputStream(TEST_OUTPUT_DIR + "/or_test.xlsx"));
    }

    @Test
    public void testCombinedAndOrFormula() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue(15);  // A1 = 15
        row.createCell(1).setCellValue(3);   // B1 = 3
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        JAndFormula combinedFormula = new JAndFormula(
                "OR(A1>10,B1<2)",
                "A1<20"
        );
        factory.applyFormula(sheet, 1, 0, combinedFormula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(TEST_OUTPUT_DIR + "/combined_and_or_test.xlsx"));
    }
}
