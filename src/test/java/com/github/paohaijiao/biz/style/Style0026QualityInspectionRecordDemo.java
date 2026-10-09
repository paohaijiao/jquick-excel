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
 * 场景 26：质量检验缺陷记录表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>业务：质检部门按批次登记抽检数量与缺陷数量，逐行算「缺陷率 = 缺陷数量 / 抽检数量」，
 * 末尾一行汇总抽检、缺陷总数与整体缺陷率；缺陷率超标的批次单元格标红，提示判定不合格。
 *
 * <p>🟢 统计与样式全部在 {@code jquick/biz/style/0026_style_quality-inspection-record.xml}：
 * FORMULAS 做逐行除法与合计行汇总，STYLE 给合计行高亮并给超标批次标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0026QualityInspectionRecordDemo {

    private static final String XML = "jquick/biz/style/0026_style_quality-inspection-record.xml";

    /** 导出：纯 XML 完成缺陷率、合计行汇总与超标标红。 */
    @Test
    public void exportQualityInspection() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(batch("PC2026001", "A型电机", 1000, 8, "合格"));
        rows.add(batch("PC2026002", "B型水泵", 800, 12, "合格"));
        // 第 4 行：C 型阀门缺陷率 3.60% 超标，模板中 E4 标红
        rows.add(batch("PC2026003", "C型阀门", 500, 18, "不合格"));
        rows.add(batch("PC2026004", "D型轴承", 1200, 6, "合格"));
        // 第 6 行：E 型齿轮缺陷率 4.17% 超标，模板中 E6 标红
        rows.add(batch("PC2026005", "E型齿轮", 600, 25, "不合格"));
        // 合计占位行：缺陷率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0026_style_quality-inspection-record.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("cyan", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0026QualityInspectionRecordService service = factory.createApi(Style0026QualityInspectionRecordService.class);
            service.exportQualityInspection("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("质量检验缺陷记录");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首批次缺陷率 = 缺陷 / 抽检 = 8 / 1000
            Cell e2 = sheet.getRow(1).getCell(4);
            Assert.assertEquals("D2/C2", e2.getCellFormula());
            Assert.assertEquals(0.008, evaluator.evaluate(e2).getNumberValue(), 0.0001);
            // 合计：抽检总数 SUM(C2:C6) = 4100
            Cell c7 = sheet.getRow(6).getCell(2);
            Assert.assertEquals("SUM(C2:C6)", c7.getCellFormula());
            Assert.assertEquals(4100.0, evaluator.evaluate(c7).getNumberValue(), 0.0001);
            // 合计：缺陷总数 SUM(D2:D6) = 69
            Cell d7 = sheet.getRow(6).getCell(3);
            Assert.assertEquals("SUM(D2:D6)", d7.getCellFormula());
            Assert.assertEquals(69.0, evaluator.evaluate(d7).getNumberValue(), 0.0001);
            // 超标批次（第4行 C 型阀门、第6行 E 型齿轮）缺陷率字体标红
            XSSFCellStyle overC = (XSSFCellStyle) sheet.getRow(3).getCell(4).getCellStyle();
            Assert.assertEquals("C 型阀门缺陷率应标红", (int) IndexedColors.RED.getIndex(), (int) overC.getFont().getColor());
            Assert.assertEquals("标红单元应加粗", Boolean.TRUE, overC.getFont().getBold());
            XSSFCellStyle overE = (XSSFCellStyle) sheet.getRow(5).getCell(4).getCellStyle();
            Assert.assertEquals("E 型齿轮缺陷率应标红", (int) IndexedColors.RED.getIndex(), (int) overE.getFont().getColor());
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(6).getCell(0).getCellStyle()).getFont().getBold());

            System.out.println("【场景26】质量检验缺陷记录导出: " + out.getAbsolutePath()
                    + "，抽检合计 " + evaluator.evaluate(c7).getNumberValue());
        }
    }

    /** 导入解析：读取导出的质量检验缺陷记录表（先重算公式）。 */
    @Test
    public void importQualityInspection() throws Exception {
        File src = BizKit.outFile("style", "0026_style_quality-inspection-record.xlsx");
        if (!src.exists()) {
            exportQualityInspection();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0026_style_quality-inspection-record-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0026QualityInspectionRecordService service = factory.createApi(Style0026QualityInspectionRecordService.class);
            List<JQuickRow> rows = service.importQualityInspection("field", "value");

            System.out.println("【场景26】质量检验缺陷记录导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    批次=" + r.get("batchNo") + ", 产品=" + r.get("productName")
                        + ", 抽检=" + r.get("inspectQty") + ", 缺陷=" + r.get("defectQty") + ", 缺陷率=" + r.get("defectRate"));
            }
            // 表头不计入数据行：5 个批次 + 1 行合计
            Assert.assertEquals("5 个批次 + 1 合计", 6, rows.size());
        }
    }

    /** 构造一条质检批次（a=批次号，b=产品，c=抽检数量，d=缺陷数量，f=判定）。 */
    private static JQuickRow batch(String batchNo, String product, int inspectQty, int defectQty, String verdict) {
        JQuickRow row = new JQuickRow();
        row.put("a", batchNo);
        row.put("b", product);
        row.put("c", inspectQty);
        row.put("d", defectQty);
        row.put("e", null);
        row.put("f", verdict);
        return row;
    }
}
