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
 * 翡翠绿主题。
 * <p>翡翠绿表头搭配浅薄荷斑马纹，清润通透，适合环保、健康类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JJadeTheme extends JAbstractExcelTheme {

    public JJadeTheme() {
        super("jade", "翡翠绿", "翡翠绿表头搭配浅薄荷斑马纹，清润通透");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("00A86B");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E0F2ED");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("0F5132");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("A3D9C4");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("C6E8DA");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("3D7D5F");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("006B45");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
