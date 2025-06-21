package com.paohaijiao.javelin.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JExcelExportModel {

    private String source;

    private String outputFile;

    private Object sheet;

    private Boolean header=true;

    private String range=null;

    Map<String, String> format = new HashMap<>();

    Map<String, String> formulas = new HashMap<>();

    Map<String, String> mapping = new HashMap<>();

    Map<String, String> transforms = new HashMap<>();

}
