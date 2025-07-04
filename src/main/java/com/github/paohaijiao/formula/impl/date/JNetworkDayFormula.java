package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JNetworkDayFormula extends JAbstractExcelFormula {
    private  String start_date;
    private  String days;
    private  String months;
    private  String holidays;


    public JNetworkDayFormula(String start_date, String days) {
        super("NETWORKDAYS(" + start_date +","+days+ ")");
        this.start_date = start_date;
        this.days = days;
    }
    public JNetworkDayFormula(String start_date, String days, String holidays) {
        super("NETWORKDAYS(" + start_date +","+days+","+holidays +")");
        this.start_date = start_date;
        this.days = days;
        this.holidays = holidays;
    }

}
