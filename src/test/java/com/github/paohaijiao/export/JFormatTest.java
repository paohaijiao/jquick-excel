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
package com.github.paohaijiao.export;

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
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JFormatTest {

    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }

    @Test
    public void sheet() throws FileNotFoundException {
        String input = "EXPORT   WITH\n" +
                "    SHEET=\"年度汇总\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    MAPPING= {\n" +
                "        \"id\": \"主键\",\n" +
                "        \"name\": \"姓名\",\n" +
                "        \"gender\":\"性别\",\n" +
                "        \"age\":\"年龄\",\n" +
                "        \"enrollmentDate\":\"入学时间\",\n" +
                "        \"className\":\"课程名称\"\n" +
                "    }";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//format.xlsx");
        JContext context=new JContext();
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);

    }
    @Test
    public void sheet1() throws FileNotFoundException {
        String input = "EXPORT   WITH\n" +
                "    SHEET=\"1\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    MAPPING= {\n" +
                "        \"name\": \"姓名\",\n" +
                "        \"gender\":\"性别\"\n" +
                "    },\n" +
                "    FORMAT = {\n" +
                "        \"name\": NUMBER('¥#,##0.00'),\n" +
                "        \"gender\": DATE('yyyy年mm月dd日')\n" +
                "    }";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//sheet1.xlsx");
        JContext context=new JContext();
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);

    }
    @Test
    public void header() throws FileNotFoundException {
        String input = "EXPORT   WITH\n" +
                "    SHEET=\"1\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    MAPPING= {\n" +
                "        \"name\": \"姓名\",\n" +
                "        \"gender\":\"性别\"\n" +
                "    },\n" +
                "    FORMAT = {\n" +
                "        \"name\": NUMBER('¥#,##0.00'),\n" +
                "        \"gender\": DATE('yyyy年mm月dd日')\n" +
                "    }";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//header.xlsx");
        JContext context=new JContext();
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);

    }
    @Test
    public void header1() throws FileNotFoundException {
        String input = "EXPORT   WITH\n" +
                "    SHEET=\"1\",\n" +
                "    HEADER='NO',\n" +
                "    RANGE=\"A3\",\n" +
                "    MAPPING= {\n" +
                "        \"name\": \"姓名\",\n" +
                "        \"gender\":\"性别\"\n" +
                "    },\n" +
                "    FORMAT = {\n" +
                "        \"name\": NUMBER('¥#,##0.00'),\n" +
                "        \"gender\": DATE('yyyy年mm月dd日')\n" +
                "    }";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//headerno.xlsx");
        JContext context=new JContext();
        context.put("fos", fileOutputStream);
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(context,data);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);

    }


}
