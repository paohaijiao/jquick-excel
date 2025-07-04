package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JSumIFFormula extends JAbstractExcelFormula {

    private final String range;
    private final String condition;

    public JSumIFFormula(String range, String condition) {
        super("SUM(" + range +","+condition+ ")");
        this.range = range;
        this.condition = condition;
    }

    public String getRange() {
        return range;
    }

    public String getCondition() {
        return condition;
    }
}
