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
package com.github.paohaijiao.export.format;

import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JStudentModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.util.JObjectConverter;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class JFormatTest {

    public static List<JStudentModel> getData() {
        List<JStudentModel> students = new ArrayList<>();
        students.add(new JStudentModel("1001", "张三s", 1, 20, new Date(), "计算机1班", "true"));
        students.add(new JStudentModel("1002", "李四", 0, 21, new Date(), "计算机2班", "true"));
        students.add(new JStudentModel("1003", "王五", 1, 22, new Date(), "计算机3班", "true"));
        return students;
    }

    @Test
    public void transform() throws IOException {
        String input = "EXPORT  WITH\n" +
                     "SHEET=\"学生表\",\n"+
                    "HEADER=true,\n"+
                    "MAPPING={\n" +
                        "\t\"id\":\"主键\",\n"+
                        "\t\"name\":\"姓名\",\n"+
                        "\t\"gender\":\"性别\",\n"+
                        "\t\"age\":\"年龄\",\n"+
                        "\t\"enrollmentDate\":\"入学时间\",\n"+
                        "\t\"className\":\"班级\",\n"+
                        "\t\"ignoreField\":\"是否忽略\"\n"+
                    "}," +
                    "FORMAT={"+
                    "\t\"enrollmentDate\":\"yyyy-MM-dd\"\n"+
                "},\n" +
                "\n" +
                "TRANSFORM={\n"
                +"\"name\": toUpper(${name}),\n"+
                "\"enrollmentDate\": dateFormat(${enrollmentDate},'yyyy-MM-dd'),\n"+
                "\"gender\": trans(${dict},${gender})\n"+
                "}";
        System.out.println(input);
        List<Map<String, Object>> data = JObjectConverter.convert(getData());
        FileOutputStream fileOutputStream=new FileOutputStream("d://test//transform.xlsx");
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(input);
        HashMap<String,Object> map = new HashMap<>();
        map.put("1","男");
        map.put("0","女");
        JContext context = new JContext();
        context.put("dict",map);
        JExcelExportHandler handler = new JExcelExportHandler(config,context,data);
        Workbook workbook=handler.getWorkBook();
        workbook.write(fileOutputStream);
    }


}
