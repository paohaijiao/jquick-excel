package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JCountFormula extends JAbstractExcelFormula {
    private final String range;

    public JCountFormula(String range) {
        super("COUNT(" + range + ")");
        this.range = range;
    }

    public String getRange() {
        return range;
    }
}
