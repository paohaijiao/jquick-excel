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
package com.github.paohaijiao.theme.impl;

import com.github.paohaijiao.theme.JAbstractExcelTheme;
import com.github.paohaijiao.theme.JRgbColor;

/**
 * 蜜桃主题。
 * <p>蜜桃橙表头搭配粉橙斑马纹，甜美柔和，适合母婴、生活服务类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JPeachTheme extends JAbstractExcelTheme {

    public JPeachTheme() {
        super("peach", "蜜桃", "蜜桃橙表头搭配粉橙斑马纹，甜美柔和");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("E8825B");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("FDE5DA");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("7C3A1E");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("F5C3A6");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("F9D5BE");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("C75F36");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("CF6B3E");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
