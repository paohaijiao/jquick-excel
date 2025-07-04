package com.github.paohaijiao.css;

import com.github.paohaijiao.handler.JExcelProcessor;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import com.github.paohaijiao.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class JStyleTest {

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
                " ROW 1: { font: bold, rightBorderColor: red },  \n" +
                " ROW 2..5: { font: italic, border: thin },        \n" +
                " COL C: { font: bold, alignment: center },  \n" +
                " A1: { font: bold, color: red },    \n" +
                " B2: { border: thick, bgColor: green },           \n" +
                "A1:B5: { border: dashed, alignment: right } }\n" ;

        System.out.println(configText);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(configText));
        JQuickExcelParser parser = new JQuickExcelParser(new CommonTokenStream(lexer));
        JQuickExcelParser.ExportConfigContext exportConfig = parser.exportConfig();
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(data);
        visitor.visitExportConfig(exportConfig);
    }


        @Test
        public void testSimpleImport() {
            String input = "IMPORT FROM \"d://student.xlsx\" TO myTable";
            JExcelImportModel result = parseImportConfig(input);
            System.out.println(result);
        }

        @Test
        public void testImportWithOptions() {
            String input = "IMPORT FROM \"d://student.xlsx\" WITH SHEET=1, HEADER='YES', RANGE=\"A1:D100\" TO sales_data";
            JExcelImportModel result = parseImportConfig(input);
            System.out.println(result);
        }

        @Test
        public void testImportWithMappings() {
            String input = "IMPORT FROM \"d://student.xlsx\" WITH " +
                    "MAPPING = {\"Name\": \"full_name\", \"Age\": \"employee_age\", \"Dept\": \"department\"} " +
                    "TO employee_records";
            JExcelImportModel result = parseImportConfig(input);
            System.out.println(result);
        }

        @Test
        public void testImportWithTransforms() {
            String input = "IMPORT FROM \"d://student.xlsx\" WITH " +
                    "TRANSFORM = {\"Price\": \"formatCurrency($.Price)\", \"Date\": \"parseDate($.Date)\"} " +
                    "TO product_list";
            JExcelImportModel model = parseImportConfig(input);
            System.out.println(model);

        }

        @Test
        public void testComplexImport() {
            String input = "IMPORT FROM \"d://student.xlsx\" WITH " +
                    "SHEET=\"Q1 Report\", " +
                    "HEADER=YES" +
                    "RANGE=\"B2:M50\", " +
                    "MAPPING = {\"Account\": \"account_code\", \"Amount\": \"convertToDecimal($.Amount)\"}, " +
                    "TRANSFORM = {\"Notes\": \"sanitizeText($.Notes)\"} " +
                    "TO q1_financials";
            JExcelImportModel model = parseImportConfig(input);
            System.out.println(model);
        }




        private JExcelImportModel parseImportConfig(String input) {
            JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JQuickExcelParser parser = new JQuickExcelParser(tokens);
            ParseTree tree = parser.importConfig();
            JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
            JExcelImportModel result = (JExcelImportModel) visitor.visit(tree);
            return result;
        }

        @Test
        public void testComplexImport() throws IOException {
            String input = "IMPORT FROM \"d://student.xlsx\"" +
                    " WITH SHEET=\"学生信息\", HEADER=\"YES\", RANGE=\"A1:2F\"," +
                    "MAPPING = {\"学号\": \"NO\", \"姓名\": \"NAME\"}, " +
                    "TRANSFORM = {\"NO\": \"sanitizeText($.Notes)\"} TO q1_financials";
            System.out.println(input);
            JExcelImportModel model = parseImportConfig(input);
            System.out.println(model);
            List<Map<String, Object>> processor = new JExcelProcessor().importData(model);
            System.out.println(processor);
        }


}
