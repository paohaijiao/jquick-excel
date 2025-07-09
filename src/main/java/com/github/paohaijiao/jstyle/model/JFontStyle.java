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
package com.github.paohaijiao.jstyle.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.jstyle.model
 *
 * @author Martin
 * @version 1.0.0
 * @className JRowStyle
 * @date 2025/7/4
 * @description
 */
public class JFontStyle extends HashMap<String, Object> {


    public static final String fontHeightInPoints = "fontHeightInPoints";
    public static final String fontName = "fontName";
    public static final String fontHeight = "fontHeight";
    public static final String strikeout = "strikeout";
    public static final String offset = "offset";
    public static final String bold = "bold";
    public static final String italic = "italic";
    public static final String color = "color";
    public static final String underLine = "underLine";

    public void setFontHeightInPoints(BigDecimal v) {
        put(fontHeightInPoints, v);
    }

    public BigDecimal getFontHeightInPoints() {
        return containsKey(fontHeightInPoints) ? (BigDecimal) get(fontHeightInPoints) : null;
    }

    public void setFontName(String name) {
        put(fontName, name);
    }

    public String getFontName() {
        return containsKey(fontName) ? (String) get(fontName) : null;
    }

    public void setBold(Boolean b) {
        put(bold, b);
    }

    public Boolean isBold() {
        return containsKey(bold) ? (Boolean) get(bold) : null;
    }

    public void setItalic(Boolean b) {
        put(italic, b);
    }

    public Boolean isItalic() {
        return containsKey(italic) ? (Boolean) get(italic) : null;
    }

    public void setColor(String c) {
        put(color, c);
    }
    public void setUnderLine(Boolean line) {
        put(underLine, line);
    }
    public Boolean getUnderLine() {
        return containsKey(underLine) ? (Boolean) get(underLine) : null;
    }
}
