
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
package com.github.paohaijiao.importFile.string;

import com.github.paohaijiao.factory.JExcelValidationRuleFactory;
import com.github.paohaijiao.validate.impl.number.JMinValueRule;
import com.github.paohaijiao.validate.impl.string.JNotContainWithRule;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * packageName com.github.paohaijiao.importFile
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JNotContainWidthTest {

    @Test
    public void notContainWidth1() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JNotContainWithRule booleanRule = JExcelValidationRuleFactory.notContainWith(true,map,"不能为空");
        System.out.println(booleanRule.test(null));
    }
    @Test
    public void notContainWidth2() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JNotContainWithRule booleanRule = JExcelValidationRuleFactory.notContainWith(false,map,"不能为空");
        System.out.println(booleanRule.test(null));
    }
    @Test
    public void notContainWidth3() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("notContain", "2");
        JNotContainWithRule booleanRule = JExcelValidationRuleFactory.notContainWith(true,map,"不能为空");
        System.out.println(booleanRule.test("28"));
    }
    @Test
    public void notContainWidth4() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("notContain", "cdscds2");
        JNotContainWithRule booleanRule = JExcelValidationRuleFactory.notContainWith(true,map,"不能为空");
        System.out.println(booleanRule.test("18"));
    }
}
