package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JMaxFormula extends JAbstractExcelFormula {
    private final String range;

    public JMaxFormula(String range) {
        super("MAX(" + range + ")");
        this.range = range;
    }

    public String getRange() {
        return range;
    }
}
