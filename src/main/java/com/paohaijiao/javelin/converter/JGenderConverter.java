package com.paohaijiao.javelin.converter;

public class JGenderConverter implements JCustomConverter<Integer>{
    @Override
    public Integer convert(String value) {
        if ("男".equals(value)) return 1;
        if ("女".equals(value)) return 0;
        return -1;
    }

    @Override
    public String reverseConvert(Integer value) {
        if (value == 1) return "男";
        if (value == 0) return "女";
        return "未知";
    }
}
