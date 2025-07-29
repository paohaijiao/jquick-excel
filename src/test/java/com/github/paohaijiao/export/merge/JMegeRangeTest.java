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

import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.export.model.JDataModel;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JObjectConverter;
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
import java.util.HashMap;
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
public class JMegeRangeTest {
    public static List<JDataModel> getData() {
        List<JDataModel> students = new ArrayList<>();
        students.add(new JDataModel(5, 22, 3, 4, 5, 6, 1));
        students.add(new JDataModel(2, 0, 1, 1, 1, 1, 7));
        students.add(new JDataModel(3, -1, 1, 1, 1, 1, 8));
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
    public void value() throws IOException {
        String rule = "EXPORT WITH MERGE: {\n" +
                "    ROWS 1 WITH MERGE_WITH_VALUE"+
                "}";
        System.out.println(rule);
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//merge.xlsx");
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("0","女");
        JContext context = new JContext();
        context.put("dict",map);
        context.put("value","汇总");
        JExcelExportHandler handler = new JExcelExportHandler(config,context,data);
        Workbook workbook=handler.getWorkBook();
        workbook.write(fileOutputStream);
    }
    @Test
    public void concat() throws IOException {
        String rule = "EXPORT WITH MERGE: {\n" +
                "    ROWS 1 WITH MERGE_WITH_CONCAT"+
                "}";
        System.out.println(rule);
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//merge.xlsx");
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
    public void firstAndLast() throws IOException {
        String rule = "EXPORT WITH MERGE: {\n" +
                "    ROWS 1 WITH MERGE_WITH_FIRST,"+
                "    ROWS 2..3 WITH MERGE_WITH_LAST"+
                "}";
        System.out.println(rule);
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//merge.xlsx");
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
    public void avgAndCount() throws IOException {
        String rule = "EXPORT WITH MERGE: {\n" +
                "    ROWS 1 WITH MERGE_WITH_AVG,"+
                "    ROWS 2..3 WITH MERGE_WITH_COUNT"+
                "}";
        System.out.println(rule);
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//merge.xlsx");
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
    public void maxandMin() throws IOException {
        String rule = "EXPORT WITH MERGE: {\n" +
                "    ROWS 1 WITH MERGE_WITH_MAX,"+
                "    ROWS 2..3 WITH MERGE_WITH_MIN"+
                "}";
        System.out.println(rule);
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//merge.xlsx");
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
