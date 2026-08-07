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
 * 落日橙主题。
 * <p>暖橙表头搭配杏色斑马纹，热情明亮，适合营销、运营类报表。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JSunsetOrangeTheme extends JAbstractExcelTheme {

    public JSunsetOrangeTheme() {
        super("sunsetOrange", "落日橙", "暖橙表头搭配杏色斑马纹，热情明亮");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("C2410C");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("FDEBD0");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("7C2D12");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("FDBA74");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("FED7AA");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("9A3412");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("C2410C");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
