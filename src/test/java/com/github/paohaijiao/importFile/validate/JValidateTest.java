package com.github.paohaijiao.importFile.validate;

import com.github.paohaijiao.executor.JQuickExcelCommonImportExecutor;
import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.importFile.mapping.JMappingTest;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JValidateTest {
    @Test
    public void bool() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "}," +
                "VALIDATION={"+
                "   C2:C4:{\n" +
                "    boolean{required:true,msg:'性别非法',map:{'1':'男','2':'女'}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void date_format() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\",\n" +
                "\"入学日期(字符串格式)\": \"registerDay\"\n" +
                "}," +
                "VALIDATION={"+
                "   E2:E4:{\n" +
                "    date_format{required:true,msg:'不符合日期格式',map:{'format':'yyyy-MM-dd'}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void max_date() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\",\n" +
                "\"入学日期(字符串格式)\": \"registerDay\"\n" +
                "}," +
                "VALIDATION={"+
                "   E2:E4:{\n" +
                "    max_date{required:true,msg:'超过最大日期',map:{'format':'yyyy-MM-dd',maxDate:2025-01-01}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void min_date() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\",\n" +
                "\"入学日期(字符串格式)\": \"registerDay\"\n" +
                "}," +
                "VALIDATION={"+
                "   E2:E4:{\n" +
                "    min_date{required:true,msg:'不能小于最小日期',map:{'format':'yyyy-MM-dd',minDate:2022-01-01}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void integer() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\",\n" +
                "\"入学日期(字符串格式)\": \"registerDay\"\n" +
                "}," +
                "VALIDATION={"+
                "   D2:D4:{\n" +
                "    integer{required:true,msg:'要求该字段是整形'" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void decimal() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\",\n" +
                "\"入学日期(字符串格式)\": \"registerDay\"\n" +
                "}," +
                "VALIDATION={"+
                "   D2:D4:{\n" +
                "    decimal{required:true,msg:'要求该字段是整形'" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void max_value() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\",\n" +
                "\"入学日期(字符串格式)\": \"registerDay\"\n" +
                "}," +
                "VALIDATION={"+
                "   D2:D4:{\n" +
                "    max_value{required:true,msg:'年龄不能超过最大值',map:{'maxValue':50}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }


    @Test
    public void min_value() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\",\n" +
                "\"入学日期(字符串格式)\": \"registerDay\"\n" +
                "}," +
                "VALIDATION={"+
                "   D2:D4:{\n" +
                "    min_value{required:true,msg:'年龄不能小于xx',map:{'minValue':2}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void dict() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "}," +
                "VALIDATION={"+
                "   C2:C4:{\n" +
                "    dict{required:true,msg:'性别非法',map:{'1':'男','2':'女'}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);
    }
    @Test
    public void email() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "}," +
                "VALIDATION={"+
                "   C2:C4:{\n" +
                "    email{required:true,msg:'邮箱格式不正确'" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void mobile() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "}," +
                "VALIDATION={"+
                "   C2:C4:{\n" +
                "    mobile{required:true,msg:'手机格式不正确'" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void max_length() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "}," +
                "VALIDATION={"+
                "   B2:B4:{\n" +
                "    max_length{required:true,msg:'最大长度有误',map:{'maxLength':7}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void min_length() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "}," +
                "VALIDATION={"+
                "   B2:B4:{\n" +
                "    min_length{required:true,msg:'最小长度有误',map:{'minLength':1}" +
                "   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void regex() throws IOException {
        String input = "IMPORT WITH HEADER=true,SHEET='Sheet1',MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "},VALIDATION={   D2:D4:{\n" +
                "    regex{required:true,msg:'不符合正则表达式',map:{pattern:'^\\d+$'}   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void start_with() throws IOException {
        String input = "IMPORT WITH HEADER=true,SHEET='Sheet1',MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "},VALIDATION={   B2:B4:{\n" +
                "   start_with{required:true,msg:'不符合正则表达式',map:{startWith:'张三'}   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void not_start_with() throws IOException {
        String input = "IMPORT WITH HEADER=true,SHEET='Sheet1',MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "},VALIDATION={   B2:B4:{\n" +
                "   not_start_with{required:true,msg:'不符合正则表达式',map:{notStartWith:'张三'}   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void end_with() throws IOException {
        String input = "IMPORT WITH HEADER=true,SHEET='Sheet1',MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "},VALIDATION={   B2:B4:{\n" +
                "   end_with{required:true,msg:'不符合正则表达式',map:{endWith:'张三'}   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void not_end_with() throws IOException {
        String input = "IMPORT WITH HEADER=true,SHEET='Sheet1',MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "},VALIDATION={   B2:B4:{\n" +
                "   not_end_with{required:true,msg:'不符合表达式',map:{notEndWith:'张三'}   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void contain() throws IOException {
        String input = "IMPORT WITH HEADER=true,SHEET='Sheet1',MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "},VALIDATION={   B2:B4:{\n" +
                "   contain{required:true,msg:'没有包含该关键字',map:{contains:'张三'}   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
    @Test
    public void not_contain() throws IOException {
        String input = "IMPORT WITH HEADER=true,SHEET='Sheet1',MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "},VALIDATION={   B2:B4{\n" +
                "   not_contain{required:true,msg:'不应该包含该关键字',map:{notContain:'张三'}   }\n" +
                "}";
        System.out.println(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("2","女");
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
}
