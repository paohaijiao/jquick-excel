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
 * 场景 6：客户分层报表（🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0006_formulas_customer-tier-report.xml} 一一对应。
 * 贡献占比与合计全部由模板 FORMULAS 计算，Java 仅构造模拟数据。
 */
public interface Formulas0006CustomerTierReportService {

    /** 导出：客户分层报表（A/B/C 分层明细 + 贡献占比 + 合计）。 */
    void exportCustomerTier(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的客户分层报表。 */
    List<JQuickRow> importCustomerTier(@Param("field") String field, @Param("value") String value);
}
