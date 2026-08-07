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
 * 紫梅主题。
 * <p>深梅紫表头搭配浅紫斑马纹，神秘优雅，适合奢侈品、文创类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JPlumTheme extends JAbstractExcelTheme {

    public JPlumTheme() {
        super("plum", "紫梅", "深梅紫表头搭配浅紫斑马纹，神秘优雅");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("4A2545");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F0E6EF");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3D1F37");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("C9B0C4");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("DBC3D6");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("7A5470");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("2E1626");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
