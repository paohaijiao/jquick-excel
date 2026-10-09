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
 * 场景 110：月度销量矩阵（🟢 纯 XML + SURFACE 曲面图）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/graph/0110_graph_monthly-product-matrix.xml} 中的
 * {@code <excel name=...>} 一一对应。末行按月份汇总由模板 FORMULAS 计算，
 * 曲面图由模板 GRAPH 声明，Java 仅负责构造模拟数据。
 */
public interface Graph0110MonthlyProductMatrixService {

    /** 导出：月度销量矩阵（产品 × 月份 + 末行汇总 + 曲面图）。 */
    void exportMonthlyProductMatrix(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的月度销量矩阵，按表头映射回字段。 */
    List<JQuickRow> importMonthlyProductMatrix(@Param("field") String field, @Param("value") String value);
}
