package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JExactFormula extends JAbstractExcelFormula {

    private final String text;

    public JExactFormula(String text,String text1) {
        super("EXACT(" + text + ","+text1+")");
        this.text = text;
    }
    
}
