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


public class JQuickExcelExportXmlParseFactory implements JQuickParseHandler {

    private JContext context=new JContext();

    private JConsole console=new JConsole();

    private List<JQuickRow> data=new ArrayList<>();

    private OutputStream outputStream;

    private String theme=null;


    public JQuickExcelExportXmlParseFactory(List<JQuickRow> rows, OutputStream os) {
        this(null,null,rows,os);
    }
    public JQuickExcelExportXmlParseFactory(String theme,List<JQuickRow> rows, OutputStream os) {
        this(theme,null,rows,os);
    }
    public JQuickExcelExportXmlParseFactory(JContext jcontext,List<JQuickRow> rows, OutputStream os) {
      this(null,jcontext,rows,os);
    }

    public JQuickExcelExportXmlParseFactory(String theme,JContext jcontext , List<JQuickRow> rows, OutputStream os) {
        JAssert.notNull(rows,"rows required not null");
        JAssert.notNull(os,"OutputStream required not null");
        this.outputStream=os;
        this.data=rows;
        this.theme=theme;
        if (null!=jcontext&&!jcontext.isEmpty()){
            context.putAll(jcontext);
        }
    }

    @Override
    public JQuickXmlElement createJQuickXmlElement() {
        return new JQuickJavaExcelElement();
    }

    @Override
    public JQuickXmlInvocationHandler createlInvocationHandler() {
        return new JQuickExcelExportXmlInvocationHandler(theme,context,data,outputStream);
    }
}

