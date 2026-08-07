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
 * 粉晶主题。
 * <p>玫瑰粉表头搭配浅粉斑马纹，柔美温婉，适合美妆、母婴类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JRoseQuartzTheme extends JAbstractExcelTheme {

    public JRoseQuartzTheme() {
        super("roseQuartz", "粉晶", "玫瑰粉表头搭配浅粉斑马纹，柔美温婉");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("C44569");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("FCE4EC");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("6A1B4A");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("F1B6C9");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("F8BBD0");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("A85272");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("8E2D52");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
