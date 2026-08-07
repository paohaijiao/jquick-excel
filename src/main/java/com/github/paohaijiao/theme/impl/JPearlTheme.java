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
 * 珍珠主题。
 * <p>珍珠灰紫表头搭配珠光斑马纹，温润优雅，适合美妆、婚庆类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JPearlTheme extends JAbstractExcelTheme {

    public JPearlTheme() {
        super("pearl", "珍珠", "珍珠灰紫表头搭配珠光斑马纹，温润优雅");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("6D6A7B");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F0EFF3");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3A3742");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D4D2DC");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E2E1E8");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("5C5970");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("4F4A66");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
