/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) [2025-2099] Martin (goudingcheng@gmail.com)
 */
package com.github.paohaijiao.theme.enums;


import com.github.paohaijiao.theme.JExcelTheme;
import com.github.paohaijiao.theme.impl.*;
import com.github.paohaijiao.theme.impl.JAmberTheme;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public enum JExcelThemeType {

    DEFAULT("default", "经典皇家蓝", JDefaultTheme::new),

    OCEAN_BLUE("oceanBlue", "海洋蓝", JOceanBlueTheme::new),

    FOREST_GREEN("forestGreen", "森林绿", JForestGreenTheme::new),

    SUNSET_ORANGE("sunsetOrange", "落日橙", JSunsetOrangeTheme::new),

    LAVENDER_PURPLE("lavenderPurple", "薰衣草紫", JLavenderPurpleTheme::new),

    SAKURA_PINK("sakuraPink", "樱花粉", JSakuraPinkTheme::new),

    MIDNIGHT_DARK("midnightDark", "午夜深色", JMidnightDarkTheme::new),

    MINIMALIST_GREY("minimalistGrey", "极简灰", JMinimalistGreyTheme::new),

    VINTAGE_SEPIA("vintageSepia", "复古棕", JVintageSepiaTheme::new),

    TROPICAL_TEAL("tropicalTeal", "热带青", JTropicalTealTheme::new),

    ROYAL_GOLD("royalGold", "皇家金", JRoyalGoldTheme::new),

    MINT_FRESH("mintFresh", "清新薄荷", JMintFreshTheme::new),

    AMBER("amber", "琥珀", com.github.paohaijiao.theme.impl.JAmberTheme::new),

    AMETHYST("amethyst", "紫水晶", JAmethystTheme::new),

    AZURE("azure", "蔚蓝", JAzureTheme::new),

    CHARCOAL("charcoal", "炭灰", JCharcoalTheme::new),

    CORAL("coral", "珊瑚", JCoralTheme::new),

    CRIMSON_RED("crimsonRed", "深红", JCrimsonRedTheme::new),

    CYAN("cyan", "青色", JCyanTheme::new),

    EMERALD("emerald", "祖母绿", JEmeraldTheme::new),

    ESPRESSO("espresso", "浓缩咖啡", JEspressoTheme::new),

    INDIGO("indigo", "靛蓝", JIndigoTheme::new),

    MUSTARD("mustard", "芥末黄", JMustardTheme::new),

    NAVY_BLUE("navyBlue", "海军蓝", JNavyBlueTheme::new),

    OLIVE_GREEN("oliveGreen", "橄榄绿", JOliveGreenTheme::new),

    PEACH("peach", "蜜桃", JPeachTheme::new),

    SAGE("sage", "鼠尾草绿", JSageTheme::new),

    SKY_BLUE("skyBlue", "天空蓝", JSkyBlueTheme::new),

    STEEL_BLUE("steelBlue", "钢蓝", JSteelBlueTheme::new),

    TERRACOTTA("terracotta", "陶土", JTerracottaTheme::new),

    TURQUOISE("turquoise", "绿松石", JTurquoiseTheme::new),

    WINE_RED("wineRed", "酒红", JWineRedTheme::new),

    PLUM("plum", "紫梅", JPlumTheme::new),

    SLATE("slate", "板岩灰", JSlateTheme::new),

    PEARL("pearl", "珍珠白", JPearlTheme::new),

    BRONZE("bronze", "青铜", JBronzeTheme::new),

    ROSE_QUARTZ("roseQuartz", "粉晶", JRoseQuartzTheme::new),

    JADE("jade", "翡翠绿", JJadeTheme::new),
    CHAMPAGNE("champagne", "香槟金", JChampagneTheme::new),

    DENIM("denim", "牛仔蓝", JDenimTheme::new),

    MAHOGANY("mahogany", "红木", JMahoganyTheme::new),

    PERIWINKLE("periwinkle", "长春花蓝", JPeriwinkleTheme::new);


    private final String code;

    private final String label;

    private final Supplier<JExcelTheme> supplier;

    JExcelThemeType(String code, String label, Supplier<JExcelTheme> supplier) {
        this.code = code;
        this.label = label;
        this.supplier = supplier;
    }

    public static JExcelThemeType codeOf(String code) {
        for (JExcelThemeType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        List<String> codes = Arrays.stream(values())
                .map(JExcelThemeType::getCode)
                .collect(Collectors.toList());
        throw new IllegalArgumentException("theme code should be one of : " + String.join(",", codes));
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 创建该主题类型的实例。
     */
    public JExcelTheme create() {
        return supplier.get();
    }
}
