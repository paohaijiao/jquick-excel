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

import java.util.HashMap;

/**
 * packageName com.github.paohaijiao.jstyle.model
 *
 * @author Martin
 * @version 1.0.0
 * @className JRowStyle
 * @date 2025/7/4
 * @description
 */
public class JRowStyle extends HashMap<String, Object> {

    private static final String height = "height";
    private static final String rowNum = "rowNum";
    private static final String rowStyle = "rowStyle";
    private static final String heightInPoints = "heightInPoints";
    private static final String zeroHeight = "zeroHeight";

    public void setHeight(short i) {
        put(height, i);
    }

    public short getHeight() {
        return containsKey(height) ? (short) get(height) : 0;
    }

    public void setRowNum(int i) {
        put(rowNum, i);
    }

    public Integer getRowNum() {
        return containsKey(rowNum) ? (int) get(rowNum) : 0;
    }

    public void setRowStyle(JCellStyle cellStyle) {
        put(rowStyle, cellStyle);
    }

    public JCellStyle getRowStyle() {
        return (JCellStyle) get(rowStyle);
    }

    public void setHeightInPoints(float v) {
        put(heightInPoints, v);
    }

    public Float getHeightInPoints() {
        return containsKey(heightInPoints) ? (float) get(heightInPoints) : 0.0f;
    }

    public void setZeroHeight(boolean b) {
        put(zeroHeight, b);
    }

    public Boolean isZeroHeight() {
        return containsKey(zeroHeight) ? (boolean) get(zeroHeight) : false;
    }
}
