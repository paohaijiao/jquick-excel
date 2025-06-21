package lexer.data;

import com.paohaijiao.javelin.model.JExcelExportModel;
import com.paohaijiao.javelin.model.JStudentModel;
import com.paohaijiao.javelin.param.JContext;
import com.paohaijiao.javelin.parser.JQuickExcelLexer;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.util.JObjectConverter;
import com.paohaijiao.javelin.visitor.JQuickExcelExportVisitor;
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
        JQuickExcelExportVisitor visitor = new JQuickExcelExportVisitor(data);
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
        JQuickExcelExportVisitor visitor = new JQuickExcelExportVisitor(data);
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

        JQuickExcelExportVisitor visitor = new JQuickExcelExportVisitor(params,data);
        @SuppressWarnings("unchecked")
        JExcelExportModel result = (JExcelExportModel)visitor.visit(tree);

    }

}
