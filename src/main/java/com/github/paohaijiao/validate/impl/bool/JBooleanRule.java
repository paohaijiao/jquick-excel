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
package com.github.paohaijiao.validate.impl.bool;

import com.github.paohaijiao.validate.JAbstractValidationRule;

import java.util.Arrays;

/**
 * packageName com.github.paohaijiao.validate.impl
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JBooleanRule extends JAbstractValidationRule {

    private final String[] trueValues;
    private final String[] falseValues;

    public JBooleanRule(String[] trueValues, String[] falseValues, boolean required) {
        super(required);
        this.trueValues = trueValues;
        this.falseValues = falseValues;
    }

    @Override
    protected boolean doValidate(String value) {
        for (String trueValue : trueValues) {
            if (value.equalsIgnoreCase(trueValue)) return true;
        }
        for (String falseValue : falseValues) {
            if (value.equalsIgnoreCase(falseValue)) return true;
        }
        return false;
    }

    @Override
    public String getErrorMessage() {
        return String.format("it must be a boolean value(true: %s, false: %s)",
                Arrays.toString(trueValues), Arrays.toString(falseValues));
    }
}
