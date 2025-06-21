package lexer.common;

import com.paohaijiao.javelin.param.JContext;
import com.paohaijiao.javelin.parser.JQuickExcelLexer;
import com.paohaijiao.javelin.parser.JQuickExcelParser;

import com.paohaijiao.javelin.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class JMethodCallTest
{
    @Test
    public void fileName() {
        String input = "'d://test/a.txt'";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.fileName();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void identifier() {
        String input = "id";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.identifier();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void functionArg() {
        String input = "${id}";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.functionArg();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void string() {
        String input = "'xsaxsa'";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.functionArg();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void number() {
        String input = "1234.23432";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.functionArg();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void identify() {
        String input = "id";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.identifier();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void variable() {
        String input = "${id}";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.variable();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void quotedFunctionCall() {
        String input = "'toInt(${id})'";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.functionArg();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void functionCall() {
        String input = "length(${id})";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.functionCall();
        JContext contextParams = new JContext();
        contextParams.put("id", "id1");
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor(contextParams);
        Object result = visitor.visit(tree);
        System.out.println(result);
    }

}
