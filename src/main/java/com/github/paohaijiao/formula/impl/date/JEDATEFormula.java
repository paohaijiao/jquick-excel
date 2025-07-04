package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JEDATEFormula extends JAbstractExcelFormula {
    private final String start_date;
    private final String months;

    public JEDATEFormula(String start_date, String months) {
        super("EDATE(" + start_date +","+months+ ")");
        this.start_date = start_date;
        this.months = months;
    }

}
