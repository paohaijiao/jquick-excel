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
package com.github.paohaijiao.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JDateConverter implements JCustomConverter<Date> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public JDateConverter() {

    }

    public JDateConverter(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public JDateConverter(String format) {
        this.sdf = new SimpleDateFormat(format);
    }

    @Override
    public Date convert(String value) {
        try {
            return sdf.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String reverseConvert(Date value) {
        return value != null ? sdf.format(value) : "";
    }
}
