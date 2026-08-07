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
public class JRowStyle extends JFontStyle {

    public static final String height = "height";
    public static final String rowNum = "rowNum";
    public static final String rowStyle = "rowStyle";
    public static final String heightInPoints = "heightInPoints";
    public static final String zeroHeight = "zeroHeight";
    public static final String fontHeightInPoints = "fontHeightInPoints";
    public static final String fontName = "fontName";
    public static final String fontHeight = "fontHeight";
    public static final String strikeout = "strikeout";
    public static final String offset = "offset";
    public static final String bold = "bold";
    public static final String italic = "italic";
    public static final String color = "color";
    public static final String underLine = "underLine";

    public BigDecimal getHeight() {
        return containsKey(height) ? (BigDecimal) get(height) : null;
    }

    public void setHeight(BigDecimal i) {
        put(height, i);
    }

    public Integer getRowNum() {
        return containsKey(rowNum) ? (int) get(rowNum) : 0;
    }

    public void setRowNum(int i) {
        put(rowNum, i);
    }

    public JCellStyle getRowStyle() {
        Map<String, Object> map = (Map<String, Object>) get(rowStyle);
        if (null == map) return new JCellStyle();
        JCellStyle cellStyle = new JCellStyle();
        cellStyle.putAll(map);
        return cellStyle;

    }

    public void setRowStyle(JCellStyle cellStyle) {
        put(rowStyle, cellStyle);
    }

    public BigDecimal getHeightInPoints() {
        return containsKey(heightInPoints) ? (BigDecimal) get(heightInPoints) : null;
    }

    public void setHeightInPoints(BigDecimal v) {
        put(heightInPoints, v);
    }

    public void setZeroHeight(boolean b) {
        put(zeroHeight, b);
    }

    public Boolean isZeroHeight() {
        return containsKey(zeroHeight) ? (boolean) get(zeroHeight) : null;
    }

    public BigDecimal getFontHeightInPoints() {
        return containsKey(fontHeightInPoints) ? (BigDecimal) get(fontHeightInPoints) : null;
    }

    public void setFontHeightInPoints(BigDecimal v) {
        put(fontHeightInPoints, v);
    }

    public String getFontName() {
        return containsKey(fontName) ? (String) get(fontName) : null;
    }

    public void setFontName(String name) {
        put(fontName, name);
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

    public Boolean getUnderLine() {
        return containsKey(underLine) ? (Boolean) get(underLine) : null;
    }

    public void setUnderLine(Boolean line) {
        put(underLine, line);
    }
}
