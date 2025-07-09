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
import com.github.paohaijiao.handler.JExcelExportHandler;
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
public class JStyleTest {
    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }
    @Test
    public void testRowFormulas() throws IOException {
        String config = "EXPORT WITH\n" +
                "  SHEET=\"销售报表\",\n" +
                "  HEADER=true,\n" +
                "  FOOTER=\"NIXSAXSAXSAXASXSAXSAXSA\",\n" +
                "  STYLE={\n" +
                "    ROW 1: {\n" +
                "      fontName: Arial,\n" +
                "      fontHeightInPoints: 12,\n" +
                "      italic: true,\n" +
                "      bold: true\n" +
                "    },\n" +
                "    ROW 2..5: {\n" +
                "      fontName: Arial,\n" +
                "      underLine: uSingle,\n" +
                "      strikeout: true,\n" +
                "      color: red\n" +
                "    }\n" +
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
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//styleRow.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void testCellFormulas() throws IOException {
        String config = "EXPORT WITH  \n" +
                "  STYLE={\n" +
                "    C1: { " +
                "color: red " +
                "" +
                "},\n" +
                "    F1: { color: red }\n" +
                "  }";
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
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//styleCell.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void testColFormulas() throws IOException {
        String config = "EXPORT WITH  \n" +
                "  STYLE={\n" +
                "    COL A: { bottomBorderColor: black },\n" +
                "    COL C..D: { topBorderColor: black }\n" +
                "  }\n";
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
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
        JExcelExportHandler excelProcessor = new JExcelExportHandler(result,data);
        Workbook workbook1=excelProcessor.getWorkBook();
        workbook1.write(new FileOutputStream("d://test//haha.xlsx"));

    }
}
