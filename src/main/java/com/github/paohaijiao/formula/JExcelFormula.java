package com.github.paohaijiao.formula;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public interface JExcelFormula {

    String getFormula();

    Object evaluate(FormulaEvaluator evaluator, Cell cell);
}
