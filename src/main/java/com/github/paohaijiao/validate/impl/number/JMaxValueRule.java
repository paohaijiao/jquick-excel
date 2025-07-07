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
package com.github.paohaijiao.validate.impl.number;

import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.validate.JAbstractValidationRule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.validate.impl
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JMaxValueRule extends JAbstractValidationRule {

    private  double maxValue;

    public JMaxValueRule(boolean required, Map<String,Object> map, String customMessage) {
        super(required, map, customMessage);

    }

    @Override
    protected boolean doValidate(String value) {
        JAssert.notNull(map, "the map must not be null");
        Object maxValueObject=map.get("maxValue");
        JAssert.notNull(maxValueObject, "the maxValue Value must not be null");
        BigDecimal bigDecimal=(BigDecimal) maxValueObject;
        this.maxValue =bigDecimal.doubleValue();
        try {
            double num = Double.parseDouble(value);
            return num <= maxValue;
        } catch (NumberFormatException e) {
            JAssert.throwNewException(this.buildMsg());
            return false;
        }
    }
    @Override
    public String getDefaultMsg() {
        return String.format("the value cannot be greater than %s", maxValue);
    }
}
