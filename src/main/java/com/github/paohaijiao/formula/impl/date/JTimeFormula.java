package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JTimeFormula extends JAbstractExcelFormula {
    private  String hour;
    private  String minute;
    private  String second;


    public JTimeFormula(String start_date, String days, String holidays) {
        super("TIME(" + start_date +","+days+","+holidays +")");
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

}
