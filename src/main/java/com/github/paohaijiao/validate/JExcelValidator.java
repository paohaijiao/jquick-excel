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
package com.github.paohaijiao.validate;

import com.github.paohaijiao.model.JStudentModel;

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
