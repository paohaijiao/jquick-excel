package com.github.paohaijiao.formula.impl.judge;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JIfFormula extends JAbstractExcelFormula {

    private final String condition;
    private final String value;
    private final String otherValue;

    public JIfFormula(String condition, String value, String otherValue) {
        super(String.format("IF(%s, \"%s\", \"%s\")", condition, value, otherValue));
        this.condition = condition;
        this.value = value;
        this.otherValue = otherValue;
    }

    public String getCondition() {
        return condition;
    }

}
