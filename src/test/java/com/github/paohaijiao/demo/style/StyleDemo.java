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
package com.github.paohaijiao.demo.style;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * style 子包 demo：STYLE 的 ROW 与区域两种作用目标。
 *
 * <p>独立规则文件：{@code demo/style/jquick-excel.xml}。
 * 边框取值如 thin/m，填充模式 fillPattern 取 solid_foreground，
 * 颜色取 POI 调色板名（yellow/darkBlue/lightYellow）。
 */
public class StyleDemo {

    @Test
    public void exportStyle() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.personRows());
        File out = DemoKit.out("style-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            StyleService service = DemoKit.exportApi(
                    "demo/style/jquick-excel.xml", rows, os, StyleService.class);
            service.exportStyle("field", "value");
        }

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("样式");
            Row header = sheet.getRow(0);
            // POI 3.x 的 CellStyle 接口没有 getFont()，XSSFCellStyle 实现类上才有
            XSSFCellStyle headerStyle = (XSSFCellStyle) header.getCell(0).getCellStyle();
            // 表头加粗
            Assert.assertTrue(headerStyle.getFont().getBold());
            // 数据区域带下边框
            CellStyle bodyStyle = sheet.getRow(1).getCell(0).getCellStyle();
            Assert.assertNotNull(bodyStyle.getBorderBottom());
            System.out.println("表头加粗=" + headerStyle.getFont().getBold()
                    + "，数据区下边框=" + bodyStyle.getBorderBottom());
        }
    }
}
