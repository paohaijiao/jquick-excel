package com.github.paohaijiao.formula.impl.judge;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class JAndFormula extends JAbstractExcelFormula {
    private final String[] conditions;

    public JAndFormula(String... conditions) {
        super(buildFormulaString(conditions));
        this.conditions = conditions;
    }

    private static String buildFormulaString(String... conditions) {
        if (conditions == null || conditions.length == 0) {
            throw new IllegalArgumentException("AND 函数至少需要一个参数");
        }
        return "AND(" + String.join(",", conditions) + ")";
    }

    public int getConditionCount() {
        return conditions.length;
    }

    public String getCondition(int index) {
        return conditions[index];
    }

    @Override
    public Object evaluate(FormulaEvaluator evaluator, Cell cell) {
        Object result = super.evaluate(evaluator, cell);
        if (result instanceof Boolean) {
            return result;
        }
        if (result instanceof Number) {
            return ((Number) result).doubleValue() != 0;
        }
        return false;
    }
}
