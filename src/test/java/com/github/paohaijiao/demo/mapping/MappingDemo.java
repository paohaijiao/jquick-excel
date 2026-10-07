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
package com.github.paohaijiao.demo.mapping;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * mapping 子包 demo：MAPPING 把中文表头映射成业务字段名。
 *
 * <p>独立规则文件：{@code demo/mapping/jquick-excel.xml}。
 */
public class MappingDemo {

    @Test
    public void importWithMapping() throws Exception {
        File file = DemoKit.prepareImportFile();
        try (InputStream in = new FileInputStream(file)) {
            MappingService service = DemoKit.importApi("demo/mapping/jquick-excel.xml", in, MappingService.class);
            List<JQuickRow> rows = service.importWithMapping("field", "value");
            JQuickRow first = rows.get(0);
            System.out.println("no=" + first.get("no") + ", name=" + first.get("name")
                    + ", className=" + first.get("className"));
            Assert.assertEquals(3, rows.size());
            Assert.assertEquals("2024001", first.get("no"));
            Assert.assertEquals("计算机1班", first.get("className"));
        }
    }
}
