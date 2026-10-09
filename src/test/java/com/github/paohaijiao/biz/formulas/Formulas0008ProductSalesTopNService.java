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
 * 场景 8：产品销量 Top N 排行（🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0008_formulas_product-sales-topn.xml} 一一对应。
 * 销售占比与合计全部由模板 FORMULAS 计算，Java 仅提供已排序的模拟数据与静态序号。
 */
public interface Formulas0008ProductSalesTopNService {

    /** 导出：产品销量 Top N 排行（排名 + 销量 + 销售额 + 占比 + 合计）。 */
    void exportProductSalesTopN(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的产品销量排行。 */
    List<JQuickRow> importProductSalesTopN(@Param("field") String field, @Param("value") String value);
}
