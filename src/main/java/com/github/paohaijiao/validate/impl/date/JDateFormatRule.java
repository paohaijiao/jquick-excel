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
package com.github.paohaijiao.validate.impl.date;

import com.github.paohaijiao.validate.JAbstractValidationRule;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * packageName com.github.paohaijiao.validate.impl
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JDateFormatRule extends JAbstractValidationRule {

    private final String format;

    public JDateFormatRule(String format, boolean required) {
        super(required);
        this.format = format;
    }

    @Override
    protected boolean doValidate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return String.format("the date format must be: %s", format);
    }
}
