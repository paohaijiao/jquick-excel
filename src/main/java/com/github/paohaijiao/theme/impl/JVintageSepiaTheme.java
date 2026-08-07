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
 * 复古棕主题。
 * <p>咖啡色表头搭配米色斑马纹与奶油色文字，怀旧温润，适合档案、文史类导出。</p>
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public class JVintageSepiaTheme extends JAbstractExcelTheme {

    public JVintageSepiaTheme() {
        super("vintageSepia", "复古棕", "咖啡色表头搭配米色斑马纹，怀旧温润");
    }

    @Override
    protected JRgbColor headerBackground() {
        return JRgbColor.of("6F4E37");
    }

    @Override
    protected JRgbColor headerForeground() {
        return JRgbColor.of("F5E6D3");
    }

    @Override
    protected JRgbColor dataEvenBackground() {
        return JRgbColor.of("F5E6D3");
    }

    @Override
    protected JRgbColor dataOddBackground() {
        return JRgbColor.of("FBF5EC");
    }

    @Override
    protected JRgbColor dataForeground() {
        return JRgbColor.of("4A3424");
    }

    @Override
    protected JRgbColor borderColor() {
        return JRgbColor.of("D4B996");
    }

    @Override
    protected JRgbColor accentBackground() {
        return JRgbColor.of("E8D5B7");
    }

    @Override
    protected JRgbColor footerForeground() {
        return JRgbColor.of("8B6F47");
    }

    @Override
    protected JRgbColor titleBackground() {
        return JRgbColor.of("4A3424");
    }

    @Override
    protected JRgbColor titleForeground() {
        return JRgbColor.of("F5E6D3");
    }

    @Override
    protected HorizontalAlignment dataAlignment() {
        return HorizontalAlignment.LEFT;
    }
}
