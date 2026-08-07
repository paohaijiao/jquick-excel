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
package com.github.paohaijiao.theme.factory;

import com.github.paohaijiao.theme.JExcelTheme;
import com.github.paohaijiao.theme.enums.JExcelThemeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JExcelThemeFactory {

    private JExcelThemeFactory() {
    }

    /**
     * 按主题类型创建主题实例，{@code null} 时回退到默认主题。
     */
    public static JExcelTheme create(JExcelThemeType type) {
        if (type == null) {
            return JExcelThemeType.DEFAULT.create();
        }
        return type.create();
    }

    /**
     * 按主题编码创建主题实例，{@code null}/空串时回退到默认主题。
     */
    public static JExcelTheme create(String code) {
        if (code == null || code.trim().isEmpty()) {
            return JExcelThemeType.DEFAULT.create();
        }
        return JExcelThemeType.codeOf(code).create();
    }

    /**
     * 创建默认主题。
     */
    public static JExcelTheme create() {
        return JExcelThemeType.DEFAULT.create();
    }

    /**
     * 创建默认主题。
     */
    public static JExcelTheme defaultTheme() {
        return create();
    }

    /**
     * 创建所有内置主题实例，便于预览/展示。
     */
    public static List<JExcelTheme> allThemes() {
        List<JExcelTheme> list = new ArrayList<>();
        for (JExcelThemeType type : JExcelThemeType.values()) {
            list.add(type.create());
        }
        return list;
    }

    /**
     * 返回所有内置主题编码。
     */
    public static List<String> allCodes() {
        return Arrays.stream(JExcelThemeType.values())
                .map(JExcelThemeType::getCode)
                .collect(Collectors.toList());
    }
}
