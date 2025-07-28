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
import com.github.paohaijiao.validate.impl.string.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * packageName com.github.paohaijiao.factory
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JExcelValidationRuleFactory {

    public static JBooleanRule booleanRule(boolean required, Map<String,Object> map, String customMessage) {
        return new JBooleanRule(required,map, customMessage);
    }

    public static JRequiredRule required(boolean required, Map<String,Object> map, String customMessage) {
        return new JRequiredRule(required,map, customMessage);
    }

    public static JDateFormatRule dateFormat(boolean required, Map<String,Object> map, String customMessage) {
        return new JDateFormatRule(required,map, customMessage);
    }
    public static JMinDateRule minDate(boolean required, Map<String,Object> map, String customMessage) {
        return new JMinDateRule(required,map, customMessage);
    }
    public static JMaxDateRule maxDate(boolean required, Map<String,Object> map, String customMessage) {
        return new JMaxDateRule(required,map, customMessage);
    }
    public static JDecimalRule decimal(boolean required, Map<String,Object> map, String customMessage) {
        return new JDecimalRule(required,map, customMessage);
    }
    public static JIntegerRule integer(boolean required, Map<String,Object> map, String customMessage) {
        return new JIntegerRule(required,map, customMessage);
    }
    public static JMaxValueRule maxValue(boolean required, Map<String,Object> map, String customMessage) {
        return new JMaxValueRule(required,map, customMessage);
    }
    public static JMinValueRule minValue(boolean required, Map<String,Object> map, String customMessage) {
        return new JMinValueRule(required,map, customMessage);
    }

    public static JContainWithRule containWith(boolean required, Map<String,Object> map, String customMessage) {
        return new JContainWithRule(required,map, customMessage);
    }
    public static JEndWithRule endsWith(boolean required, Map<String,Object> map, String customMessage) {
        return new JEndWithRule(required,map, customMessage);
    }
    public static JMaxLengthRule maxLength(boolean required, Map<String,Object> map, String customMessage) {
        return new JMaxLengthRule(required,map, customMessage);
    }
    public static JMinLengthRule minLength(boolean required, Map<String,Object> map, String customMessage) {
        return new JMinLengthRule(required,map, customMessage);
    }
    public static JNotContainWithRule notContainWith(boolean required, Map<String,Object> map, String customMessage) {
        return new JNotContainWithRule(required,map, customMessage);
    }
    public static JNotEndWithRule notEndWith(boolean required, Map<String,Object> map, String customMessage) {
        return new JNotEndWithRule(required,map, customMessage);
    }

    public static JNotStartWithRule notStart(boolean required, Map<String,Object> map, String customMessage) {
        return new JNotStartWithRule(required,map, customMessage);
    }

    public static JRegexRule regex(boolean required, Map<String,Object> map, String customMessage) {
        return new JRegexRule(required,map, customMessage);
    }
    public static JStartWithRule startWith(boolean required, Map<String,Object> map, String customMessage) {
        return new JStartWithRule(required,map, customMessage);
    }

    public static JEmailRule email(boolean required, Map<String,Object> map, String customMessage) {
        return new JEmailRule(required,map, customMessage);
    }
    public static JMobileRule mobile(boolean required, Map<String,Object> map, String customMessage) {
        return new JMobileRule(required,map, customMessage);
    }

    public static JDictRule dict(boolean required, Map<String,Object> map, String customMessage) {
        return new JDictRule(required,map, customMessage);
    }


    public static JCompositeRule composite() {
        return new JCompositeRule();
    }





}
