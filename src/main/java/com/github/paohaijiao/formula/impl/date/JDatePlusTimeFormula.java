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

/**
 * packageName com.github.paohaijiao.formula.impl.date
 *
 * @author Martin
 * @version 1.0.0
 * @className JTodayFormula
 * @date 2025/7/4
 * @description
 */
public class JDatePlusTimeFormula extends JAbstractExcelFormula {
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;
    public JDatePlusTimeFormula(String year, String month, String day, String hour , String minute, String second) {
        super(String.format("DATE(%s,%s,%s) + TIME(%s,%s,%s)",year,month,day,hour,minute,second));
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
}
