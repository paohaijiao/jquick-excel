package com.github.paohaijiao.font;

import com.github.paohaijiao.color.JColorEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum JFont {

    COLOR_NORMAL(Short.MAX_VALUE, "colorNormal"),
    COLOR_RED((short) 10, "colorRed"),
    SS_NONE((short) 0, "ssNone"),
    SS_SUPER((short) 1, "ssSuper"),
    SS_SUB((short) 2, "ssSub"),
    U_NONE((byte) 0, "uNone"),
    U_SINGLE((byte) 1, "uSingle"),
    U_DOUBLE((byte) 2, "uDouble"),
    U_SINGLE_ACCOUNTING((byte) 33, "uSingleAccounting"),
    U_DOUBLE_ACCOUNTING((byte) 34, "uDoubleAccounting"),
    ANSI_CHARSET((byte) 0, "ansiCharset"),
    DEFAULT_CHARSET((byte) 1, "defaultCharset"),
    SYMBOL_CHARSET((byte) 2, "symbolCharset"),
    TWIPS_PER_POINT(20, "twipsPerPoint");

    private final Number code;
    private final String name;

    JFont(Number code, String name) {
        this.code = code;
        this.name = name;
    }

    public Number getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static JFont codeOf(Number code) {
        for (JFont value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant with code: " + code);
    }

    public static JFont nameOf(String name) {
        for (JFont value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        List<String> colorList= Arrays.stream(values()).map(JFont::getName).collect(Collectors.toList());
        throw new IllegalArgumentException("font code should be one of : " + StringUtils.join(colorList,","));
    }
}
