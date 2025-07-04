package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JProperFormula extends JAbstractExcelFormula {

    private final String text;

    public JProperFormula(String text) {
        super("PROPER(" + text + ")");
        this.text = text;
    }
    
}
