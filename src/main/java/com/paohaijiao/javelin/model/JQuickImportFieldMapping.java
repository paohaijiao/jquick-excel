package com.paohaijiao.javelin.model;

import lombok.Data;

@Data
public class JQuickImportFieldMapping {

    private String fieldName;

    private String mappingFieldName;
    private Object functionCall;
    private Object jsonPath;

}
