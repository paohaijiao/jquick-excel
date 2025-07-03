package com.github.paohaijiao.enums;

import lombok.Getter;

@Getter
public enum JFormulaType {

    row("row","row"),

    col("col","col");

    private String code;

    private String name;

    private JFormulaType(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public static JFormulaType codeOf(String code) {
        for (JFormulaType jFormulaType : JFormulaType.values()) {
            if (jFormulaType.getCode().equals(code)) {
                return jFormulaType;
            }
        }
        return null;
    }
}
