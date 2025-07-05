package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JSumFormula extends JAbstractExcelFormula {

    public JSumFormula(String range) {
        super("SUM(" + range + ")");
    }
    public JSumFormula(String cell1,String cell2) {
        super("SUM(" + cell1 +","+cell2 +")");
    }
}
