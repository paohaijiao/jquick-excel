package com.github.paohaijiao.xml.demo;

import com.github.paohaijiao.importFile.mapping.JMappingTest;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import com.github.paohaijiao.xml.service.JQuickExcelExportService;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class JQuickExcelDemo {
    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }
    @Test
    public void exportExcel() throws FileNotFoundException {
        List<JQuickRow> rows= JQuickRow.toRows( JObjectConverter.convert(getData()));
        OutputStream fileOutputStream=new FileOutputStream("d://test//style.xlsx");
        JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows,fileOutputStream);
        JQuickFactory factory = new JQuickXmlFactory(parser,"jquick-excel.xml");
        System.out.println(factory);
        JQuickExcelExportService excelExportService = factory.createApi(JQuickExcelExportService.class);
        excelExportService.exportExcel("1","2");
        System.out.println("导出成功");
    }
    @Test
    public void importExcel() throws FileNotFoundException {
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        Map<String,Object> sex=new HashMap<>();
        sex.put("男","1");
        sex.put("女","2");
        JContext context = new JContext();
        context.put("dict",sex);
        JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(context,is);
        JQuickFactory factory = new JQuickXmlFactory(parser,"jquick-excel.xml");
        System.out.println(factory);
        JQuickExcelExportService excelExportService = factory.createApi(JQuickExcelExportService.class);
        List<JQuickRow> list=excelExportService.importExcel("1","2");
        System.out.println("导入成功:"+list.size());
    }
}
