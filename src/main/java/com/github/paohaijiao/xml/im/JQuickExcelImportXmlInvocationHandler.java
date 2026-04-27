package com.github.paohaijiao.xml.im;

import com.github.paohaijiao.console.JConsole;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.executor.JQuickExcelCommonImportExecutor;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.invocation.JQuickXmlInvocationHandler;
import com.github.paohaijiao.xml.util.ParamUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JQuickExcelImportXmlInvocationHandler extends JQuickXmlInvocationHandler {

    private JConsole console=new JConsole();


    private InputStream inputStream;

    private JContext context=new JContext();

    public JQuickExcelImportXmlInvocationHandler( InputStream is) {
        JAssert.notNull(is,"InputStream required not null");
        this.inputStream=is;
    }

    public JQuickExcelImportXmlInvocationHandler(JContext jcontext , InputStream is) {
        JAssert.notNull(jcontext,"context required not null");
        JAssert.notNull(is,"InputStream required not null");
        this.inputStream=is;
        this.context =jcontext;
        if (!jcontext.isEmpty()){
            context.putAll(jcontext);
        }
    }



    @Override
    protected Object loadResult(String lexerStr, JContext jcontext, Method method, Object[] args) {
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(lexerStr);
        XSSFWorkbook workbook= null;
        try {
            workbook = new XSSFWorkbook(inputStream);
            JExcelImportHandler handler=new JExcelImportHandler(workbook,context);
            List<JQuickRow> list= handler.importData(model);
            console.info("完成导入:"+list.size()+"项数据");
            return list;
        } catch (IOException e) {
            console.error("IO Exception occured",e);
        }
        return new ArrayList<JQuickRow>();

    }

}

