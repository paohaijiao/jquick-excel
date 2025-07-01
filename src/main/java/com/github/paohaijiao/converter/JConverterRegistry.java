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

import java.util.HashMap;
import java.util.Map;

public class JConverterRegistry {
    private static final Map<String, JCustomConverter<?>> converters = new HashMap<>();

    static {
        registerConverter("gender", new JGenderConverter());
        registerConverter("date", new JDateConverter());
    }

    public static void registerConverter(String name, JCustomConverter<?> converter) {
        converters.put(name, converter);
    }

    @SuppressWarnings("unchecked")
    public static <T> JCustomConverter<T> getConverter(String name) {
        return (JCustomConverter<T>) converters.get(name);
    }
}
