package com.github.paohaijiao.formula.impl.judge;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JAndFormula extends JAbstractExcelFormula {
    private final String[] conditions;

    public JAndFormula(String... conditions) {
        super(buildFormulaString(conditions));
        this.conditions = conditions;
    }

    private static String buildFormulaString(String... conditions) {
        if (conditions == null || conditions.length == 0) {
            throw new IllegalArgumentException("and  function requires at least one parameter");
        }
        return "AND(" + String.join(",", conditions) + ")";
    }

    public int getConditionCount() {
        return conditions.length;
    }

    public String getCondition(int index) {
        return conditions[index];
    }

}
