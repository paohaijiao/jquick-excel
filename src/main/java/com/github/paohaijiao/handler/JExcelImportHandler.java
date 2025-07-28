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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.*;

public class JExcelImportHandler extends JExcelCommonHandler {




    public JExcelImportHandler(XSSFWorkbook xssfWorkbook) {
        this.workbook = xssfWorkbook;
        this.context = new JContext();
    }

    public JExcelImportHandler(XSSFWorkbook xssfWorkbook,JContext contextParams) {
        this.workbook = xssfWorkbook;
        this.context = contextParams;
    }

    public List<Map<String, Object>> importData(JExcelImportModel config) throws IOException {
        setSheet(config.getSheet());
        applyValidate(config);
        boolean hasHeader = config.getHeader();
        List<String> headers = new ArrayList<>();
        Map<String, String> mappings = config.getMappings();
        List<Map<String, Object>> data = new ArrayList<>();
        int startCol=0;
         Row headerRow = currentSheet.getRow(0);
         int  endCol=getUsedColumnCount(currentSheet);
         for (int i = 0; i <= endCol; i++) {
                Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String headerName = dataFormatter.formatCellValue(cell);
                headers.add(mappings.getOrDefault(headerName, headerName));
         }
        int lastRowNum=this.getLastRowNum(currentSheet);
        int startRow = 0;
        if (hasHeader) {
            startRow = 1;
        }
        for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
            Row row = currentSheet.getRow(rowNum);
            if (row == null) continue;
            Map<String, Object> rowData = new LinkedHashMap<>();
            for (int colNum = startCol; colNum <= endCol; colNum++) {
                if (colNum >= headers.size()) break;
                Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Object value =getCellValue(cell);
                Map<String, String> transforms = config.getTransforms();
                String fieldName = headers.get(colNum);
                if (transforms.containsKey(fieldName)) {
                    value = applyTransform(fieldName, value, transforms.get(fieldName));
                }
                if(StringUtils.isNotEmpty(headers.get(colNum))&&null!=value){
                    rowData.put(headers.get(colNum), value);
                }
            }
            data.add(rowData);
        }
        return data;
    }


    private  void applyValidate(JExcelImportModel config){
        int maxCol=this.getUsedColumnCount(this.currentSheet);
        Map<String, List<JAbstractValidationRule>>  rowValidate=config.getRowValidate();
        for (Map.Entry<String, List<JAbstractValidationRule>> entry : rowValidate.entrySet()) {
            String rowNum=entry.getKey();
            List<JAbstractValidationRule> rules=entry.getValue();
            if (rowNum.contains("..")) {
                StringTokenizer tokenizer = new StringTokenizer(rowNum, "..");
                int start = Integer.parseInt(tokenizer.nextToken());
                int rowStart=start-1>0?start-1:0;
                int end = Integer.parseInt(tokenizer.nextToken());
                int rowEnd=end-1>0?end-1:0;
                for (int i = rowStart; i <= rowEnd; i++) {
                    for (int j = 0; j < maxCol; j++) {
                        String cellValue=getCellValueStringByIndex(this.currentSheet,i,j);
                        for(JAbstractValidationRule rule:rules){
                             rule.test(cellValue);
                        }
                    }
                }
            }else{
                for (int j = 0; j < maxCol; j++) {
                    Integer rowNumInteger=Integer.valueOf(rowNum);
                    int i=rowNumInteger-1>0?rowNumInteger-1:0;
                    String cellValue=getCellValueStringByIndex(this.currentSheet,i,j);
                    for(JAbstractValidationRule rule:rules){
                      boolean bool=  rule.test(cellValue);
                      if(!bool){
                          throw new RuntimeException("not validate data");
                      }
                    }
                }
            }

        }
        Map<String, List<JAbstractValidationRule>>  coalValidate=config.getColValidate();
        for (Map.Entry<String, List<JAbstractValidationRule>> keyset : coalValidate.entrySet()) {
            String colNum = keyset.getKey();
            if(StringUtils.isBlank(colNum)){
                continue;
            }
            List<JAbstractValidationRule> rules = keyset.getValue();
            if(colNum.contains("..")){
                StringTokenizer tokenizer = new StringTokenizer(colNum, "..");
                String start = tokenizer.nextToken();
                String end = tokenizer.nextToken();
                CellReference startCellReference=new CellReference(start);
                Short startCol=startCellReference.getCol();
                CellReference endCellReference=new CellReference(end);
                Short endCol=endCellReference.getCol();
                for (int i = startCol; i <= endCol; i++) {
                    for (int j = 0; j < this.getLastRowNum(this.currentSheet); j++) {
                        String cellValue=getCellValueStringByIndex(this.currentSheet,i,j);
                        for(JAbstractValidationRule rule:rules){
                            rule.test(cellValue);
                        }
                    }
                }
            }else{
                for (int i = 0; i < this.getLastRowNum(this.currentSheet); i++) {
                    CellReference cellReference=new CellReference(colNum);
                    Short col=cellReference.getCol();
                    String cellValue=getCellValueStringByIndex(this.currentSheet,i,col.intValue());
                    for(JAbstractValidationRule rule:rules){
                        rule.test(cellValue);
                    }
                }
            }
        }
        for (Map.Entry<String,  List<JAbstractValidationRule>> keyset : config.getCellValidate().entrySet()) {
            String key = keyset.getKey();
            if(StringUtils.isBlank(key)){
                continue;
            }

            List<JAbstractValidationRule> rules = keyset.getValue();
            CellReference cellReference=new CellReference(key);
            String cellValue=getCellValueStringByIndex(this.currentSheet,cellReference.getRow(),cellReference.getCol());
            for(JAbstractValidationRule rule:rules){
                rule.test(cellValue);
            }
        }
        for (Map.Entry<String,  List<JAbstractValidationRule>> keyset : config.getRangeValidate().entrySet()) {
            String key = keyset.getKey();
            if(StringUtils.isBlank(key)){
                continue;
            }
            List<JAbstractValidationRule> rules = keyset.getValue();
            CellRangeAddress rangeRegion = CellRangeAddress.valueOf(key);
            int firstRow = rangeRegion.getFirstRow();
            int lastRow = rangeRegion.getLastRow();
            int firstCol = rangeRegion.getFirstColumn();
            int lastCol = rangeRegion.getLastColumn();
            for (int i = firstRow; i <= lastRow; i++) {
                for (int j = firstCol; j <= lastCol; j++) {
                    Cell cell=getCellByIndex(this.currentSheet,i,j);
                    DataFormatter formatter = new DataFormatter();
                    String cellValue=formatter.formatCellValue(cell);
                    for(JAbstractValidationRule rule:rules){
                        rule.test(cellValue);
                    }
                }
            }
        }
    }

}
