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
package com.github.paohaijiao.biz.style;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
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
 * 场景 59：生产设备 OEE 分析表（绩效考核类，🟢 纯 XML）。
 *
 * <p>业务：按设备统计时间开动率、性能开动率与合格品率，逐行算「OEE = 三者之积」，
 * 末尾一行给出平均 OEE，OEE 低于标杆的设备标红。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0059_style_equipment-oee-analysis.xml}：
 * FORMULAS 做逐行连乘与平均 AVERAGE，STYLE 给均值行高亮、给 OEE 偏低设备标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0059EquipmentOeeAnalysisDemo {

    private static final String XML = "jquick/biz/style/0059_style_equipment-oee-analysis.xml";

    /** 导出：纯 XML 完成 OEE 连乘、平均 OEE 与偏低设备标红。 */
    @Test
    public void exportEquipmentOeeAnalysis() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(equipment("MC2601", "一号压铸机", 0.92, 0.95, 0.98, "达标"));
        rows.add(equipment("MC2602", "二号压铸机", 0.88, 0.90, 0.97, "达标"));
        rows.add(equipment("MC2603", "数控铣床", 0.85, 0.82, 0.95, "待改善"));
        rows.add(equipment("MC2604", "注塑机", 0.90, 0.93, 0.96, "达标"));
        rows.add(equipment("MC2605", "装配线", 0.95, 0.90, 0.99, "达标"));
        // 合计占位行：平均 OEE 留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "平均");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0059_style_equipment-oee-analysis.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("sakuraPink", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0059EquipmentOeeAnalysisService service = factory.createApi(Style0059EquipmentOeeAnalysisService.class);
            service.exportEquipmentOeeAnalysis("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("生产设备OEE分析表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首台 OEE = 时间开动率 × 性能开动率 × 合格品率 = 0.92 × 0.95 × 0.98
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("C2*D2*E2", f2.getCellFormula());
            Assert.assertEquals(0.92 * 0.95 * 0.98, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 偏低设备（第 3 台）OEE = 0.85 × 0.82 × 0.95
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals("C4*D4*E4", f4.getCellFormula());
            Assert.assertEquals(0.85 * 0.82 * 0.95, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 平均 OEE = AVERAGE(F2:F6)
            Cell f7 = sheet.getRow(6).getCell(5);
            Assert.assertEquals("AVERAGE(F2:F6)", f7.getCellFormula());
            double expectedAvg = (0.92 * 0.95 * 0.98 + 0.88 * 0.90 * 0.97 + 0.85 * 0.82 * 0.95
                    + 0.90 * 0.93 * 0.96 + 0.95 * 0.90 * 0.99) / 5.0;
            Assert.assertEquals(expectedAvg, evaluator.evaluate(f7).getNumberValue(), 0.0001);
            // 均值行加粗高亮
            Assert.assertTrue("均值行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());
            // OEE 偏低设备标红
            Assert.assertEquals("OEE 偏低应标红", (int) IndexedColors.RED.getIndex(),
                    (int) ((XSSFCellStyle) f4.getCellStyle()).getFont().getColor());

            System.out.println("【场景59】生产设备OEE分析表导出: " + out.getAbsolutePath()
                    + "，平均 OEE " + evaluator.evaluate(f7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的生产设备 OEE 分析表（先重算公式）。 */
    @Test
    public void importEquipmentOeeAnalysis() throws Exception {
        File src = BizKit.outFile("style", "0059_style_equipment-oee-analysis.xlsx");
        if (!src.exists()) {
            exportEquipmentOeeAnalysis();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0059_style_equipment-oee-analysis-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0059EquipmentOeeAnalysisService service = factory.createApi(Style0059EquipmentOeeAnalysisService.class);
            List<JQuickRow> rows = service.importEquipmentOeeAnalysis("field", "value");

            System.out.println("【场景59】生产设备OEE分析表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    设备=" + r.get("equipmentNo") + "/" + r.get("equipmentName")
                        + ", 时间开动率=" + r.get("availability") + ", 性能开动率=" + r.get("performance")
                        + ", 合格品率=" + r.get("quality") + ", OEE=" + r.get("oee")
                        + ", 评价=" + r.get("evaluation"));
            }
            // 表头不计入数据行：5 台设备 + 1 行平均
            Assert.assertEquals("5 台设备 + 1 平均行", 6, rows.size());
        }
    }

    /** 构造一台设备（a=编号，b=名称，c=时间开动率，d=性能开动率，e=合格品率，g=评价，f 留空由 FORMULAS 计算）。 */
    private static JQuickRow equipment(String equipmentNo, String equipmentName,
                                       double availability, double performance, double quality, String evaluation) {
        JQuickRow row = new JQuickRow();
        row.put("a", equipmentNo);
        row.put("b", equipmentName);
        row.put("c", availability);
        row.put("d", performance);
        row.put("e", quality);
        row.put("f", null);
        row.put("g", evaluation);
        return row;
    }
}
