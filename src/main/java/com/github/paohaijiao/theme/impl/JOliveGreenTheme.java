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
 * 橄榄绿主题。
 * <p>橄榄绿表头搭配嫩黄绿斑马纹，质朴自然，适合农业、户外类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JOliveGreenTheme extends JAbstractExcelTheme {

    public JOliveGreenTheme() {
        super("oliveGreen", "橄榄绿", "橄榄绿表头搭配嫩黄绿斑马纹，质朴自然");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("556B2F");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("EEF2D6");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3E4A1C");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("C5D17F");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("D6E0A8");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("6B7A1E");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("4A5A1E");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
