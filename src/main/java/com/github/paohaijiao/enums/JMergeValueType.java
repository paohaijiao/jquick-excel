package com.github.paohaijiao.enums;

import lombok.Getter;

@Getter
public enum JMergeValueType {
    MERGE_WITH_MAX("MERGE_WITH_MAX", "max"),
    MERGE_WITH_MIN("MERGE_WITH_MIN", "min"),
    MERGE_WITH_VALUE("MERGE_WITH_VALUE", "value"),
    MERGE_WITH_AVG("MERGE_WITH_AVG", "avg"),
    MERGE_WITH_COUNT("MERGE_WITH_COUNT", "count"),
    MERGE_WITH_SUM("MERGE_WITH_SUM", "sum"),
    MERGE_WITH_CONCAT("MERGE_WITH_CONCAT", "concat"),
    MERGE_WITH_FIRST("MERGE_WITH_FIRST", "first"),
    MERGE_WITH_LAST("MERGE_WITH_LAST", "last");
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
