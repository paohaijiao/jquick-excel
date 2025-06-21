package com.paohaijiao.javelin.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JExcelColumn {
    String name() default "";
    int index() default -1;
    String format() default "";
    String converter() default "";
}
