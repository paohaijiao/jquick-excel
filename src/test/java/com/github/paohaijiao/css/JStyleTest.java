package com.github.paohaijiao.css;

import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.util.*;

public class JStyleTest {
    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }

    @Test
    public void style() {
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", i);
            row.put("name", "Name " + i);
            row.put("score", Math.random() * 100);
            row.put("passed", Math.random() > 0.5);
            data.add(row);
        }
        String configText = "EXPORT FROM data TO \"d://test//styl.xlsx\" WITH STYLE = {     \n" +
                " ROW 1: { font: bold, bgColor: yellow },  \n" +
                " ROW 2..5: { font: italic, border: thin },        \n" +
                " COL C: { font: bold, alignment: center },  \n" +
                " A1: { font: bold, color: red },    \n" +
                " B2: { border: thick, bgColor: green },           \n" +
                "A1:B5: { border: dashed, alignment: right } }\n" +
                "FORMAT = {    score: NUMBER(#,##0.00),    passed: STRING(Yes/No) }";

        System.out.println(configText);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(configText));
        JQuickExcelParser parser = new JQuickExcelParser(new CommonTokenStream(lexer));
        JQuickExcelParser.ExportConfigContext exportConfig = parser.exportConfig();
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(data);
        visitor.visitExportConfig(exportConfig);
    }
}
