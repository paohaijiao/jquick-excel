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
 * 场景 72：来料检验不合格记录表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>业务：按来料批次登记送检数量与不合格数量，逐行算「不合格率 = 不合格数量 / 送检数量」，
 * 末尾一行给出汇总数量与整体不合格率；不合格率超标的批次标红。
 *
 * <p>导出侧统计与样式全部在 {@code jquick/biz/style/0072_style_incoming-inspection-defect.xml}：
 * FORMULAS 求逐行不合格率、数量合计与整体不合格率，STYLE 给合计行高亮、给超标批次标红。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Style0072IncomingInspectionDefectDemo {

    private static final String XML = "jquick/biz/style/0072_style_incoming-inspection-defect.xml";

    /** 导出：纯 XML 完成不合格率、数量汇总与超标标红。 */
    @Test
    public void exportIncomingInspectionDefect() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        // f=不合格率 留空，由 FORMULAS 计算
        rows.add(batch("IQ2601", "冷轧钢板", "华新电子", 2000, 20, "让步接收"));
        rows.add(batch("IQ2602", "铜芯电缆", "恒通材料", 1500, 15, "让步接收"));
        rows.add(batch("IQ2603", "铝合金型材", "佳美包装", 1200, 96, "退货"));
        rows.add(batch("IQ2604", "不锈钢管", "新元五金", 800, 12, "让步接收"));
        rows.add(batch("IQ2605", "橡胶密封圈", "天海电子", 5000, 40, "让步接收"));
        rows.add(batch("IQ2606", "弹簧垫片", "中兴物流", 3000, 30, "让步接收"));
        // 合计占位行：数量合计与整体不合格率留给 FORMULAS
        JQuickRow total = new JQuickRow();
        total.put("a", "合计");
        total.put("b", null);
        total.put("c", null);
        total.put("d", null);
        total.put("e", null);
        total.put("f", null);
        total.put("g", null);
        rows.add(total);

        File out = BizKit.outFile("style", "0072_style_incoming-inspection-defect.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("peach", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0072IncomingInspectionDefectService service = factory.createApi(Style0072IncomingInspectionDefectService.class);
            service.exportIncomingInspectionDefect("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("来料检验不合格记录表");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 首行不合格率 = 不合格 / 送检 = 20 / 2000
            Cell f2 = sheet.getRow(1).getCell(5);
            Assert.assertEquals("E2/D2", f2.getCellFormula());
            Assert.assertEquals(0.01, evaluator.evaluate(f2).getNumberValue(), 0.0001);
            // 送检数量合计 = SUM(D2:D7) = 13500
            Cell d8 = sheet.getRow(7).getCell(3);
            Assert.assertEquals("SUM(D2:D7)", d8.getCellFormula());
            Assert.assertEquals(13500.00, evaluator.evaluate(d8).getNumberValue(), 0.0001);
            // 不合格数量合计 = SUM(E2:E7) = 213
            Cell e8 = sheet.getRow(7).getCell(4);
            Assert.assertEquals(213.00, evaluator.evaluate(e8).getNumberValue(), 0.0001);
            // 超标批次（第 3 个批次，Excel 第 4 行）不合格率 = 96 / 1200
            Cell f4 = sheet.getRow(3).getCell(5);
            Assert.assertEquals(0.08, evaluator.evaluate(f4).getNumberValue(), 0.0001);
            // 合计行加粗高亮
            Assert.assertTrue("合计行应加粗", ((XSSFCellStyle) sheet.getRow(7).getCell(0).getCellStyle()).getFont().getBold());
            // 超标批次不合格率标红加粗
            XSSFCellStyle defectStyle = (XSSFCellStyle) f4.getCellStyle();
            Assert.assertTrue("超标不合格率应加粗", defectStyle.getFont().getBold());
            Assert.assertEquals("超标不合格率应标红", (int) IndexedColors.RED.getIndex(), (int) defectStyle.getFont().getColor());

            System.out.println("【场景72】来料检验不合格记录表导出: " + out.getAbsolutePath()
                    + "，整体不合格率 " + evaluator.evaluate(sheet.getRow(7).getCell(5)).getNumberValue());
        }
    }

    /** 导入解析：读取导出的来料检验记录（先重算公式）。 */
    @Test
    public void importIncomingInspectionDefect() throws Exception {
        File src = BizKit.outFile("style", "0072_style_incoming-inspection-defect.xlsx");
        if (!src.exists()) {
            exportIncomingInspectionDefect();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("style", "0072_style_incoming-inspection-defect-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Style0072IncomingInspectionDefectService service = factory.createApi(Style0072IncomingInspectionDefectService.class);
            List<JQuickRow> rows = service.importIncomingInspectionDefect("field", "value");

            System.out.println("【场景72】来料检验不合格记录表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    批次号=" + r.get("batchNo") + ", 物料=" + r.get("materialName")
                        + ", 供应商=" + r.get("supplier") + ", 送检=" + r.get("inspectionQty")
                        + ", 不合格=" + r.get("defectQty") + ", 不合格率=" + r.get("defectRate")
                        + ", 处理=" + r.get("disposal"));
            }
            // 表头不计入数据行：6 个批次 + 1 行合计
            Assert.assertEquals("6 个批次 + 1 合计行", 7, rows.size());
        }
    }

    /** 构造一条来料检验（a=批次号，b=物料名称，c=供应商，d=送检数量，e=不合格数量，f=不合格率，g=处理方式）。 */
    private static JQuickRow batch(String batchNo, String materialName, String supplier,
                                   int inspectionQty, int defectQty, String disposal) {
        JQuickRow row = new JQuickRow();
        row.put("a", batchNo);
        row.put("b", materialName);
        row.put("c", supplier);
        row.put("d", inspectionQty);
        row.put("e", defectQty);
        row.put("f", null);
        row.put("g", disposal);
        return row;
    }
}
