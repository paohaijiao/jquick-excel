package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JSubstituteFormula extends JAbstractExcelFormula {



    public JSubstituteFormula(String text, String old_text,String new_text) {
        super("SUBSTITUTE(\"" + text +"\",\"" + old_text +"\",\""+new_text+ "\")");
        System.out.println("SUBSTITUTE(" + text +"," + old_text +","+new_text+ ")");
    }

    public JSubstituteFormula(String text, String old_text, String new_text, BigDecimal instance_num) {
        super("SUBSTITUTE(\"" + text +"\",\"" + old_text +"\",\""+new_text+"\","+instance_num.toPlainString()+ ")");
        System.out.println("SUBSTITUTE(" + text +"," + old_text +","+new_text+","+instance_num.toPlainString()+ ")");
    }

    
}
