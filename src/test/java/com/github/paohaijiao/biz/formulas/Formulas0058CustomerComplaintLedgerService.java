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
 * 场景 58：客户投诉处理台账（其他拓展类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0058_formulas_customer-complaint-ledger.xml} 一一对应。
 * 平均处理天数、合计行高亮与超期标红全部由 XML 模板完成，Java 只提供投诉记录数据。
 */
public interface Formulas0058CustomerComplaintLedgerService {

    /** 导出：客户投诉处理台账。 */
    void exportCustomerComplaintLedger(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的客户投诉处理台账。 */
    List<JQuickRow> importCustomerComplaintLedger(@Param("field") String field, @Param("value") String value);
}
