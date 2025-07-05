
package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JTextFormula extends JAbstractExcelFormula {


    public JTextFormula(Object text,String format_text) {
        super("TEXT(" + text.toString()+ ","+format_text+")");
    }
    
}
