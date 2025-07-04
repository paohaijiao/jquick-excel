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
package com.github.paohaijiao.css;

import com.github.paohaijiao.factory.JExcelFormulaFactory;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class JExcelExportFormulatTest {

    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
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
        JExcelFormulaFactory formulaFactory = new JExcelFormulaFactory(workbook);
        String configText = "EXPORT FROM data TO \"d://test//output.xlsx\" WITH FORMULAS =\n" +
                " {   ROW 12: \"SUM(A${rowNum}:A${rowNum})\",   " +
                "COL E : \"IF(A${rowNum}>5,A1,A2)\", " +
                "D1 : \"VLOOKUP(A1,A1:B10,2,FALSE)\",   " +
                "ROW 13: \"AVERAGE(B1:B10)\" }";
        System.out.println(configText);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(configText));
        JQuickExcelParser parser = new JQuickExcelParser(new CommonTokenStream(lexer));
        JQuickExcelParser.ExportConfigContext exportConfig = parser.exportConfig();
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(data);
        visitor.visitExportConfig(exportConfig);
    }
}
