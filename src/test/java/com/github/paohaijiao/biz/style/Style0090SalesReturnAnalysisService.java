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
 * 场景 90：销售退货分析表（其他拓展类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0090_style_sales-return-analysis.xml} 一一对应。
 * 退货金额、退货率、各列合计与最高退货率标红全部由 XML 模板完成，Java 只构造数据。
 */
public interface Style0090SalesReturnAnalysisService {

    /** 导出：销售退货分析表。 */
    void exportSalesReturnAnalysis(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的销售退货分析表。 */
    List<JQuickRow> importSalesReturnAnalysis(@Param("field") String field, @Param("value") String value);
}
