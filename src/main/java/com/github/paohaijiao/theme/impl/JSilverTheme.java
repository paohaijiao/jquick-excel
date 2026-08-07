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
 * 银灰主题。
 * <p>银灰表头搭配浅银斑马纹，简约现代，适合科技、硬件类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JSilverTheme extends JAbstractExcelTheme {

    public JSilverTheme() {
        super("silver", "银灰", "银灰表头搭配浅银斑马纹，简约现代");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("708090");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E2E5E9");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3A4250");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("BCC3CC");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("D2D6DC");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("5A6677");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("4A5462");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
