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
package com.github.paohaijiao.demo.spi;

import com.github.paohaijiao.function.manager.JQuickMethodInvocationManager;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类：<b>SPI</b> —— 通过 SPI 扩展自定义函数，供 {@code TRANSFORM} 调用。
 *
 * <p>全部调用都直接使用框架入口 {@link JQuickExcelExportXmlParseFactory} /
 * {@link JQuickExcelImportXmlParseFactory}，规则文件 {@code demo/spi/jquick-excel.xml}，
 * 不使用任何自封装方法。产物目录：{@code D:\test\excel}。
 *
 * <p>自定义函数（{@link JQuickMaskNameFunctionProvider}、{@link JQuickGradeFunctionProvider}）
 * 通过 {@code META-INF/services} 注册，与依赖 jar 内置函数（add/trim）在同一个注册表中并存。
 */
public class SpiDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/spi/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 导入：姓名走自定义 SPI 函数 maskname 脱敏，张三 -> 张*。 */
    @Test
    public void importSpiTransform() throws Exception {
        try (InputStream in = SpiDemo.class.getClassLoader().getResourceAsStream("demo/import-source.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            SpiService service = factory.createApi(SpiService.class);
            List<JQuickRow> rows = service.importSpiTransform("field", "value");
            JQuickRow first = rows.get(0);
            System.out.println("【SPI】导入脱敏后 name=" + first.get("name") + ", className=" + first.get("className"));
            Assert.assertEquals(3, rows.size());
            Assert.assertEquals("张*", first.get("name"));
        }
    }

    /** 导出：maskname/grade 为自定义 SPI，add/trim 为内置 SPI，四列同时生效。 */
    @Test
    public void exportSpiTransform() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "张三丰");
        r1.put("b", 95);
        r1.put("c", 20);
        r1.put("d", "  备注  ");
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "李四");
        r2.put("b", 58);
        r2.put("c", 22);
        r2.put("d", " 说明 ");
        rows.add(r2);

        File out = new File(OUT_DIR, "spi-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            SpiService service = factory.createApi(SpiService.class);
            service.exportSpiTransform("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Row dataRow = wb.getSheet("SPI转换").getRow(1);
            System.out.println("【SPI】导出转换后 a=" + dataRow.getCell(0).getStringCellValue()
                    + ", b=" + dataRow.getCell(1).getStringCellValue()
                    + ", c=" + dataRow.getCell(2).getNumericCellValue()
                    + ", d=" + dataRow.getCell(3).getStringCellValue());
            Assert.assertEquals("张**", dataRow.getCell(0).getStringCellValue());
            Assert.assertEquals("A", dataRow.getCell(1).getStringCellValue());
            Assert.assertEquals(21.0, dataRow.getCell(2).getNumericCellValue(), 0.0001);
            Assert.assertEquals("备注", dataRow.getCell(3).getStringCellValue());
        }
    }

    /** SPI 注册表：自定义与内置函数共存，并可直接按名调用。 */
    @Test
    public void spiRegistry() {
        JQuickMethodInvocationManager manager = JQuickMethodInvocationManager.getInstance();
        Assert.assertTrue("自定义 maskname 未注册", manager.hasMethod("maskname"));
        Assert.assertTrue("自定义 grade 未注册", manager.hasMethod("grade"));
        Assert.assertTrue("内置 add 未注册", manager.hasMethod("add"));
        Assert.assertEquals("张*", manager.invoke("maskname", "张三"));
        Assert.assertEquals("A", manager.invoke("grade", 95));
        System.out.println("【SPI】注册表函数总数: " + manager.getSupportedMethods().size());
    }
}
