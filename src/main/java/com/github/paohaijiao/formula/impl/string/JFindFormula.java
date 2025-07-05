package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JFindFormula extends JAbstractExcelFormula {


    private  String text;
    private  String withinText;
    private  Integer startNum;

    public JFindFormula(String text,String withinText) {
        super("FIND(" + text +", " + withinText + ")");
        System.out.println("FIND(" + text +", " + withinText + ")");
        this.text = text;
        this.withinText = withinText;
    }

    public JFindFormula(String text,String withinText,Integer startNum) {
        super("FIND(" + text +", " + withinText +","+startNum+ ")");
        this.text = text;
        this.withinText = withinText;
        this.startNum = startNum;
    }

    
}
