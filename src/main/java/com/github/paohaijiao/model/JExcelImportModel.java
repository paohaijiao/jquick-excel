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

import com.github.paohaijiao.validate.JExcelValidationRule;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class JExcelImportModel {


    private Object sheet;

    private Boolean header = true;

    private String range = null;

    Map<String, String> mappings = new HashMap<>();

    Map<String, String> transforms = new HashMap<>();

    private Map<String, List<JExcelValidationRule>> rowValidate = new HashMap<>();

    private Map<String, List<JExcelValidationRule >> colValidate = new HashMap<>();

    private Map<String, List<JExcelValidationRule >> cellValidate = new HashMap<>();

    private Map<String, List<JExcelValidationRule >> rangeValidate = new HashMap<>();


}
