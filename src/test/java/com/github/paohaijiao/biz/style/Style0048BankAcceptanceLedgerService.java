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
package com.github.paohaijiao.biz.style;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 48：银行承兑汇票台账（台账汇总类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0048_style_bank-acceptance-ledger.xml} 一一对应。
 * 票面金额合计、合计行高亮与逾期标红全部由 XML 模板完成，Java 只提供原始台账数据。
 */
public interface Style0048BankAcceptanceLedgerService {

    /** 导出：银行承兑汇票台账。 */
    void exportBankAcceptanceLedger(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的银行承兑汇票台账。 */
    List<JQuickRow> importBankAcceptanceLedger(@Param("field") String field, @Param("value") String value);
}
