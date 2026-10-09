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
 * 场景 45：采购价格对比分析表（数据对比差异类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0045_formulas_purchase-price-compare.xml} 一一对应。
 * 价差、价差率、均价行与高于基准价的标红全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Formulas0045PurchasePriceCompareService {

    /** 导出：采购价格对比分析表。 */
    void exportPurchasePriceCompare(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的采购价格对比分析表。 */
    List<JQuickRow> importPurchasePriceCompare(@Param("field") String field, @Param("value") String value);
}
