package com.paohaijiao.javelin.enums;

import com.paohaijiao.javelin.exception.JAssert;
import lombok.Getter;

@Getter
public enum JQuickExcelFormatSpecEnums {
    DATE("DATE","DATE"),
    STRING("STRING","STRING"),
    CUSTOM("CUSTOM","CUSTOM"),
    NUMBER("NUMBER","NUMBER");

    private String code;
    private String name;
    JQuickExcelFormatSpecEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public JQuickExcelFormatSpecEnums codeOf(String code) {
        for (JQuickExcelFormatSpecEnums e : JQuickExcelFormatSpecEnums.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        JAssert.throwNewException("invalid code");
        return null;
    }
}
