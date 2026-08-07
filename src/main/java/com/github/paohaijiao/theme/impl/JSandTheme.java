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
 * 沙漠米主题。
 * <p>沙漠米表头（深棕字）搭配米白斑马纹，暖意沙漠风，适合旅行、户外类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JSandTheme extends JAbstractExcelTheme {

    public JSandTheme() {
        super("sand", "沙漠米", "沙漠米表头搭配米白斑马纹，暖意沙漠风");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("C2A878");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("4A3A20");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F7F0E4");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("5C4A2A");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("DBC9A4");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("EAD9B8");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8C7548");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("9C8454");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("4A3A20");
    }
}
