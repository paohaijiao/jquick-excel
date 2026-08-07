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
 * 热带青主题。
 * <p>青绿表头搭配薄荷斑马纹，清爽明亮，适合医疗、健康、旅游类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JTropicalTealTheme extends JAbstractExcelTheme {

    public JTropicalTealTheme() {
        super("tropicalTeal", "热带青", "青绿表头搭配薄荷斑马纹，清爽明亮");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("006D77");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E0F2F1");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("00474D");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("83C5BE");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("B2DFDB");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("00838F");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("00524A");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
