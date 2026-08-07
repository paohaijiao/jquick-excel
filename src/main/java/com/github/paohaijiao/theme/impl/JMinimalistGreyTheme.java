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
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * 极简灰主题。
 * <p>炭黑表头搭配极浅灰斑马纹，无强烈色彩，左对齐文本，适合正式、文档型导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JMinimalistGreyTheme extends JAbstractExcelTheme {

    public JMinimalistGreyTheme() {
        super("minimalistGrey", "极简灰", "炭黑表头搭配极浅灰斑马纹，克制简洁");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("262626");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F2F2F2");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("333333");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D9D9D9");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E6E6E6");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("808080");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("262626");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected HorizontalAlignment dataAlignment() {
        return HorizontalAlignment.LEFT;
    }
}
