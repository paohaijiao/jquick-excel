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
package com.github.paohaijiao.importFile.mapping;

import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.executor.JQuickExcelCommonImportExecutor;
import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.model.JExcelImportModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.importFile.mapping
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/27
 */
public class JMappingTest {
    @Test
    public void boolRequire() throws IOException {
        String input = " \n" +
                "IMPORT WITH " +
                "HEADER=true,"+
                "MAPPING = {\n" +
                "    \"学号\": \"no\",\n" +
                "    \"姓名\": \"name\",\n" +
                "    \"性别\": \"sex\",\n" +
                "    \"年龄\": \"age\",\n" +
                "    \"出生日期\": \"birthday\"\n" +
                "}";
        System.out.println(input);
        JQuickExcelCommonImportExecutor executor=new JQuickExcelCommonImportExecutor();
        JExcelImportModel model= (JExcelImportModel)executor.execute(input);
        InputStream is = JMappingTest.class.getClassLoader().getResourceAsStream("templates/student.xlsx");
        XSSFWorkbook workbook=new XSSFWorkbook(is);
        JExcelImportHandler handler=new JExcelImportHandler(workbook);
        List<Map<String, Object>> list= handler.importData(model);
        System.out.println(list);

    }
}
