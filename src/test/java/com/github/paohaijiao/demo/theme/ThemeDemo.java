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
package com.github.paohaijiao.demo.theme;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.statement.JQuickRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * theme 子包 demo：两种指定主题的方式。
 *
 * <p>独立规则文件：{@code demo/theme/jquick-excel.xml}。
 * 常用主题编码：oceanBlue（海洋蓝）、jade（翡翠绿）、royalGold（皇家金）、
 * rosePink、forestGreen、purpleNight、sunsetOrange、classicGray 等
 * （完整列表见 {@code JExcelThemeType} 枚举）。
 */
public class ThemeDemo {

    /** 方式一：XML 构造器首参传主题编码 new JQuickExcelExportXmlParseFactory("oceanBlue", rows, os)。 */
    @Test
    public void exportThemeByXmlFactory() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.personRows());
        File out = DemoKit.out("theme-ocean-blue.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            ThemeService service = DemoKit.exportApi(
                    "demo/theme/jquick-excel.xml", "oceanBlue", rows, os, ThemeService.class);
            service.exportTheme("field", "value");
        }
        System.out.println("XML 构造器指定 oceanBlue 主题: " + out.getName());
        Assert.assertTrue(out.length() > 0);
    }

    /** 方式二：编程式 model.setTheme("jade") 后自己导出。 */
    @Test
    public void exportThemeProgrammatically() throws Exception {
        List<JQuickRow> rows = DemoKit.toRows(DemoKit.personRows());
        String dsl = "EXPORT WITH SHEET=\"主题\", HEADER=true, MAPPING={\"a\":\"姓名\",\"b\":\"年龄\"}";
        JExcelExportModel model = (JExcelExportModel) new JQuickExcelCommonExportExecutor().execute(dsl);
        model.setTheme("jade");
        JExcelExportHandler handler = new JExcelExportHandler(model, rows);

        File out = DemoKit.out("theme-jade.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            handler.getWorkBook().write(os);
        }
        System.out.println("编程式指定 jade 主题: " + out.getName());
        Assert.assertTrue(out.length() > 0);
    }
}
