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
package com.github.paohaijiao.demo.transform;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 分类：<b>TRANSFORM</b> —— 值转换表达式。
 *
 * <p>导入方向是「Excel 文字 → 内部值」，导出方向是「内部值 → 单元格文字」，
 * 同一个 {@code TRANSFORM} 关键字在两侧语义相反。方法名与
 * {@code demo/transform/jquick-excel.xml} 中的 {@code <excel name=...>} 一一对应。
 */
public interface TransformService {

    /** 导入：性别字典反查 + 姓名大写 + 出生日期归一。 */
    List<JQuickRow> importTransform(@Param("field") String field, @Param("value") String value);

    /** 导出：姓名大写 + 性别码值转中文 + 年龄加 1。 */
    void exportTransform(@Param("field") String field, @Param("value") String value);
}
