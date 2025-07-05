package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JReplaceFormula extends JAbstractExcelFormula {


    public JReplaceFormula(String text, BigDecimal start_num, BigDecimal num_chars, String new_text) {
        super("REPLACE(" + text +"," + start_num.toPlainString()+","+num_chars.toPlainString()+ ","+new_text+")");
        System.out.println("REPLACE(" + text +"," + start_num.toPlainString()+","+num_chars.toPlainString()+ ","+new_text+")");
    }


    
}
