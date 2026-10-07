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
package com.github.paohaijiao.demo.validation;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.statement.JQuickRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * validation 子包 demo：VALIDATION 通过 / 失败两条路径。
 *
 * <p>独立规则文件：{@code demo/validation/jquick-excel.xml}。
 *
 * <p><b>以下行为为逐条用例实测结论（3.6.x/3.7.x），与直觉可能不同：</b>
 * <ul>
 *   <li>校验不通过时框架直接抛异常，异常 message 就是规则块里的 {@code msg}。</li>
 *   <li>{@code integer} 只对<b>文本</b>单元格（"20"）有效；数值单元格读出来是
 *       Double，toString 得到 "20.0"，Integer.parseInt 失败。整数列请保证按文本
 *       写入，或改用 decimal + min_value/max_value 组合。</li>
 *   <li>{@code date_format}/{@code min_date}/{@code max_date} 同样只对<b>文本日期</b>
 *       有效，真实日期单元格会被格式化成 Date.toString 再解析，必然失败。</li>
 *   <li>多列区间 {@code COL A..C} 会把表头行纳入遍历导致误判；请用单列
 *       {@code COL C} 或矩形范围 {@code A2:C4}。</li>
 *   <li>{@code boolean} 规则必须带 {@code map:{'T':'true','F':'false'}}，否则空指针。</li>
 * </ul>
 */
public class ValidationDemo {

    private static final String XML = "demo/validation/jquick-excel.xml";

    /** 规则集对文本夹具全部通过，返回 3 行数据。 */
    @Test
    public void importWithValidation() throws Exception {
        File file = DemoKit.prepareImportFile();
        try (InputStream in = new FileInputStream(file)) {
            ValidationService service = DemoKit.importApi(XML, in, ValidationService.class);
            List<JQuickRow> rows = service.importWithValidation("field", "value");
            System.out.println("校验通过，读取 " + rows.size() + " 行");
            Assert.assertEquals(3, rows.size());
        }
    }

    /** 学号最长 3 位的规则必然失败，异常信息即 msg 文本。 */
    @Test
    public void importWithValidationFail() throws Exception {
        File file = DemoKit.prepareImportFile();
        try (InputStream in = new FileInputStream(file)) {
            ValidationService service = DemoKit.importApi(XML, in, ValidationService.class);
            try {
                service.importWithValidationFail("field", "value");
                Assert.fail("应当抛出校验异常");
            } catch (Exception e) {
                System.out.println("校验失败：" + e.getMessage());
                Assert.assertTrue(e.getMessage().contains("学号超过3位"));
            }
        }
    }
}
