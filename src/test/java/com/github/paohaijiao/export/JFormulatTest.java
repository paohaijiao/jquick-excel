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
package com.github.paohaijiao.export;

import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.visitor.JQuickExcelComonExportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JFormulatTest {

    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }
    @Test
    public void formulat() throws IOException {
        String input = "EXPORT  WITH\n" +
                "    SHEET=\"年度汇总\",\n" +
                "    HEADER=true,\n" +
                "    ROW 5 = {\n" +
                "        \"YTDTotal\": \"SUM(D4:D15)\""+
                "    }\n";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//formulat.xlsx");
        JContext context=new JContext();
        JQuickExcelComonExportVisitor visitor = new JQuickExcelComonExportVisitor(context);
        JExcelExportModel result = (JExcelExportModel) visitor.visit(tree);
        List<Map<String, Object>> list = JObjectConverter.convert(getData());
        JExcelExportHandler excelExportHandler=new JExcelExportHandler(result,list);
        Workbook wb=excelExportHandler.getWorkBook();
        wb.write(fileOutputStream);
    }

}
