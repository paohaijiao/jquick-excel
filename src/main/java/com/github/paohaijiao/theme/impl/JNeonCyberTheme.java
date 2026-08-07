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
 * 赛博霓虹主题。
 * <p>深紫黑底搭配霓虹青字与品红页脚，赛博朋克风，适合极客、游戏类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JNeonCyberTheme extends JAbstractExcelTheme {

    public JNeonCyberTheme() {
        super("neonCyber", "赛博霓虹", "深紫黑底搭配霓虹青字，赛博朋克风");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("0D0221");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("00F5D4");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("1A0B2E");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("0D0221");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("E0E0E0");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("2E1A4D");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("7209B7");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("FF006E");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("240046");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("00F5D4");
    }
}
