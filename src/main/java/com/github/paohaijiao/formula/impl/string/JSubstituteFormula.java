package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JSubstituteFormula extends JAbstractExcelFormula {


    private  String text;
    private  String old_text;
    private  String new_text;
    private  Integer startNum;

    public JSubstituteFormula(String text, String old_text,String new_text) {
        super("SUBSTITUTE(" + text +"," + old_text +","+new_text+ ")");
        this.text = text;
        this.old_text = old_text;
        this.new_text = new_text;
    }

    public JSubstituteFormula(String text, String old_text,String new_text,Integer instance_num) {
        super("SUBSTITUTE(" + text +"," + old_text +","+new_text+","+instance_num+ ")");
        this.text = text;
        this.old_text = old_text;
        this.new_text = new_text;
    }

    
}
