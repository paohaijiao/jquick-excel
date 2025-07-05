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
package com.github.paohaijiao.export.merge;

import com.github.paohaijiao.export.model.JDataModel;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.export.style
 *
 * @author Martin
 * @version 1.0.0
 * @className JStyleTest
 * @date 2025/7/5
 * @description
 */
public class JMegeTest {
    public static List<JDataModel> getData() {
        List<JDataModel> students = new ArrayList<>();
        students.add(new JDataModel(1, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(2, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(3, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(4, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(5, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(6, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(7, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(8, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(9, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(10, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(11, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(12, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(13, 1, 1, 1, 1, 1, 1));
        students.add(new JDataModel(14, 1, 1, 1, 1, 1, 1));
        return students;
    }
    @Test
    public void max() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH MAX"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//rowMerge.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void MIN() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH MIN"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//min.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void avg() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH AVG"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//avg.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void count() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH COUNT"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//count.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void sum() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH SUM"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//sum.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void concat() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH CONCAT"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//concat.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void SUM() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH SUM"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//sum.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void FIRST() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH FIRST"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//first.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void Last() throws IOException {
        String config = "EXPORT WITH MERGE: {\n" +
                "    ROWS 5 WITH LAST"+
                "}";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        for (int i = 0; i < 2; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i + 1);  // A1:A10 = 1-10
            row.createCell(1).setCellValue((i + 1) * 10);  // B1:B10 = 10-100
        }
        System.out.println(config);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(config));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//last.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
}
