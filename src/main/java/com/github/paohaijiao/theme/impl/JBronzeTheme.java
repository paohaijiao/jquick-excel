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
 * 青铜主题。
 * <p>青铜表头搭配浅棕斑马纹，古朴厚重，适合博物馆、文物类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JBronzeTheme extends JAbstractExcelTheme {

    public JBronzeTheme() {
        super("bronze", "青铜", "青铜表头搭配浅棕斑马纹，古朴厚重");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("7A5230");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("EADFCF");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("4A2F16");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D0B489");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E2CDA4");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8A5E36");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("5C3A1E");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
