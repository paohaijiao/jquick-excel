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
package com.github.paohaijiao.biz.graph;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 107：库存量趋势（🟢 纯 XML + AREA3D 三维面积图）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/graph/0107_graph_inventory-trend.xml} 中的
 * {@code <excel name=...>} 一一对应。滚动期末与末行汇总由模板 FORMULAS 计算，
 * 三维面积图由模板 GRAPH 声明，Java 仅负责构造模拟数据。
 */
public interface Graph0107InventoryTrendService {

    /** 导出：库存量趋势（期初 / 入库 / 出库 / 滚动期末 + 三维面积图）。 */
    void exportInventoryTrend(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的库存量趋势，按表头映射回字段。 */
    List<JQuickRow> importInventoryTrend(@Param("field") String field, @Param("value") String value);
}
