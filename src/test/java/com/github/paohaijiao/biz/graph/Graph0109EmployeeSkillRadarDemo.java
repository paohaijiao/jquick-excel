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
package com.github.paohaijiao.biz.graph;

import com.github.paohaijiao.biz.BizKit;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
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
 * 场景 109：员工能力评估（🟢 纯 XML 无代码 + GRAPH 雷达图）。
 *
 * <p>业务：三位员工在技术 / 沟通 / 协作 / 执行四个维度的评分，末行算各人平均分；
 * 附雷达图看能力画像。统计与图表全部写在
 * {@code jquick/biz/graph/0109_graph_employee-skill-radar.xml}：
 * <ul>
 *   <li>FORMULAS：末行 {@code AVERAGE} 计算各人平均分；</li>
 *   <li>STYLE：平均行加粗高亮；</li>
 *   <li>GRAPH：雷达图（RADAR），图表数据写入独立的「员工能力雷达图」sheet。</li>
 * </ul>
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Graph0109EmployeeSkillRadarDemo {

    private static final String XML = "jquick/biz/graph/0109_graph_employee-skill-radar.xml";

    /** 导出：4 个维度 + 1 行平均 + 雷达图，回读校验平均分与图表 sheet。 */
    @Test
    public void exportEmployeeSkillRadar() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(detail("技术", 85, 75, 90));
        rows.add(detail("沟通", 70, 90, 80));
        rows.add(detail("协作", 90, 80, 85));
        rows.add(detail("执行", 80, 85, 88));
        rows.add(averageRow());

        File out = BizKit.outFile("graph", "0109_graph_employee-skill-radar.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("crimsonRed", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0109EmployeeSkillRadarService service = factory.createApi(Graph0109EmployeeSkillRadarService.class);
            service.exportEmployeeSkillRadar("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("员工能力评估");
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            // 末行平均分
            Assert.assertEquals("AVERAGE(B2:B5)", sheet.getRow(5).getCell(1).getCellFormula());
            Assert.assertEquals(81.25, evaluator.evaluate(sheet.getRow(5).getCell(1)).getNumberValue(), 0.0001);
            Assert.assertEquals("AVERAGE(C2:C5)", sheet.getRow(5).getCell(2).getCellFormula());
            Assert.assertEquals(82.5, evaluator.evaluate(sheet.getRow(5).getCell(2)).getNumberValue(), 0.0001);
            Assert.assertEquals("AVERAGE(D2:D5)", sheet.getRow(5).getCell(3).getCellFormula());
            Assert.assertEquals(85.75, evaluator.evaluate(sheet.getRow(5).getCell(3)).getNumberValue(), 0.0001);
            // 平均行加粗高亮
            Assert.assertTrue("平均行应加粗", ((XSSFCellStyle) sheet.getRow(5).getCell(0).getCellStyle()).getFont().getBold());
            // 雷达图数据落在独立的「员工能力雷达图」sheet
            Assert.assertNotNull("应生成「员工能力雷达图」图表 sheet", wb.getSheet("员工能力雷达图"));

            System.out.println("【场景109】员工能力评估导出: " + out.getAbsolutePath()
                    + "，sheet 列表=" + wb.getNumberOfSheets() + " 个");
        }
    }

    /** 导入解析：读取导出的员工能力评估（先重算公式），打印每行字段。 */
    @Test
    public void importEmployeeSkillRadar() throws Exception {
        File src = BizKit.outFile("graph", "0109_graph_employee-skill-radar.xlsx");
        if (!src.exists()) {
            exportEmployeeSkillRadar();
        }
        File recalced = BizKit.recalc(src, BizKit.outFile("graph", "0109_graph_employee-skill-radar-recalc.xlsx"));
        try (InputStream in = new FileInputStream(recalced)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Graph0109EmployeeSkillRadarService service = factory.createApi(Graph0109EmployeeSkillRadarService.class);
            List<JQuickRow> rows = service.importEmployeeSkillRadar("field", "value");

            System.out.println("【场景109】员工能力评估导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    维度=" + r.get("dimension") + ", 张三=" + r.get("zhangsan")
                        + ", 李四=" + r.get("lisi") + ", 王五=" + r.get("wangwu"));
            }
            Assert.assertEquals("4 个维度 + 1 行平均", 5, rows.size());
        }
    }

    /** 构造一条维度明细（平均分由 FORMULAS 计算，此处不写）。 */
    private static JQuickRow detail(String dimension, double zhangsan, double lisi, double wangwu) {
        JQuickRow row = new JQuickRow();
        row.put("a", dimension);
        row.put("b", zhangsan);
        row.put("c", lisi);
        row.put("d", wangwu);
        return row;
    }

    /** 平均行：只写「平均」文字，其余列以空值占位（值由 XML FORMULAS 计算）。 */
    private static JQuickRow averageRow() {
        JQuickRow row = new JQuickRow();
        row.put("a", "平均");
        row.put("b", null);
        row.put("c", null);
        row.put("d", null);
        return row;
    }
}
