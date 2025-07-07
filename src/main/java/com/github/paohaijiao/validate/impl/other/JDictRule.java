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
package com.github.paohaijiao.validate.impl.other;

import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.validate.JAbstractValidationRule;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * packageName com.github.paohaijiao.validate.impl.other
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JDictRule extends JAbstractValidationRule {

    private Set<String> dict;

    public JDictRule(boolean required, Map<String,Object> map, String customMessage) {
        super(required, map, customMessage);
        JAssert.notNull(map, "the map must not be null");

    }

    @Override
    protected boolean doValidate(String value) {
        Set<String> set=new HashSet<>();
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            String v = entry.getValue().toString();
            set.add(v);
        }
        this.dict = set;
        return dict.contains(value);
    }

    @Override
    public String getDefaultMsg() {
        return "dict value invalid";
    }
}
