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
package com.github.paohaijiao.validate.impl.number;

import com.github.paohaijiao.validate.JAbstractValidationRule;

/**
 * packageName com.github.paohaijiao.validate.impl
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JMinValueRule extends JAbstractValidationRule {

    private final double minValue;

    public JMinValueRule(double minValue, boolean required) {
        super(required);
        this.minValue = minValue;
    }

    @Override
    protected boolean doValidate(String value) {
        try {
            double num = Double.parseDouble(value);
            return num >= minValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return String.format("the value cannot be less than%.2f", minValue);
    }
}
