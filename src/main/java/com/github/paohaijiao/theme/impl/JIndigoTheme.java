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
 * 靛蓝主题。
 * <p>靛蓝表头搭配浅蓝紫斑马纹，沉静深邃，适合科技、研发类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JIndigoTheme extends JAbstractExcelTheme {

    public JIndigoTheme() {
        super("indigo", "靛蓝", "靛蓝表头搭配浅蓝紫斑马纹，沉静深邃");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("3F3D7A");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E5E8F5");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("262456");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("B4B8E0");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("C9CDEE");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("4A4880");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("2E2C66");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
