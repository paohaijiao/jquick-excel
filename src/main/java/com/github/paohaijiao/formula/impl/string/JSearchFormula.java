package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JSearchFormula extends JAbstractExcelFormula {


    private  String text;
    private  String withinText;
    private  Integer startNum;

    public JSearchFormula(String text, String withinText) {
        super("SEARCH(" + text +", " + withinText + ")");
        this.text = text;
        this.withinText = withinText;
    }

    public JSearchFormula(String text, String withinText, Integer startNum) {
        super("SEARCH(" + text +", " + withinText +","+startNum+ ")");
        this.text = text;
        this.withinText = withinText;
        this.startNum = startNum;
    }

    
}
