package test;

import com.github.paohaijiao.handler.JExcelProcessor;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JImLexerTest {

    private JExcelImportModel parseImportConfig(String input) {
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.importConfig();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        JExcelImportModel result = (JExcelImportModel)visitor.visit(tree);
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
        List<Map<String, String>> processor= new JExcelProcessor().importData(model);
        System.out.println(processor);
    }

}
