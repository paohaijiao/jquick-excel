package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JWeekNumFormula extends JAbstractExcelFormula {
    private final String serial_number;
    private final String num;

    public JWeekNumFormula(String serial_number, String num) {
        super("WEEKNUM(" + serial_number +","+num+ ")");
        this.serial_number = serial_number;
        this.num = num;
    }

}
