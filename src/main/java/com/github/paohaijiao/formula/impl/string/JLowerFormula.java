package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JLowerFormula extends JAbstractExcelFormula {

    private final String text;

    public JLowerFormula(String text) {
        super("LOWER(" + text + ")");
        this.text = text;
    }
    
}
