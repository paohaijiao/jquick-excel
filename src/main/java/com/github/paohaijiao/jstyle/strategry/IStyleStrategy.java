package com.github.paohaijiao.jstyle.strategry;

import com.github.paohaijiao.jstyle.model.JCellStyle;
import com.github.paohaijiao.jstyle.model.JRowStyle;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public interface IStyleStrategy {

    void applyStyle(Workbook workbook, Sheet sheet, Map<String, Object> style);

    default JRowStyle transferToJRowStyle(Map<String, Object> style) {
        if (style == null) {
            return null;
        }
        JRowStyle rowStyle = new JRowStyle();
        rowStyle.putAll(style);
        return rowStyle;
    }

    default JCellStyle transferToJCellStyle(Map<String, Object> style) {
        if (style == null) {
            return null;
        }

        JCellStyle rowStyle = new JCellStyle();
        rowStyle.putAll(style);
        return rowStyle;
    }
}
