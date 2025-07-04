package com.github.paohaijiao.formula.context;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import com.github.paohaijiao.formula.JExcelFormula;
import com.github.paohaijiao.formula.impl.JCustomFormula;
import com.github.paohaijiao.formula.impl.JVLookupFormula;
import com.github.paohaijiao.formula.impl.judge.JAndFormula;
import com.github.paohaijiao.formula.impl.judge.JIfFormula;
import com.github.paohaijiao.formula.impl.math.JAverageFormula;
import com.github.paohaijiao.formula.impl.math.JSumFormula;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.util.Collection;

public class JExcelFormulaContext {

    private final Workbook workbook;

    private final FormulaEvaluator evaluator;


    public JExcelFormulaContext(Workbook workbook) {
        this.workbook = workbook;
        this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

    public Cell applyFormula(Sheet sheet, int rowNum, int colNum, JExcelFormula formula) {
        Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
        Cell cell = row.getCell(colNum) != null ? row.getCell(colNum) : row.createCell(colNum);
        cell.setCellFormula(formula.getFormula());
        return cell;
    }

    public Cell applyFormula(Sheet sheet, String cellRef, JExcelFormula formula) {
        CellReference ref = new CellReference(cellRef);
        return applyFormula(sheet, ref.getRow(), ref.getCol(), formula);
    }

    public Object evaluateFormula(Cell cell) {
        if (cell.getCellType() != CellType.FORMULA) {
            throw new IllegalArgumentException("Cell does not contain a formula");
        }

        CellValue cellValue = evaluator.evaluate(cell);
        return new JAbstractExcelFormula("") {
        }.evaluate(evaluator, cell);
    }

    public JSumFormula createSumFormula(String range) {
        return new JSumFormula(range);
    }

    public JAverageFormula createAverageFormula(String range) {
        return new JAverageFormula(range);
    }

    public JIfFormula createIfFormula(String condition, String trueValue, String falseValue) {
        return new JIfFormula(condition, trueValue, falseValue);
    }

    public JVLookupFormula createVLookupFormula(String lookupValue, String tableArray,
                                                int colIndex, boolean exactMatch) {
        return new JVLookupFormula(lookupValue, tableArray, colIndex, exactMatch);
    }

    public JCustomFormula createCustomFormula(String formula) {
        return new JCustomFormula(formula);
    }

    public void evaluateAll() {
        evaluator.evaluateAll();
    }

    public JAndFormula createAndFormula(String... conditions) {
        return new JAndFormula(conditions);
    }

    public JAndFormula createAndFormula(Collection<String> conditions) {
        return new JAndFormula(conditions.toArray(new String[0]));
    }
}
