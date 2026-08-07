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
 * 石墨主题。
 * <p>石墨黑底搭配浅灰斑马纹与亮色文字，冷峻科技感，适合运维、监控类大屏导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JGraphiteTheme extends JAbstractExcelTheme {

    public JGraphiteTheme() {
        super("graphite", "石墨", "石墨黑底搭配浅灰斑马纹与亮色文字，冷峻科技感");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("2B2B2B");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("E0E0E0");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("3A3A3A");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("2F2F2F");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("D0D0D0");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("555555");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("1F1F1F");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("888888");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("1A1A1A");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("E0E0E0");
    }
}
