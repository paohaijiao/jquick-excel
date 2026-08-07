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
 * 青色主题。
 * <p>青色表头搭配浅青斑马纹，清新通透，适合水务、环保类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JCyanTheme extends JAbstractExcelTheme {

    public JCyanTheme() {
        super("cyan", "青色", "青色表头搭配浅青斑马纹，清新通透");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("00838F");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("D0EEEE");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("004D4D");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("A0DCDC");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("B8E6E6");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("006B6B");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("005A5A");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
