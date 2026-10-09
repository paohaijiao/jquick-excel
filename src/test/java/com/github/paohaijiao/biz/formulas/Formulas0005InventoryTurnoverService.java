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
package com.github.paohaijiao.biz.formulas;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 5：库存周转报表（🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0005_formulas_inventory-turnover.xml} 一一对应。
 * 周转率 = 出库次数 / 期末库存，合计行用 SUM / AVERAGE 汇总，均由模板 FORMULAS 计算。
 */
public interface Formulas0005InventoryTurnoverService {

    /** 导出：库存周转报表（逐行周转率，末行汇总次数 / 库存 / 平均库龄）。 */
    void exportInventoryTurnover(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的库存周转报表。 */
    List<JQuickRow> importInventoryTurnover(@Param("field") String field, @Param("value") String value);
}
