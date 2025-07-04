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
package lexer.ex;

import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JImLexerTest {

    private JExcelExportModel parseExportConfig(String input) {
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data = new ArrayList<>();
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(data);
        @SuppressWarnings("unchecked")
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
        return result;
    }

    @Test
    public void testSimpleExport() {
        String input = "EXPORT FROM customer_data TO \"output/customers.xlsx\"\n";
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport02() {
        String input = "EXPORT FROM sales_records TO \"output/sales_Q2.xlsx\" WITH SHEET=\"第二季度\"\n";
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport03() {
        String input = "EXPORT FROM product_inventory TO \"output/inventory_report.xlsx\" WITH HEADER='YES'\n";
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport04() {
        String input = "EXPORT FROM employee_list TO \"output/staff.xlsx\" WITH RANGE=\"B5\"\n";
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport05() {
        String input = "EXPORT FROM financial_data TO \"output/financials.xlsx\" WITH\n" +
                "    FORMAT = {\n" +
                "        \"Revenue\": NUMBER('$#,##0.00'),\n" +
                "        \"GrowthRate\": NUMBER('0.00%')\n" +
                "    }";
        System.out.println(input);
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport06() {
        String input = "EXPORT FROM event_schedule TO \"output/events.xlsx\" WITH\n" +
                "    FORMAT = {\n" +
                "        \"StartDate\": DATE('yyyy-mm-dd'),\n" +
                "        \"EndDate\": DATE('dd-mmm-yy')\n" +
                "    }";
        System.out.println(input);
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport07() {
        String input = "EXPORT FROM monthly_sales TO \"output/sales_summary.xlsx\" WITH\n" +
                "    FORMULAS = {\n" +
                "        \"Total\": \"SUM(B2:B13)\"\n" +
                "    }";
        System.out.println(input);
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport08() {
        String input = "EXPORT FROM student_scores TO \"output/score_report.xlsx\" WITH\n" +
                "    FORMULAS = {\n" +
                "        \"Average\": \"AVERAGE(C2:C50)\",\n" +
                "        \"MaxScore\": \"MAX(D2:D50)\",\n" +
                "        \"PassRate\": \"COUNTIF(E2:E50,\\\">=60\\\")/COUNTA(E2:E50)\"\n" +
                "    }";
        System.out.println(input);
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport09() {
        String input = "EXPORT FROM project_tasks TO \"output/project_status.xlsx\" WITH\n" +
                "    FORMULAS = {\n" +
                "        \"Completion\": \"\\\"=\\\"\\\"已完成 \\\"\\\"&COUNTIF(F2:F100,\\\"Done\\\")&\\\"\\\" 项\\\"\\\"\\\"\"\n" +
                "    }";
        System.out.println(input);
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void testSimpleExport10() {
        String input = "EXPORT FROM annual_report TO \"output/report_2023.xlsx\" WITH\n" +
                "    SHEET=\"年度汇总\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    FORMAT = {\n" +
                "        \"Amount\": NUMBER('¥#,##0.00'),\n" +
                "        \"Date\": DATE('yyyy年mm月dd日')\n" +
                "    },\n" +
                "    FORMULAS = {\n" +
                "        \"YTDTotal\": \"SUM(D4:D15)\",\n" +
                "        \"YoYGrowth\": \"TEXT((D15-D4)/D4,\\\"0.00%\\\")\"\n" +
                "    }\n";
        System.out.println(input);
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }

    @Test
    public void export() {
        String input = "EXPORT FROM annual_report TO \"d://test//report_2023.xlsx\" WITH\n" +
                "    SHEET=\"年度汇总\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    FORMAT = {\n" +
                "        \"Amount\": NUMBER('¥#,##0.00'),\n" +
                "        \"Date\": DATE('yyyy年mm月dd日')\n" +
                "    },\n" +
                "    FORMULAS = {\n" +
                "        \"YTDTotal\": \"SUM(D4:D15)\",\n" +
                "        \"YoYGrowth\": \"TEXT((D15-D4)/D4,\\\"0.00%\\\")\"\n" +
                "    }\n";
        System.out.println(input);
        JExcelExportModel result = parseExportConfig(input);
        System.out.println(result);
    }
}
