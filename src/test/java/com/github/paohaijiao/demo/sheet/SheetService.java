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
package com.github.paohaijiao.demo.sheet;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * sheet 子包服务契约：方法名与 demo/sheet/jquick-excel.xml 中的 name 一一对应。
 */
public interface SheetService {

    /** SHEET="学生信息"：按名称选择工作表。 */
    List<JQuickRow> importByName(@Param("field") String field, @Param("value") String value);

    /** SHEET=1：按 1 基索引选择工作表。 */
    List<JQuickRow> importByIndex(@Param("field") String field, @Param("value") String value);
}
