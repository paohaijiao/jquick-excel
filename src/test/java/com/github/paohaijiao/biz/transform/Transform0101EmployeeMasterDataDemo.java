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
package com.github.paohaijiao.biz.transform;

import com.github.paohaijiao.biz.BizKit;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景 101：员工主数据码值转换表（🟢 纯 XML + TRANSFORM 字典）。
 *
 * <p>业务：员工主数据里性别 / 部门 / 状态以内部码值存储，导出时按字典转成中文；
 * 入职日期按 {@code yyyy-MM-dd} 格式化、年假按整数输出。
 * 转换规则全部写在 {@code jquick/biz/transform/0101_transform_employee-master-data.xml}，
 * 字典经 {@link JContext} 以 {@code ${dict}} 注入，Java 侧只提供原始数据、不做任何计算。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Transform0101EmployeeMasterDataDemo {

    private static final String XML = "jquick/biz/transform/0101_transform_employee-master-data.xml";

    /** 导出：内部码值经 TRANSFORM 字典转为中文，回读校验性别 / 部门 / 状态三列。 */
    @Test
    public void exportEmployeeMasterData() throws Exception {
        JContext ctx = new JContext();
        Map<String, Object> dict = new HashMap<>();
        // 性别码值
        dict.put("1", "男");
        dict.put("2", "女");
        // 部门码值
        dict.put("RD", "研发部");
        dict.put("MK", "市场部");
        dict.put("FN", "财务部");
        // 状态码值
        dict.put("A", "在职");
        dict.put("B", "离职");
        ctx.put("dict", dict);

        List<JQuickRow> rows = new ArrayList<>();
        rows.add(employee("E2601", "张伟", "1", "RD", "2019-03-01", "A", 6));
        rows.add(employee("E2602", "李娜", "2", "MK", "2020-06-15", "A", 8));
        rows.add(employee("E2603", "王强", "1", "FN", "2018-09-20", "B", 0));
        rows.add(employee("E2604", "刘洋", "2", "RD", "2021-01-10", "A", 5));
        rows.add(employee("E2605", "陈静", "1", "MK", "2017-11-05", "A", 12));
        rows.add(employee("E2606", "赵磊", "2", "FN", "2022-03-18", "A", 3));

        File out = BizKit.outFile("transform", "0101_transform_employee-master-data.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("navyBlue", ctx, rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Transform0101EmployeeMasterDataService service = factory.createApi(Transform0101EmployeeMasterDataService.class);
            service.exportEmployeeMasterData("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工主数据");
            Row first = sheet.getRow(1);
            // 码值 -> 中文
            Assert.assertEquals("男", first.getCell(2).getStringCellValue());
            Assert.assertEquals("研发部", first.getCell(3).getStringCellValue());
            Assert.assertEquals("在职", first.getCell(5).getStringCellValue());
            // 日期格式化与整数输出
            Assert.assertEquals("yyyy-MM-dd", first.getCell(4).getCellStyle().getDataFormatString());
            Assert.assertEquals(6.0, first.getCell(6).getNumericCellValue(), 0.0001);
            // 第二行：女 / 市场部 / 在职
            Assert.assertEquals("女", sheet.getRow(2).getCell(2).getStringCellValue());
            Assert.assertEquals("市场部", sheet.getRow(2).getCell(3).getStringCellValue());

            System.out.println("【场景101】员工主数据码值转换表导出: " + out.getAbsolutePath()
                    + "，共 " + sheet.getLastRowNum() + " 行数据");
        }
    }

    /** 导入解析：读取导出的员工主数据，按表头映射回字段。 */
    @Test
    public void importEmployeeMasterData() throws Exception {
        File src = BizKit.outFile("transform", "0101_transform_employee-master-data.xlsx");
        if (!src.exists()) {
            exportEmployeeMasterData();
        }
        try (InputStream in = new FileInputStream(src)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Transform0101EmployeeMasterDataService service = factory.createApi(Transform0101EmployeeMasterDataService.class);
            List<JQuickRow> rows = service.importEmployeeMasterData("field", "value");

            System.out.println("【场景101】员工主数据码值转换表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    工号=" + r.get("employeeNo") + ", 姓名=" + r.get("name")
                        + ", 性别=" + r.get("sex") + ", 部门=" + r.get("dept") + ", 入职日期=" + r.get("hireDate"));
            }
            Assert.assertEquals(6, rows.size());
            Assert.assertEquals("张伟", rows.get(0).get("name"));
            Assert.assertEquals("男", rows.get(0).get("sex"));
            Assert.assertEquals("研发部", rows.get(0).get("dept"));
        }
    }

    /** 构造一条员工主数据（c/d/f 为内部码值，导出时经字典转中文；e 为日期对象走 FORMAT）。 */
    private static JQuickRow employee(String no, String name, String sex, String dept,
                                      String hireDate, String status, int leaveDays) throws Exception {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", name);
        row.put("c", sex);
        row.put("d", dept);
        row.put("e", new SimpleDateFormat("yyyy-MM-dd").parse(hireDate));
        row.put("f", status);
        row.put("g", leaveDays);
        return row;
    }
}
