package com.paohaijiao.javelin.validate;

import com.paohaijiao.javelin.model.JStudentModel;

import java.util.ArrayList;
import java.util.List;

public class JExcelValidator {
    public static List<String> validate(JStudentModel student) {
        List<String> errors = new ArrayList<>();

        if (student.getId() == null || student.getId().isEmpty()) {
            errors.add("学号不能为空");
        }

        if (student.getName() == null || student.getName().isEmpty()) {
            errors.add("姓名不能为空");
        }

        if (student.getGender() == null || (student.getGender() != 0 && student.getGender() != 1)) {
            errors.add("性别必须为男或女");
        }

        if (student.getAge() == null || student.getAge() <= 0) {
            errors.add("年龄必须大于0");
        }

        return errors;
    }
}
