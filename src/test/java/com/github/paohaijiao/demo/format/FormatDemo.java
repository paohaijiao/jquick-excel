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
package com.github.paohaijiao.demo.format;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * format 子包 demo：FORMAT 真实格式码（回读工作簿核对 dataFormat）。
 *
 * <p>独立规则文件：{@code demo/format/jquick-excel.xml}。
 */
public class FormatDemo {

    @Test
    public void exportFormat() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.formatRows());
        File out = DemoKit.out("format-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            FormatService service = DemoKit.exportApi(
                    "demo/format/jquick-excel.xml", rows, os, FormatService.class);
            service.exportFormat("field", "value");
        }

        // 回读第一行数据，断言各列拿到的是 Excel 内置/自定义格式码
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("格式");
            Row row = sheet.getRow(1);
            String a = row.getCell(0).getCellStyle().getDataFormatString();
            String c = row.getCell(2).getCellStyle().getDataFormatString();
            String e = row.getCell(4).getCellStyle().getDataFormatString();
            String f = row.getCell(5).getCellStyle().getDataFormatString();
            System.out.println("格式码: a=" + a + ", c=" + c + ", e=" + e + ", f=" + f);
            Assert.assertEquals("000000", a);
            Assert.assertEquals("#,##0.00", c);
            Assert.assertEquals("0.00%", e);
            Assert.assertEquals("yyyy-MM-dd", f);

            // 比率列存的是数值 0.856，而不是文本
            Cell ratio = row.getCell(4);
            Assert.assertEquals(CellType.NUMERIC, ratio.getCellType());
        }
    }
}
