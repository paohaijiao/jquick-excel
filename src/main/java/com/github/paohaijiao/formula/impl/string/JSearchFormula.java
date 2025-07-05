package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JSearchFormula extends JAbstractExcelFormula {


    public JSearchFormula(String text, String withinText) {
        super("SEARCH(" + text +", " + withinText + ")");
        System.out.println("SEARCH(" + text +", " + withinText + ")");
    }

    public JSearchFormula(String text, String withinText, BigDecimal startNum) {
        super("SEARCH(" + text +", " + withinText +","+startNum.toPlainString()+ ")");
        System.out.println("SEARCH(" + text +", " + withinText +","+startNum.toPlainString()+ ")");
    }

    
}
