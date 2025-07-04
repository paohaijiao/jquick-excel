package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

public class JConcatFormula extends JAbstractExcelFormula {

    private final String[] text;

    public JConcatFormula(String... text) {
        super(buildFormulaString(text));
        this.text = text;
    }

    private static String buildFormulaString(String... text) {
        if (text == null || text.length == 0) {
            throw new IllegalArgumentException("and  function requires at least one parameter");
        }
        return "CONCAT(" + String.join(",", text) + ")";
    }
    
}
