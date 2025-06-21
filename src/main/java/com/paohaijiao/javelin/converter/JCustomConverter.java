package com.paohaijiao.javelin.converter;

public interface JCustomConverter <T>{
    T convert(String value);
    String reverseConvert(T value);
}
