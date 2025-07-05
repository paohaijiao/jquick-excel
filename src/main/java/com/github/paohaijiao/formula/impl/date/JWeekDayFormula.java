package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JWeekDayFormula extends JAbstractExcelFormula {

    public JWeekDayFormula(String serial_number, BigDecimal num) {
        super("WEEKDAY(" + serial_number +","+num.toPlainString()+ ")");
    }

}
