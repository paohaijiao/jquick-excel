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
 * 场景 91：差旅补贴发放明细表（费用票据类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0091_formulas_travel-allowance-detail.xml} 一一对应。
 * 补贴合计、各列汇总与合计行高亮全部由 XML 模板完成，Java 只构造差旅数据。
 */
public interface Formulas0091TravelAllowanceDetailService {

    /** 导出：差旅补贴发放明细表。 */
    void exportTravelAllowanceDetail(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的差旅补贴发放明细表。 */
    List<JQuickRow> importTravelAllowanceDetail(@Param("field") String field, @Param("value") String value);
}
