package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JEOMONTHFormula extends JAbstractExcelFormula {

    public JEOMONTHFormula(String start_date, BigDecimal months) {
        super("EOMONTH(" + start_date +","+months.toPlainString()+ ")");
    }

}
