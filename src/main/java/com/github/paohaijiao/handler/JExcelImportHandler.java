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
package com.github.paohaijiao.handler;

import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.validate.JAbstractValidationRule;
import com.github.paohaijiao.validate.JExcelValidationRule;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JExcelImportHandler extends JExcelCommonHandler {






    private final DataFormatter dataFormatter = new DataFormatter();


    public JExcelImportHandler(XSSFWorkbook xssfWorkbook) {
        this.workbook = xssfWorkbook;
        this.contextParams = new JContext();
    }

    public JExcelImportHandler(XSSFWorkbook xssfWorkbook,JContext contextParams) {
        this.workbook = xssfWorkbook;
        this.contextParams = contextParams;
    }

    public List<Map<String, Object>> importData(JExcelImportModel config) throws IOException {
        setSheet(config.getSheet());
        boolean hasHeader = config.getHeader();
        List<String> headers = new ArrayList<>();
        Map<String, String> mappings = config.getMappings();
        List<Map<String, Object>> data = new ArrayList<>();
        int startCol=0;
        if (hasHeader) {
            Row headerRow = currentSheet.getRow(0);
            int  endCol=getUsedColumnCount(currentSheet,headerRow);
            for (int i = 0; i <= endCol; i++) {
                Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String headerName = dataFormatter.formatCellValue(cell);
                headers.add(mappings.getOrDefault(headerName, headerName));
            }
        }
        int lastRowNum=this.getLastRowNum(currentSheet);
        for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
            Row row = currentSheet.getRow(rowNum);
            if (row == null) continue;
            Map<String, Object> rowData = new LinkedHashMap<>();
            int  endCol=getUsedColumnCount(currentSheet,row);
            for (int colNum = startCol; colNum <= endCol; colNum++) {
                if (colNum >= headers.size()) break;
                Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Object value = dataFormatter.formatCellValue(cell);
                Map<String, String> transforms = config.getTransforms();
                String fieldName = headers.get(colNum);
                if (transforms.containsKey(fieldName)) {
                    value = applyTransform(fieldName, value, transforms.get(fieldName));
                }
                rowData.put(headers.get(colNum), value);
            }
            data.add(rowData);
        }
        return data;
    }

    private  void applyValidate(JExcelImportModel config){
        Map<String, List<JAbstractValidationRule>>  rowValidate=config.getRowValidate();
        for (Map.Entry<String, List<JAbstractValidationRule>> entry : rowValidate.entrySet()) {
            String row=entry.getKey();
            List<JAbstractValidationRule> rules=entry.getValue();
            for(JAbstractValidationRule rule:rules){
               // rule.test(rule.);
            }
        }

    }

}
