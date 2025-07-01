//package com.github.paohaijiao.ext;
//
//import com.github.paohaijiao.model.JExcelChartData;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xddf.usermodel.chart.*;
//import org.apache.poi.xssf.usermodel.*;
//
//import static com.github.paohaijiao.factory.JExcelChartFactory.writeDataToSheet;
//
//public class JExcelDonutChart implements JExcelExtendsChartProvider{
//    @Override
//    public void createDonutChart(XSSFWorkbook workbook, JExcelChartData chartData, String sheetName) {
//        XSSFSheet sheet = workbook.createSheet(sheetName);
//        writeDataToSheet(sheet, chartData);
//
//        XSSFDrawing drawing = sheet.createDrawingPatriarch();
//        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 0, 12, 20);
//        XSSFChart chart = drawing.createChart(anchor);
//        chart.setTitleText(chartData.getTitle());
//        XDDFChartData pieData = chart.createData(ChartTypes.PIE, null, null);
//        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange(
//                sheet, new CellRangeAddress(1, chartData.getCategories().size(), 0, 0));
//        XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(
//                sheet, new CellRangeAddress(1, chartData.getCategories().size(), 1, 1));
//
//        XDDFPieChartData.Series series = (XDDFPieChartData.Series) pieData.addSeries(categories, values);
//        series.setTitle(chartData.getSeries().get(0).getName(), null);
//
//        // 设置为环形图
//        if (chart.getCTChart().getPlotArea().getPieChartArray(0) != null) {
//            chart.getCTChart().getPlotArea().getPieChartArray(0).addNewHoleSize().setVal(50); // 50%空心
//        }
//
//        chart.plot(pieData);
//    }
//}
