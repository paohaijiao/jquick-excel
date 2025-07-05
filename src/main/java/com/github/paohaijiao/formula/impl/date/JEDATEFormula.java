package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JEDATEFormula extends JAbstractExcelFormula {

    public JEDATEFormula(String start_date, BigDecimal months) {
        super("EDATE(" + start_date +","+months.toPlainString()+ ")");

    }

}
