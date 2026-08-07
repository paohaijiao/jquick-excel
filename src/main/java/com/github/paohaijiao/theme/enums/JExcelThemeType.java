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

import com.github.paohaijiao.theme.*;
import com.github.paohaijiao.theme.impl.*;

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

    MINT_FRESH("mintFresh", "清新薄荷", JMintFreshTheme::new);


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
