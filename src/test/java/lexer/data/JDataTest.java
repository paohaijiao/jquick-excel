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
package lexer.data;

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
import org.junit.Test;

import java.util.*;

public class JDataTest {

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
                "        \"Amount\": NUMBER('¥#,##0.00'),\n" +
                "        \"Date\": DATE('yyyy年mm月dd日')\n" +
                "    },\n" +
                "    FORMULAS = {\n" +
                "        \"YTDTotal\": \"SUM(D4:D15)\",\n" +
                "        \"YoYGrowth\": \"TEXT((D15-D4)/D4,\\\"0.00%\\\")\"\n" +
                "    }\n";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data= JObjectConverter.convert(getData());
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(data);
        @SuppressWarnings("unchecked")
        JExcelExportModel result = (JExcelExportModel)visitor.visit(tree);

    }
    @Test
    public void mapping() {
        String input = "EXPORT FROM annual_report TO \"d://test//report_2023.xlsx\" WITH\n" +
                "    SHEET=\"年度汇总\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    MAPPING= {\n" +
                "        \"name\": \"姓名\",\n" +
                "        \"gender\":\"性别\"\n" +
                "    },\n" +
                "    FORMAT = {\n" +
                "        \"name\": NUMBER('¥#,##0.00'),\n" +
                "        \"gender\": DATE('yyyy年mm月dd日')\n" +
                "    },\n" +
                "    FORMULAS = {\n" +
                "        \"YTDTotal\": \"SUM(D4:D15)\",\n" +
                "        \"YoYGrowth\": \"TEXT((D15-D4)/D4,\\\"0.00%\\\")\"\n" +
                "    }";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data= JObjectConverter.convert(getData());
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(data);
        @SuppressWarnings("unchecked")
        JExcelExportModel result = (JExcelExportModel)visitor.visit(tree);

    }
    @Test
    public void trans() {
        String input = "\n" +
                "EXPORT FROM annual_report TO \"d://test//report_2023.xlsx\" WITH\n" +
                "    SHEET=\"年度汇总\",\n" +
                "    HEADER='YES',\n" +
                "    RANGE=\"A3\",\n" +
                "    MAPPING= {\n" +
                "        \"name\": \"姓名\",\n" +
                "        \"gender\":\"性别\",\n" +
                "        \"enrollmentDate\":\"入校日期\"\n" +
                "    },\n" +
                "\tTRANSFORM= {\n" +
                "        \"gender\":trans(${codeTable},${gender}),\n" +
                "        \"enrollmentDate\":dateFormat(${enrollmentDate},'yyyy-MM-dd')\n" +
                "    }";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.exportConfig();
        List<Map<String, Object>> data= JObjectConverter.convert(getData());
        HashMap<String,Object> variable=new HashMap<>();
        JContext params = new JContext();
        variable.put("1","男");
        variable.put("2","女");
        params.put("codeTable",variable);

        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(params,data);
        @SuppressWarnings("unchecked")
        JExcelExportModel result = (JExcelExportModel)visitor.visit(tree);

    }

}
