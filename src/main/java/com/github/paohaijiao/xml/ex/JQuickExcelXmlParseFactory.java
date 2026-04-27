package com.github.paohaijiao.xml.ex;

import com.github.paohaijiao.console.JConsole;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.JQuickJavaExcelElement;
import com.github.paohaijiao.xml.element.JQuickXmlElement;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.invocation.JQuickXmlInvocationHandler;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class JQuickExcelXmlParseFactory implements JQuickParseHandler {

    private JContext context=new JContext();

    private JConsole console=new JConsole();

    private List<JQuickRow> data=new ArrayList<>();

    private OutputStream outputStream;


    public JQuickExcelXmlParseFactory(List<JQuickRow> rows,OutputStream os) {
        JAssert.notNull(rows,"rows required not null");
        JAssert.notNull(os,"OutputStream required not null");
        this.outputStream=os;
        this.data=rows;
    }

    public JQuickExcelXmlParseFactory(JContext jcontext , List<JQuickRow> rows,OutputStream os) {
        JAssert.notNull(jcontext,"context required not null");
        JAssert.notNull(rows,"rows required not null");
        JAssert.notNull(os,"OutputStream required not null");
        this.outputStream=os;
        this.data=rows;
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
        return new JQuickExcelExportXmlInvocationHandler(context,data,outputStream);
    }
}

