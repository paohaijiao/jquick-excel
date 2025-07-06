package com.github.paohaijiao.enums;

import com.github.paohaijiao.exception.JAssert;
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

public enum JMethodValidationRuleType {
    BOOLEAN("boolean", JBooleanRule.class),
    DATE_FORMAT("date_format", JDateFormatRule.class),
    MAX_DATE("max_date", JMaxDateRule.class),
    MIN_DATE("min_date", JMinDateRule.class),
    INTEGER("integer", JIntegerRule.class),
    DECIMAL("decimal", JDecimalRule.class),
    MAX_VALUE("max_value", JMaxValueRule.class),
    MIN_VALUE("min_value", JMinValueRule.class),
    DICT("dict", JDictRule.class),
    EMAIL("email", JEmailRule.class),
    MOBILE("mobile", JMobileRule.class),
    MAX_LENGTH("max_length",JMaxLengthRule.class),
    MIN_LENGTH("min_length", JMinLengthRule.class),
    REGEX("min_length", JRegexRule.class),
    START_WITH("start_with", JStartWithRule.class),
    NOT_START_WITH("not_start_with", JNotStartWithRule.class),
    END_WITH("end_with", JEndWithRule.class),
    NOT_END_WITH("not_end_with", JNotEndWithRule.class),
    CONTAIN("contain", JContainWithRule.class),
    NOTCONTAIN("not_contain", JNotContainWithRule.class);

    private String key;

    private Class clazz;

    private JMethodValidationRuleType(String key, Class clazz) {
        this.key=key;
        this.clazz=clazz;
    }
    public static JMethodValidationRuleType codeOf(String code) {
        for (JMethodValidationRuleType type : values()) {
            if (type.key.equals(code)) {
                return type;
            }
        }
        JAssert.throwNewException("no such method validation rule type");
        return null;
    }
}
