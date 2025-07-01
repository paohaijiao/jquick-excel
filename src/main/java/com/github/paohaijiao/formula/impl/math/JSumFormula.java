package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JSumFormula extends JAbstractExcelFormula {
    private final String range;

    public JSumFormula(String range) {
        super("SUM(" + range + ")");
        this.range = range;
    }

    public String getRange() {
        return range;
    }
}
