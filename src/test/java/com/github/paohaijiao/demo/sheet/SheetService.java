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
 * sheet 子包服务契约：SHEET —— 导出指定输出工作表名，导入按名称 / 1 基索引选择工作表。
 *
 * <p>方法名与 {@code demo/sheet/jquick-excel.xml} 中的 {@code <excel name=...>} 一一对应。
 */
public interface SheetService {

    /** 导出：SHEET="学生信息" 指定输出工作表名称。 */
    void exportSheet(@Param("field") String field, @Param("value") String value);

    /** 导入：SHEET="学生信息" 按工作表名称选择。 */
    List<JQuickRow> importByName(@Param("field") String field, @Param("value") String value);

    /** 导入：SHEET=1 按 1 基索引选择。 */
    List<JQuickRow> importByIndex(@Param("field") String field, @Param("value") String value);

    /** 导入：SHEET="班级信息" 选择第二张工作表。 */
    List<JQuickRow> importClassSheet(@Param("field") String field, @Param("value") String value);
}
