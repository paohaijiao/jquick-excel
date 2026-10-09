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
 * 场景 4：现金流量表导出（🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0004_formulas_cash-flow-statement.xml} 一一对应。
 * 净额 = 流入 - 流出、合计行汇总均由模板 FORMULAS 计算。
 */
public interface Formulas0004CashFlowStatementService {

    /** 导出：现金流量表（逐行净额 = 流入 - 流出，末行合计）。 */
    void exportCashFlow(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的现金流量表。 */
    List<JQuickRow> importCashFlow(@Param("field") String field, @Param("value") String value);
}
