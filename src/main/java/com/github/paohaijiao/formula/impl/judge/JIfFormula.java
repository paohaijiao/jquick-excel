package com.github.paohaijiao.formula.impl.judge;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JIfFormula extends JAbstractExcelFormula {

    private final String condition;
    private final String trueValue;
    private final String falseValue;

    public JIfFormula(String condition, String trueValue, String falseValue) {
        super(String.format("IF(%s, %s, %s)", condition, trueValue, falseValue));
        this.condition = condition;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    public String getCondition() {
        return condition;
    }
}
