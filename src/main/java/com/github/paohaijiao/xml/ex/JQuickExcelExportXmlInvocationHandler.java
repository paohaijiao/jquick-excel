package com.github.paohaijiao.xml.ex;

import com.github.paohaijiao.console.JConsole;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.invocation.JQuickXmlInvocationHandler;
import com.github.paohaijiao.xml.util.ParamUtil;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JQuickExcelExportXmlInvocationHandler extends JQuickXmlInvocationHandler {

    private JConsole console=new JConsole();

    private  List<JQuickRow> data=new ArrayList<>();

    private OutputStream outputStream;

    private JContext context=new JContext();

    public JQuickExcelExportXmlInvocationHandler(List<JQuickRow> rows,OutputStream os) {
        JAssert.notNull(rows,"rows required not null");
        JAssert.notNull(os,"OutputStream required not null");
        this.outputStream=os;
        this.data=rows;
    }

    public JQuickExcelExportXmlInvocationHandler(JContext jcontext , List<JQuickRow> rows,OutputStream os) {
        JAssert.notNull(jcontext,"context required not null");
        JAssert.notNull(rows,"rows required not null");
        JAssert.notNull(os,"OutputStream required not null");
        this.outputStream=os;
        this.data=rows;
        this.context =jcontext;
        if (!jcontext.isEmpty()){
            context.putAll(jcontext);
        }
    }



    @Override
    protected Object loadResult(String lexerStr, JContext jcontext, Method method, Object[] args) {
        if(!jcontext.isEmpty()){
            context.putAll(jcontext);
        }
        Map<String, Object> map= ParamUtil.bindParams(method,args);
        if(!map.isEmpty()){
            context.putAll(jcontext);
        }
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(lexerStr);
        JExcelExportHandler handler = new JExcelExportHandler(config,context, data);
        Workbook workbook=handler.getWorkBook();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            console.error("write excel workbook error",e);
        }
        try {
            workbook.close();
        } catch (IOException e) {
            console.error("write excel workbook io error",e);
        }
        return null;

    }

}

