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
 * 香槟金主题。
 * <p>香槟色表头搭配米白斑马纹，低调奢华，适合金融、礼仪类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JChampagneTheme extends JAbstractExcelTheme {

    public JChampagneTheme() {
        super("champagne", "香槟金", "香槟色表头搭配米白斑马纹，低调奢华");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("B5985A");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("3A2E15");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F7EFDF");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("4A3A1A");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D9C9A3");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("EAD9B5");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8A7444");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("8B6F35");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
