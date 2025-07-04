package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Data;
import lombok.Getter;

@Getter
public class JYearFormula extends JAbstractExcelFormula {
    private final String serial_number;

    public JYearFormula(String serial_number) {
        super("YEAR(" + serial_number + ")");
        this.serial_number = serial_number;
    }

}
