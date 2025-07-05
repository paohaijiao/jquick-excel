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

import com.github.paohaijiao.enums.JMergeValueType;
import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.merge.JMergeHandler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

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
    public static List<Object> flapTheList(List<List<Object>> data) {
        if (null == data || data.isEmpty()) {
            return new ArrayList<>();
        }
        List<Object> result = new ArrayList<>();
        data.forEach(e -> {
            result.addAll(e);
        });
        return result;
    }
    public static boolean filterDouble(Object obj) {
        if (null == obj || !(obj instanceof Number)) {
            return false;
        }else {
            return true;
        }

    }

    public static Object buildResult(JMergeValueType type, List<List<Object>> data) {
        List<Object> list = flapTheList(data);
        if (null == list || list.isEmpty()) {
            return null;
        }
        if (JMergeValueType.FIRST.getCode().equals(type.getCode())) {
            return list.get(0);
        }
        if (JMergeValueType.LAST.getCode().equals(type.getCode())) {
            return list.get(list.size() - 1);
        }
        if (JMergeValueType.CONCAT.getCode().equals(type.getCode())) {
            return list.stream().filter(e->filterDouble(e)).map(String::valueOf).collect(Collectors.joining(","));
        }

        if (JMergeValueType.MAX.getCode().equals(type.getCode())) {
            OptionalDouble d = list.stream().filter(e->filterDouble(e)).mapToDouble(e -> Double.valueOf(e.toString())).max();
            return d.isPresent() ? d.getAsDouble() : 0d;
        }
        if (JMergeValueType.MIN.getCode().equals(type.getCode())) {
            OptionalDouble d = list.stream().filter(e->filterDouble(e)).mapToDouble(e -> Double.valueOf(e.toString())).min();
            return d.isPresent() ? d.getAsDouble() : 0d;
        }
        if (JMergeValueType.AVG.getCode().equals(type.getCode())) {
            OptionalDouble d = list.stream().filter(e->filterDouble(e)).mapToDouble(e -> Double.valueOf(e.toString())).average();
            return d.isPresent() ? d.getAsDouble() : 0d;
        }
        if (JMergeValueType.COUNT.getCode().equals(type.getCode())) {
            Long d = list.stream().filter(e->filterDouble(e)).mapToDouble(e -> Double.valueOf(e.toString())).count();
            return d;
        }
        if (JMergeValueType.SUM.getCode().equals(type.getCode())) {
            double d = list.stream().filter(e->filterDouble(e)).mapToDouble(e -> Double.valueOf(e.toString())).sum();
            return d;
        }
        return null;
    }


    public static void setMergedRegionValue(Workbook workbook, Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, JMergeValueType mergeType) {
        JAssert.notNull(mergeType, "invalid mergeType ");
        CellRangeAddress mergedRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(mergedRegion);
        Row row = sheet.getRow(firstRow);
        if (row == null) {
            row = sheet.createRow(firstRow);
        }
        List<List<Object>> list = getRangeValues(sheet, mergedRegion);
        Object object = buildResult(mergeType, list);
        Cell cell = row.getCell(firstCol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (object == null) {
            cell.setCellValue("");
        } else if (object instanceof String) {
            cell.setCellValue((String) object);
        } else if (object instanceof Double) {
            cell.setCellValue((Double) object);
        } else {
            cell.setCellValue(object.toString());
        }
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(style);
    }

    public static List<List<Object>> getRangeValues(Sheet sheet, CellRangeAddress range) {
        List<List<Object>> data = new ArrayList<>();
        for (int rowNum = range.getFirstRow(); rowNum <= range.getLastRow(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            List<Object> rowData = new ArrayList<>();
            for (int colNum = range.getFirstColumn(); colNum <= range.getLastColumn(); colNum++) {
                if (row != null) {
                    Cell cell = row.getCell(colNum);
                    Object value = getCellValue(cell);
                    rowData.add(value);
                }
            }
            data.add(rowData);
        }
        return data;
    }

    /**
     * only support the( number or string)
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                return cell.getStringCellValue();
            case BLANK:
                return "";
            default:
                return null;
        }
    }
}
