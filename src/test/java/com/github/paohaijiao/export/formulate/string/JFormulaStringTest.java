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
package com.github.paohaijiao.export.formulate.string;

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

public class JFormulaStringTest {

    private static String fileDir ="d://test";
    @Test
    public void concatenate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("CONCATENATE(A1, B1)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("SUM result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/concatenate.xlsx"));
    }
    @Test
    public void concat() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("CONCAT(A1, B1)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("concat result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/concat.xlsx"));
    }
    @Test
    public void exact() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("EXACT(\"A1\", \"B1\")");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("exact result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/exact.xlsx"));
    }
    @Test
    public void find() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("FIND('\"o\"', '\"Microsoft\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/find.xlsx"));
    }
    @Test
    public void left() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("LEFT('\"sxxasxsaxaso\"', 3)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find result: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/left.xlsx"));
    }
    @Test
    public void length() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("LEN('\"hahah\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find length: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/length.xlsx"));
    }
    @Test
    public void lower() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("LOWER('\"hahah\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find lower: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/lower.xlsx"));
    }

    @Test
    public void mid() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("MID('\"hahah\"',1,2)");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find MID: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/mid.xlsx"));
    }
    @Test
    public void proper() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("PROPER('\"hahah\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find proper: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/proper.xlsx"));
    }
    @Test
    public void replace() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("REPLACE('\"hahah\"',1,2,'\"xixi\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find proper: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/proper.xlsx"));
    }
    @Test
    public void right() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("RIGHT('\"hahah\"',3')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find proper: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/proper.xlsx"));
    }
    @Test
    public void search() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("SEARCH('\"e\"','\"Excel\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find proper: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/search.xlsx"));
    }
    @Test
    public void substitute() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("SUBSTITUTE('\"Excel教程\"','\"教程\"','\"技巧\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find proper: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/search.xlsx"));
    }
    @Test
    public void text() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("TEXT(12.345,'\"0.00\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find text: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/text.xlsx"));
    }
    @Test
    public void trim() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("TRIM('\" 0.00 xsxasxas \"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find text: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/trim.xlsx"));
    }
    @Test
    public void upper() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("UPPER('\" 0.00 xsxasxas \"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find text: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/upper.xlsx"));
    }    @Test
    public void value() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JAbstractExcelFormula formula = factory.createFormulaInstance("VALUE('\"12234\"')");
        factory.applyFormula(sheet, 10, 0, formula);  // A11
        Cell sumCell = sheet.getRow(10).getCell(0);
        Object sumResult = factory.evaluateFormula(sumCell);
        System.out.println("find text: " + sumResult);
        workbook.write(new FileOutputStream(fileDir+"/value.xlsx"));
    }


}
