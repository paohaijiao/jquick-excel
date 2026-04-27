package com.github.paohaijiao.xml.im;

import com.github.paohaijiao.console.JConsole;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.JQuickJavaExcelElement;
import com.github.paohaijiao.xml.element.JQuickXmlElement;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.invocation.JQuickXmlInvocationHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class JQuickExcelImportXmlParseFactory implements JQuickParseHandler {
    private JConsole console=new JConsole();


    private InputStream inputStream;

    private JContext context=new JContext();

    public JQuickExcelImportXmlParseFactory( InputStream is) {
        JAssert.notNull(is,"InputStream required not null");
        this.inputStream=is;
    }

    public JQuickExcelImportXmlParseFactory(JContext jcontext , InputStream is) {
        JAssert.notNull(jcontext,"context required not null");
        JAssert.notNull(is,"InputStream required not null");
        this.inputStream=is;
        this.context =jcontext;
        if (!jcontext.isEmpty()){
            context.putAll(jcontext);
        }
    }

    @Override
    public JQuickXmlElement createJQuickXmlElement() {
        return new JQuickJavaExcelElement();
    }

    @Override
    public JQuickXmlInvocationHandler createlInvocationHandler() {
        return new JQuickExcelImportXmlInvocationHandler(context,inputStream);
    }
}

