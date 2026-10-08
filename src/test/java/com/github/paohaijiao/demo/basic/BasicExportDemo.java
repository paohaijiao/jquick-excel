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
package com.github.paohaijiao.demo.basic;

import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类：<b>基础导出</b> —— 三步调用骨架（ParseFactory → JQuickXmlFactory → 代理接口）。
 *
 * <p>规则文件 {@code demo/basic/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory}，不使用任何自封装方法。
 * 产物目录：{@code D:\test\excel}。
 */
public class BasicExportDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/basic/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 基础导出：MAPPING 转中文表头，性别码值经字典转中文，姓名转大写。 */
    @Test
    public void exportBasic() throws Exception {
        JContext ctx = new JContext();
        Map<String, Object> dict = new HashMap<>();
        dict.put("1", "男");
        dict.put("2", "女");
        ctx.put("dict", dict);

        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", "1");
        r1.put("c", 20);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", "2");
        r2.put("c", 21);
        rows.add(r2);

        File out = new File(OUT_DIR, "basic-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(ctx, rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            BasicService service = factory.createApi(BasicService.class);
            service.exportBasic("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Row header = wb.getSheet("基础导出").getRow(0);
            System.out.println("【基础导出】表头: " + header.getCell(0).getStringCellValue()
                    + "/" + header.getCell(1).getStringCellValue()
                    + "/" + header.getCell(2).getStringCellValue());
            Assert.assertEquals("姓名", header.getCell(0).getStringCellValue());
            Assert.assertEquals("性别", header.getCell(1).getStringCellValue());
            Assert.assertEquals("年龄", header.getCell(2).getStringCellValue());
        }
    }
}
