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
 * 牛仔蓝主题。
 * <p>牛仔蓝表头搭配浅蓝灰斑马纹，随性干练，适合零售、电商类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JDenimTheme extends JAbstractExcelTheme {

    public JDenimTheme() {
        super("denim", "牛仔蓝", "牛仔蓝表头搭配浅蓝灰斑马纹，随性干练");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("2B4C7E");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E8EEF5");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("1A2F4E");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("B8C8DE");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("D0DCEC");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("5A7299");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("1A2F4E");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
