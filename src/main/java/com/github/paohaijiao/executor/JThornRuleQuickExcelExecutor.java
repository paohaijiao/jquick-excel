package com.github.paohaijiao.executor;

import com.github.paohaijiao.antlr.impl.JAbstractAntlrExecutor;
import com.github.paohaijiao.exception.JAntlrExecutionException;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelCoreVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

public class JThornRuleQuickExcelExecutor extends JAbstractAntlrExecutor<String, Object> {
    @Override
    protected Lexer createLexer(CharStream input) {
        return new JQuickExcelLexer(input);
    }

    @Override
    protected Parser createParser(TokenStream tokens) {
        return new JQuickExcelParser(tokens);
    }

    @Override
    protected Object parse(Parser parser) throws JAntlrExecutionException {
        JQuickExcelParser calcParser = (JQuickExcelParser) parser;
        JQuickExcelParser.ConfigContext tree = calcParser.config();
        JQuickExcelCoreVisitor visitor = new JQuickExcelCoreVisitor();
        return visitor.visit(tree);
    }

}
