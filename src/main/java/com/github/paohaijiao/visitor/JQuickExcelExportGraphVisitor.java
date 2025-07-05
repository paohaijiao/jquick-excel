
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
package com.github.paohaijiao.visitor;

import com.github.paohaijiao.graph.model.JSeriesData;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JStringUtils;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName com.paohaijiao.javelin.visitor
 *
 * @author Martin
 * @version 1.0.0
 * @className JQuickExcelExportVisitor
 * @date 2025/6/18
 * @description
 */
public class JQuickExcelExportGraphVisitor extends JQuickExcelExportMergeVisitor {
    @Override
    public Void visitGraphOption(JQuickExcelParser.GraphOptionContext ctx) {
        for (JQuickExcelParser.GraphSpecContext specCtx : ctx.graphSpec()) {
           visit(specCtx);
        }
        return null;
    }
    @Override
    public Void visitGraphSpec(JQuickExcelParser.GraphSpecContext ctx) {
        if (ctx.chartType() != null) {
            visitChartType(ctx.chartType());
        }else if(ctx.chartTitle() != null) {
            visitChartTitle(ctx.chartTitle());
        } else if (ctx.categoryAxis() != null) {
            visitCategoryAxis(ctx.categoryAxis());
        } else if (ctx.valueAxis() != null) {
            visitValueAxis(ctx.valueAxis());
        } else if (ctx.categories()!=null) {
            visitCategories(ctx.categories());
        } else if (ctx.series() != null) {
            visitSeries(ctx.series());
        }
        return null;
    }


    @Override
    public Void visitChartType(JQuickExcelParser.ChartTypeContext ctx) {
        String chartType=ctx.chartTypeValue().getText();
        config.getGraph().setChartType(chartType);
        return null;
    }

    @Override
    public Object visitChartTitle(JQuickExcelParser.ChartTitleContext ctx) {
        String title = ctx.IDENTIFIER().getText();
        String titleString= JStringUtils.trim(title);
        config.getGraph().setTitle(titleString);
        return null;
    }

    @Override
    public Object visitCategoryAxis(JQuickExcelParser.CategoryAxisContext ctx) {
        String axis = ctx.IDENTIFIER().getText();
        String titleString= JStringUtils.trim(axis);
        config.getGraph().setCategoryAxisTitle(titleString);
        return null;
    }

    @Override
    public Void visitValueAxis(JQuickExcelParser.ValueAxisContext ctx) {
        String axis = ctx.IDENTIFIER().getText();
        String titleString= JStringUtils.trim(axis);
        config.getGraph().setValueAxisTitle(titleString);
        return null;
    }

    @Override
    public Void visitCategories(JQuickExcelParser.CategoriesContext ctx) {
        List<String> categories = new ArrayList<>();
        for (JQuickExcelParser.FunctionArgContext functionArgContext : ctx.functionArg()) {
            Object object=visitFunctionArg(functionArgContext);
            categories.add(object.toString());
        }
        config.getGraph().setCategories(categories);
        return null;
    }

    @Override
    public Void visitSeries(JQuickExcelParser.SeriesContext ctx) {
        List<JSeriesData> seriesList = new ArrayList<>();
        for (JQuickExcelParser.SeriesSpecContext seriesSpecCtx : ctx.seriesSpec()) {
            JSeriesData seriesData=visitSeriesSpec(seriesSpecCtx);
            seriesList.add(seriesData);
        }

        config.getGraph().setSeries(seriesList);
        return null;
    }

    @Override
    public JSeriesData visitSeriesSpec(JQuickExcelParser.SeriesSpecContext ctx) {
        JSeriesData data=new JSeriesData();
        String seriName=null;
        List<Double> seriesDataList=new ArrayList<>();
        if(ctx.seriesName()!=null) {
            seriName=visitSeriesName(ctx.seriesName());
        }
        if(ctx.seriesData()!=null) {
            seriesDataList=visitSeriesData(ctx.seriesData());
        }
        data.setName(seriName);
        data.setData(seriesDataList);
        return data;
    }
    @Override
    public String visitSeriesName(JQuickExcelParser.SeriesNameContext ctx) {
        return ctx.IDENTIFIER().getText();
    }
    @Override
    public List<Double> visitSeriesData(JQuickExcelParser.SeriesDataContext ctx) {
        List<Double> list=new ArrayList<>();
        for (TerminalNode context:ctx.NUMBER()){
           String string= context.getText();
            list.add(Double.parseDouble(JStringUtils.trim(string)));
        }
        return list;
    }

}
