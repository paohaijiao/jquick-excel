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

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * merge 子包 demo：MERGE 两种方向 + 两种聚合策略。
 *
 * <p>独立规则文件：{@code demo/merge/jquick-excel.xml}。
 * 九种聚合策略完整 code：MERGE_WITH_MAX / MIN / VALUE / AVG / COUNT / SUM /
 * CONCAT / FIRST / LAST。
 */
public class MergeDemo {

    private static final String XML = "demo/merge/jquick-excel.xml";

    /** ROWS 1 取首个值（表头行跨列合并），ROWS 2 拼接该行所有值。 */
    @Test
    public void mergeRows() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.mergeRows());
        File out = DemoKit.out("merge-rows.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            MergeService service = DemoKit.exportApi(XML, rows, os, MergeService.class);
            service.mergeRows("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            List<CellRangeAddress> regions = wb.getSheet("合并-行").getMergedRegions();
            System.out.println("行合并区域: " + regions);
            Assert.assertFalse(regions.isEmpty());
        }
    }

    /** COLS A..F 六列分别纵向合并。 */
    @Test
    public void mergeCols() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.mergeRows());
        File out = DemoKit.out("merge-cols.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            MergeService service = DemoKit.exportApi(XML, rows, os, MergeService.class);
            service.mergeCols("field", "value");
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            List<CellRangeAddress> regions = wb.getSheet("合并-列").getMergedRegions();
            System.out.println("列合并区域数: " + regions.size());
            Assert.assertTrue(regions.size() >= 6);
        }
    }
}
