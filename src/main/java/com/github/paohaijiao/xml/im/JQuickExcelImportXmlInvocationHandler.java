package com.github.paohaijiao.xml.im;

import com.github.paohaijiao.console.JConsole;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.executor.JQuickExcelCommonImportExecutor;
import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.invocation.JQuickXmlInvocationHandler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JQuickExcelImportXmlInvocationHandler extends JQuickXmlInvocationHandler {

    private JConsole console = new JConsole();


    private InputStream inputStream;

    private JContext context = new JContext();

    public JQuickExcelImportXmlInvocationHandler(InputStream is) {
        JAssert.notNull(is, "InputStream required not null");
        this.inputStream = is;
    }

    public JQuickExcelImportXmlInvocationHandler(JContext jcontext, InputStream is) {
        JAssert.notNull(jcontext, "context required not null");
        JAssert.notNull(is, "InputStream required not null");
        this.inputStream = is;
        this.context = jcontext;
        if (!jcontext.isEmpty()) {
            context.putAll(jcontext);
        }
    }


    @Override
    protected Object loadResult(String lexerStr, JContext jcontext, Method method, Object[] args) {
        JQuickExcelCommonImportExecutor executor = new JQuickExcelCommonImportExecutor();
        JExcelImportModel model = (JExcelImportModel) executor.execute(lexerStr);
        try {
            // 大数据量导入：JExcelImportHandler 内部已根据 JQuickExcelConfig 自动采用 OPCPackage 共享解析
            JExcelImportHandler handler = new JExcelImportHandler(inputStream, context);
            List<JQuickRow> list = handler.importData(model);
            console.info("完成导入:" + list.size() + "项数据");
            return list;
        } catch (IOException e) {
            console.error("IO Exception occured", e);
        }
        return new ArrayList<JQuickRow>();

    }

}

