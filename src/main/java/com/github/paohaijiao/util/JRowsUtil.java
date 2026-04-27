package com.github.paohaijiao.util;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlInvocationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JRowsUtil {

    public static List<JQuickRow> toRows(List<Map<String, Object>> list) {
        if (list == null) return null;
        List<JQuickRow> rows = new ArrayList<JQuickRow>();
        for (Map<String, Object> row : list) {
            rows.add(new JQuickRow(row));
        }
        return rows;
    }
}
