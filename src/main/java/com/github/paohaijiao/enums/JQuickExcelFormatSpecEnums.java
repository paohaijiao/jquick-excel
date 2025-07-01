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
package com.github.paohaijiao.enums;

import com.github.paohaijiao.exception.JAssert;
import lombok.Getter;

@Getter
public enum JQuickExcelFormatSpecEnums {
    DATE("DATE","DATE"),
    STRING("STRING","STRING"),
    CUSTOM("CUSTOM","CUSTOM"),
    NUMBER("NUMBER","NUMBER");

    private String code;
    private String name;
    JQuickExcelFormatSpecEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public JQuickExcelFormatSpecEnums codeOf(String code) {
        for (JQuickExcelFormatSpecEnums e : JQuickExcelFormatSpecEnums.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        JAssert.throwNewException("invalid code");
        return null;
    }
}
