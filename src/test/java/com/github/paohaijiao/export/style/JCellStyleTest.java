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
package com.github.paohaijiao.export.style;

import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import com.github.paohaijiao.jstyle.context.JStyleContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

public class JCellStyleTest {

    private static String fileDir ="d://test";
    @Test
    public void alignment() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A1");
        HashMap<String,Object> map=new HashMap<>();
        map.put("alignment","left");
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/alignment.xlsx"));
    }
    @Test
    public void top() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A1");
        HashMap<String,Object> map=new HashMap<>();
        map.put("verticalAlignment","top");
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/top.xlsx"));
    }
    @Test
    public void borderBottom() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A1");
        HashMap<String,Object> map=new HashMap<>();
        map.put("borderBottom","dotted");
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/borderBottom.xlsx"));
    }
    @Test
    public void borderLeft() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("borderLeft","double");
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/borderLeft.xlsx"));
    }
    @Test
    public void borderRight() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("borderRight","double");
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/borderRight.xlsx"));
    }
    @Test
    public void borderTop() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("borderTop","double");
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/borderTop.xlsx"));
    }
    @Test
    public void fillPattern() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("fillPattern","sparse_dots");
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/fillPattern.xlsx"));
    }
    @Test
    public void wrapText() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("wrapText",true);
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/wrapText.xlsx"));
    }
    @Test
    public void rotation() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("rotation",new BigDecimal(25));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/rotation.xlsx"));
    }
    @Test
    public void indention() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("indention",new BigDecimal(2000));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/indention.xlsx"));
    }
    @Test
    public void locked() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("locked",true);
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/locked.xlsx"));
    }
    @Test
    public void hidden() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("hidden",true);
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/hidden.xlsx"));
    }
    @Test
    public void shrinkToFit() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        map.put("shrinkToFit",true);
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/shrinkToFit.xlsx"));
    }
    @Test
    public void bottomBorderColor() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("A2");
        HashMap<String,Object> map=new HashMap<>();
        short colorIndex = IndexedColors.BLUE.getIndex();
        map.put("bottomBorderColor",new BigDecimal(colorIndex));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/bottomBorderColor.xlsx"));
    }
    @Test
    public void leftBorderColor() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("B1");
        HashMap<String,Object> map=new HashMap<>();
        short colorIndex = IndexedColors.BLUE.getIndex();
        map.put("leftBorderColor",new BigDecimal(colorIndex));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/leftBorderColor.xlsx"));
    }
    @Test
    public void rightBorderColor() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("B1");
        HashMap<String,Object> map=new HashMap<>();
        short colorIndex = IndexedColors.RED.getIndex();
        map.put("rightBorderColor",new BigDecimal(colorIndex));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/rightBorderColor.xlsx"));
    }
    @Test
    public void topBorderColor() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("B1");
        HashMap<String,Object> map=new HashMap<>();
        short colorIndex = IndexedColors.RED.getIndex();
        map.put("topBorderColor",new BigDecimal(colorIndex));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/topBorderColor.xlsx"));
    }
    @Test
    public void fillForegroundColor() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("B1");
        HashMap<String,Object> map=new HashMap<>();
        short colorIndex = IndexedColors.RED.getIndex();
        map.put("fillForegroundColor",new BigDecimal(colorIndex));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/fillForegroundColor.xlsx"));
    }
    @Test
    public void fillBackgroundColor() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forCell("B3");
        HashMap<String,Object> map=new HashMap<>();
        short colorIndex = IndexedColors.RED.getIndex();
        map.put("fillBackgroundColor",new BigDecimal(colorIndex));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/fillBackgroundColor.xlsx"));
    }





    @Test
    public void test() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Fill Background Example");

        Row row = sheet.createRow(0);

        // 示例1：纯色填充（只需前景色）
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("纯色填充");
        CellStyle style1 = workbook.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell1.setCellStyle(style1);

        // 示例2：图案填充（前景色+背景色）
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("图案填充");
        CellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.RED.getIndex());    // 图案颜色
        style2.setFillBackgroundColor(IndexedColors.YELLOW.getIndex()); // 背景颜色
        style2.setFillPattern(FillPatternType.FINE_DOTS); // 点状图案
        cell2.setCellStyle(style2);

        // 示例3：条纹填充
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("条纹填充");
        CellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style3.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
        style3.setFillPattern(FillPatternType.THICK_HORZ_BANDS);
        cell3.setCellStyle(style3);
        try (FileOutputStream out = new FileOutputStream(fileDir+"/test.xlsx")) {
            workbook.write(out);
        }

        System.out.println("文件已生成，请用Excel查看效果");
    }

}
