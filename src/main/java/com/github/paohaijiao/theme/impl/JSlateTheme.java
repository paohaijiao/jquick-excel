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
 * 板岩灰主题。
 * <p>板岩灰表头搭配浅灰蓝斑马纹，冷静商务，适合咨询、法务类报表。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JSlateTheme extends JAbstractExcelTheme {

    public JSlateTheme() {
        super("slate", "板岩灰", "板岩灰表头搭配浅灰蓝斑马纹，冷静商务");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("44546A");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E2E5EB");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("2A3340");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("BFC6D2");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("D2D7DF");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("5A6878");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("36414F");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
