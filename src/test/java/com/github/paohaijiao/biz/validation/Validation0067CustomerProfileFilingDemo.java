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
package com.github.paohaijiao.biz.validation;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 场景 67：客户档案信息补录填报表（数据校验填报类，🟢 纯 XML + 导入校验）。
 *
 * <p>业务：汇总补录的客户档案（编号 / 名称 / 类型 / 信用等级 / 联系人 / 电话 / 年采购额 / 状态），
 * 末尾一行汇总年采购额；导入时对填报内容做规则校验。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/validation/0067_validation_customer-profile-filing.xml}：
 * FORMULAS 求年采购额合计，STYLE 给合计行高亮；导入侧的 VALIDATION 规则块负责校验。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Validation0067CustomerProfileFilingDemo {

    private static final String XML = "jquick/biz/validation/0067_validation_customer-profile-filing.xml";

    /** 导出：纯 XML 完成年采购额汇总与合计行高亮。 */
    @Test
    public void exportCustomerProfileFiling() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(customer("C2601", "华新电子有限公司", "企业客户", "AAA", "张明", "13800138001", 1200000, "已建档"));
        rows.add(customer("C2602", "恒通材料有限公司", "企业客户", "AA", "李强", "13900139002", 860000, "已建档"));
        rows.add(customer("C2603", "佳美包装有限公司", "企业客户", "A", "王芳", "13700137003", 540000, "已建档"));
        rows.add(customer("C2604", "个人客户赵敏", "个人客户", "A", "赵敏", "13600136004", 120000, "已建档"));
        rows.add(customer("C2605", "个人客户陈杰", "个人客户", "B", "陈杰", "13500135005", 60000, "已建档"));
        rows.add(customer("C2606", "天海物流有限公司", "企业客户", "AA", "周涛", "13400134006", 720000, "待审核"));
        // 合计占位行：年采购额合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        total.put("h", null);
        rows.add(total);

        File out = BizKit.outFile("validation", "0067_validation_customer-profile-filing.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("tropicalTeal", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0067CustomerProfileFilingService service = factory.createApi(Validation0067CustomerProfileFilingService.class);
            service.exportCustomerProfileFiling("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("客户档案信息补录填报表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 年采购额合计 = SUM(G2:G7) = 1200000 + 860000 + 540000 + 120000 + 60000 + 720000
            Cell g8 = sheet.getRow(7).getCell(6);
            Assert.assertEquals("SUM(G2:G7)", g8.getCellFormula());
            Assert.assertEquals(3500000.00, evaluator.evaluate(g8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景67】客户档案信息补录填报表导出: " + out.getAbsolutePath()
                    + "，年采购额合计 " + evaluator.evaluate(g8).getNumberValue());
        }
    }

    /** 导入解析：读取补录结果并逐项校验（先重算公式）。 */
    @Test
    public void importCustomerProfileFiling() throws Exception {
        File src = BizKit.outFile("validation", "0067_validation_customer-profile-filing.xlsx");
        if (!src.exists()) {
            exportCustomerProfileFiling();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("validation", "0067_validation_customer-profile-filing-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0067CustomerProfileFilingService service = factory.createApi(Validation0067CustomerProfileFilingService.class);
            List<JQuickRow> rows = service.importCustomerProfileFiling("field", "value");

            System.out.println("【场景67】客户档案信息补录填报表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    客户编号=" + r.get("customerNo") + ", 客户名称=" + r.get("customerName")
                        + ", 类型=" + r.get("customerType") + ", 信用=" + r.get("creditLevel")
                        + ", 电话=" + r.get("phone") + ", 年采购额=" + r.get("annualAmount")
                        + ", 状态=" + r.get("filingStatus"));
            }
            // 表头不计入数据行：6 条补录 + 1 行合计
            Assert.assertEquals("6 条补录 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条客户补录（a=客户编号，b=客户名称，c=客户类型，d=信用等级，e=联系人，f=电话，g=年采购额，h=建档状态）。 */
    private static JQuickRow customer(String customerNo, String customerName, String customerType,
                                      String creditLevel, String contact, String phone,
                                      int annualAmount, String filingStatus) {
        JQuickRow row = new JQuickRow();
        row.put("a", customerNo);
        row.put("b", customerName);
        row.put("c", customerType);
        row.put("d", creditLevel);
        row.put("e", contact);
        row.put("f", phone);
        row.put("g", annualAmount);
        row.put("h", filingStatus);
        return row;
    }
}
