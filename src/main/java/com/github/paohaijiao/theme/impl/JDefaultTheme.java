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
 * 经典皇家蓝主题。
 * <p>与 {@code JExcelCommonHandler} 的默认导出样式保持一致：皇家蓝表头、灰色斑马纹、千分位数字格式。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JDefaultTheme extends JAbstractExcelTheme {

    public JDefaultTheme() {
        super("default", "经典皇家蓝", "与默认导出样式保持一致的皇家蓝主题");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("1F4E79");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("D9D9D9");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("000000");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("BFBFBF");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("FFF2CC");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("808080");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("1F4E79");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected String evenDataFormat() {
        return "#,##0";
    }
}
