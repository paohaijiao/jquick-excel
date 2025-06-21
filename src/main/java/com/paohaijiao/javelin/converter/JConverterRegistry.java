package com.paohaijiao.javelin.converter;

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
