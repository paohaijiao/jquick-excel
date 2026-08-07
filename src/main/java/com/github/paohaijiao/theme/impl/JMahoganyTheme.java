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
 * 红木主题。
 * <p>红木色表头搭配浅褐斑马纹，沉稳大气，适合房地产、古典类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JMahoganyTheme extends JAbstractExcelTheme {

    public JMahoganyTheme() {
        super("mahogany", "红木", "红木色表头搭配浅褐斑马纹，沉稳大气");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("5D2A1F");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("F5E6D3");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("EEDDD3");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FBF5F0");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3D1A12");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("C9A89A");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("D9BFAE");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8B4A38");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("3D1A12");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("F5E6D3");
    }
}
