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
import org.apache.poi.ss.usermodel.BorderStyle;

/**
 * 皇家金主题。
 * <p>纯黑表头搭配金色字体与金色边框，米色斑马纹，华贵大气，适合年报、金融、奢侈品类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JRoyalGoldTheme extends JAbstractExcelTheme {

    public JRoyalGoldTheme() {
        super("royalGold", "皇家金", "纯黑表头搭配金色字体与边框，华贵大气");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("1A1A1A");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("D4AF37");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("FBF7E8");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("1A1A1A");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D4AF37");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("F5E6A8");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8C7B27");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("0D0D0D");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("D4AF37");
    }

    @Override
    protected BorderStyle borderStyle() {
        return BorderStyle.MEDIUM;
    }

    @Override
    protected short titleFontSize() {
        return 18;
    }
}
