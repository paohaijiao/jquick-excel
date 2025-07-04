package com.github.paohaijiao.formula.impl.judge;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JORFormula extends JAbstractExcelFormula {
    private final String[] conditions;

    public JORFormula(String... conditions) {
        super(buildFormulaString(conditions));
        this.conditions = conditions;
    }

    private static String buildFormulaString(String... conditions) {
        if (conditions == null || conditions.length == 0) {
            throw new IllegalArgumentException("and  function requires at least one parameter");
        }
        return "OR(" + String.join(",", conditions) + ")";
    }

    public int getConditionCount() {
        return conditions.length;
    }

    public String getCondition(int index) {
        return conditions[index];
    }
    
}
