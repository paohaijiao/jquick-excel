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
package com.github.paohaijiao.demo.sheet;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * sheet 子包 demo：演示 IMPORT WITH 的 SHEET 两种写法。
 *
 * <p>独立规则文件：{@code demo/sheet/jquick-excel.xml}。
 */
public class SheetDemo {

    private static final String XML = "demo/sheet/jquick-excel.xml";

    /** SHEET="学生信息"：字符串按工作表名称匹配。 */
    @Test
    public void importByName() throws Exception {
        File file = DemoKit.prepareImportFile();
        try (InputStream in = new FileInputStream(file)) {
            SheetService service = DemoKit.importApi(XML, in, SheetService.class);
            List<JQuickRow> rows = service.importByName("field", "value");
            System.out.println("按工作表名称导入，共 " + rows.size() + " 行: " + rows.get(0));
            Assert.assertEquals(3, rows.size());
            Assert.assertEquals("张三", rows.get(0).get("姓名"));
        }
    }

    /** SHEET=1：数字按 1 基索引匹配。 */
    @Test
    public void importByIndex() throws Exception {
        File file = DemoKit.prepareImportFile();
        try (InputStream in = new FileInputStream(file)) {
            SheetService service = DemoKit.importApi(XML, in, SheetService.class);
            List<JQuickRow> rows = service.importByIndex("field", "value");
            System.out.println("按工作表索引导入，共 " + rows.size() + " 行");
            Assert.assertEquals(3, rows.size());
        }
    }
}
