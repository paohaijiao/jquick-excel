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
package com.github.paohaijiao.demo.bigdata;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 分类：<b>大数据量导入导出</b>。
 *
 * <p>方法名与 {@code demo/bigdata/jquick-excel.xml} 中的 {@code <excel name=...>} 一一对应。
 */
public interface BigDataService {

    /** 大数据量导出（达到阈值自动切 SXSSF 流式）。 */
    void exportLarge(@Param("field") String field, @Param("value") String value);

    /** 大数据量导入（XML 入口，全量返回）。 */
    List<JQuickRow> importLarge(@Param("field") String field, @Param("value") String value);
}
