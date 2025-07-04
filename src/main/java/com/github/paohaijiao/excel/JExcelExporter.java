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
package com.github.paohaijiao.excel;

import com.github.paohaijiao.anno.JExcelColumn;
import com.github.paohaijiao.anno.JExcelIgnore;
import com.github.paohaijiao.anno.JExcelSheet;
import com.github.paohaijiao.converter.JConverterRegistry;
import com.github.paohaijiao.converter.JCustomConverter;
import com.github.paohaijiao.listener.JWriteListener;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class JExcelExporter<T> {
    private final Class<T> clazz;
    private JWriteListener listener;
    private int headRowNumber = 1;
    private String sheetName;
    private final List<Field> fields = new ArrayList<>();
    private final Map<Field, JExcelColumn> fieldAnnotations = new HashMap<>();

    public JExcelExporter(Class<T> clazz) {
        this.clazz = clazz;
        init();
    }

    private void init() {
        JExcelSheet sheetAnnotation = clazz.getAnnotation(JExcelSheet.class);
        if (sheetAnnotation != null) {
            sheetName = sheetAnnotation.name();
            headRowNumber = sheetAnnotation.headRowNumber();
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(JExcelIgnore.class)) {
                continue;
            }
            JExcelColumn column = field.getAnnotation(JExcelColumn.class);
            if (column != null) {
                field.setAccessible(true);
                fields.add(field);
                fieldAnnotations.put(field, column);
            }
        }

        // 按index排序
        fields.sort(Comparator.comparingInt(f -> {
            JExcelColumn column = fieldAnnotations.get(f);
            return column != null ? column.index() : 0;
        }));
    }

    public void setListener(JWriteListener listener) {
        this.listener = listener;
    }

    public void write(OutputStream outputStream, List<T> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = sheetName != null ? workbook.createSheet(sheetName) : workbook.createSheet();
        createHeader(workbook, sheet);
        fillData(workbook, sheet, data);
        workbook.write(outputStream);
        workbook.close();
        if (listener != null) {
            listener.onSuccess();
        }
    }

    public void writeWithTemplate(InputStream templateStream, OutputStream outputStream, List<T> data) throws IOException {
        Workbook workbook = new XSSFWorkbook(templateStream);
        Sheet sheet = sheetName != null ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
        if (sheet == null) {
            throw new RuntimeException("Sheet not found in template");
        }
        fillData(workbook, sheet, data);
        workbook.write(outputStream);
        workbook.close();
        if (listener != null) {
            listener.onSuccess();
        }
    }

    private void createHeader(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        // CellStyle headerStyle = JCustomCellStyle.createHeaderStyle(workbook);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            JExcelColumn column = fieldAnnotations.get(field);
            String headerName = column != null && !column.name().isEmpty() ? column.name() : field.getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerName);
            //cell.setCellStyle(headerStyle);
        }
    }

    private void fillData(Workbook workbook, Sheet sheet, List<T> data) {
        //   CellStyle dataStyle = JCustomCellStyle.createDataStyle(workbook);
        int startRow = headRowNumber;
        for (int i = 0; i < data.size(); i++) {
            T item = data.get(i);
            Row row = sheet.createRow(startRow + i);
            for (int j = 0; j < fields.size(); j++) {
                Field field = fields.get(j);
                JExcelColumn column = fieldAnnotations.get(field);
                try {
                    T value = (T) field.get(item);
                    Cell cell = row.createCell(j);
                    //       cell.setCellStyle(dataStyle);
                    if (value != null) {
                        String cellValue = convertValueToString(value, column);
                        cell.setCellValue(cellValue);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(new RuntimeException("导出数据失败: " + e.getMessage()));
                    }
                }
            }
        }
    }

    private String convertValueToString(T value, JExcelColumn column) throws Exception {
        if (column != null && !column.converter().isEmpty()) {
            JCustomConverter<T> converter = JConverterRegistry.getConverter(column.converter());
            if (converter != null) {
                return converter.reverseConvert(value);
            }
        }

        // 默认转换
        if (value instanceof Date) {
            String format = column != null && !column.format().isEmpty() ? column.format() : "yyyy-MM-dd";
            return new SimpleDateFormat(format).format((Date) value);
        }

        return value.toString();
    }
}
