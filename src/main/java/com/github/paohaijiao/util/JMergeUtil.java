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
package com.github.paohaijiao.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * packageName com.github.paohaijiao.util
 *
 * @author Martin
 * @version 1.0.0
 * @className JMergeUtil
 * @date 2025/7/4
 * @description
 */
public class JMergeUtil {
    public static void setMergedRegionValue(Sheet sheet ) {
        CellRangeAddress mergedRegion = new CellRangeAddress(0, 2, 0, 2); // 行0-2，列0-2
        sheet.addMergedRegion(mergedRegion);
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            firstRow = sheet.createRow(0);
        }
        Cell firstCell = firstRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        firstCell.setCellValue("合并单元格内容");
    }

    public static void setMergedRegionValue(Sheet sheet, int firstRow, int lastRow,
                                            int firstCol, int lastCol, String value) {
        CellRangeAddress mergedRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(mergedRegion);
        Row row = sheet.getRow(firstRow);
        if (row == null) {
            row = sheet.createRow(firstRow);
        }
        Cell cell = row.getCell(firstCol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(style);
    }
}
