package com.github.paohaijiao.ext;

import com.github.paohaijiao.model.JExcelChartData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface JExcelExtendsChartProvider {
    public  void createDonutChart(XSSFWorkbook workbook, JExcelChartData chartData, String sheetName);
}
