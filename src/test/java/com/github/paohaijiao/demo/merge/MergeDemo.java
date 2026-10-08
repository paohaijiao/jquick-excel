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
package com.github.paohaijiao.demo.merge;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
 * 分类：<b>MERGE</b> —— 单元格合并。
 *
 * <p>规则文件 {@code demo/merge/jquick-excel.xml}，直接使用框架入口
 * {@link JQuickExcelExportXmlParseFactory}，不使用任何自封装方法。
 * 聚合关键字必须写完整 code：{@code MERGE_WITH_MAX / MIN / VALUE / AVG / COUNT /
 * SUM / CONCAT / FIRST / LAST}；ROWS 与 COLS 不能同表叠加（区域重叠会报错）。
 * 产物目录：{@code D:\test\excel}。
 */
public class MergeDemo {

    private static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel");
    private static final String XML = "demo/merge/jquick-excel.xml";

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    /** 按行合并：第 1 行取首个值，第 2 行拼接全部值。 */
    @Test
    public void mergeRows() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "研发部");
        r1.put("b", "张三");
        r1.put("c", 100);
        r1.put("d", 120);
        r1.put("e", 130);
        r1.put("f", 140);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "研发部");
        r2.put("b", "李四");
        r2.put("c", 110);
        r2.put("d", 125);
        r2.put("e", 135);
        r2.put("f", 145);
        rows.add(r2);

        File out = new File(OUT_DIR, "merge-rows.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            MergeService service = factory.createApi(MergeService.class);
            service.mergeRows("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            List<CellRangeAddress> regions = wb.getSheet("合并-行").getMergedRegions();
            System.out.println("【MERGE】行合并区域: " + regions);
            Assert.assertFalse(regions.isEmpty());
        }
    }

    /** 按列合并：A~F 六列分别纵向合并。 */
    @Test
    public void mergeCols() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        JQuickRow r1 = new JQuickRow();
        r1.put("a", "研发部");
        r1.put("b", "张三");
        r1.put("c", 100);
        r1.put("d", 120);
        r1.put("e", 130);
        r1.put("f", 140);
        rows.add(r1);
        JQuickRow r2 = new JQuickRow();
        r2.put("a", "研发部");
        r2.put("b", "李四");
        r2.put("c", 110);
        r2.put("d", 125);
        r2.put("e", 135);
        r2.put("f", 145);
        rows.add(r2);

        File out = new File(OUT_DIR, "merge-cols.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            MergeService service = factory.createApi(MergeService.class);
            service.mergeCols("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            List<CellRangeAddress> regions = wb.getSheet("合并-列").getMergedRegions();
            System.out.println("【MERGE】列合并区域数: " + regions.size());
            Assert.assertTrue(regions.size() >= 6);
        }
    }
}
