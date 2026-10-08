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

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类：<b>STYLE</b> —— 单元格 / 行 / 列 / 区域样式配置。
 *
 * <p>规则文件 {@code demo/style/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory}，不使用任何自封装方法。
 * 覆盖四类目标：{@code ROW 1} 行样式、{@code COL B} 列样式、{@code C2} 单元格样式、
 * {@code A2:A4} 区域样式；样式属性涵盖字体 / 行高 / 对齐 / 文本 / 保护 / 边框 / 填充。
 * 产物目录：{@code D:\test\excel}。
 */
public class StyleDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/style/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导出：ROW/COL/CELL/RANGE 四类样式目标，回读核对各自生效的属性。 */
    @Test
    public void exportStyle() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三");
        r1.put("b", 20);
        r1.put("c", "计算机1班");
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", 21);
        r2.put("c", "软件工程2班");
        rows.add(r2);
        JQuickRow r3 = new JQuickRow();
        r3.put("a", "王五");
        r3.put("b", 22);
        r3.put("c", "网络工程3班");
        rows.add(r3);

        File out = new File(OUT_DIR, "style-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            StyleService service = factory.createApi(StyleService.class);
            service.exportStyle("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("样式");
            // ROW 1：行高 + 表头字体（加粗、斜体、Arial、白色、下划线、深蓝底）
            Row header = sheet.getRow(0);
            XSSFCellStyle headerStyle = (XSSFCellStyle) header.getCell(0).getCellStyle();
            Assert.assertEquals("行高应来自 ROW 1 的 heightInPoints", 28.0, header.getHeightInPoints(), 0.01);
            Assert.assertTrue("表头应加粗", headerStyle.getFont().getBold());
            Assert.assertTrue("表头应斜体", headerStyle.getFont().getItalic());
            Assert.assertEquals("表头字体应为 Arial", "Arial", headerStyle.getFont().getFontName());

            Row dataRow = sheet.getRow(1);
            // COL B：整列左对齐 + 缩进 + 自动换行 + 左边框
            XSSFCellStyle colStyle = (XSSFCellStyle) dataRow.getCell(1).getCellStyle();
            Assert.assertEquals("COL B 应对齐左", HorizontalAlignment.LEFT, colStyle.getAlignment());
            Assert.assertEquals("COL B 应缩进 2", 2, colStyle.getIndention());
            Assert.assertTrue("COL B 应自动换行", colStyle.getWrapText());
            Assert.assertEquals("COL B 应有细左边框", BorderStyle.THIN, colStyle.getBorderLeft());

            // C2：单元格旋转 + 上双线边框
            XSSFCellStyle cellStyle = (XSSFCellStyle) dataRow.getCell(2).getCellStyle();
            Assert.assertEquals("C2 应旋转 45 度", 45, cellStyle.getRotation());
            Assert.assertEquals("C2 应有上双线边框", BorderStyle.DOUBLE, cellStyle.getBorderTop());

            // A2:A4：区域顶对齐 + 虚线下边框 + 浅黄填充
            XSSFCellStyle rangeStyle = (XSSFCellStyle) dataRow.getCell(0).getCellStyle();
            Assert.assertEquals("区域应顶对齐", VerticalAlignment.TOP, rangeStyle.getVerticalAlignment());
            Assert.assertEquals("区域应有虚线下边框", BorderStyle.DASHED, rangeStyle.getBorderBottom());

            System.out.println("【STYLE】表头加粗=" + headerStyle.getFont().getBold()
                    + "，行高=" + header.getHeightInPoints()
                    + "，COL B 对齐=" + colStyle.getAlignment()
                    + "，C2 旋转=" + cellStyle.getRotation()
                    + "，区域下边框=" + rangeStyle.getBorderBottom());
        }
    }
}
