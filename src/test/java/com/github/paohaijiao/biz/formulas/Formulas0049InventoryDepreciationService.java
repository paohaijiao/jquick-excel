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
 * 场景 49：存货跌价准备计提表（台账汇总类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0049_formulas_inventory-depreciation.xml} 一一对应。
 * 账面成本、跌价准备、合计与样式全部由 XML 模板完成，Java 只提供原始存货数据。
 */
public interface Formulas0049InventoryDepreciationService {

    /** 导出：存货跌价准备计提表。 */
    void exportInventoryDepreciation(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的存货跌价准备计提表。 */
    List<JQuickRow> importInventoryDepreciation(@Param("field") String field, @Param("value") String value);
}
