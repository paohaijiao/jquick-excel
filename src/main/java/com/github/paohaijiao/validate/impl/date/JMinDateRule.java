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
package com.github.paohaijiao.validate.impl.date;

import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.validate.JAbstractValidationRule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.validate.impl
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JMinDateRule extends JAbstractValidationRule {

    private  Date minDate;

    private  String format;

    public JMinDateRule(boolean required, Map<String,Object> map, String customMessage) {
        super(required, map, customMessage);
    }

    @Override
    protected boolean doValidate(String value) {
        JAssert.notNull(map, "the map must not be null");
        Object minDateObject=map.get("minDate");
        JAssert.notNull(minDateObject, "the minDate Value must not be null");
        this.minDate =(Date) minDateObject;
        Object formatValue=map.get("format");
        JAssert.notNull(formatValue, "the format Value must not be null");
        this.format = formatValue.toString();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false);
            Date date = sdf.parse(value);
            return !date.before(minDate);
        } catch (ParseException e) {
            return false;
        }
    }
    @Override
    public String getDefaultMsg() {
        return String.format("the date cannot be earlier than %s", minDate);
    }

}
