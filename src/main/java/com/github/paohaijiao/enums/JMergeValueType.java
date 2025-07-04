package com.github.paohaijiao.enums;

import lombok.Getter;

@Getter
public enum JMergeValueType {
    MAX("max", "max"),
    MIN("min", "min"),
    VALUE("value", "value"),
    AVG("avg", "avg"),
    COUNT("count", "count"),
    SUM("sum", "sum"),
    CONCAT("concat", "concat"),
    FIRST("first", "first"),
    LAST("last", "last");
    private String code;

    private String name;

    private JMergeValueType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public JMergeValueType codeOf(String code) {
        for (JMergeValueType jMergeValueType : JMergeValueType.values()) {
            if (jMergeValueType.code.equals(code)) {
                return jMergeValueType;
            }
        }
        return null;
    }
}
