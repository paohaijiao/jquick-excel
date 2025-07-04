package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JWeekDayFormula extends JAbstractExcelFormula {
    private final String serial_number;
    private final String num;

    public JWeekDayFormula(String serial_number,String num) {
        super("WEEKDAY(" + serial_number +","+num+ ")");
        this.serial_number = serial_number;
        this.num = num;
    }

}
