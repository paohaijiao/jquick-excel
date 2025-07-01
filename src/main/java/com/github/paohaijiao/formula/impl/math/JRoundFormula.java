package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JRoundFormula extends JAbstractExcelFormula {
    private final String range;

    public JRoundFormula(String range) {
        super("ROUND(" + range + ")");
        this.range = range;
    }

    public String getRange() {
        return range;
    }
}
