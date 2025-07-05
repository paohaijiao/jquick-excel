package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JWorkDayFormula extends JAbstractExcelFormula {



    public JWorkDayFormula(String start_date, BigDecimal days) {
        super("WORKDAY(" + start_date +","+days.toPlainString()+ ")");
        System.out.println("WORKDAY(" + start_date +","+days.toPlainString()+ ")");
    }
    public JWorkDayFormula(String start_date, BigDecimal days,String holidays) {
        super("WORKDAY(" + start_date +","+days.toPlainString()+","+holidays +")");
        System.out.println("WORKDAY(" + start_date +","+days.toPlainString()+","+holidays +")");
    }

}
