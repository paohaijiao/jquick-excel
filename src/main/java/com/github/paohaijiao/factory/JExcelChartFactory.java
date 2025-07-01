package com.github.paohaijiao.factory;

import com.github.paohaijiao.enums.JExcelChartType;
import com.github.paohaijiao.model.JExcelChartData;
import com.github.paohaijiao.model.JSeriesData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;

public class JExcelChartFactory {

    public static XSSFWorkbook createWorkbookWithChart(JExcelChartData chartData, JExcelChartType chartType, String sheetName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        createChart(workbook, chartData, chartType, sheetName);
        return workbook;
    }
    public static void createChart(XSSFWorkbook workbook, JExcelChartData chartData, JExcelChartType chartType, String sheetName) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        writeDataToSheet(sheet, chartData);
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                chartData.getCategories().size() + 2, 0,
                chartData.getCategories().size() + 12, 20);

        XSSFChart chart = drawing.createChart(anchor);
        if (chartData.getTitle() != null) {
            chart.setTitleText(chartData.getTitle());
        }
        XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        if (chartData.getCategoryAxisTitle() != null) {
            categoryAxis.setTitle(chartData.getCategoryAxisTitle());
        }
        XDDFValueAxis valueAxis = chart.createValueAxis(AxisPosition.LEFT);
        if (chartData.getValueAxisTitle() != null) {
            valueAxis.setTitle(chartData.getValueAxisTitle());
        }
        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange(
                sheet, new CellRangeAddress(1, chartData.getCategories().size(), 0, 0));
        XDDFChartData chartDataObj = createChartData(chart, chartType, categoryAxis, valueAxis);
        for (JSeriesData series : chartData.getSeries()) {
            XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(1, series.getData().size(),
                            chartData.getSeries().indexOf(series) + 1,
                            chartData.getSeries().indexOf(series) + 1));
            XDDFChartData.Series seriesObj = chartDataObj.addSeries(categories, values);
            seriesObj.setTitle(series.getName(), null);
        }
        chart.plot(chartDataObj);
    }
    private static void writeDataToSheet(XSSFSheet sheet, JExcelChartData chartData) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Categories");
        for (int i = 0; i < chartData.getSeries().size(); i++) {
            headerRow.createCell(i + 1).setCellValue(chartData.getSeries().get(i).getName());
        }
        for (int i = 0; i < chartData.getCategories().size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(chartData.getCategories().get(i));
            for (int j = 0; j < chartData.getSeries().size(); j++) {
                row.createCell(j + 1).setCellValue(
                        chartData.getSeries().get(j).getData().get(i).doubleValue());
            }
        }
    }
    private static XDDFChartData createChartData(XSSFChart chart, JExcelChartType chartType,
                                                 XDDFCategoryAxis categoryAxis, XDDFValueAxis valueAxis) {
        switch (chartType) {
            case BAR:
                XDDFBarChartData barData = (XDDFBarChartData) chart.createData(
                        ChartTypes.BAR, categoryAxis, valueAxis);
                barData.setBarDirection(BarDirection.BAR);
                return barData;
            case BAR3D:
                XDDFBar3DChartData bar3DData = (XDDFBar3DChartData) chart.createData(
                        ChartTypes.BAR3D, categoryAxis, valueAxis);
                bar3DData.setBarDirection(BarDirection.BAR);
                return bar3DData;

            case COLUMN:
                XDDFBarChartData columnData = (XDDFBarChartData) chart.createData(
                        ChartTypes.BAR, categoryAxis, valueAxis);
                columnData.setBarDirection(BarDirection.COL);
                return columnData;
            case LINE:
                return chart.createData(ChartTypes.LINE, categoryAxis, valueAxis);
            case PIE:
                return chart.createData(ChartTypes.PIE, categoryAxis, valueAxis);
            case AREA:
                return chart.createData(ChartTypes.AREA, categoryAxis, valueAxis);
            case AREA3D:
                return chart.createData(ChartTypes.AREA3D, categoryAxis, valueAxis);
            case SCATTER:
                return chart.createData(ChartTypes.SCATTER, categoryAxis, valueAxis);
            case RADAR:
                return chart.createData(ChartTypes.RADAR, categoryAxis, valueAxis);
            case SURFACE:
                return chart.createData(ChartTypes.SURFACE, categoryAxis, valueAxis);
            default:
                throw new IllegalArgumentException("Unsupported chart type: " + chartType);
        }
    }
    public static void writeWorkbookToStream(XSSFWorkbook workbook, OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
        workbook.close();
    }
}
