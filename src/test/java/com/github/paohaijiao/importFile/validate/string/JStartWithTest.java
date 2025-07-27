

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
package com.github.paohaijiao.importFile.validate.string;

import com.github.paohaijiao.factory.JExcelValidationRuleFactory;
import com.github.paohaijiao.validate.impl.string.JRegexRule;
import com.github.paohaijiao.validate.impl.string.JStartWithRule;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * packageName com.github.paohaijiao.importFile
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JStartWithTest {

    @Test
    public void startWith1() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JStartWithRule booleanRule = JExcelValidationRuleFactory.startWith(true,map,"不能为空");
        System.out.println(booleanRule.test(null));
    }
    @Test
    public void startWith2() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JStartWithRule booleanRule = JExcelValidationRuleFactory.startWith(false,map,"不能为空");
        System.out.println(booleanRule.test(null));
    }
    @Test
    public void startWith3() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("startWith", "2");
        JStartWithRule booleanRule = JExcelValidationRuleFactory.startWith(true,map,"不能为空");
        System.out.println(booleanRule.test("28"));
    }
    @Test
    public void startWith4() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("startWith", "[0-9]*");
        JStartWithRule booleanRule = JExcelValidationRuleFactory.startWith(true,map,"不能为空");
        System.out.println(booleanRule.test("a"));
    }
}
