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
 * 大地棕主题。
 * <p>大地棕表头搭配浅褐斑马纹，朴实厚重，适合地产、建筑类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JEarthBrownTheme extends JAbstractExcelTheme {

    public JEarthBrownTheme() {
        super("earthBrown", "大地棕", "大地棕表头搭配浅褐斑马纹，朴实厚重");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("6D4C41");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("EDE0D8");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("4E342E");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D7C0B0");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E4D2C4");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("5D4037");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("4E342E");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
