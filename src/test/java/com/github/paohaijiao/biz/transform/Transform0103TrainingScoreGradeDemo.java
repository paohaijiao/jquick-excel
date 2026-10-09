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
import java.util.List;

/**
 * 场景 103：培训考核成绩等级表（🟢 纯 XML + TRANSFORM SPI 函数）。
 *
 * <p>业务：培训考核中分数换算为等级（≥90 A、≥80 B、≥60 C、其余 D），并规范化考核日期。
 * 转换规则全部写在 {@code jquick/biz/transform/0103_transform_training-score-grade.xml}：
 * 等级走自定义 SPI 函数 {@code grade}，考核日期由 FORMAT 按 {@code yyyy-MM-dd} 输出。
 * Java 侧只提供分数与日期原始数据，等级换算不落在代码里。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public class Transform0103TrainingScoreGradeDemo {

    private static final String XML = "jquick/biz/transform/0103_transform_training-score-grade.xml";

    /** 导出：分数经 SPI grade 换算为等级，回读校验等级列与日期格式。 */
    @Test
    public void exportTrainingScoreGrade() throws Exception {
        List<JQuickRow> rows = new ArrayList<>();
        rows.add(student("S2601", "张三丰", 95, "2005-09-01", "初级班"));
        rows.add(student("S2602", "李四", 82, "2005-09-01", "初级班"));
        rows.add(student("S2603", "王五", 58, "2005-09-02", "中级班"));
        rows.add(student("S2604", "赵六", 90, "2005-09-02", "中级班"));
        rows.add(student("S2605", "孙七", 76, "2005-09-03", "高级班"));

        File out = BizKit.outFile("transform", "0103_transform_training-score-grade.xlsx");
        try (OutputStream os = new FileOutputStream(out)) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory("amber", rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Transform0103TrainingScoreGradeService service = factory.createApi(Transform0103TrainingScoreGradeService.class);
            service.exportTrainingScoreGrade("field", "value");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(out))) {
            Sheet sheet = wb.getSheet("培训考核成绩");
            Row first = sheet.getRow(1);
            // 分数 -> 等级：95 -> A、82 -> B、58 -> D、90 -> A、76 -> C
            Assert.assertEquals("A", first.getCell(2).getStringCellValue());
            Assert.assertEquals("B", sheet.getRow(2).getCell(2).getStringCellValue());
            Assert.assertEquals("D", sheet.getRow(3).getCell(2).getStringCellValue());
            Assert.assertEquals("A", sheet.getRow(4).getCell(2).getStringCellValue());
            Assert.assertEquals("C", sheet.getRow(5).getCell(2).getStringCellValue());
            // 考核日期格式化
            Assert.assertEquals("yyyy-MM-dd", first.getCell(3).getCellStyle().getDataFormatString());

            System.out.println("【场景103】培训考核成绩等级表导出: " + out.getAbsolutePath()
                    + "，共 " + sheet.getLastRowNum() + " 行数据");
        }
    }

    /** 导入解析：读取成绩等级表，按表头映射回字段。 */
    @Test
    public void importTrainingScoreGrade() throws Exception {
        File src = BizKit.outFile("transform", "0103_transform_training-score-grade.xlsx");
        if (!src.exists()) {
            exportTrainingScoreGrade();
        }
        try (InputStream in = new FileInputStream(src)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
            JQuickFactory factory = new JQuickXmlFactory(parser, XML);
            Transform0103TrainingScoreGradeService service = factory.createApi(Transform0103TrainingScoreGradeService.class);
            List<JQuickRow> rows = service.importTrainingScoreGrade("field", "value");

            System.out.println("【场景103】培训考核成绩等级表导入解析: " + rows.size() + " 行");
            for (JQuickRow r : rows) {
                System.out.println("    学号=" + r.get("studentNo") + ", 姓名=" + r.get("studentName")
                        + ", 等级=" + r.get("gradeLevel") + ", 考核日期=" + r.get("examDate") + ", 班级=" + r.get("className"));
            }
            Assert.assertEquals(5, rows.size());
            Assert.assertEquals("A", rows.get(0).get("gradeLevel"));
            Assert.assertEquals("初级班", rows.get(0).get("className"));
        }
    }

    /** 构造一名考生成绩（c 为分数，导出时经 SPI grade 换算为等级；d 为日期对象走 FORMAT）。 */
    private static JQuickRow student(String no, String name, int score, String examDate, String className) throws Exception {
        JQuickRow row = new JQuickRow();
        row.put("a", no);
        row.put("b", name);
        row.put("c", score);
        row.put("d", new SimpleDateFormat("yyyy-MM-dd").parse(examDate));
        row.put("e", className);
        return row;
    }
}
