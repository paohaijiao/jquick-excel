package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JWeekNumFormula extends JAbstractExcelFormula {

    public JWeekNumFormula(String serial_number, BigDecimal num) {
        super("WEEKNUM(" + serial_number +","+num.toPlainString()+ ")");
    }

}
