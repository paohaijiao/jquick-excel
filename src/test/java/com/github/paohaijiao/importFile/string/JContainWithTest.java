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
import com.github.paohaijiao.validate.impl.number.JDecimalRule;
import com.github.paohaijiao.validate.impl.string.JContainWithRule;
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
public class JContainWithTest {

    @Test
    public void containWith1() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JContainWithRule booleanRule = JExcelValidationRuleFactory.containWith(true,map,"不能为空");
        System.out.println(booleanRule.test(null));
    }
    @Test
    public void containWith2() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JContainWithRule booleanRule = JExcelValidationRuleFactory.containWith(false,map,"不能为空");
        System.out.println(booleanRule.test(null));
    }
    @Test
    public void containWith3() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("contains","qwe");
        JContainWithRule booleanRule = JExcelValidationRuleFactory.containWith(true,map,"不能为空");
        System.out.println(booleanRule.test("qwer"));
    }
    @Test
    public void containWith4() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("contains","qwer1");
        JContainWithRule booleanRule = JExcelValidationRuleFactory.containWith(true,map,"不能为空");
        System.out.println(booleanRule.test("qwer"));
    }
}
