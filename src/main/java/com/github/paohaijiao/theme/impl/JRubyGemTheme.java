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
 * 宝石红主题。
 * <p>宝石红表头搭配浅红斑马纹，艳丽高贵，适合奢侈品、高端零售类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JRubyGemTheme extends JAbstractExcelTheme {

    public JRubyGemTheme() {
        super("rubyGem", "宝石红", "宝石红表头搭配浅红斑马纹，艳丽高贵");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("9B111E");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F7E0E2");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("6B0F18");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("E8A8AE");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("F0C0C5");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("AB1622");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("7B0F1A");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
