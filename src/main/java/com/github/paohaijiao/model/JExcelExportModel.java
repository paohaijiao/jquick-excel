/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) [2025-2099] Martin (goudingcheng@gmail.com)
 */
package com.github.paohaijiao.model;

import com.github.paohaijiao.graph.model.JChartData;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JExcelExportModel {

    private String source;

    private String outputFile;

    private Object sheet;

    private Boolean header = true;

    private String range = null;

    Map<String, String> format = new HashMap<>();

    Map<String, String> cellFormulas = new HashMap<>();

    Map<String, String> rowFormulas = new HashMap<>();

    Map<String, String> colFormulas = new HashMap<>();

    Map<String, String> mapping = new HashMap<>();

    Map<String, String> transforms = new HashMap<>();

    Map<String, Map<String, Object>> rowStyles = new HashMap<>();

    Map<String, Map<String, Object>> colStyles = new HashMap<>();

    Map<String, Map<String, Object>> cellStyles = new HashMap<>();

    Map<String, Map<String, Object>> rangeStyles = new HashMap<>();

    Map<String, Object> rowMerge = new HashMap<>();

    Map<String, Object> colMerge = new HashMap<>();

    Map<String, Object> rangeMerge = new HashMap<>();

    JChartData graph =new JChartData();

}
