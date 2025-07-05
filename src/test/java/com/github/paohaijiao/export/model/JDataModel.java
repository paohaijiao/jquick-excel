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
package com.github.paohaijiao.export.model;

import com.github.paohaijiao.anno.JExcelColumn;
import com.github.paohaijiao.anno.JExcelSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@JExcelSheet(name = "学生信息", headRowNumber = 2)
@AllArgsConstructor
@NoArgsConstructor
public class JDataModel {
    @JExcelColumn(name = "学号", index = 0)
    private Integer value;

    @JExcelColumn(name = "姓名", index = 1)
    private Integer value1;

    @JExcelColumn(name = "性别", index = 2, converter = "gender")
    private Integer value2;

    @JExcelColumn(name = "年龄", index = 3)
    private Integer value3;

    @JExcelColumn(name = "入学日期", index = 4, format = "yyyy-MM-dd", converter = "date")
    private Integer value4;

    @JExcelColumn(name = "班级", index = 5)
    private Integer value5;

    // 忽略的字段
    private Integer ignoreField;
}
