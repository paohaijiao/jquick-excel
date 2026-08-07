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
 * 绿松石主题。
 * <p>绿松石表头搭配薄荷斑马纹，明艳灵动，适合珠宝、旅游类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JTurquoiseTheme extends JAbstractExcelTheme {

    public JTurquoiseTheme() {
        super("turquoise", "绿松石", "绿松石表头搭配薄荷斑马纹，明艳灵动");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("1ABC9C");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("D4F5EF");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("0E5A4D");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("A6E6D9");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("BFEEE5");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("16A085");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("138D72");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
