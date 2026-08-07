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
 * 清新薄荷主题。
 * <p>海绿表头搭配薄荷绿斑马纹，轻盈通透，适合教育、公益、生活服务类数据。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JMintFreshTheme extends JAbstractExcelTheme {

    public JMintFreshTheme() {
        super("mintFresh", "清新薄荷", "海绿表头搭配薄荷绿斑马纹，轻盈通透");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("2E8B57");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("E0F2E9");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FFFFFF");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("1F4D2C");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("A8D5BA");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("C8E6C9");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("3CB371");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("2E7D52");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("FFFFFF");
    }
}
