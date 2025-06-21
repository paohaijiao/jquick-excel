package com.paohaijiao.javelin.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JDateConverter implements JCustomConverter<Date>{
    private   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public JDateConverter(){

    }
    public JDateConverter(SimpleDateFormat sdf){
        this.sdf=sdf;
    }
    public JDateConverter(String format){
        this.sdf= new SimpleDateFormat(format);
    }

    @Override
    public Date convert(String value) {
        try {
            return sdf.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String reverseConvert(Date value) {
        return value != null ? sdf.format(value) : "";
    }
}
