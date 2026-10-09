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
package com.github.paohaijiao.biz.merge;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 68：合同履行台账（台账汇总类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0068_merge_contract-performance-ledger.xml} 一一对应。
 * 履行率、分段小计与合计由 XML 模板完成，Java 只做分组与占位行。
 */
public interface Merge0068ContractPerformanceLedgerService {

    /** 导出：合同履行台账。 */
    void exportContractPerformanceLedger(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的合同履行台账。 */
    List<JQuickRow> importContractPerformanceLedger(@Param("field") String field, @Param("value") String value);
}
