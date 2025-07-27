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

import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelCommonImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class JFieldMappingTest {

    @Test
    public void fieldMapping1() {
        String input = " \"name\": \"姓名\"";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.fieldMapping();
        JQuickExcelCommonImportVisitor visitor = new JQuickExcelCommonImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }

    @Test
    public void fieldMapping2() {
        String input = " \"name\": length(12340)";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.fieldMapping();
        JQuickExcelCommonImportVisitor visitor = new JQuickExcelCommonImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }

    @Test
    public void cellFormat() {
        String input = " \"name\": DATE('12340')";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.cellFormat();
        JQuickExcelCommonImportVisitor visitor = new JQuickExcelCommonImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }

    @Test
    public void transformRule() {
        String input = " \"name\": length('12340')";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.transformRule();
        JQuickExcelCommonImportVisitor visitor = new JQuickExcelCommonImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }

    @Test
    public void transformValue() {
        String input = "  length('12340')";
        System.out.println(input);
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        ParseTree tree = parser.transformValue();
        JQuickExcelCommonImportVisitor visitor = new JQuickExcelCommonImportVisitor();
        Object result = visitor.visit(tree);
        System.out.println(result);
    }
}
