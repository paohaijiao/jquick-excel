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
 * 长春花蓝主题。
 * <p>长春花蓝表头搭配浅紫蓝斑马纹，柔和梦幻，适合设计、文创类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JPeriwinkleTheme extends JAbstractExcelTheme {

    public JPeriwinkleTheme() {
        super("periwinkle", "长春花蓝", "长春花蓝表头搭配浅紫蓝斑马纹，柔和梦幻");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("6C5DD3");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("ECE9F8");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("3A2F8E");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("BDB6E6");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("D4CFF0");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("6F62A8");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("4A3DB0");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
