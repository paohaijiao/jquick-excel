package com.github.paohaijiao.xml.demo;

import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.util.JRowsUtil;
import com.github.paohaijiao.xml.ex.JQuickExcelXmlParseFactory;
import com.github.paohaijiao.xml.export.JQuickExcelExportService;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.param.Param;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        List<JQuickRow> rows= JRowsUtil.toRows( JObjectConverter.convert(getData()));
        OutputStream fileOutputStream=new FileOutputStream("d://test//style.xlsx");
        JQuickParseHandler parser = new JQuickExcelXmlParseFactory(rows,fileOutputStream);
        JQuickFactory factory = new JQuickXmlFactory(parser,"jquick-excel.xml");
        System.out.println(factory);
        JQuickExcelExportService excelExportService = factory.createApi(JQuickExcelExportService.class);
        excelExportService.exportExcel("1","2");
        System.out.println("导出成功");
    }
}
