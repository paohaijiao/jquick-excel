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

import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;

/**
 * packageName com.github.paohaijiao.jstyle.model
 *
 * @author Martin
 * @version 1.0.0
 * @className JCellStyle
 * @date 2025/7/4
 * @description
 */
public class JCellStyle extends HashMap<String, Object> {
    private static final String INDEX = "index";
    private static final String DATA_FORMAT = "dataFormat";
    private static final String DATA_FORMAT_STRING = "dataFormatString";
    private static final String FONT = "font";
    private static final String FONT_INDEX = "fontIndex";
    private static final String HIDDEN = "hidden";
    private static final String LOCKED = "locked";
    private static final String QUOTE_PREFIXED = "quotePrefixed";
    private static final String ALIGNMENT = "alignment";
    private static final String WRAP_TEXT = "wrapText";
    private static final String VERTICAL_ALIGNMENT = "verticalAlignment";
    private static final String ROTATION = "rotation";
    private static final String INDENTION = "indention";
    private static final String BORDER_LEFT = "borderLeft";
    private static final String BORDER_RIGHT = "borderRight";
    private static final String BORDER_TOP = "borderTop";
    private static final String BORDER_BOTTOM = "borderBottom";
    private static final String LEFT_BORDER_COLOR = "leftBorderColor";
    private static final String RIGHT_BORDER_COLOR = "rightBorderColor";
    private static final String TOP_BORDER_COLOR = "topBorderColor";
    private static final String BOTTOM_BORDER_COLOR = "bottomBorderColor";
    private static final String FILL_PATTERN = "fillPattern";
    private static final String FILL_BACKGROUND_COLOR = "fillBackgroundColor";
    private static final String FILL_FOREGROUND_COLOR = "fillForegroundColor";
    private static final String SHRINK_TO_FIT = "shrinkToFit";

    public short getIndex() {
        return containsKey(INDEX) ? (short) get(INDEX) : 0;
    }

    public void setDataFormat(short var1) {
        put(DATA_FORMAT, var1);
    }

    public short getDataFormat() {
        return containsKey(DATA_FORMAT) ? (short) get(DATA_FORMAT) : 0;
    }

    public String getDataFormatString() {
        return (String) get(DATA_FORMAT_STRING);
    }

    public void setFont(Font var1) {
        put(FONT, var1);
    }

    public int getFontIndex() {
        return containsKey(FONT_INDEX) ? (int) get(FONT_INDEX) : 0;
    }

    @Deprecated
    public int getFontIndexAsInt() {
        return getFontIndex();
    }

    public void setHidden(boolean var1) {
        put(HIDDEN, var1);
    }

    public boolean getHidden() {
        return containsKey(HIDDEN) ? (boolean) get(HIDDEN) : false;
    }

    public void setLocked(boolean var1) {
        put(LOCKED, var1);
    }

    public boolean getLocked() {
        return containsKey(LOCKED) ? (boolean) get(LOCKED) : false;
    }

    public void setQuotePrefixed(boolean var1) {
        put(QUOTE_PREFIXED, var1);
    }

    public boolean getQuotePrefixed() {
        return containsKey(QUOTE_PREFIXED) ? (boolean) get(QUOTE_PREFIXED) : false;
    }

    public void setAlignment(HorizontalAlignment var1) {
        put(ALIGNMENT, var1);
    }

    public HorizontalAlignment getAlignment() {
        return (HorizontalAlignment) get(ALIGNMENT);
    }

    public void setWrapText(boolean var1) {
        put(WRAP_TEXT, var1);
    }

    public boolean getWrapText() {
        return containsKey(WRAP_TEXT) ? (boolean) get(WRAP_TEXT) : false;
    }

    public void setVerticalAlignment(VerticalAlignment var1) {
        put(VERTICAL_ALIGNMENT, var1);
    }

    public VerticalAlignment getVerticalAlignment() {
        return (VerticalAlignment) get(VERTICAL_ALIGNMENT);
    }

    public void setRotation(short var1) {
        put(ROTATION, var1);
    }

    public short getRotation() {
        return containsKey(ROTATION) ? (short) get(ROTATION) : 0;
    }

