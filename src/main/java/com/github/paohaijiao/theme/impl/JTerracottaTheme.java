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
 * 陶土主题。
 * <p>陶土棕表头搭配浅褐斑马纹，质朴温润，适合手工艺、家居类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JTerracottaTheme extends JAbstractExcelTheme {

    public JTerracottaTheme() {
        super("terracotta", "陶土", "陶土棕表头搭配浅褐斑马纹，质朴温润");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("A0522D");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F2DFD3");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("5C2E1A");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("DDB89E");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E8C9B4");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8C4521");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("7A3F20");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
