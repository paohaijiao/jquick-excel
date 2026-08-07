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
 * 酒红主题。
 * <p>酒红表头搭配浅红斑马纹，醇厚典雅，适合高端餐饮、礼品类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JWineRedTheme extends JAbstractExcelTheme {

    public JWineRedTheme() {
        super("wineRed", "酒红", "酒红表头搭配浅红斑马纹，醇厚典雅");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("6E2C3D");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F4E0E4");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("4A1E2C");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D7AEB8");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E8C5CD");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("884054");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("5B2433");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
