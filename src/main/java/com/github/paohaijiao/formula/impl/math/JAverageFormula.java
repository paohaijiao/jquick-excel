package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JAverageFormula extends JAbstractExcelFormula {

    private final String range;

    public JAverageFormula(String range) {
        super("AVERAGE(" + range + ")");
        this.range = range;
    }

    public String getRange() {
        return range;
    }
}
