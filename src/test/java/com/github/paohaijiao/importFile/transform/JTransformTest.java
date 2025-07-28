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
package com.github.paohaijiao.importFile.transform;

import com.github.paohaijiao.executor.JQuickExcelCommonImportExecutor;
import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.importFile.mapping.JMappingTest;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.param.JContext;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.importFile.transform
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/27
 */
public class JTransformTest {
    @Test
    public void boolRequire() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "SHEET='Sheet1',"+
                "MAPPING = {\n" +
                "\"学号\": \"no\",\n" +
                "\"姓名\": \"name\",\n" +
                "\"性别\": \"sex\",\n" +
                "\"年龄\": \"age\",\n" +
                "\"出生日期\": \"birthday\"\n" +
                "}," +
                "TRANSFORM={" +
                    "\"sex\":trans(${dict},${sex})"+
                    "\"birthday\":dateFormat(${birthday},'yyyy-MM-dd')"+
                "}";
        System.out.println(input);
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        Map<String,Object> sex=new HashMap<>();
        sex.put("男","1");
        sex.put("女","2");
        JContext context = new JContext();
        context.put("dict",sex);
        JExcelImportHandler handler=new JExcelImportHandler(workbook,context);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
}
