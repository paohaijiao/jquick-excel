package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JupperFormula extends JAbstractExcelFormula {

    private final String text;

    public JupperFormula(String text) {
        super("UPPER(" + text + ")");
        this.text = text;
    }
    
}
