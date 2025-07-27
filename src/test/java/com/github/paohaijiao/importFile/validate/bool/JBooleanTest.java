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
package com.github.paohaijiao.importFile.validate.bool;

import com.github.paohaijiao.factory.JExcelValidationRuleFactory;
import com.github.paohaijiao.validate.impl.JCompositeRule;
import com.github.paohaijiao.validate.impl.bool.JBooleanRule;
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
public class JBooleanTest {

    @Test
    public void boolRequire() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JBooleanRule booleanRule = JExcelValidationRuleFactory.booleanRule(true,map,"");
        System.out.println(booleanRule.test(null));
    }
    @Test
    public void boolRequire1() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        JBooleanRule booleanRule = JExcelValidationRuleFactory.booleanRule(false,map,"");
        System.out.println(booleanRule.test(null));
    }

    @Test
    public void boolRequire2() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("true","是");
        map.put("false","否");
        JBooleanRule booleanRule = JExcelValidationRuleFactory.booleanRule(true,map,"没有该字典");
        System.out.println(booleanRule.test("是"));
    }
    @Test
    public void boolRequire3() throws IOException {
        HashMap<String,Object> map = new HashMap<>();
        map.put("true","是");
        map.put("false","否");
        JBooleanRule booleanRule = JExcelValidationRuleFactory.booleanRule(true,map,null);
        System.out.println(booleanRule.test("哈哈"));
    }

}
