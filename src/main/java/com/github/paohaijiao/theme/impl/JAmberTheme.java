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
 * 琥珀主题。
 * <p>琥珀金表头搭配浅黄斑马纹，温润光泽，适合珠宝、收藏品类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JAmberTheme extends JAbstractExcelTheme {

    public JAmberTheme() {
        super("amber", "琥珀", "琥珀金表头搭配浅黄斑马纹，温润光泽");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("B8860B");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("FCEFC7");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("7D5A00");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("E8D178");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("F2DC8B");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("9A7200");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("8C6700");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
