package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JSecondFormula extends JAbstractExcelFormula {
    private final String serial_number;

    public JSecondFormula(String serial_number) {
        super("SECOND(" + serial_number + ")");
        this.serial_number = serial_number;
    }

}
