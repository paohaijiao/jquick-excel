package com.github.paohaijiao.enums;

import lombok.Getter;

@Getter
public enum JMergeValueType {
    MAX("MAX", "max"),
    MIN("MIN", "min"),
    VALUE("VALUE", "value"),
    AVG("AVG", "avg"),
    COUNT("COUNT", "count"),
    SUM("SUM", "sum"),
    CONCAT("CONCAT", "concat"),
    FIRST("FIRST", "first"),
    LAST("LAST", "last");
    private String code;

    private String name;

    private JMergeValueType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public  static JMergeValueType codeOf(String code) {
        for (JMergeValueType jMergeValueType : JMergeValueType.values()) {
            if (jMergeValueType.code.equals(code)) {
                return jMergeValueType;
            }
        }
        throw new RuntimeException("No JMergeValueType with code " + code + " found");
    }
}
