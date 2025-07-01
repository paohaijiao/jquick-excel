package com.github.paohaijiao.formula.impl;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JVLookupFormula extends JAbstractExcelFormula {
    private final String lookupValue;
    private final String tableArray;
    private final int colIndex;
    private final boolean exactMatch;

    public JVLookupFormula(String lookupValue, String tableArray,
                          int colIndex, boolean exactMatch) {
        super(String.format("VLOOKUP(%s, %s, %d, %s)",
                lookupValue, tableArray, colIndex, exactMatch ? "TRUE" : "FALSE"));
        this.lookupValue = lookupValue;
        this.tableArray = tableArray;
        this.colIndex = colIndex;
        this.exactMatch = exactMatch;
    }
    public boolean isExactMatch() {
        return exactMatch;
    }
}
