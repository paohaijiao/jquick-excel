package com.github.paohaijiao.formula;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class JAbstractExcelFormula implements JExcelFormula {

    protected final String formula;

    protected JAbstractExcelFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public String getFormula() {
        return formula;
    }

    @Override
    public Object evaluate(FormulaEvaluator evaluator, Cell cell) {
        CellValue cellValue = evaluator.evaluate(cell);
        switch (cellValue.getCellType()) {
            case NUMERIC:
                return cellValue.getNumberValue();
            case STRING:
                return cellValue.getStringValue();
            case BOOLEAN:
                return cellValue.getBooleanValue();
            case ERROR:
                return FormulaError.forInt(cellValue.getErrorValue()).getString();
            default:
                return null;
        }
    }
}
