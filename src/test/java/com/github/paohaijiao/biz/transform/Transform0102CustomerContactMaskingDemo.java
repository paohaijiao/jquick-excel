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
 * 场景 102：客户联系人脱敏台账（🟢 纯 XML + TRANSFORM SPI 函数）。
 *
 * <p>业务：客户台账导出时对联系人姓名脱敏（保留首字），客户等级按字典由内部编码转为客户分层。
 * 转换规则全部写在 {@code jquick/biz/transform/0102_transform_customer-contact-masking.xml}：
 * 联系人走自定义 SPI 函数 {@code maskname}（张三 -> 张*），客户等级走 {@code trans(${dict}, ...)}，
 * 字典经 {@link JContext} 以 {@code ${dict}} 注入。Java 侧只提供原始数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Transform0102CustomerContactMaskingDemo {

    private static final String XML = "jquick/biz/transform/0102_transform_customer-contact-masking.xml";

    /** 导出：联系人经 SPI maskname 脱敏、等级经字典转中文，回读校验两列。 */
    @Test
    public void exportCustomerContactMasking() throws Exception {
        JContext ctx = new JContext();
        Map<String, Object> dict = new HashMap<>();
        dict.put("v1", "钻石客户");
        dict.put("v2", "黄金客户");
        dict.put("v3", "普通客户");
        ctx.put("dict", dict);

        List<JQuickRow> rows = new ArrayList<>();
        rows.add(customer("C2601", "华东贸易有限公司", "张伟", "13800138001", "v1", 1250000.00, "2020-01-10"));
        rows.add(customer("C2602", "南方物流集团", "李娜", "13900139002", "v2", 880000.00, "2020-05-22"));
        rows.add(customer("C2603", "西部能源股份", "王强", "13700137003", "v3", 320000.00, "2021-03-08"));
        rows.add(customer("C2604", "北方建设集团", "刘洋", "13600136004", "v1", 1560000.00, "2019-11-16"));

        File out = BizKit.outFile("transform", "0102_transform_customer-contact-masking.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sakuraPink", ctx, rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Transform0102CustomerContactMaskingService service = factory.createApi(Transform0102CustomerContactMaskingService.class);
            service.exportCustomerContactMasking("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("客户联系人脱敏台账");
            Row first = sheet.getRow(1);
            // 联系人脱敏：张伟 -> 张*
            Assert.assertEquals("张*", first.getCell(2).getStringCellValue());
            // 客户等级：v1 -> 钻石客户
            Assert.assertEquals("钻石客户", first.getCell(4).getStringCellValue());
            // 年金额千分位与签约日期格式
            Assert.assertEquals("#,##0.00", first.getCell(5).getCellStyle().getDataFormatString());
            Assert.assertEquals("yyyy-MM-dd", first.getCell(6).getCellStyle().getDataFormatString());
            // 第二行：李娜 -> 李*、黄金客户
            Assert.assertEquals("李*", sheet.getRow(2).getCell(2).getStringCellValue());
            Assert.assertEquals("黄金客户", sheet.getRow(2).getCell(4).getStringCellValue());

            System.out.println("【场景102】客户联系人脱敏台账导出: " + out.getAbsolutePath()
                    + "，共 " + sheet.getLastRowNum() + " 行数据");
        }
    }

    /** 导入解析：读取脱敏后的客户台账，按表头映射回字段。 */
    @Test
    public void importCustomerContactMasking() throws Exception {
        File src = BizKit.outFile("transform", "0102_transform_customer-contact-masking.xlsx");
        if (!src.exists()) {
            exportCustomerContactMasking();
        }
        try (InputStream in = new FileInputStream(src)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Transform0102CustomerContactMaskingService service = factory.createApi(Transform0102CustomerContactMaskingService.class);
            List<JQuickRow> rows = service.importCustomerContactMasking("field", "value");

            System.out.println("【场景102】客户联系人脱敏台账导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    客户编码=" + r.get("customerNo") + ", 客户名称=" + r.get("customerName")
                        + ", 联系人=" + r.get("contact") + ", 客户等级=" + r.get("level"));
            }
            Assert.assertEquals(4, rows.size());
            Assert.assertEquals("张*", rows.get(0).get("contact"));
            Assert.assertEquals("钻石客户", rows.get(0).get("level"));
        }
    }

    /** 构造一条客户台账（c 走 SPI 脱敏、e 走字典，g 为日期对象走 FORMAT）。 */
    private static JQuickRow customer(String no, String name, String contact, String phone,
                                      String level, double annualAmount, String signDate) throws Exception {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", name);
        row.put("c", contact);
        row.put("d", phone);
        row.put("e", level);
        row.put("f", annualAmount);
        row.put("g", new SimpleDateFormat("yyyy-MM-dd").parse(signDate));
        return row;
    }
}
