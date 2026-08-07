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
 * 炭灰主题。
 * <p>炭灰表头搭配浅灰斑马纹，沉稳内敛，适合正式、行政类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JCharcoalTheme extends JAbstractExcelTheme {

    public JCharcoalTheme() {
        super("charcoal", "炭灰", "炭灰表头搭配浅灰斑马纹，沉稳内敛");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("36454F");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E4E7EA");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("222B31");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("C2C7CC");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("D3D8DC");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("4A5A66");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("2C3940");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
