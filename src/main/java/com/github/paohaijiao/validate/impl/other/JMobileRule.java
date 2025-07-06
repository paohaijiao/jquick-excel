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
package com.github.paohaijiao.validate.impl.other;

import com.github.paohaijiao.validate.JAbstractValidationRule;
import com.github.paohaijiao.validate.JExcelValidationRule;

/**
 * packageName com.github.paohaijiao.validate.impl.other
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JMobileRule extends JAbstractValidationRule {

    private static final String CHINA_MOBILE_REGEX = "^1[3-9]\\d{9}$";

   private String mobile;

    public JMobileRule(String mobile, boolean required) {
        super(required);
        this.mobile=mobile;
    }

    @Override
    protected boolean doValidate(String value) {
        return value.matches(CHINA_MOBILE_REGEX);
    }

    @Override
    public String getErrorMessage() {
        return "invalid mobile number";
    }

}
