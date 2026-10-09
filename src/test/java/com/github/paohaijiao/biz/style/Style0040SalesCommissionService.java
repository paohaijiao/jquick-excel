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
 * 场景 40：销售提成计算表（绩效考核类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0040_style_sales-commission.xml} 一一对应。
 * 达成率、提成金额、合计行与未达成标红全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Style0040SalesCommissionService {

    /** 导出：销售提成计算表。 */
    void exportSalesCommission(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的销售提成计算表。 */
    List<JQuickRow> importSalesCommission(@Param("field") String field, @Param("value") String value);
}
