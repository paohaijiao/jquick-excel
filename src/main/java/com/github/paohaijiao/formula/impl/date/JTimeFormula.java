package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class JTimeFormula extends JAbstractExcelFormula {



    public JTimeFormula(BigDecimal hour, BigDecimal minute, BigDecimal second) {
        super("TIME(" + hour.toPlainString() +","+minute.toPlainString()+","+second.toPlainString() +")");
    }

}
