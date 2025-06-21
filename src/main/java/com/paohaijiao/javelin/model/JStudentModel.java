package com.paohaijiao.javelin.model;

import com.paohaijiao.javelin.anno.JExcelColumn;
import com.paohaijiao.javelin.anno.JExcelSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@JExcelSheet(name = "学生信息", headRowNumber = 2)
@AllArgsConstructor
@NoArgsConstructor
public class JStudentModel {
    @JExcelColumn(name = "学号", index = 0)
    private String id;

    @JExcelColumn(name = "姓名", index = 1)
    private String name;

    @JExcelColumn(name = "性别", index = 2, converter = "gender")
    private Integer gender;

    @JExcelColumn(name = "年龄", index = 3)
    private Integer age;

    @JExcelColumn(name = "入学日期", index = 4, format = "yyyy-MM-dd", converter = "date")
    private Date enrollmentDate;

    @JExcelColumn(name = "班级", index = 5)
    private String className;

    // 忽略的字段
    private String ignoreField;
}
