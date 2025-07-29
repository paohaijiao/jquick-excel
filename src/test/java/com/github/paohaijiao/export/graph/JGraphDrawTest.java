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
package com.github.paohaijiao.export.graph;

import com.github.paohaijiao.enums.JExcelChartType;
import com.github.paohaijiao.executor.JQuickExcelCommonExportExecutor;
import com.github.paohaijiao.factory.JExcelChartFactory;
import com.github.paohaijiao.graph.model.JChartData;
import com.github.paohaijiao.graph.model.JSeriesData;
import com.github.paohaijiao.handler.JExcelExportHandler;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelComonExportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * packageName com.github.paohaijiao.export.graph
 *
 * @author Martin
 * @version 1.0.0
 * @className JGraphTest
 * @date 2025/7/5
 * @description
 */
public class JGraphDrawTest {

    @Test
    public void rangeMerge() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = LINE,\n" +
                "    TITLE = \"SmartphoneMarketShare2023\",\n" +
                "    CATEGORIES = [\"Apple\", \"Samsung\", \"Xiaomi\", \"OPPO\", \"vivo\", \"Other\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = MarketShare,\n" +
                "        DATA = [38.5, 22.3, 15.7, 10.2, 8.5, 4.8]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config=(JExcelExportModel)executor.execute(rule);
        JExcelExportHandler excelExportHandler=new JExcelExportHandler(config,new ArrayList<>());
        excelExportHandler.exportData(config,new ArrayList<>());
        Workbook workbook= excelExportHandler.getWorkBook();
        workbook.write(new FileOutputStream(new File("D:\\test\\line.xls")));
    }
    @Test
    public void column() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = COLUMN,\n" +
                "    TITLE = \"销售数据统计\",\n" +
                "    CATEGORY_AXIS = \"产品\",\n" +
                "    VALUE_AXIS = \"销量\",\n" +
                "    CATEGORIES = [\"产品A\", \"产品B\", \"产品C\", \"产品D\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"第一季度\",\n" +
                "        DATA = [120, 200, 150, 180]\n" +
                "    }, {\n" +
                "        NAME = \"第二季度\",\n" +
                "        DATA = [180, 210, 190, 220]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());
        try (FileOutputStream out = new FileOutputStream("D://test//column.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void barChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = BAR,\n" +
                "    TITLE = \"2023年季度销售数据\",\n" +
                "    CATEGORY_AXIS = \"季度\",\n" +
                "    VALUE_AXIS = \"销售额(万元)\",\n" +
                "    CATEGORIES = [\"第一季度\", \"第二季度\", \"第三季度\", \"第四季度\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"产品A\",\n" +
                "        DATA = [450, 520, 480, 600]\n" +
                "    }, {\n" +
                "        NAME = \"产品B\",\n" +
                "        DATA = [320, 380, 420, 500]\n" +
                "    }, {\n" +
                "        NAME = \"产品C\",\n" +
                "        DATA = [280, 310, 350, 400]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());

        try (FileOutputStream out = new FileOutputStream("D://test//bar.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void bar3dChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = BAR3D,\n" +
                "    TITLE = \"产品季度销售(3D)\",\n" +
                "    CATEGORY_AXIS = \"产品\",\n" +
                "    VALUE_AXIS = \"销售额(万元)\",\n" +
                "    CATEGORIES = [\"笔记本\", \"手机\", \"平板\", \"显示器\", \"配件\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"第一季度\",\n" +
                "        DATA = [450, 680, 320, 280, 190]\n" +
                "    }, {\n" +
                "        NAME = \"第二季度\",\n" +
                "        DATA = [520, 720, 380, 310, 210]\n" +
                "    }, {\n" +
                "        NAME = \"第三季度\",\n" +
                "        DATA = [480, 750, 350, 330, 230]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());
        try (FileOutputStream out = new FileOutputStream("D://test//bar3D.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void lineChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = LINE,\n" +
                "    TITLE = \"2023年北京月平均温度变化\",\n" +
                "    CATEGORY_AXIS = \"月份\",\n" +
                "    VALUE_AXIS = \"温度(℃)\",\n" +
                "    CATEGORIES = [\"1月\", \"2月\", \"3月\", \"4月\", \"5月\", \"6月\", \n" +
                "                 \"7月\", \"8月\", \"9月\", \"10月\", \"11月\", \"12月\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"平均温度\",\n" +
                "        DATA = [-3.2, 0.5, 7.8, 15.2, 21.3, 25.7, \n" +
                "                27.9, 26.8, 21.5, 14.6, 6.3, -1.0]\n" +
                "    }, {\n" +
                "        NAME = \"最高温度\",\n" +
                "        DATA = [2.1, 5.3, 12.7, 20.5, 26.8, 30.4, \n" +
                "                32.6, 31.5, 27.2, 20.8, 12.5, 4.2]\n" +
                "    }, {\n" +
                "        NAME = \"最低温度\",\n" +
                "        DATA = [-8.5, -4.2, 2.9, 9.9, 15.8, 21.0, \n" +
                "                23.2, 22.1, 15.8, 8.4, 0.1, -6.2]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());
        try (FileOutputStream out = new FileOutputStream("D://test//line.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void pieChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = PIE,\n" +
                "    TITLE = \"2023年智能手机市场份额\",\n" +
                "    CATEGORIES = [\"Apple\", \"Samsung\", \"Xiaomi\", \"OPPO\", \"vivo\", \"其他\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"市场份额\",\n" +
                "        DATA = [38.5, 22.3, 15.7, 10.2, 8.5, 4.8]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());
        try (FileOutputStream out = new FileOutputStream("D://test//pie.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void areaChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = AREA,\n" +
                "    TITLE = \"季度销售趋势分析\",\n" +
                "    CATEGORY_AXIS = \"季度\",\n" +
                "    VALUE_AXIS = \"销售额(万元)\",\n" +
                "    CATEGORIES = [\"Q1\", \"Q2\", \"Q3\", \"Q4\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"产品线A\",\n" +
                "        DATA = [120, 150, 180, 210]\n" +
                "    }, {\n" +
                "        NAME = \"产品线B\",\n" +
                "        DATA = [80, 110, 130, 170]\n" +
                "    }, {\n" +
                "        NAME = \"产品线C\",\n" +
                "        DATA = [60, 75, 90, 120]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());
        try (FileOutputStream out = new FileOutputStream("D://test//area.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void area3dChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = AREA3D,\n" +
                "    TITLE = \"季度销售趋势(3D)\",\n" +
                "    CATEGORY_AXIS = \"季度\",\n" +
                "    VALUE_AXIS = \"销售额(万元)\",\n" +
                "    CATEGORIES = [\"Q1\", \"Q2\", \"Q3\", \"Q4\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"产品线A\",\n" +
                "        DATA = [120, 150, 180, 210]\n" +
                "    }, {\n" +
                "        NAME = \"产品线B\",\n" +
                "        DATA = [80, 110, 130, 170]\n" +
                "    }, {\n" +
                "        NAME = \"产品线C\",\n" +
                "        DATA = [60, 75, 90, 120]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());

        try (FileOutputStream out = new FileOutputStream("D://test//area3D.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void scatterChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = SCATTER,\n" +
                "    TITLE = \"身高体重分布\",\n" +
                "    CATEGORY_AXIS = \"身高(cm)\",\n" +
                "    VALUE_AXIS = \"体重(kg)\",\n" +
                "    CATEGORIES = [\"160\", \"165\", \"170\", \"175\", \"180\", \"185\", \"190\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"男性\",\n" +
                "        DATA = [55, 60, 65, 70, 75, 80, 85]\n" +
                "    }, {\n" +
                "        NAME = \"女性\",\n" +
                "        DATA = [50, 55, 58, 62, 65, 68, 70]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());
        try (FileOutputStream out = new FileOutputStream("D://test//scatter.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void radarChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = RADAR,\n" +
                "    TITLE = \"员工能力评估\",\n" +
                "    CATEGORIES = [\"编程能力\", \"沟通能力\", \"设计能力\", \n" +
                "                 \"解决问题\", \"团队合作\", \"学习能力\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"员工A\",\n" +
                "        DATA = [90, 70, 85, 80, 75, 88]\n" +
                "    }, {\n" +
                "        NAME = \"员工B\",\n" +
                "        DATA = [75, 85, 70, 90, 80, 75]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());
        try (FileOutputStream out = new FileOutputStream("D://test//radar.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void surfaceChart() throws IOException {
        String rule = "EXPORT WITH GRAPH = {\n" +
                "    TYPE = SURFACE,\n" +
                "    TITLE = \"地形高度示例\",\n" +
                "    CATEGORY_AXIS = \"X坐标\",\n" +
                "    VALUE_AXIS = \"Y坐标\",\n" +
                "    CATEGORIES = [\"1\", \"2\", \"3\", \"4\", \"5\"],\n" +
                "    SERIES = [{\n" +
                "        NAME = \"Y=1\",\n" +
                "        DATA = [10, 15, 25, 30, 25]\n" +
                "    }, {\n" +
                "        NAME = \"Y=2\",\n" +
                "        DATA = [15, 20, 35, 40, 35]\n" +
                "    }, {\n" +
                "        NAME = \"Y=3\",\n" +
                "        DATA = [25, 35, 50, 55, 50]\n" +
                "    }, {\n" +
                "        NAME = \"Y=4\",\n" +
                "        DATA = [20, 30, 45, 50, 45]\n" +
                "    }, {\n" +
                "        NAME = \"Y=5\",\n" +
                "        DATA = [15, 25, 35, 40, 35]\n" +
                "    }]\n" +
                "}";
        System.out.println(rule);
        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
        JExcelExportHandler excelExportHandler = new JExcelExportHandler(config, new ArrayList<>());
        excelExportHandler.exportData(config, new ArrayList<>());

        try (FileOutputStream out = new FileOutputStream("D://test//surface.xlsx")) {
            excelExportHandler.getWorkBook().write(out);
            System.out.println("Excel文件生成成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
