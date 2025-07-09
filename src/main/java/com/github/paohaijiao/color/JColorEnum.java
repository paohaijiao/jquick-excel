package com.github.paohaijiao.color;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum JColorEnum {

    BLACK1("black1", 0, IndexedColors.BLACK),
    WHITE1("white1", 1, IndexedColors.WHITE),
    RED1("red1", 2, IndexedColors.RED),
    BRIGHT_GREEN1("brightGreen1", 3, IndexedColors.BRIGHT_GREEN),
    BLUE1("blue1", 4, IndexedColors.BLUE),
    YELLOW1("yellow1", 5, IndexedColors.YELLOW),
    PINK1("pink1", 6, IndexedColors.PINK),
    TURQUOISE1("turquoise1", 7, IndexedColors.TURQUOISE),
    BLACK("black", 8, IndexedColors.BLACK),
    WHITE("white", 9, IndexedColors.WHITE),
    RED("red", 10, IndexedColors.RED),
    BRIGHT_GREEN("brightGreen", 11, IndexedColors.BRIGHT_GREEN),
    BLUE("blue", 12, IndexedColors.BLUE),
    YELLOW("yellow", 13, IndexedColors.YELLOW),
    PINK("pink", 14, IndexedColors.PINK),
    TURQUOISE("turquoise", 15, IndexedColors.TURQUOISE),

    DARK_RED("darkRed", 16, IndexedColors.DARK_RED),
    GREEN("green", 17, IndexedColors.GREEN),
    DARK_BLUE("darkBlue", 18, IndexedColors.DARK_BLUE),
    DARK_YELLOW("darkYellow", 19, IndexedColors.DARK_YELLOW),
    VIOLET("violet", 20, IndexedColors.VIOLET),
    TEAL("teal", 21, IndexedColors.TEAL),
    GREY_25_PERCENT("grey25Percent", 22, IndexedColors.GREY_25_PERCENT),
    GREY_50_PERCENT("grey50Percent", 23, IndexedColors.GREY_50_PERCENT),
    CORNFLOWER_BLUE("cornflowerBlue", 24, IndexedColors.CORNFLOWER_BLUE),
    MAROON("maroon", 25, IndexedColors.MAROON),
    LEMON_CHIFFON("lemonChiffon", 26, IndexedColors.LEMON_CHIFFON),
    LIGHT_TURQUOISE1("lightTurquoise1", 27, IndexedColors.LIGHT_TURQUOISE),
    ORCHID("orchid", 28, IndexedColors.ORCHID),
    CORAL("coral", 29, IndexedColors.CORAL),
    ROYAL_BLUE("royalBlue", 30, IndexedColors.ROYAL_BLUE),
    LIGHT_CORNFLOWER_BLUE("lightCornflowerBlue", 31, IndexedColors.LIGHT_CORNFLOWER_BLUE),
    SKY_BLUE("skyBlue", 40, IndexedColors.SKY_BLUE),
    LIGHT_TURQUOISE("lightTurquoise", 41, IndexedColors.LIGHT_TURQUOISE),
    LIGHT_GREEN("lightGreen", 42, IndexedColors.LIGHT_GREEN),
    LIGHT_YELLOW("lightYellow", 43, IndexedColors.LIGHT_YELLOW),
    PALE_BLUE("paleBlue", 44, IndexedColors.PALE_BLUE),
    ROSE("rose", 45, IndexedColors.ROSE),
    LAVENDER("lavender", 46, IndexedColors.LAVENDER),
    TAN("tan", 47, IndexedColors.TAN),
    LIGHT_BLUE("lightBlue", 48, IndexedColors.LIGHT_BLUE),
    AQUA("aqua", 49, IndexedColors.AQUA),
    LIME("lime", 50, IndexedColors.LIME),
    GOLD("gold", 51, IndexedColors.GOLD),
    LIGHT_ORANGE("lightOrange", 52, IndexedColors.LIGHT_ORANGE),
    ORANGE("orange", 53, IndexedColors.ORANGE),
    BLUE_GREY("blueGrey", 54, IndexedColors.BLUE_GREY),
    GREY_40_PERCENT("grey40Percent", 55, IndexedColors.GREY_40_PERCENT),
    DARK_TEAL("darkTeal", 56, IndexedColors.DARK_TEAL),
    SEA_GREEN("seaGreen", 57, IndexedColors.SEA_GREEN),
    DARK_GREEN("darkGreen", 58, IndexedColors.DARK_GREEN),
    OLIVE_GREEN("oliveGreen", 59, IndexedColors.OLIVE_GREEN),
    BROWN("brown", 60, IndexedColors.BROWN),
    PLUM("plum", 61, IndexedColors.PLUM),
    INDIGO("indigo", 62, IndexedColors.INDIGO),
    GREY_80_PERCENT("grey80Percent", 63, IndexedColors.GREY_80_PERCENT),
    AUTOMATIC("automatic", 64, IndexedColors.AUTOMATIC);

    private final String code;
    private final int index;
    private final IndexedColors indexedColor;

    JColorEnum(String code, int index, IndexedColors indexedColor) {
        this.code = code;
        this.index = index;
        this.indexedColor = indexedColor;
    }

    public String getCode() {
        return code;
    }

    public int getIndex() {
        return index;
    }

    public IndexedColors getIndexedColor() {
        return indexedColor;
    }

    public static JColorEnum codeOf(String code) {
        for (JColorEnum color : values()) {
            if (color.code.equalsIgnoreCase(code)) {
                return color;
            }
        }
        List<String> colorList=Arrays.stream(values()).map(JColorEnum::getCode).collect(Collectors.toList());
        throw new IllegalArgumentException("color code should be one of : " + StringUtils.join(colorList,","));
    }
    public static JColorEnum fromIndexedColor(IndexedColors indexedColor) {
        for (JColorEnum color : values()) {
            if (color.indexedColor == indexedColor) {
                return color;
            }
        }
        throw new IllegalArgumentException("No matching ColorEnum for IndexedColors: " + indexedColor);
    }

    public static JColorEnum fromIndex(int index) {
        for (JColorEnum color : values()) {
            if (color.index == index) {
                return color;
            }
        }
        throw new IllegalArgumentException("Invalid ColorEnum index: " + index);
    }

}
