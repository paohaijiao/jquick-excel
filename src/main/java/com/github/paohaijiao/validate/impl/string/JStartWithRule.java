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
package com.github.paohaijiao.validate.impl.string;

import com.github.paohaijiao.validate.JAbstractValidationRule;

/**
 * packageName com.github.paohaijiao.validate
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JStartWithRule extends JAbstractValidationRule {

    private String ruleValue;

    public JStartWithRule(String ruleValue, boolean required) {
        super(required);
        this.ruleValue = ruleValue;
    }

    @Override
    protected boolean doValidate(String value) {
        return value.startsWith(ruleValue);
    }

    @Override
    public String getErrorMessage() {
        return String.format("the value should start with %s characters\n", ruleValue);
    }
}
