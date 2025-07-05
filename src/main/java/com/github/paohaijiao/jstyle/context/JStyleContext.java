package com.github.paohaijiao.jstyle.context;

import com.github.paohaijiao.jstyle.strategry.IStyleStrategy;
import com.github.paohaijiao.jstyle.strategry.impl.JCellStyleStrategy;
import com.github.paohaijiao.jstyle.strategry.impl.JColumnStyleStrategy;
import com.github.paohaijiao.jstyle.strategry.impl.JRowStyleStrategy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class JStyleContext {

    private final Workbook workbook;

    private final Sheet sheet;

    private IStyleStrategy strategy;

    public JStyleContext(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
    }

    public void setStrategy(IStyleStrategy strategy) {
        this.strategy = strategy;
    }

    public void applyStyle(Map<String, Object> style) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy not set");
        }
        strategy.applyStyle(workbook, sheet, style);
    }

    public  JStyleContext forRow( int rowNum) {
        JStyleContext context = new JStyleContext(workbook, sheet);
        context.setStrategy(new JRowStyleStrategy(rowNum));
        return context;
    }

    public  JStyleContext forColumn( int colNum) {
        JStyleContext context = new JStyleContext(workbook, sheet);
        context.setStrategy(new JColumnStyleStrategy(colNum));
        return context;
    }

    public  JStyleContext forCell( String cellRef) {
        JStyleContext context = new JStyleContext(workbook, sheet);
        context.setStrategy(new JCellStyleStrategy(cellRef));
        return context;
    }

    public  JStyleContext forCell( int rowNum, int colNum) {
        JStyleContext context = new JStyleContext(workbook, sheet);
        context.setStrategy(new JCellStyleStrategy(rowNum, colNum));
        return context;
    }
}
