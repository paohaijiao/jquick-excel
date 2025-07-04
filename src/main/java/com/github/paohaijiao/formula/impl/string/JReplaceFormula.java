package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

@Getter
public class JReplaceFormula extends JAbstractExcelFormula {


    private  String text;
    private  Integer start_num;
    private  Integer num_chars;
    private  String new_text;

    public JReplaceFormula(String text, Integer start_num, Integer num_chars,String new_text) {
        super("REPLACE(" + text +"," + start_num +","+num_chars+ ","+new_text+")");
        this.text = text;
        this.start_num = start_num;
        this.num_chars = num_chars;
        this.new_text = new_text;
    }


    
}
