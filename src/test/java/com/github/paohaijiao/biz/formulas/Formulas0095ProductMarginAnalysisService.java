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
 * 场景 95：产品毛利贡献分析表（其他拓展类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0095_formulas_product-margin-analysis.xml} 一一对应。
 * 毛利、毛利率、贡献占比、各列合计与最低毛利率标红全部由 XML 模板完成，Java 只构造数据。
 */
public interface Formulas0095ProductMarginAnalysisService {

    /** 导出：产品毛利贡献分析表。 */
    void exportProductMarginAnalysis(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的产品毛利贡献分析表。 */
    List<JQuickRow> importProductMarginAnalysis(@Param("field") String field, @Param("value") String value);
}
