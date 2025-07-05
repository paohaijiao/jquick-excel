/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) [2025-2099] Martin (goudingcheng@gmail.com)
 */
package com.github.paohaijiao.formula.impl.date;

import com.github.paohaijiao.formula.JAbstractExcelFormula;

import java.math.BigDecimal;

/**
 * packageName com.github.paohaijiao.formula.impl.date
 *
 * @author Martin
 * @version 1.0.0
 * @className JTodayFormula
 * @date 2025/7/4
 * @description
 */
public class JDateTimeFormula extends JAbstractExcelFormula {

    public JDateTimeFormula(BigDecimal year, BigDecimal month, BigDecimal day, BigDecimal hour , BigDecimal minute, BigDecimal second) {
        super("DATETIME (" + year + "," + month + "," + day +","+hour+","+minute+","+second+ ")");
    }
}
