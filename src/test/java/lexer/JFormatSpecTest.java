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
package lexer;

import com.paohaijiao.javelin.parser.JQuickExcelLexer;
import com.paohaijiao.javelin.parser.JQuickExcelParser;
import com.paohaijiao.javelin.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

/**
 * packageName lexer
 *
 * @author Martin
 * @version 1.0.0
 * @className JFormatSpecTest
 * @date 2025/6/18
 * @description
 */
public class JFormatSpecTest {
    @Test
    public void CUSTOM() {
        String input = "CUSTOM('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.customFormat();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void stringFormat() {
        String input = "STRING('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.stringFormat();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void numberFormat() {
        String input = "NUMBER('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.numberFormat();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void dateFormat() {
        String input = "DATE('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.dateFormat();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void formatSpec() {
        String input = "DATE('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.formatSpec();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void formatSpec1() {
        String input = "NUMBER('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.formatSpec();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void formatSpec2() {
        String input = "STRING('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.formatSpec();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
    @Test
    public void formatSpec3() {
        String input = "CUSTOM('hahaijiao')";
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.formatSpec();
        JQuickExcelImportVisitor visitor = new JQuickExcelImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
}
