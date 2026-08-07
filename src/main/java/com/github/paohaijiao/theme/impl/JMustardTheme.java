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
 * 芥末黄主题。
 * <p>芥末黄表头搭配米黄斑马纹，复古温暖，适合文创、手作类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JMustardTheme extends JAbstractExcelTheme {

    public JMustardTheme() {
        super("mustard", "芥末黄", "芥末黄表头搭配米黄斑马纹，复古温暖");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("C9A227");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("FAF1D0");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("7A6411");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("DBC784");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E6D7A2");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("A8891C");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("8E7418");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
