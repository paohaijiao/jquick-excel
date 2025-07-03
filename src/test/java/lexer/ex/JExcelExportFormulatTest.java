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

import com.github.paohaijiao.factory.ExcelFormulaFactory;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class JExcelExportFormulatTest {

    public static   List<JStudentModel> getData(){
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班","true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班","true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班","true"));
        return students;
    }
    @Test
    public void export() {
        String input = "EXPORT FROM annual_report TO \"d://test//report_2023.xlsx\" WITH\n" +
                "    SHEET=\"年度汇总\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    FORMAT = {\n" +
                "        \"age\": NUMBER('¥#,##0.00'),\n" +
                "        \"gender\": NUMBER('¥#,##0.00')\n" +
                "    }\n";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data= JObjectConverter.convert(getData());
        JContext params = new JContext();

        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(params,data);
        @SuppressWarnings("unchecked")
        JExcelExportModel result = (JExcelExportModel)visitor.visit(tree);
    }
    @Test
    public void testFormulaApplication() throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", i);
            row.put("value", i * 10);
            row.put("category", i > 5 ? "A" : "B");
            data.add(row);
        }
        Workbook workbook = new XSSFWorkbook();
        ExcelFormulaFactory formulaFactory = new ExcelFormulaFactory(workbook);
        String configText = "EXPORT FROM data TO \"d://test//output.xlsx\" WITH FORMULAS =\n" +
                " {   ROW 12: \"SUM(A{rowNum}:A{rowNum})\",   " +
                "COL C: \"IF(A1>5,\\\"Large\\\",\\\"Small\\\")\", " +
                "D1 : \"VLOOKUP(A1,A1:B10,2,FALSE)\",   " +
                "ROW 13: \"AVERAGE(B1:B10)\" }";
        System.out.println(configText);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(configText));
        JQuickExcelParser parser = new JQuickExcelParser(new CommonTokenStream(lexer));
        JQuickExcelParser.ExportConfigContext exportConfig = parser.exportConfig();
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(data);
        visitor.visitExportConfig(exportConfig);
        Sheet sheet = workbook.getSheetAt(0);
        // 验证行公式
        Row sumRow = sheet.getRow(10); // 第11行(0-based)
        Cell sumCell = sumRow.getCell(0);
        System.out.println("SUM公式: " + sumCell.getCellFormula());
        System.out.println("SUM结果: " + formulaFactory.evaluateFormula(sumCell));
        for (int i = 0; i < 10; i++) {
            Cell ifCell = sheet.getRow(i).getCell(2); // C列
            System.out.printf("C%d公式: %s, 结果: %s%n",
                    i+1,
                    ifCell.getCellFormula(),
                    formulaFactory.evaluateFormula(ifCell));
        }

        // 验证单元格公式
        Cell vlookupCell = sheet.getRow(0).getCell(3); // D1
        System.out.println("D1公式: " + vlookupCell.getCellFormula());
        System.out.println("D1结果: " + formulaFactory.evaluateFormula(vlookupCell));

        // 验证第二个行公式
        Row avgRow = sheet.getRow(11); // 第12行
        Cell avgCell = avgRow.getCell(0);
        System.out.println("AVERAGE公式: " + avgCell.getCellFormula());
        System.out.println("AVERAGE结果: " + formulaFactory.evaluateFormula(avgCell));

        workbook.close();
    }
}
