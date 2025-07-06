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
package com.github.paohaijiao.factory;

import com.github.paohaijiao.validate.impl.JCompositeRule;
import com.github.paohaijiao.validate.impl.JRequiredRule;
import com.github.paohaijiao.validate.impl.bool.JBooleanRule;
import com.github.paohaijiao.validate.impl.date.JDateFormatRule;
import com.github.paohaijiao.validate.impl.date.JMaxDateRule;
import com.github.paohaijiao.validate.impl.date.JMinDateRule;
import com.github.paohaijiao.validate.impl.number.JDecimalRule;
import com.github.paohaijiao.validate.impl.number.JIntegerRule;
import com.github.paohaijiao.validate.impl.number.JMaxValueRule;
import com.github.paohaijiao.validate.impl.number.JMinValueRule;
import com.github.paohaijiao.validate.impl.other.JDictRule;
import com.github.paohaijiao.validate.impl.other.JEmailRule;
import com.github.paohaijiao.validate.impl.other.JMobileRule;
import com.github.paohaijiao.validate.impl.string.JMaxLengthRule;
import com.github.paohaijiao.validate.impl.string.JMinLengthRule;
import com.github.paohaijiao.validate.impl.string.JRegexRule;
import java.util.Date;
import java.util.Set;

/**
 * packageName com.github.paohaijiao.factory
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JExcelValidationRuleFactory {

    public static JRequiredRule required() {
        return new JRequiredRule();
    }

    public static JMinLengthRule minLength(int minLength, boolean required) {
        return new JMinLengthRule(minLength, required);
    }

    public static JMaxLengthRule maxLength(int maxLength, boolean required) {
        return new JMaxLengthRule(maxLength, required);
    }

    public static JRegexRule regex(String pattern, boolean required) {
        return new JRegexRule(pattern, required);
    }

    public static JIntegerRule integer(boolean required) {
        return new JIntegerRule(required);
    }

    public static JDecimalRule decimal(boolean required) {
        return new JDecimalRule(required);
    }

    public static JMinValueRule minValue(double minValue, boolean required) {
        return new JMinValueRule(minValue, required);
    }

    public static JMaxValueRule maxValue(double maxValue, boolean required) {
        return new JMaxValueRule(maxValue, required);
    }

    public static JBooleanRule booleanRule(String[] trueValues, String[] falseValues, boolean required) {
        return new JBooleanRule(trueValues, falseValues, required);
    }

    public static JDateFormatRule dateFormat(String format, boolean required) {
        return new JDateFormatRule(format, required);
    }

    public static JMinDateRule minDate(Date minDate, String format, boolean required) {
        return new JMinDateRule(minDate, format, required);
    }

    public static JMaxDateRule maxDate(Date maxDate, String format, boolean required) {
        return new JMaxDateRule(maxDate, format, required);
    }

    public static JCompositeRule composite() {
        return new JCompositeRule();
    }
    public static JEmailRule emailRule(String email, boolean required) {
        return new JEmailRule(email,required);
    }

    public static JMobileRule mobileRule(String mobile, boolean required) {
        return new JMobileRule(mobile,required);
    }

    public static JDictRule dictRule(String value, boolean required, Set<String> dict) {
        return new JDictRule(value,required,dict);
    }
}
