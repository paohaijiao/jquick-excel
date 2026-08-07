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
 * 蔚蓝主题。
 * <p>蔚蓝表头搭配冰蓝斑马纹，通透辽阔，适合气象、物流类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JAzureTheme extends JAbstractExcelTheme {

    public JAzureTheme() {
        super("azure", "蔚蓝", "蔚蓝表头搭配冰蓝斑马纹，通透辽阔");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("0077B6");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("CFE9F5");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("023E5C");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("90C4DC");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("B0D9EA");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("0077A8");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("02577A");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
