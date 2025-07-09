package com.github.paohaijiao.jstyle.strategry.impl;

import com.github.paohaijiao.color.JColorEnum;
import com.github.paohaijiao.font.JFont;
import com.github.paohaijiao.font.JFontUnderline;
import com.github.paohaijiao.jstyle.model.JRowStyle;
import com.github.paohaijiao.jstyle.strategry.IStyleStrategy;
import com.github.paohaijiao.util.JStyleHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import sun.plugin2.message.ShowDocumentMessage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class JFontBuilder  {


    protected void buildFont( CellStyle cellStyle,Font font, HashMap<String, Object> map){
        if(null==font||null==map||map.size()==0){
            return;
        }
        if(map.get(JRowStyle.bold)==null){
            font.setBold(true);
        }
        if(map.get(JRowStyle.fontName)==null){
            String fontName=(String)map.get(JRowStyle.fontName);
            font.setFontName(fontName);
        }
        if(null!=map.get(JRowStyle.fontHeightInPoints)){
            Short fontHeightInPoints=(Short)map.get(JRowStyle.fontHeightInPoints);
            font.setFontHeightInPoints(fontHeightInPoints);
        }
        if(null!=map.get(JRowStyle.fontHeight)){
            Short fontHeight=(Short)map.get(JRowStyle.fontHeight);
            font.setFontHeight(fontHeight);
        }
        if(null!=map.get(JRowStyle.bold)){
            Boolean bold=(Boolean)map.get(JRowStyle.bold);
            font.setBold(bold);
        }
        if(null!=map.get(JRowStyle.italic)){
            Boolean italic=(Boolean)map.get(JRowStyle.italic);
            font.setItalic(italic);
        }
        if(null!=map.get(JRowStyle.underLine)){
            String type=(String)map.get(JRowStyle.underLine);
            JFont jFont=JFont.nameOf(type);
            font.setUnderline(jFont.getCode().byteValue());
        }
        if(null!=map.get(JRowStyle.color)){
            String color=(String)map.get(JRowStyle.color);
            JColorEnum colorEnum= JColorEnum.codeOf(color);
            font.setColor(colorEnum.getIndexedColor().getIndex());
        }
        if(null!=map.get(JRowStyle.strikeout)){
            Boolean strikeout=(Boolean)map.get(JRowStyle.strikeout);
            font.setStrikeout(strikeout);
        }
        if(null!=map.get(JRowStyle.underLine)){
            String underLine=(String)map.get(JRowStyle.underLine);
            FontUnderline fontUnderline=JFontUnderline.codeOf(underLine);
            font.setUnderline(fontUnderline.getByteValue());
        }
        cellStyle.setFont(font);

    }

}
