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
 * 海军蓝主题。
 * <p>海军蓝表头搭配浅蓝斑马纹，稳重专业，适合政企、合规类报表。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JNavyBlueTheme extends JAbstractExcelTheme {

    public JNavyBlueTheme() {
        super("navyBlue", "海军蓝", "海军蓝表头搭配浅蓝斑马纹，稳重专业");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("003366");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("D6E4F0");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("002244");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("AEC6E6");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("BDD7EE");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("1A3C5E");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("002244");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
