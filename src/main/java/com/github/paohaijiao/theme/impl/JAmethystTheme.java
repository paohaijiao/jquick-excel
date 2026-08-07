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
 * 紫水晶主题。
 * <p>紫水晶表头搭配淡紫斑马纹，神秘高雅，适合设计、奢侈品导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JAmethystTheme extends JAbstractExcelTheme {

    public JAmethystTheme() {
        super("amethyst", "紫水晶", "紫水晶表头搭配淡紫斑马纹，神秘高雅");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("7D3C98");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("EBDEF0");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("4A235A");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D2B4DE");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E8DAEF");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("6C3483");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("5B2C6F");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
