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
package com.github.paohaijiao.formula.impl.string;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import lombok.Getter;

/**
 * packageName com.github.paohaijiao.formula.impl.string
 *
 * @author Martin
 * @version 1.0.0
 * @className JLenFormula
 * @date 2025/7/4
 * @description
 */
@Getter
public class JMidFormula extends JAbstractExcelFormula {

    private final String text;
    private final Integer startNum;
    private final Integer numChars;

    public JMidFormula(String text, Integer startNum,Integer numChars) {
        
        super("MID(" + text +","+startNum+","+numChars+")");
        this.text = text;
        this.startNum = startNum;
        this.numChars = numChars;
    }


}
