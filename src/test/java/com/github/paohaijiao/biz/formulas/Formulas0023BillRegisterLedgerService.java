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
 * 场景 23：票据登记台账（费用票据类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0023_formulas_bill-register-ledger.xml} 一一对应。
 * 票据期限、票面金额合计与样式高亮全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Formulas0023BillRegisterLedgerService {

    /** 导出：票据登记台账。 */
    void exportBillRegister(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的票据登记台账。 */
    List<JQuickRow> importBillRegister(@Param("field") String field, @Param("value") String value);
}
