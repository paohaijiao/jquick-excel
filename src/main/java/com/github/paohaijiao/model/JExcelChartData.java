package com.github.paohaijiao.model;

import lombok.Data;

import java.util.List;
@Data
public class JExcelChartData {
    private String title;
    private String categoryAxisTitle;
    private String valueAxisTitle;
    private List<String> categories;
    private List<JSeriesData> series;
}
