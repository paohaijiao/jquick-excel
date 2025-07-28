package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JValueFormula extends JAbstractExcelFormula {

    private final String text;

    public JValueFormula(String text) {
        super("VALUE(\"" + text + "\")");
        this.text = text;
    }
    
}
