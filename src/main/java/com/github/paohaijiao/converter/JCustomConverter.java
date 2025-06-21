package com.github.paohaijiao.converter;

public interface JCustomConverter <T>{
    T convert(String value);
    String reverseConvert(T value);
}
