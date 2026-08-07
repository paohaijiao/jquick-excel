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
 * 午夜深色主题。
 * <p>深色背景搭配浅色文字，护眼且具科技感，适合大屏看板、监控类数据导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JMidnightDarkTheme extends JAbstractExcelTheme {

    public JMidnightDarkTheme() {
        super("midnightDark", "午夜深色", "深色背景搭配浅色文字，护眼且具科技感");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("1A1A2E");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("EAEAEA");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("232342");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("1B1B30");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("D6D6E0");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("3A3A5A");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("16213E");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8888AA");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("0F3460");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("EAEAEA");
    }
}
