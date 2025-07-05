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
package com.github.paohaijiao.export.formulate.date;

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

public class JFormulaDateValueTest {

    private static String fileDir ="d://test";
    @Test
    public void dateDif() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-01-01");
        sheet.createRow(1).createCell(0).setCellValue("2023-12-31");
        JAbstractExcelFormula formula = factory.createFormulaInstance("DATEDIF(A1,A2,'\"D\"')");
        factory.applyFormula(sheet, 2, 0, formula);
        Cell resultCell = sheet.getRow(2).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/datedif.xlsx"));
    }
    @Test
    public void date() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        JAbstractExcelFormula formula = factory.createFormulaInstance("DATE(2023,5,15)");
        factory.applyFormula(sheet, 0, 0, formula);
        Cell resultCell = sheet.getRow(0).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/date.xlsx"));
    }
    @Test
    public void dateTime() throws IOException {//NOT SUPPORT
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        JAbstractExcelFormula formula = factory.createFormulaInstance("DATETIME(2023,5,15,14,30,0)");
        factory.applyFormula(sheet, 0, 0, formula);
        Cell resultCell = sheet.getRow(0).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/datetime.xlsx"));
    }
    @Test
    public void day() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-05-15");
        JAbstractExcelFormula formula = factory.createFormulaInstance("DAY(A1)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/day.xlsx"));
    }
    @Test
    public void days() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-01-01");
        sheet.createRow(1).createCell(0).setCellValue("2023-12-31");
        JAbstractExcelFormula formula = factory.createFormulaInstance("DAYS(A2,A1)");
        factory.applyFormula(sheet, 2, 0, formula);
        Cell resultCell = sheet.getRow(2).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/days.xlsx"));
    }
    @Test
    public void edate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-01-31");
        JAbstractExcelFormula formula = factory.createFormulaInstance("EDATE(A1,3)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/edate.xlsx"));
    }
    @Test
    public void eomonth() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-02-15");
        JAbstractExcelFormula formula = factory.createFormulaInstance("EOMONTH(A1,0)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/eomonth.xlsx"));
    }
    @Test
    public void hour() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("14:30:00");
        JAbstractExcelFormula formula = factory.createFormulaInstance("HOUR(A1)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/hour.xlsx"));
    }
    @Test
    public void minute() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("14:30:45");
        JAbstractExcelFormula formula = factory.createFormulaInstance("MINUTE(A1)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/minute.xlsx"));
    }
    @Test
    public void month() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-05-15");
        JAbstractExcelFormula formula = factory.createFormulaInstance("MONTH(A1)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/month.xlsx"));
    }
    @Test
    public void networkDays() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-05-01");
        sheet.createRow(1).createCell(0).setCellValue("2023-05-07");
        JAbstractExcelFormula formula = factory.createFormulaInstance("NETWORKDAYS(A1,A2)");
        factory.applyFormula(sheet, 2, 0, formula);
        Cell resultCell = sheet.getRow(2).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/networkdays.xlsx"));
    }
    @Test
    public void now() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        JAbstractExcelFormula formula = factory.createFormulaInstance("NOW()");
        factory.applyFormula(sheet, 0, 0, formula);
        Cell resultCell = sheet.getRow(0).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/now.xlsx"));
    }
    @Test
    public void second() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("14:30:45");
        JAbstractExcelFormula formula = factory.createFormulaInstance("SECOND(A1)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/second.xlsx"));
    }
    @Test
    public void time() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        JAbstractExcelFormula formula = factory.createFormulaInstance("TIME(14,30,0)");
        factory.applyFormula(sheet, 0, 0, formula);
        Cell resultCell = sheet.getRow(0).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/time.xlsx"));
    }
    @Test
    public void today() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        JAbstractExcelFormula formula = factory.createFormulaInstance("TODAY()");
        factory.applyFormula(sheet, 0, 0, formula);
        Cell resultCell = sheet.getRow(0).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/today.xlsx"));
    }
    @Test
    public void weekday() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-05-15");
        JAbstractExcelFormula formula = factory.createFormulaInstance("WEEKDAY(A1,2)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/weekday.xlsx"));
    }
    @Test
    public void weeknum() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-01-01");
        JAbstractExcelFormula formula = factory.createFormulaInstance("WEEKNUM(A1,1)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/weeknum.xlsx"));
    }
    @Test
    public void workday() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-05-15");
        sheet.createRow(1).createCell(0).setCellValue("2023-05-17");
        JAbstractExcelFormula formula = factory.createFormulaInstance("WORKDAY(A1,3,A2)");
        factory.applyFormula(sheet, 2, 0, formula);
        Cell resultCell = sheet.getRow(2).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/workday.xlsx"));
    }
    @Test
    public void year() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        sheet.createRow(0).createCell(0).setCellValue("2023-05-15");
        JAbstractExcelFormula formula = factory.createFormulaInstance("YEAR(A1)");
        factory.applyFormula(sheet, 1, 0, formula);
        Cell resultCell = sheet.getRow(1).getCell(0);
        Object result = factory.evaluateFormula(resultCell);
        System.out.println(result);
        workbook.write(new FileOutputStream(fileDir+"/year.xlsx"));
    }




}
