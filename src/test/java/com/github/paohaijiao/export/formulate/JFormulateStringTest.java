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
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.util.JObjectConverter;
import org.apache.poi.ss.usermodel.Workbook;
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
public class JFormulateStringTest {

    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }
    @Test
    public void DATETIME() throws IOException {
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
                "D5:'DATETIME(2023,5,15,14,30,0)'"+
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
    public void DAY() throws IOException {
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
                "D5:'DAY(\"2025-01-23\")'"+
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
    public void DAYS() throws IOException {
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
                "D5:'DAYS(\"2025-01-23\",\"2025-01-28\")'"+
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
    public void EOMONTH() throws IOException {
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
                "D5:'EOMONTH(\"2025-01-23\",3)'"+
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
    public void EDATE() throws IOException {
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
                "D5:'EDATE(\"2025-01-23\",3)'"+
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
    public void HOUR() throws IOException {
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
                "D5:'HOUR(\"2025-01-23\")'"+
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
    public void CONCATENATE() throws IOException {
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
                "D5:'CONCATENATE(A1, B1)'"+
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
    public void CONCAT() throws IOException {
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
                "D5:'CONCAT(A1, B1)'"+
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
    public void EXACT() throws IOException {
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
                "D5:'EXACT(\"A1\", \"B1\")'"+
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
    public void FIND() throws IOException {
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
                "D5:'FIND(\"o\", \"Microsoft\")'"+
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
    public void LEFT() throws IOException {
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
                "D5:'LEFT(\"hello world\", 3)'"+
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
    public void RIGHT() throws IOException {
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
                "D5:'RIGHT(\"hello world\", 3)'"+
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
    public void LEN() throws IOException {
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
                "D5:'LEN(\"hello world\")'"+
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
    public void MID() throws IOException {
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
                "D5:'MID(\"hello world\",1,2)'"+
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
    public void SUBSTITUTE() throws IOException {
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
                "D5:'SUBSTITUTE(\"hello world\",\"hello\",\"new\")'"+
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
    public void TRIM() throws IOException {
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
                "D5:'TRIM(\"hello world\")'"+
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
    public void LOWER() throws IOException {
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
                "D5:'LOWER(\"hello world\")'"+
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
    public void PROPER() throws IOException {
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
                "D5:'PROPER(\"hello world\")'"+
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
    public void REPLACE() throws IOException {
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
                "D5:'REPLACE(\"ABCD\",2,2,\"XY\")'"+
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
    public void TEXT() throws IOException {
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
                "D5:'TEXT(0.25,\"0.0%\")'"+
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
    public void UPPER() throws IOException {
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
                "D5:'UPPER(\"email\")'"+
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
    public void VALUE() throws IOException {
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
                "D5:'VALUE(\"¥1,000\")'"+
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
    public void SEARCH() throws IOException {
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
                "D5:'SEARCH(\"e\",\"Excel\")'"+
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
