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

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分类：<b>FORMAT</b> —— 单元格显示格式。
 *
 * <p>规则文件 {@code demo/format/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory}，不使用任何自封装方法。
 * {@code FORMAT} 必须是真实的 Excel 数字格式码，不能写 currency/percent 等描述词。
 * 产物目录：{@code D:\test\excel}。
 */
public class FormatDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/format/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导出：五种真实格式码（补零/千分位/人民币/百分比/日期），回读核对 dataFormat。 */
    @Test
    public void exportFormat() throws Exception {
        Date now = new Date();
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", 240001);
        r1.put("b", "张三");
        r1.put("c", 12345.6);
        r1.put("d", 12345.6);
        r1.put("e", 0.856);
        r1.put("f", now);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", 240002);
        r2.put("b", "李四");
        r2.put("c", 999.99);
        r2.put("d", 999.99);
        r2.put("e", 0.123);
        r2.put("f", now);
        rows.add(r2);

        File out = new File(OUT_DIR, "format-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            FormatService service = factory.createApi(FormatService.class);
            service.exportFormat("field", "value");
        }

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("格式");
            Row row = sheet.getRow(1);
            String a = row.getCell(0).getCellStyle().getDataFormatString();
            String c = row.getCell(2).getCellStyle().getDataFormatString();
            String e = row.getCell(4).getCellStyle().getDataFormatString();
            String f = row.getCell(5).getCellStyle().getDataFormatString();
            System.out.println("【FORMAT】格式码: a=" + a + ", c=" + c + ", e=" + e + ", f=" + f);
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
