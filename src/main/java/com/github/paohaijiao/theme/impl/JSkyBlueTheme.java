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
 * 天空蓝主题。
 * <p>天空蓝表头搭配浅蓝斑马纹，明快清爽，适合互联网、SaaS 类报表。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JSkyBlueTheme extends JAbstractExcelTheme {

    public JSkyBlueTheme() {
        super("skyBlue", "天空蓝", "天空蓝表头搭配浅蓝斑马纹，明快清爽");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("2E86C1");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("D6EAF8");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("1B4F72");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("AED6F1");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("A9CCE3");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("2874A6");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("2471A3");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
