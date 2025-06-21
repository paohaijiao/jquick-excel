package com.paohaijiao.javelin.excel;

import com.paohaijiao.javelin.anno.JExcelColumn;
import com.paohaijiao.javelin.anno.JExcelIgnore;
import com.paohaijiao.javelin.anno.JExcelSheet;
import com.paohaijiao.javelin.converter.JConverterRegistry;
import com.paohaijiao.javelin.converter.JCustomConverter;
import com.paohaijiao.javelin.listener.JReadListener;
import com.paohaijiao.javelin.model.JStudentModel;
import com.paohaijiao.javelin.validate.JExcelValidator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelImporter <T> {
    private final Class<T> clazz;
    private JReadListener<T> listener;
    private int headRowNumber = 1;
    private String sheetName;
    private final Map<Integer, Field> fieldMap = new HashMap<>();

    public ExcelImporter(Class<T> clazz) {
        this.clazz = clazz;
        init();
    }

    private void init() {

        JExcelSheet sheetAnnotation = clazz.getAnnotation(JExcelSheet.class);
        if (sheetAnnotation != null) {
            sheetName = sheetAnnotation.name();
            headRowNumber = sheetAnnotation.headRowNumber();
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(JExcelIgnore.class)) {
                continue;
            }
            JExcelColumn column = field.getAnnotation(JExcelColumn.class);
            if (column != null) {
                int index = column.index();
                if (index >= 0) {
                    field.setAccessible(true);
                    fieldMap.put(index, field);
                }
            }
        }
    }

    public void setListener(JReadListener<T> listener) {
        this.listener = listener;
    }

    public void read(InputStream inputStream) throws Exception {
        List<T> dataList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = sheetName != null ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
        if (sheet == null) {
            throw new RuntimeException("Sheet not found");
        }
        Iterator<Row> rowIterator = sheet.iterator();
        int currentRow = 0;
        while (currentRow < headRowNumber && rowIterator.hasNext()) {
            rowIterator.next();
            currentRow++;
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            T obj = clazz.getDeclaredConstructor().newInstance();
            boolean hasValue = false;
            List<String> errors = new ArrayList<>();
            for (Cell cell : row) {
                int columnIndex = cell.getColumnIndex();
                Field field = fieldMap.get(columnIndex);
                if (field != null) {
                    try {
                        Object value = getCellValue(cell, field);
                        if (value != null) {
                            hasValue = true;
                        }
                        field.set(obj, value);
                    } catch (Exception e) {
                        errors.add(String.format("第%d行第%d列数据格式错误: %s",
                                row.getRowNum() + 1, columnIndex + 1, e.getMessage()));
                    }
                }
            }

            if (hasValue) {
                // 校验数据
                if (obj instanceof JStudentModel) {
                    errors.addAll(JExcelValidator.validate((JStudentModel) obj));
                }

                if (errors.isEmpty()) {
                    dataList.add(obj);
                } else if (listener != null) {
                    listener.onError(new RuntimeException(String.join("; ", errors)));
                }
            }
        }
        workbook.close();
        if (listener != null) {
            listener.onSuccess(dataList);
            listener.onComplete();
        }
    }

    private Object getCellValue(Cell cell, Field field) throws Exception {
        JExcelColumn column = field.getAnnotation(JExcelColumn.class);
        Class<?> fieldType = field.getType();
        String cellValue = getStringCellValue(cell);
        if (cellValue == null || cellValue.isEmpty()) {
            return null;
        }
        // 使用自定义转换器
        if (column != null && !column.converter().isEmpty()) {
            JCustomConverter<?> converter = JConverterRegistry.getConverter(column.converter());
            if (converter != null) {
                return converter.convert(cellValue);
            }
        }
        if (fieldType == String.class) {
            return cellValue;
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.parseInt(cellValue);
        } else if (fieldType == Long.class || fieldType == long.class) {
            return Long.parseLong(cellValue);
        } else if (fieldType == Double.class || fieldType == double.class) {
            return Double.parseDouble(cellValue);
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.parseBoolean(cellValue);
        } else if (fieldType == Date.class) {
            String format = column != null && !column.format().isEmpty() ? column.format() : "yyyy-MM-dd";
            return new SimpleDateFormat(format).parse(cellValue);
        }
        return cellValue;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

}
