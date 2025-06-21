package com.paohaijiao.javelin.test;

import com.paohaijiao.javelin.excel.ExcelImporter;
import com.paohaijiao.javelin.excel.JExcelExporter;
import com.paohaijiao.javelin.listener.JReadListener;
import com.paohaijiao.javelin.listener.JWriteListener;
import com.paohaijiao.javelin.model.JStudentModel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App {
    public static void main(String[] args) {
        exportExample();
        importExample();
        templateExportExample();
    }

    private static void exportExample() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班","true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班","true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班","true"));
        JExcelExporter<JStudentModel> exporter = new JExcelExporter<>(JStudentModel.class);
        exporter.setListener(new JWriteListener() {
            @Override
            public void onSuccess() {
                System.out.println("导出成功");
            }

            @Override
            public void onError(Exception e) {
                System.err.println("导出失败: " + e.getMessage());
            }
        });

        try (OutputStream out = new FileOutputStream("D:\\students.xlsx")) {
            exporter.write(out, students);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void importExample() {
        ExcelImporter<JStudentModel> importer = new ExcelImporter<>(JStudentModel.class);
        importer.setListener(new JReadListener<JStudentModel>() {
            @Override
            public void onSuccess(List<JStudentModel> data) {
                System.out.println("导入成功，共导入 " + data.size() + " 条数据:");
                data.forEach(System.out::println);
            }

            @Override
            public void onError(Exception e) {
                System.err.println("导入错误: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("导入完成");
            }
        });

        try (InputStream in = new FileInputStream("students.xlsx")) {
            importer.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void templateExportExample() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("2001", "赵六", 1, 23, new Date(), "软件工程1班","true"));
        students.add(new JStudentModel("2002", "钱七", 0, 22, new Date(), "软件工程2班","true"));

        JExcelExporter<JStudentModel> exporter = new JExcelExporter<>(JStudentModel.class);
        exporter.setListener(new JWriteListener() {
            @Override
            public void onSuccess() {
                System.out.println("模板导出成功");
            }

            @Override
            public void onError(Exception e) {
                System.err.println("模板导出失败: " + e.getMessage());
            }
        });

        try (InputStream template = App.class.getResourceAsStream("/templates/student_template.xlsx");
             OutputStream out = new FileOutputStream("students_from_template.xlsx")) {
            exporter.writeWithTemplate(template, out, students);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
