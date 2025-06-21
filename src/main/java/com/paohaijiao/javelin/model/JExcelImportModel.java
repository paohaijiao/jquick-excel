package com.paohaijiao.javelin.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JExcelImportModel {

    private String fileName;

    private String destination;

    private Object sheet;

    private Boolean header=true;

    private String range=null;

    Map<String, String> mappings = new HashMap<>();

    Map<String, String> transforms = new HashMap<>();


}
