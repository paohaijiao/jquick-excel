package lexer;

import com.paohaijiao.javelin.parser.JQuickExcelLexer;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class JFieldMappingTest {

    @Test
    public void fieldMapping1() {
        String input = " \"name\": \"姓名\"";
        System.out.println( input );
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.fieldMapping();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void fieldMapping2() {
        String input = " \"name\": length(12340)";
        System.out.println( input );
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.fieldMapping();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void cellFormat() {
        String input = " \"name\": DATE('12340')";
        System.out.println( input );
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.cellFormat();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void transformRule() {
        String input = " \"name\": length('12340')";
        System.out.println( input );
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.transformRule();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void transformValue() {
        String input = "  length('12340')";
        System.out.println( input );
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.transformValue();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
}
