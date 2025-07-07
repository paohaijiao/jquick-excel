package com.github.paohaijiao.validate;

import java.util.function.Predicate;

public interface JExcelValidationRule extends Predicate<String>
{
    public default  String getDefaultMsg(){
        return "invalid value";
    }
}
