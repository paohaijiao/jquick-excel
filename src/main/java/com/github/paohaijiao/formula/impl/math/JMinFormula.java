package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JMinFormula extends JAbstractExcelFormula {
    private final String range;

    public JMinFormula(String range) {
        super("MIN(" + range + ")");
        this.range = range;
    }

    public String getRange() {
        return range;
    }
}
