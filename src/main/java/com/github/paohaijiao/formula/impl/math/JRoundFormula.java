package com.github.paohaijiao.formula.impl.math;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

import java.math.BigDecimal;

public class JRoundFormula extends JAbstractExcelFormula {


    public JRoundFormula(BigDecimal a1,BigDecimal a2) {
        super("ROUND(" + a1.toPlainString()+","+a2.toPlainString() + ")");
    }
}
