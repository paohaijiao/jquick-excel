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
package com.github.paohaijiao.export.formulate.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class JFormulaMathValueTest {

    private static String fileDir ="d://test";
    @Test
    public void abs() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(- 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((- 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("ABS(-2)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("SUM result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/abs.xlsx"));
    }
    @Test
    public void avg() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("AVERAGE(A1:A10)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("avg result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/avg.xlsx"));
    }
    @Test
    public void count() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("COUNT(A1:A10)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("count result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/count.xlsx"));
    }
    @Test
    public void max() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 9; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("MAX(A1:A10)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("count result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/max.xlsx"));
    }
    @Test
    public void min() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 9; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("MIN(A1:A10)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("min result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/min.xlsx"));
    }
    @Test
    public void power() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 9; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("POWER(2,3)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("power result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/power.xlsx"));
    }
    @Test
    public void rand() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 9; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("RAND()");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("power result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/rand.xlsx"));
    }
    @Test
    public void range() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 9; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("RANK(2,A1:A10,1)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("rank result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/rank.xlsx"));
    }
    @Test
    public void sqrt() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 9; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("SQRT(4)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("rank result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/sqrt.xlsx"));
    }
    @Test
    public void stdev() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 9; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("STDEV(A1:A10)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("rank result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/stdev.xlsx"));
    }

    @Test
    public void sum() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("SUM(A1:A10)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("SUM result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/sum.xlsx"));
    }





}
