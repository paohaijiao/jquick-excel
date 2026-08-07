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
 * 钢蓝主题。
 * <p>钢蓝表头搭配浅灰蓝斑马纹，冷静理性，适合制造、工程类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JSteelBlueTheme extends JAbstractExcelTheme {

    public JSteelBlueTheme() {
        super("steelBlue", "钢蓝", "钢蓝表头搭配浅灰蓝斑马纹，冷静理性");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("4682B4");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("D9E6F2");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("2A4D6B");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("B6CDE2");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("CADDEE");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("3A6E96");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("336180");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
