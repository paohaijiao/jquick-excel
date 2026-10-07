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
package com.github.paohaijiao.demo.transform;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * transform 子包 demo：TRANSFORM 三种典型改写。
 *
 * <p>独立规则文件：{@code demo/transform/jquick-excel.xml}。
 * 注意导入字典方向是「Excel 文字 → 内部码值」，与导出方向相反。
 */
public class TransformDemo {

    @Test
    public void importWithMappingTransform() throws Exception {
        File file = DemoKit.prepareImportFile();
        // 导入字典：男 -> 1，女 -> 2
        JContext ctx = DemoKit.importDict();
        try (InputStream in = new FileInputStream(file)) {
            TransformService service = DemoKit.importApi(
                    "demo/transform/jquick-excel.xml", ctx, in, TransformService.class);
            List<JQuickRow> rows = service.importWithMappingTransform("field", "value");
            JQuickRow first = rows.get(0);
            System.out.println("no=" + first.get("no") + ", name=" + first.get("name")
                    + ", sex=" + first.get("sex") + ", birthday=" + first.get("birthday"));
            Assert.assertEquals("张三".toUpperCase(), first.get("name"));
            Assert.assertEquals("1", first.get("sex"));
            Assert.assertEquals("2004-09-01", first.get("birthday"));
        }
    }
}
