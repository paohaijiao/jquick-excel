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
    public void ABS() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'ABS(D2)'"+
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
    public void AVERAGE() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'AVERAGE(D2:D4)'"+
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
    public void COUNT() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'COUNT(D2:D4)'"+
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
    public void MAX() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'MAX(D2:D4)'"+
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
    public void MIN() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'MIN(D2:D4)'"+
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
    public void POWER() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'POWER(2,3)'"+
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
    public void RAND() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'RAND()'"+
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
    public void RANK() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'RANK(20,D2:D4)'"+
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
    public void ROUND() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'ROUND(3.1415926,3)'"+
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
    public void SQRT() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'STDEV(D2:D4)'"+
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
    public void SUM() throws IOException {
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
                "FORMULAS={\n" +
                "D5:'SUM(D2:D4)'"+
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

}
