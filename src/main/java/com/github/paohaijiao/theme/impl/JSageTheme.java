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
 * 鼠尾草绿主题。
 * <p>鼠尾草绿表头搭配浅绿斑马纹，柔和舒缓，适合健康、冥想类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JSageTheme extends JAbstractExcelTheme {

    public JSageTheme() {
        super("sage", "鼠尾草绿", "鼠尾草绿表头搭配浅绿斑马纹，柔和舒缓");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("5C7A5C");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E3ECE3");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("324A32");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("B5CFB5");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("C8DAC8");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("4F6E4F");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("446244");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
