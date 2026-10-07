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

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * basic 子包 demo：三步调用骨架（ParseFactory -> JQuickXmlFactory -> 代理接口）。
 *
 * <p>独立规则文件：{@code demo/basic/jquick-excel.xml}。
 * 产物：{@code target/demo-output/basic-export.xlsx}。
 */
public class BasicExportDemo {

    @Test
    public void exportBasic() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.personRows());
        // 导出字典方向：码值 -> Excel 文字（1=男，2=女）
        JContext ctx = DemoKit.exportDict();

        File out = DemoKit.out("basic-export.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            BasicService service = DemoKit.exportApi(
                    "demo/basic/jquick-excel.xml", ctx, rows, os, BasicService.class);
            service.exportBasic("field", "value");
        }
        System.out.println("已导出: " + out.getAbsolutePath());
        Assert.assertTrue(out.length() > 0);
    }
}
