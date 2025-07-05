package com.github.paohaijiao.enums;

public enum JExcelChartType {
    BAR("bar", "条形图/柱形图"),
    BAR3D("bar3d", "条形图/柱形图"),
    AREA3D("area3d", "基础3D面积图"),
    COLUMN("column", "柱形图(垂直)"),
    LINE("line", "折线图"),
    PIE("pie", "饼图"),
    AREA("area", "面积图"),        //
    RADAR("radar", "雷达图"),        //
    SURFACE("surface", "曲面图"),        //
    SCATTER("scatter", "散点图");
    private String code;
    private String name;

    private JExcelChartType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static JExcelChartType codeOf(String code) {
        for (JExcelChartType jExcelChartType : JExcelChartType.values()) {
            if (jExcelChartType.code.equalsIgnoreCase(code)) {
                return jExcelChartType;
            }
        }
        return null;
    }
}
