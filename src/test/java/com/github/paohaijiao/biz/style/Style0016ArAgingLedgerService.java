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
 * 场景 16：应收账款账龄台账（台账汇总类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0016_style_ar-aging-ledger.xml} 一一对应。
 * 未收余额、占比、合计以及异常单元格标红全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Style0016ArAgingLedgerService {

    /** 导出：应收账款账龄台账（含账龄异常标红）。 */
    void exportArAgingLedger(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的应收账款账龄台账。 */
    List<JQuickRow> importArAgingLedger(@Param("field") String field, @Param("value") String value);
}
