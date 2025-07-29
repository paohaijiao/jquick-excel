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

        JQuickExcelCommonExportExecutor executor = new JQuickExcelCommonExportExecutor();
        JExcelExportModel config=(JExcelExportModel)executor.execute(rule);
        JExcelExportHandler excelExportHandler=new JExcelExportHandler(config,new ArrayList<>());
        excelExportHandler.exportData(config,new ArrayList<>());
        Workbook workbook= excelExportHandler.getWorkBook();
        workbook.write(new FileOutputStream(new File("D:\\test\\line.xls")));
    }


}
