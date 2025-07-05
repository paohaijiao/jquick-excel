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
public class JRowStyle extends HashMap<String, Object> {

    private static final String height = "height";
    private static final String rowNum = "rowNum";
    private static final String rowStyle = "rowStyle";
    private static final String heightInPoints = "heightInPoints";
    private static final String zeroHeight = "zeroHeight";

    public void setHeight(BigDecimal i) {
        put(height, i);
    }

    public BigDecimal getHeight() {
        return containsKey(height) ? (BigDecimal) get(height) : null;
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
        Map<String, Object> map=(Map<String, Object>) get(rowStyle);
        JCellStyle cellStyle=new JCellStyle();
        cellStyle.putAll(map);
        return cellStyle;

    }

    public void setHeightInPoints(BigDecimal v) {
        put(heightInPoints, v);
    }

    public BigDecimal getHeightInPoints() {
        return containsKey(heightInPoints) ? (BigDecimal) get(heightInPoints) : null;
    }

    public void setZeroHeight(boolean b) {
        put(zeroHeight, b);
    }

    public Boolean isZeroHeight() {
        return containsKey(zeroHeight) ? (boolean) get(zeroHeight) : null;
    }
}
