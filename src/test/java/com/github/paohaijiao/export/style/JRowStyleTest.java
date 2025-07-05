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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

public class JRowStyleTest {

    private static String fileDir ="d://test";
    @Test
    public void height() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forRow(1);
        HashMap<String,Object> map=new HashMap<>();
        map.put("height",new BigDecimal("800"));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/height.xlsx"));
    }
    @Test
    public void heightInPoints() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forRow(1);
        HashMap<String,Object> map=new HashMap<>();
        map.put("heightInPoints",new BigDecimal("80"));
        context.applyStyle(map);
        workbook.write(new FileOutputStream(fileDir+"/heightInPoints.xlsx"));
    }
    @Test
    public void zeroHeight() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        JExcelFormulaContext factory = new JExcelFormulaContext(workbook);
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(-1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((1) * 10);  // B1:B10 = 10-100
        }
        JStyleContext context=new JStyleContext(workbook,sheet).forRow(1);
        HashMap<String,Object> map=new HashMap<>();
        map.put("zeroHeight",true);
        HashMap<String,Object> rowStyle=new HashMap<>();
        short colorIndex = IndexedColors.RED.getIndex();
        rowStyle.put("fillBackgroundColor",new BigDecimal(colorIndex));
        map.put("rowStyle",rowStyle);
        context.applyStyle(map);

        workbook.write(new FileOutputStream(fileDir+"/zeroHeight.xlsx"));
    }
}
