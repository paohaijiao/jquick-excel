package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JWorkDayFormula extends JAbstractExcelFormula {
    private  String start_date;
    private  String days;
    private  String months;
    private  String holidays;


    public JWorkDayFormula(String start_date, String days) {
        super("WORKDAY(" + start_date +","+days+ ")");
        this.start_date = start_date;
        this.days = days;
    }
    public JWorkDayFormula(String start_date, String days,String holidays) {
        super("WORKDAY(" + start_date +","+days+","+holidays +")");
        this.start_date = start_date;
        this.days = days;
        this.holidays = holidays;
    }

}
