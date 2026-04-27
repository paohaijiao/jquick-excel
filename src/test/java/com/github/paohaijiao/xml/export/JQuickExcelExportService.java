package com.github.paohaijiao.xml.export;

import com.github.paohaijiao.xml.param.Param;

public interface JQuickExcelExportService {

    public void exportExcel(@Param("field")String field, @Param("value")String value);
}
