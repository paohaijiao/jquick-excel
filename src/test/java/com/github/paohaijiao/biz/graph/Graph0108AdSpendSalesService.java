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
 * 场景 108：广告投产分析（🟢 纯 XML + SCATTER 散点图）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/graph/0108_graph_ad-spend-sales.xml} 中的
 * {@code <excel name=...>} 一一对应。投产比与末行汇总由模板 FORMULAS 计算，
 * 散点图由模板 GRAPH 声明，Java 仅负责构造模拟数据。
 */
public interface Graph0108AdSpendSalesService {

    /** 导出：广告投产分析（投入 / 产出 / 投产比 + 散点图）。 */
    void exportAdSpendSales(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的广告投产分析，按表头映射回字段。 */
    List<JQuickRow> importAdSpendSales(@Param("field") String field, @Param("value") String value);
}
