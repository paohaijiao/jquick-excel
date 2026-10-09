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
package com.github.paohaijiao.biz.merge;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 104：分组销售聚合（大区切片 + 季度矩阵，🟡 Java 仅分组）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0104_merge_group-sales-aggregate.xml} 中的
 * {@code <excel name=...>} 一一对应。大区名称列的纵向合并范围由模板 MERGE 声明，
 * Java 侧只负责按大区排序分组并补齐全部映射列。
 */
public interface Merge0104GroupSalesAggregateService {

    /** 导出：分组销售聚合（大区名称列纵向合并）。 */
    void exportGroupSalesAggregate(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的分组销售聚合。 */
    List<JQuickRow> importGroupSalesAggregate(@Param("field") String field, @Param("value") String value);
}
