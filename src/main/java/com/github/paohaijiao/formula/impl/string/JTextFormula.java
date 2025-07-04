
package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JTextFormula extends JAbstractExcelFormula {

    private final String text;

    public JTextFormula(String text) {
        super("TEXT(" + text + ")");
        this.text = text;
    }
    
}
