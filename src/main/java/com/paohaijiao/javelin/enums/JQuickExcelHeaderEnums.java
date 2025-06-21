package com.paohaijiao.javelin.enums;

public enum JQuickExcelHeaderEnums {
    YES("1","YES"),
    NO("0","NO");

    private String code;
    private String name;
    JQuickExcelHeaderEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public static JQuickExcelHeaderEnums codeOf(String code) {
        for (JQuickExcelHeaderEnums e : JQuickExcelHeaderEnums.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static JQuickExcelHeaderEnums nameOf(String name) {
        for (JQuickExcelHeaderEnums e : JQuickExcelHeaderEnums.values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
