package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JMonthFormula extends JAbstractExcelFormula {
    private final String serial_number;

    public JMonthFormula(String serial_number) {
        super("MONTH(" + serial_number + ")");
        this.serial_number = serial_number;
    }

}
