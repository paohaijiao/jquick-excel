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
package com.github.paohaijiao.importFile;

import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelCommonImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.importFile
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/7
 */
public class ImportG4Test {
    public XSSFWorkbook testGenerateExcelFile() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test Sheet");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Score"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        Object[][] data = {{1, "Alice", 85}, {2, "Bob", 72}};
        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = row.createCell(j);
                if (data[i][j] instanceof Number) {
                    cell.setCellValue(((Number) data[i][j]).doubleValue());
                } else {
                    cell.setCellValue(data[i][j].toString());
                }
            }
          //  Cell gradeCell = row.createCell(3);
           // gradeCell.setCellFormula("IF(C" + (i + 2) + ">=90, \"A\", IF(C" + (i + 2) + ">=80, \"B\", IF(C" + (i + 2) + ">=70, \"C\", \"D\")))");
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }
    @Test
    public void boolRequire() throws IOException {
        String input = "IMPORT WITH VALIDATION={\n" +
                "   ROW 1:{\n" +
                    "    MAX_LENGTH{required:true,msg:'你好',map:{maxLength:1,minLength:12}}\n" +
                "   }\n" +
                "}";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.importConfig();
        JContext context=new JContext();
        JQuickExcelCommonImportVisitor visitor = new JQuickExcelCommonImportVisitor(context);
        JExcelImportModel result = (JExcelImportModel) visitor.visit(tree);
        JExcelImportHandler handler=new JExcelImportHandler(testGenerateExcelFile());
        List<Map<String, Object>> list=handler.importData(result);
        System.out.println(list);
    }
    @Test
    public void rows() throws IOException {
        String input = "IMPORT WITH VALIDATION={\n" +
                "   ROW 1..2 :{\n" +
                "    MAX_LENGTH{required:true,msg:你好,map:{maxLength:15,minLength:12}}\n" +
                "   }\n" +
                "}";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.importConfig();
        JContext context=new JContext();
        JQuickExcelCommonImportVisitor visitor = new JQuickExcelCommonImportVisitor(context);
        JExcelImportModel result = (JExcelImportModel) visitor.visit(tree);
        JExcelImportHandler handler=new JExcelImportHandler(testGenerateExcelFile());
        List<Map<String, Object>> list=handler.importData(result);
        System.out.println(list);
    }


}
