package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JHourFormula extends JAbstractExcelFormula {
    private final String serial_number;

    public JHourFormula(String serial_number) {
        super("HOUR(" + serial_number + ")");
        this.serial_number = serial_number;
    }

}
