package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JMinuteFormula extends JAbstractExcelFormula {
    private final String serial_number;

    public JMinuteFormula(String serial_number) {
        super("MINUTE(" + serial_number + ")");
        this.serial_number = serial_number;
    }

}
