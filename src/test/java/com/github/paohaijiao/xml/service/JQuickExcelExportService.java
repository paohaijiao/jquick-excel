package com.github.paohaijiao.xml.service;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

public interface JQuickExcelExportService {

    public void exportExcel(@Param("field")String field, @Param("value")String value);

    public List<JQuickRow> importExcel(@Param("field")String field, @Param("value")String value);
}
