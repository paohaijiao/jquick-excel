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
 * 场景 83：设备点检记录填报表（数据校验填报类，🟢 纯 XML + 导入校验）。
 *
 * <p>业务：逐台设备登记点检人与运行参数，末尾一行汇总总运行时长；
 * 导入侧对填报内容做规则校验（编号 / 名称长度、运行时长整数与取值范围、温度上限、结论字典）。
 *
 * <p>导出侧合计在 {@code jquick/biz/validation/0054_validation_equipment-inspection-record.xml} 的 FORMULAS 里，
 * 导入侧校验在同一模板的 {@code VALIDATION} 里；Java 只构造填报数据。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Validation0054EquipmentInspectionRecordDemo {

    private static final String XML = "jquick/biz/validation/0054_validation_equipment-inspection-record.xml";

    /** 导出：纯 XML 完成运行时长合计。 */
    @Test
    public void exportEquipmentInspectionRecord() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(equipment("EQ001", "注塑机", "张伟", 8, 62.5, "正常", ""));
        rows.add(equipment("EQ002", "空压机", "李娜", 12, 78.0, "正常", ""));
        rows.add(equipment("EQ003", "数控车床", "王强", 20, 55.5, "正常", ""));
        rows.add(equipment("EQ004", "磨床", "赵敏", 6, 48.0, "正常", ""));
        rows.add(equipment("EQ005", "铣床", "陈晨", 10, 52.0, "正常", ""));
        rows.add(equipment("EQ006", "钻床", "周涛", 4, 45.5, "正常", ""));
        // 合计占位行：运行时长合计留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("validation", "0054_validation_equipment-inspection-record.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("crimsonRed", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0054EquipmentInspectionRecordService service = factory.createApi(Validation0054EquipmentInspectionRecordService.class);
            service.exportEquipmentInspectionRecord("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("设备点检记录填报表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首台设备运行时长
            Cell d2 = sheet.getRow(1).getCell(3);
            Assert.assertEquals(8.00, evaluator.evaluate(d2).getNumberValue(), 0.0001);
            // 运行时长合计 = SUM(D2:D7) = 8 + 12 + 20 + 6 + 10 + 4
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("SUM(D2:D7)", d8.getCellFormula());
            Assert.assertEquals(60.00, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景83】设备点检记录填报表导出: " + out.getAbsolutePath()
                    + "，总运行时长 " + evaluator.evaluate(d8).getNumberValue());
        }
    }

    /** 导入解析：读取并校验设备点检记录填报表（先重算公式，再走 VALIDATION）。 */
    @Test
    public void importEquipmentInspectionRecord() throws Exception {
        File src = BizKit.outFile("validation", "0054_validation_equipment-inspection-record.xlsx");
        if (!src.exists()) {
            exportEquipmentInspectionRecord();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("validation", "0054_validation_equipment-inspection-record-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Validation0054EquipmentInspectionRecordService service = factory.createApi(Validation0054EquipmentInspectionRecordService.class);
            List<JQuickRow> rows = service.importEquipmentInspectionRecord("field", "value");

            System.out.println("【场景83】设备点检记录填报表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    设备编号=" + r.get("equipmentCode") + ", 设备名称=" + r.get("equipmentName")
                        + ", 点检人=" + r.get("inspector") + ", 运行时长=" + r.get("runningHours")
                        + ", 温度=" + r.get("temperature") + ", 点检结论=" + r.get("inspectionResult")
                        + ", 备注=" + r.get("remark"));
            }
            // 表头不计入数据行：6 台设备 + 1 行合计
            Assert.assertEquals("6 台设备 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条点检记录（a=编号，b=名称，c=点检人，d=运行时长，e=温度，f=结论，g=备注）。 */
    private static JQuickRow equipment(String code, String name, String inspector, double runningHours,
                                       double temperature, String inspectionResult, String remark) {
        JQuickRow row = new JQuickRow();
        row.put("a", code);
        row.put("b", name);
        row.put("c", inspector);
        row.put("d", runningHours);
        row.put("e", temperature);
        row.put("f", inspectionResult);
        row.put("g", remark);
        return row;
    }
}
