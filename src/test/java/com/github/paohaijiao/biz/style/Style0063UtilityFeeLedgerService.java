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
 * 场景 63：物业水电费收缴台账（费用票据类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0063_style_utility-fee-ledger.xml} 一一对应。
 * 欠缴金额、三项合计与欠缴户标红全部由 XML 模板完成，Java 只构造收缴数据。
 */
public interface Style0063UtilityFeeLedgerService {

    /** 导出：物业水电费收缴台账。 */
    void exportUtilityFeeLedger(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的收缴台账。 */
    List<JQuickRow> importUtilityFeeLedger(@Param("field") String field, @Param("value") String value);
}
