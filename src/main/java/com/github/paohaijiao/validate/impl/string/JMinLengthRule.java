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
package com.github.paohaijiao.validate.impl.string;

import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.validate.JAbstractValidationRule;

import java.math.BigDecimal;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.validate
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JMinLengthRule extends JAbstractValidationRule {

    private  int minLength;

    public JMinLengthRule(boolean required, Map<String,Object> map, String customMessage) {
        super(required, map, customMessage);
        JAssert.notNull(map, "the map must not be null");

    }

    @Override
    protected boolean doValidate(String value) {
        Object minLengthObject=map.get("minLength");
        JAssert.notNull(minLengthObject, "the minLength Value must not be null");
        BigDecimal bigDecimal=(BigDecimal) minLengthObject;
        this.minLength =bigDecimal.intValue();
        return value.length() >= minLength;
    }

    @Override
    public String getDefaultMsg() {
        return String.format("the length cannot be less than% d characters\n", minLength);
    }

}
