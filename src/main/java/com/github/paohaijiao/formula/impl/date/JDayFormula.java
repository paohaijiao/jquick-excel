package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JDayFormula extends JAbstractExcelFormula {

    public JDayFormula(String date) {
        super("DAY(" + date + ")");
    }

}
