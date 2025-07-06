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
package com.github.paohaijiao.validate.impl;

import com.github.paohaijiao.validate.JExcelValidationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName com.github.paohaijiao.validate.impl
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JCompositeRule implements JExcelValidationRule {

    private final List<JExcelValidationRule> rules = new ArrayList<>();

    public JCompositeRule addRule(JExcelValidationRule rule) {
        rules.add(rule);
        return this;
    }

    @Override
    public boolean test(String value) {
        for (JExcelValidationRule rule : rules) {
            if (!rule.test(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        StringBuilder sb = new StringBuilder();
        for (JExcelValidationRule rule : rules) {
            sb.append(rule.getErrorMessage()).append("; ");
        }
        return sb.toString();
    }
}
