package com.github.paohaijiao.enums;


import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.validate.JAbstractValidationRule;
import com.github.paohaijiao.validate.impl.JCompositeRule;
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
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Getter
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
    REGEX("regex", JRegexRule.class),
    START_WITH("start_with", JStartWithRule.class),
    NOT_START_WITH("not_start_with", JNotStartWithRule.class),
    END_WITH("end_with", JEndWithRule.class),
    NOT_END_WITH("not_end_with", JNotEndWithRule.class),
    CONTAIN("contain", JContainWithRule.class),
    NOTCONTAIN("not_contain", JNotContainWithRule.class),
    COMPOSITE("composite",JCompositeRule .class);

    private String key;

    private Class clazz;

    private JMethodValidationRuleType(String key, Class clazz) {
        this.key=key;
        this.clazz=clazz;
    }
    public static JMethodValidationRuleType codeOf(String code) {
        for (JMethodValidationRuleType type : values()) {
            if (type.key.equalsIgnoreCase(code)) {
                return type;
            }
        }
        List<String> method=new ArrayList<String>();
        for (JMethodValidationRuleType type : values()) {
            method.add(type.key);
        }
        JAssert.throwNewException("only follow method code is supported:"+ StringUtils.join(method,","));
        return null;
    }
}
