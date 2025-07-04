package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JDayFormula extends JAbstractExcelFormula {
    private final String serial_number;

    public JDayFormula(String serial_number) {
        super("DAY(" + serial_number + ")");
        this.serial_number = serial_number;
    }

}
