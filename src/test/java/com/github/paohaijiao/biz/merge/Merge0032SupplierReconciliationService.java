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
 * 场景 32：供应商往来对账表（审计对账类，🟡 Java 仅分组 + 插小计行）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0032_merge_supplier-reconciliation.xml} 一一对应。
 * Java 只按供应商分组并插入小计 / 合计占位行，差异金额与样式高亮由 XML 模板完成。
 */
public interface Merge0032SupplierReconciliationService {

    /** 导出：供应商往来对账表（按供应商分组、带小计与供应商纵向合并）。 */
    void exportSupplierReconciliation(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的供应商往来对账表。 */
    List<JQuickRow> importSupplierReconciliation(@Param("field") String field, @Param("value") String value);
}