    public void setIndention(short var1) {
        put(INDENTION, var1);
    }

    public short getIndention() {
        return containsKey(INDENTION) ? (short) get(INDENTION) : 0;
    }

    public void setBorderLeft(BorderStyle var1) {
        put(BORDER_LEFT, var1);
    }

    public BorderStyle getBorderLeft() {
        return (BorderStyle) get(BORDER_LEFT);
    }

    public void setBorderRight(BorderStyle var1) {
        put(BORDER_RIGHT, var1);
    }

    public BorderStyle getBorderRight() {
        return (BorderStyle) get(BORDER_RIGHT);
    }

    public void setBorderTop(BorderStyle var1) {
        put(BORDER_TOP, var1);
    }

    public BorderStyle getBorderTop() {
        return (BorderStyle) get(BORDER_TOP);
    }

    public void setBorderBottom(BorderStyle var1) {
        put(BORDER_BOTTOM, var1);
    }

    public BorderStyle getBorderBottom() {
        return (BorderStyle) get(BORDER_BOTTOM);
    }

    public void setLeftBorderColor(short var1) {
        put(LEFT_BORDER_COLOR, var1);
    }

    public short getLeftBorderColor() {
        return containsKey(LEFT_BORDER_COLOR) ? (short) get(LEFT_BORDER_COLOR) : 0;
    }

    public void setRightBorderColor(short var1) {
        put(RIGHT_BORDER_COLOR, var1);
    }

    public short getRightBorderColor() {
        return containsKey(RIGHT_BORDER_COLOR) ? (short) get(RIGHT_BORDER_COLOR) : 0;
    }

    public void setTopBorderColor(short var1) {
        put(TOP_BORDER_COLOR, var1);
    }

    public short getTopBorderColor() {
        return containsKey(TOP_BORDER_COLOR) ? (short) get(TOP_BORDER_COLOR) : 0;
    }

    public void setBottomBorderColor(short var1) {
        put(BOTTOM_BORDER_COLOR, var1);
    }

    public short getBottomBorderColor() {
        return containsKey(BOTTOM_BORDER_COLOR) ? (short) get(BOTTOM_BORDER_COLOR) : 0;
    }

    public void setFillPattern(FillPatternType var1) {
        put(FILL_PATTERN, var1);
    }

    public FillPatternType getFillPattern() {
        return (FillPatternType) get(FILL_PATTERN);
    }

    public void setFillBackgroundColor(short var1) {
        put(FILL_BACKGROUND_COLOR, var1);
    }

    public void setFillBackgroundColor(Color var1) {
        put(FILL_BACKGROUND_COLOR, var1);
    }

    public short getFillBackgroundColor() {
        return containsKey(FILL_BACKGROUND_COLOR) && get(FILL_BACKGROUND_COLOR) instanceof Short
                ? (short) get(FILL_BACKGROUND_COLOR) : 0;
    }

    public Color getFillBackgroundColorColor() {
        return get(FILL_BACKGROUND_COLOR) instanceof Color
                ? (Color) get(FILL_BACKGROUND_COLOR) : null;
    }

    public void setFillForegroundColor(short var1) {
        put(FILL_FOREGROUND_COLOR, var1);
    }

    public void setFillForegroundColor(Color var1) {
        put(FILL_FOREGROUND_COLOR, var1);
    }

    public short getFillForegroundColor() {
        return containsKey(FILL_FOREGROUND_COLOR) && get(FILL_FOREGROUND_COLOR) instanceof Short
                ? (short) get(FILL_FOREGROUND_COLOR) : 0;
    }

    public Color getFillForegroundColorColor() {
        return get(FILL_FOREGROUND_COLOR) instanceof Color
                ? (Color) get(FILL_FOREGROUND_COLOR) : null;
    }

    public void cloneStyleFrom(JCellStyle var1) {
        clear();
        putAll(var1);
    }

    public void setShrinkToFit(boolean var1) {
        put(SHRINK_TO_FIT, var1);
    }

    public boolean getShrinkToFit() {
        return containsKey(SHRINK_TO_FIT) ? (boolean) get(SHRINK_TO_FIT) : false;
    }
}
