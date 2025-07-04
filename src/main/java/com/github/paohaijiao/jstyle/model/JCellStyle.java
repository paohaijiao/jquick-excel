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

    public Short getIndex() {
        return containsKey(INDEX) ? (short) get(INDEX) : 0;
    }

    public void setDataFormat(short var1) {
        put(DATA_FORMAT, var1);
    }

    public Short getDataFormat() {
        return containsKey(DATA_FORMAT) ? (short) get(DATA_FORMAT) : 0;
    }

    public String getDataFormatString() {
        return (String) get(DATA_FORMAT_STRING);
    }

    public void setFont(Font var1) {
        put(FONT, var1);
    }

    public Integer getFontIndex() {
        return containsKey(FONT_INDEX) ? (int) get(FONT_INDEX) : 0;
    }

    @Deprecated
    public Integer getFontIndexAsInt() {
        return getFontIndex();
    }

    public void setHidden(boolean var1) {
        put(HIDDEN, var1);
    }

    public Boolean getHidden() {
        return containsKey(HIDDEN) ? (boolean) get(HIDDEN) : false;
    }

    public void setLocked(boolean var1) {
        put(LOCKED, var1);
    }

    public Boolean getLocked() {
        return containsKey(LOCKED) ? (boolean) get(LOCKED) : false;
    }

    public void setQuotePrefixed(boolean var1) {
        put(QUOTE_PREFIXED, var1);
    }

    public Boolean getQuotePrefixed() {
        return containsKey(QUOTE_PREFIXED) ? (boolean) get(QUOTE_PREFIXED) : false;
    }

    public void setAlignment(HorizontalAlignment var1) {
        put(ALIGNMENT, var1);
    }

    public HorizontalAlignment getAlignment() {
        if (null == get(ALIGNMENT)) {
            return null;
        }
        String align = (String) get(ALIGNMENT);
        if (align == null) {
            return null;
        } else if (align.equals("left")) {
            return HorizontalAlignment.LEFT;
        } else if (align.equals("right")) {
            return HorizontalAlignment.RIGHT;
        } else if (align.equals("center")) {
            return HorizontalAlignment.CENTER;
        } else if (align.equals("center-section")) {
            return HorizontalAlignment.CENTER_SELECTION;
        } else if (align.equals("general")) {
            return HorizontalAlignment.GENERAL;
        } else if (align.equals("fill")) {
            return HorizontalAlignment.FILL;
        } else if (align.equals("justify")) {
            return HorizontalAlignment.JUSTIFY;
        } else if (align.equals("distributed")) {
            return HorizontalAlignment.DISTRIBUTED;
        } else {
            return HorizontalAlignment.CENTER;
        }
    }

    public void setWrapText(boolean var1) {
        put(WRAP_TEXT, var1);
    }

    public Boolean getWrapText() {
        return containsKey(WRAP_TEXT) ? (boolean) get(WRAP_TEXT) : false;
    }

    public void setVerticalAlignment(VerticalAlignment var1) {
        put(VERTICAL_ALIGNMENT, var1);
    }

    public VerticalAlignment getVerticalAlignment() {
        if (null == get(VERTICAL_ALIGNMENT)) {
            return null;
        }
        String align = (String) get(VERTICAL_ALIGNMENT);
        if ("top".equals(align)) {
            return VerticalAlignment.TOP;
        } else if ("bottom".equals(align)) {
            return VerticalAlignment.BOTTOM;
        } else if ("center".equals(align)) {
            return VerticalAlignment.CENTER;
        } else if ("justify".equals(align)) {
            return VerticalAlignment.JUSTIFY;
        } else if ("distributed".equals(align)) {
            return VerticalAlignment.DISTRIBUTED;
        } else {
            return VerticalAlignment.CENTER;
        }
    }

    public void setRotation(short var1) {
        put(ROTATION, var1);
    }

    public Short getRotation() {
        return containsKey(ROTATION) ? (short) get(ROTATION) : 0;
    }

    public void setIndention(short var1) {
        put(INDENTION, var1);
    }

    public Short getIndention() {
        return containsKey(INDENTION) ? (short) get(INDENTION) : 0;
    }

    public void setBorderLeft(BorderStyle var1) {
        put(BORDER_LEFT, var1);
    }

    public BorderStyle getBorderLeft() {
        if (null == get(BORDER_LEFT)) {
            return null;
        }
        String borderLeft = (String) get(BORDER_LEFT);
        if ("none".equals(borderLeft)) {
            return BorderStyle.NONE;
        } else if ("thin".equals(borderLeft)) {
            return BorderStyle.THIN;
        } else if ("medium".equals(borderLeft)) {
            return BorderStyle.MEDIUM;
        } else if ("dashed".equals(borderLeft)) {
            return BorderStyle.DASHED;
        } else if ("dotted".equals(borderLeft)) {
            return BorderStyle.DOTTED;
        } else if ("thick".equals(borderLeft)) {
            return BorderStyle.THICK;
        } else if ("double".equals(borderLeft)) {
            return BorderStyle.DOUBLE;
        } else if ("hair".equals(borderLeft)) {
            return BorderStyle.HAIR;
        } else if ("medium_dashed".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASHED;
        } else if ("dash_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT;
        } else if ("medium_dash_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT;
        } else if ("dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT_DOT;
        } else if ("medium_dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT_DOT;
        } else if ("slanted_dash_dot".equals(borderLeft)) {
            return BorderStyle.SLANTED_DASH_DOT;
        } else {
            return BorderStyle.NONE;
        }
    }

    public void setBorderRight(BorderStyle var1) {
        put(BORDER_RIGHT, var1);
    }

    public BorderStyle getBorderRight() {
        if (null == get(BORDER_RIGHT)) {
            return null;
        }
        String borderLeft = (String) get(BORDER_RIGHT);
        if ("none".equals(borderLeft)) {
            return BorderStyle.NONE;
        } else if ("thin".equals(borderLeft)) {
            return BorderStyle.THIN;
        } else if ("medium".equals(borderLeft)) {
            return BorderStyle.MEDIUM;
        } else if ("dashed".equals(borderLeft)) {
            return BorderStyle.DASHED;
        } else if ("dotted".equals(borderLeft)) {
            return BorderStyle.DOTTED;
        } else if ("thick".equals(borderLeft)) {
            return BorderStyle.THICK;
        } else if ("double".equals(borderLeft)) {
            return BorderStyle.DOUBLE;
        } else if ("hair".equals(borderLeft)) {
            return BorderStyle.HAIR;
        } else if ("medium_dashed".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASHED;
        } else if ("dash_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT;
        } else if ("medium_dash_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT;
        } else if ("dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT_DOT;
        } else if ("medium_dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT_DOT;
        } else if ("slanted_dash_dot".equals(borderLeft)) {
            return BorderStyle.SLANTED_DASH_DOT;
        } else {
            return BorderStyle.NONE;
        }
    }

    public void setBorderTop(BorderStyle var1) {
        put(BORDER_TOP, var1);
    }

    public BorderStyle getBorderTop() {
        if (null == get(BORDER_TOP)) {
            return null;
        }
        String borderLeft = (String) get(BORDER_TOP);
        if ("none".equals(borderLeft)) {
            return BorderStyle.NONE;
        } else if ("thin".equals(borderLeft)) {
            return BorderStyle.THIN;
        } else if ("medium".equals(borderLeft)) {
            return BorderStyle.MEDIUM;
        } else if ("dashed".equals(borderLeft)) {
            return BorderStyle.DASHED;
        } else if ("dotted".equals(borderLeft)) {
            return BorderStyle.DOTTED;
        } else if ("thick".equals(borderLeft)) {
            return BorderStyle.THICK;
        } else if ("double".equals(borderLeft)) {
            return BorderStyle.DOUBLE;
        } else if ("hair".equals(borderLeft)) {
            return BorderStyle.HAIR;
        } else if ("medium_dashed".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASHED;
        } else if ("dash_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT;
        } else if ("medium_dash_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT;
        } else if ("dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT_DOT;
        } else if ("medium_dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT_DOT;
        } else if ("slanted_dash_dot".equals(borderLeft)) {
            return BorderStyle.SLANTED_DASH_DOT;
        } else {
            return BorderStyle.NONE;
        }
    }

    public void setBorderBottom(BorderStyle var1) {
        put(BORDER_BOTTOM, var1);
    }

    public BorderStyle getBorderBottom() {
        if (null == get(BORDER_BOTTOM)) {
            return null;
        }
        String borderLeft = (String) get(BORDER_BOTTOM);
        if ("none".equals(borderLeft)) {
            return BorderStyle.NONE;
        } else if ("thin".equals(borderLeft)) {
            return BorderStyle.THIN;
        } else if ("medium".equals(borderLeft)) {
            return BorderStyle.MEDIUM;
        } else if ("dashed".equals(borderLeft)) {
            return BorderStyle.DASHED;
        } else if ("dotted".equals(borderLeft)) {
            return BorderStyle.DOTTED;
        } else if ("thick".equals(borderLeft)) {
            return BorderStyle.THICK;
        } else if ("double".equals(borderLeft)) {
            return BorderStyle.DOUBLE;
        } else if ("hair".equals(borderLeft)) {
            return BorderStyle.HAIR;
        } else if ("medium_dashed".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASHED;
        } else if ("dash_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT;
        } else if ("medium_dash_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT;
        } else if ("dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.DASH_DOT_DOT;
        } else if ("medium_dash_dot_dot".equals(borderLeft)) {
            return BorderStyle.MEDIUM_DASH_DOT_DOT;
        } else if ("slanted_dash_dot".equals(borderLeft)) {
            return BorderStyle.SLANTED_DASH_DOT;
        } else {
            return BorderStyle.NONE;
        }
    }

    public void setLeftBorderColor(short var1) {
        put(LEFT_BORDER_COLOR, var1);
    }

    public Short getLeftBorderColor() {
        return containsKey(LEFT_BORDER_COLOR) ? (short) get(LEFT_BORDER_COLOR) : 0;
    }

    public void setRightBorderColor(short var1) {
        put(RIGHT_BORDER_COLOR, var1);
    }

    public Short getRightBorderColor() {
        return containsKey(RIGHT_BORDER_COLOR) ? (short) get(RIGHT_BORDER_COLOR) : 0;
    }

    public void setTopBorderColor(short var1) {
        put(TOP_BORDER_COLOR, var1);
    }

    public Short getTopBorderColor() {
        return containsKey(TOP_BORDER_COLOR) ? (short) get(TOP_BORDER_COLOR) : 0;
    }

    public void setBottomBorderColor(short var1) {
        put(BOTTOM_BORDER_COLOR, var1);
    }

    public Short getBottomBorderColor() {
        return containsKey(BOTTOM_BORDER_COLOR) ? (short) get(BOTTOM_BORDER_COLOR) : 0;
    }

    public void setFillPattern(FillPatternType var1) {
        put(FILL_PATTERN, var1);
    }

    public FillPatternType getFillPattern() {
        if (null == get(FILL_PATTERN)) {
            return null;
        }
        String borderLeft = (String) get(FILL_PATTERN);
        if ("no_fill".equals(borderLeft)) {
            return FillPatternType.NO_FILL;
        } else if ("solid_foreground".equals(borderLeft)) {
            return FillPatternType.SOLID_FOREGROUND;
        } else if ("fine_dots".equals(borderLeft)) {
            return FillPatternType.FINE_DOTS;
        } else if ("fine_dots".equals(borderLeft)) {
            return FillPatternType.FINE_DOTS;
        } else if ("alt_bars".equals(borderLeft)) {
            return FillPatternType.ALT_BARS;
        } else if ("sparse_dots".equals(borderLeft)) {
            return FillPatternType.SPARSE_DOTS;
        } else if ("thick_horz_bands".equals(borderLeft)) {
            return FillPatternType.THICK_HORZ_BANDS;
        } else if ("thick_vert_bands".equals(borderLeft)) {
            return FillPatternType.THICK_VERT_BANDS;
        } else if ("thick_backward_diag".equals(borderLeft)) {
            return FillPatternType.THICK_BACKWARD_DIAG;
        } else if ("thick_forward_diag".equals(borderLeft)) {
            return FillPatternType.THICK_FORWARD_DIAG;
        } else if ("big_spots".equals(borderLeft)) {
            return FillPatternType.BIG_SPOTS;
        } else if ("bricks".equals(borderLeft)) {
            return FillPatternType.BRICKS;
        } else if ("thin_horz_bands".equals(borderLeft)) {
            return FillPatternType.THIN_HORZ_BANDS;
        } else if ("thin_vert_bands".equals(borderLeft)) {
            return FillPatternType.THIN_VERT_BANDS;
        } else if ("thin_backward_diag".equals(borderLeft)) {
            return FillPatternType.THIN_BACKWARD_DIAG;
        } else if ("thin_forward_diag".equals(borderLeft)) {
            return FillPatternType.THIN_FORWARD_DIAG;
        } else if ("squares".equals(borderLeft)) {
            return FillPatternType.SQUARES;
        } else if ("diamonds".equals(borderLeft)) {
            return FillPatternType.DIAMONDS;
        } else if ("less_dots".equals(borderLeft)) {
            return FillPatternType.LESS_DOTS;
        } else if ("least_dots".equals(borderLeft)) {
            return FillPatternType.LEAST_DOTS;
        } else {
            return FillPatternType.NO_FILL;
        }
    }

    public void setFillBackgroundColor(short var1) {
        put(FILL_BACKGROUND_COLOR, var1);
    }

    public void setFillBackgroundColor(Color var1) {
        put(FILL_BACKGROUND_COLOR, var1);
    }

    public Short getFillBackgroundColor() {
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

    public Short getFillForegroundColor() {
        return containsKey(FILL_FOREGROUND_COLOR) && get(FILL_FOREGROUND_COLOR) instanceof Short
                ? (short) get(FILL_FOREGROUND_COLOR) : 0;
    }

    public Color getFillForegroundColorColor() {
        if (null == get(FILL_FOREGROUND_COLOR)) {
            return null;
        }
        return null;
    }

    public void cloneStyleFrom(JCellStyle var1) {
        clear();
        putAll(var1);
    }

    public void setShrinkToFit(boolean var1) {
        put(SHRINK_TO_FIT, var1);
    }

    public Boolean getShrinkToFit() {
        return containsKey(SHRINK_TO_FIT) ? (boolean) get(SHRINK_TO_FIT) : false;
    }
}
