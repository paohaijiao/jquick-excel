package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JTrimFormula extends JAbstractExcelFormula {

    private final String text;

    public JTrimFormula(String text) {
        super("TRIM(" + text + ")");
        this.text = text;
    }
    
}
