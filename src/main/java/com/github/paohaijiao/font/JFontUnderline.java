package com.github.paohaijiao.font;

import lombok.Getter;
import org.apache.poi.ss.usermodel.FontUnderline;
@Getter
public enum JFontUnderline {

    SINGLE("single", FontUnderline.SINGLE),
    DOUBLE("double", FontUnderline.DOUBLE),
    SINGLE_ACCOUNTING("singleAccounting", FontUnderline.SINGLE_ACCOUNTING),
    DOUBLE_ACCOUNTING("doubleAccounting",FontUnderline.DOUBLE_ACCOUNTING),
    NONE("none", FontUnderline.NONE),;

    private final String value;

    private final FontUnderline fontUnderline;


    private JFontUnderline(String value, FontUnderline fontUnderline) {
        this.value = value;
        this.fontUnderline = fontUnderline;
    }

    public static FontUnderline codeOf(String value) {
    for(JFontUnderline fontUnderline : values()) {
        if(fontUnderline.getValue().equals(value)) {
            return fontUnderline.fontUnderline;
        }
    }
        return FontUnderline.NONE;
    }

}
