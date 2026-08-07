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
 * 浓缩咖啡主题。
 * <p>浓缩咖啡表头搭配奶白斑马纹与奶油色文字，浓郁醇厚，适合咖啡、烘焙类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JEspressoTheme extends JAbstractExcelTheme {

    public JEspressoTheme() {
        super("espresso", "浓缩咖啡", "浓缩咖啡表头搭配奶白斑马纹，浓郁醇厚");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("3E2723");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("F5E6D3");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("EFE0D6");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FBF5F0");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3E2723");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D4B996");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E0CDB8");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("5D4037");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("3E2723");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("F5E6D3");
    }
}
