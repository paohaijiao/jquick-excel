package com.paohaijiao.javelin.enums;

public enum JQuickExcelEnums {
    EXPORT("EXPORT","EXPORT"),
    IMPORT("IMPORT","IMPORT");

    private String code;
    private String name;
    JQuickExcelEnums(String code,String name) {
        this.code = code;
        this.name = name;

    }
}
