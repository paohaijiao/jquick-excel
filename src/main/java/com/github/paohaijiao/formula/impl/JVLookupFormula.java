package com.github.paohaijiao.formula.impl;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

import java.math.BigDecimal;

public class JVLookupFormula extends JAbstractExcelFormula {


    public JVLookupFormula(String lookupValue, String tableArray,
                           BigDecimal colIndex, boolean exactMatch) {
        super(String.format("VLOOKUP(%s, %s, %d, %s)",
                lookupValue, tableArray, colIndex.toPlainString(), exactMatch ? "TRUE" : "FALSE"));
    }
}
