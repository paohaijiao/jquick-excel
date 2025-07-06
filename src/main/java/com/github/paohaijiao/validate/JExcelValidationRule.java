package com.github.paohaijiao.validate;

import java.util.function.Predicate;

public interface JExcelValidationRule extends Predicate<String>
{

    default String getErrorMessage() {
        return "data import  validation failed";
    }
}
