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
package com.github.paohaijiao.demo.support;

import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * demo 子包公共工具：统一管理输出目录、导入夹具、测试数据与代理工厂的创建。
 *
 * <p>所有产物都写到模块的 {@code target/demo-output/} 目录，既不污染源码目录，
 * 也不依赖任何机器相关的绝对路径；IDE 与 Maven 命令行下工作目录都是模块根目录。
 */
public final class DemoKit {

    /** 统一输出目录（相对模块根目录）。 */
    private static final File OUTPUT_DIR = new File("target" + File.separator + "demo-output");

    /** 导入夹具文件名。 */
    public static final String IMPORT_FILE_NAME = "importDemo.xlsx";

    private DemoKit() {
    }

    /**
     * 返回 target/demo-output 下的文件（目录不存在时自动创建）。
     *
     * @param name 文件名，如 style-demo.xlsx
     * @return 目标文件
     */
    public static File out(String name) {
        if (!OUTPUT_DIR.exists() && !OUTPUT_DIR.mkdirs()) {
            throw new IllegalStateException("无法创建输出目录: " + OUTPUT_DIR.getAbsolutePath());
        }
        return new File(OUTPUT_DIR, name);
    }

    /**
     * 生成（或复用）导入侧共用的测试工作簿。
     *
     * <p>工作表「学生信息」，9 列表头 + 3 行数据，<b>所有单元格按文本写入</b>：
     * VALIDATION 的 integer/date 规则只对文本值稳定生效，数值或真实日期单元格
     * 会触发框架内部 parse 失败（详见 validation 子包中的说明注释）。
     *
     * @return 夹具文件 target/demo-output/importDemo.xlsx
     */
    public static File prepareImportFile() throws Exception {
        File file = out(IMPORT_FILE_NAME);
        if (file.exists()) {
            return file;
        }
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("学生信息");
            String[] headers = {"学号", "姓名", "性别", "年龄", "出生日期", "班级", "邮箱", "手机号", "分数"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            String[][] data = {
                    {"2024001", "张三", "男", "20", "2004-09-01", "计算机1班", "zhangsan@example.com", "13800000001", "88.5"},
                    {"2024002", "李四", "女", "21", "2003-09-01", "计算机2班", "lisi@example.com", "13800000002", "92.0"},
                    {"2024003", "王五", "男", "22", "2002-09-02", "计算机3班", "wangwu@example.com", "13800000003", "79.5"},
            };
            for (int r = 0; r < data.length; r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < data[r].length; c++) {
                    row.createCell(c).setCellValue(data[r][c]);
                }
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
        return file;
    }

    /**
     * 把 {@code key/value} 交替参数拼成保序行。
     * 例如 {@code row("a", "张三", "b", 20)}。
     */
    public static Map<String, Object> row(Object... kv) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            map.put((String) kv[i], kv[i + 1]);
        }
        return map;
    }

    /** 基础导出 / 页脚 / 主题 / IO 示例使用的三行数据（姓名、性别码值、年龄）。 */
    public static List<Map<String, Object>> personRows() {
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(row("a", "张三", "b", "1", "c", 20));
        data.add(row("a", "李四", "b", "2", "c", 21));
        data.add(row("a", "王五", "b", "1", "c", 22));
        return data;
    }

    /** FORMAT 示例数据：a 工号、b 姓名、c/d 金额、e 比率、f 日期。 */
    public static List<Map<String, Object>> formatRows() {
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(row("a", 240001, "b", "张三", "c", 12345.6, "d", 12345.6, "e", 0.856, "f", new Date()));
        data.add(row("a", 240002, "b", "李四", "c", 999.99, "d", 999.99, "e", 0.123, "f", new Date()));
        return data;
    }

    /** MERGE 示例数据：a 部门、b 姓名、c~f 四个季度数值。 */
    public static List<Map<String, Object>> mergeRows() {
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(row("a", "研发部", "b", "张三", "c", 100, "d", 120, "e", 130, "f", 140));
        data.add(row("a", "研发部", "b", "李四", "c", 110, "d", 125, "e", 135, "f", 145));
        return data;
    }

    /** FORMULAS 示例数据（a 项目、b/c/d 三个月数值，共 3 行）。 */
    public static List<Map<String, Object>> formulaRows() {
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(row("a", "项目A", "b", 10, "c", 20, "d", 30));
        data.add(row("a", "项目B", "b", 40, "c", 50, "d", 60));
        data.add(row("a", "项目C", "b", 70, "c", 80, "d", 90));
        return data;
    }

    /** GRAPH 示例数据（a 产品、b 销量）。 */
    public static List<Map<String, Object>> productRows() {
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(row("a", "产品A", "b", 120));
        data.add(row("a", "产品B", "b", 200));
        data.add(row("a", "产品C", "b", 150));
        return data;
    }

    /** List&lt;Map&gt; 转框架内部的保序 JQuickRow 列表。 */
    public static List<JQuickRow> toRows(List<Map<String, Object>> data) {
        return JQuickRow.toRows(JObjectConverter.convert(data));
    }

    /** 导出方向字典：码值 → Excel 文字（1=男，2=女）。 */
    public static JContext exportDict() {
        JContext ctx = new JContext();
        ctx.put("dict", dict("1", "男", "2", "女"));
        return ctx;
    }

    /** 导入方向字典：Excel 文字 → 内部码值（男=1，女=2）。 */
    public static JContext importDict() {
        JContext ctx = new JContext();
        ctx.put("dict", dict("男", "1", "女", "2"));
        return ctx;
    }

    private static Map<String, Object> dict(String... kv) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            map.put(kv[i], kv[i + 1]);
        }
        return map;
    }

    /**
     * 创建导入代理（无上下文）。
     *
     * @param xmlPath 类路径下的规则文件，如 demo/sheet/jquick-excel.xml
     * @param in      Excel 输入流（调用期间保持打开）
     * @param api     服务接口类型
     */
    public static <T> T importApi(String xmlPath, InputStream in, Class<T> api) {
        JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(in);
        return new JQuickXmlFactory(parser, xmlPath).createApi(api);
    }

    /**
     * 创建导入代理（带 JContext 字典等上下文）。
     */
    public static <T> T importApi(String xmlPath, JContext ctx, InputStream in, Class<T> api) {
        JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(ctx, in);
        return new JQuickXmlFactory(parser, xmlPath).createApi(api);
    }

    /**
     * 创建导出代理（无主题、无上下文）。
     */
    public static <T> T exportApi(String xmlPath, List<JQuickRow> rows, OutputStream os, Class<T> api) {
        JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
        JQuickFactory factory = new JQuickXmlFactory(parser, xmlPath);
        return factory.createApi(api);
    }

    /**
     * 创建导出代理（带 JContext 字典等上下文）。
     */
    public static <T> T exportApi(String xmlPath, JContext ctx, List<JQuickRow> rows,
                                  OutputStream os, Class<T> api) {
        JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(ctx, rows, os);
        JQuickFactory factory = new JQuickXmlFactory(parser, xmlPath);
        return factory.createApi(api);
    }

    /**
     * 创建导出代理（XML 构造器首参指定主题编码）。
     */
    public static <T> T exportApi(String xmlPath, String theme, List<JQuickRow> rows,
                                  OutputStream os, Class<T> api) {
        JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(theme, rows, os);
        JQuickFactory factory = new JQuickXmlFactory(parser, xmlPath);
        return factory.createApi(api);
    }
}
