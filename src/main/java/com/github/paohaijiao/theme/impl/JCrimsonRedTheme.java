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
 * 深红主题。
 * <p>深红表头搭配浅红斑马纹，热烈庄重，适合预警、风控、重要指标类报表。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JCrimsonRedTheme extends JAbstractExcelTheme {

    public JCrimsonRedTheme() {
        super("crimsonRed", "深红", "深红表头搭配浅红斑马纹，热烈庄重");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("8B0000");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("FADBD8");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("641E16");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("E6B0AA");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("F5B7B1");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("922B21");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("7B241C");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
