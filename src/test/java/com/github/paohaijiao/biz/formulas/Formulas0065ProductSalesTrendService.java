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
 * 场景 65：产品销售月度趋势表（自助报表类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0065_formulas_product-sales-trend.xml} 一一对应。
 * 销售额、环比增长率与合计全部由 XML 模板完成，Java 只构造月度趋势数据。
 */
public interface Formulas0065ProductSalesTrendService {

    /** 导出：产品销售月度趋势表。 */
    void exportProductSalesTrend(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的月度趋势表。 */
    List<JQuickRow> importProductSalesTrend(@Param("field") String field, @Param("value") String value);
}
