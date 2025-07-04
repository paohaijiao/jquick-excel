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

import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class JValueTest {


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
