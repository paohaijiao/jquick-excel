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
package com.github.paohaijiao.export.formulate;
import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.visitor.JQuickExcelComonExportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * packageName com.github.paohaijiao.export.formulate
 *
 * @author Martin
 * @version 1.0.0
 * @className JFormulateIntegrationTest
 * @date 2025/7/5
 * @description
 */
public class JFormulateTest {

    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }
    @Test
    public void testRowFormulas() throws IOException {
        String rule = "EXPORT  WITH\n" +
                "SHEET=\"学生表\",\n" +
                "HEADER=true,\n" +
                "MAPPING={\n" +
                "\t\"id\":\"主键\",\n" +
                "\t\"name\":\"姓名\",\n" +
                "\t\"gender\":\"性别\",\n" +
                "\t\"age\":\"年龄\",\n" +
                "\t\"enrollmentDate\":\"入学时间\",\n" +
                "\t\"className\":\"班级\",\n" +
                "\t\"ignoreField\":\"是否忽略\"\n" +
                "},\n" +
                "FORMAT={\t\"enrollmentDate\":\"yyyy-MM-dd\"\n" +
                "},\n" +
                "TRANSFORM={\n" +
                "\t\"name\": toUpper(${name}),\n" +
                "\t\"enrollmentDate\": dateFormat(${enrollmentDate},'yyyy-MM-dd'),\n" +
                "\t\"gender\": trans(${dict},${gender})\n" +
                "},\n" +
                "FORMULAS={\n" +
                "    D5:'SUM(D2:D4)',\n" +
                "  COL H: \"SUM(A1,B1)\",\n" +
                "  COL I..J: \"SUM(A2,A4)\",\n" +
                "  A11: \"SUM(A1:A10)\",\n" +
                "  B11: \"AVERAGE(B1:B10)\",\n" +
                "  C1: \"NOW()\",\n" +
                "  D5: \"NOW()\"\n" +
                "}\n";
        System.out.println(rule);
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//transform.xlsx");
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("0","女");
        JContext context = new JContext();
        context.put("dict",map);
        JExcelExportHandler handler = new JExcelExportHandler(config,context,data);
        Workbook workbook=handler.getWorkBook();
        workbook.write(fileOutputStream);
    }
    @Test
    public void testColumnFormulas() throws IOException {
        String config = "EXPORT WITH FORMULAS = {\n" +
                "  COL G: \"SUM(A1,B1)\",\n" +
                "  COL H..I: \"SUM(A2,B2)\"\n" +
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
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//testRowFormulas.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelComonExportVisitor visitor = new JQuickExcelComonExportVisitor(context);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
    @Test
    public void testCellFormulas() throws IOException {
        String config = "EXPORT WITH FORMULAS = {\n" +
                "  A11: \"SUM(A1:A10)\",\n" +
                "  B11: \"AVERAGE(B1:B10)\",\n" +
                "  C1: \"NOW()\",\n" +
                "  D5: \"NOW()\"\n" +
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
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//testRowFormulas.xlsx");
        JContext context=new JContext();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        context.put("fos", fileOutputStream);
        JQuickExcelComonExportVisitor visitor = new JQuickExcelComonExportVisitor(context);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
    }
}
