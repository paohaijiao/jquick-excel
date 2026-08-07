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
 * 薰衣草紫主题。
 * <p>深紫表头搭配淡紫斑马纹，优雅高贵，适合设计、文创类数据展示。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JLavenderPurpleTheme extends JAbstractExcelTheme {

    public JLavenderPurpleTheme() {
        super("lavenderPurple", "薰衣草紫", "深紫表头搭配淡紫斑马纹，优雅高贵");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("5E2B97");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("EDE4F6");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3B1A5E");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("C4A7E0");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("DDC9F2");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("6B4A8E");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("4A1F7A");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
