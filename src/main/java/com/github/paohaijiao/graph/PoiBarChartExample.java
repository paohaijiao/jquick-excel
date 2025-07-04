package com.github.paohaijiao.graph;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;

public class PoiBarChartExample {
    public static void main(String[] args) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("BarChart");

            // 1. 写入数据
            Object[][] data = {
                    {"产品", "销量"},
                    {"A", 120},
                    {"B", 200},
                    {"C", 90}
            };

            for (int i = 0; i < data.length; i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = row.createCell(j);
                    if (data[i][j] instanceof String) {
                        cell.setCellValue((String) data[i][j]);
                    } else {
                        cell.setCellValue((Integer) data[i][j]);
                    }
                }
            }

            // 2. 创建绘图对象和图表
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 0, 12, 20);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("产品销量对比");
            XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis valueAxis = chart.createValueAxis(AxisPosition.LEFT);

            // 4. 配置数据区域
            XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange(
                    sheet, new CellRangeAddress(1, 3, 0, 0)); // A2:A4
            XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(1, 3, 1, 1)); // B2:B4

            // 5. 创建条形图
            XDDFBarChartData barChartData = (XDDFBarChartData) chart.createData(
                    ChartTypes.BAR, categoryAxis, valueAxis); // 使用显式定义的坐标轴

            // 6. 添加数据系列
            XDDFBarChartData.Series series = (XDDFBarChartData.Series) barChartData.addSeries(categories, values);
            series.setTitle("销量", null);
            chart.plot(barChartData);

            // 7. 保存文件
            try (FileOutputStream fos = new FileOutputStream("d://test//BarChartExample.xlsx")) {
                workbook.write(fos);
            }
            System.out.println("条形图生成完成！");
        }
    }

}
