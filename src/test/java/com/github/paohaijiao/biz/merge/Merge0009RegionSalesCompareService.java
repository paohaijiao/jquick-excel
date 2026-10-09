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
 * 场景 9：区域销售对比（省 / 市维度切片，🟡 Java 预处理）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0009_merge_region-sales-compare.xml} 一一对应。
 * Java 只负责按大区分组、插入小计 / 合计占位行；金额求和由模板 FORMULAS 完成，
 * 大区名称列的纵向合并由模板 MERGE 声明。
 */
public interface Merge0009RegionSalesCompareService {

    /** 导出：区域销售对比（大区切片 + 城市明细 + 小计 / 合计）。 */
    void exportRegionSales(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的区域销售对比。 */
    List<JQuickRow> importRegionSales(@Param("field") String field, @Param("value") String value);
}
