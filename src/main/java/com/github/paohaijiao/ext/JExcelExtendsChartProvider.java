package com.github.paohaijiao.ext;

import com.github.paohaijiao.graph.model.JChartData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface JExcelExtendsChartProvider {

    public void createDonutChart(XSSFWorkbook workbook, JChartData chartData, String sheetName);
}
