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
 * 场景 24：增值税税负分析表（费用票据类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0024_style_vat-burden-analysis.xml} 一一对应。
 * 应纳增值税、税负率、合计行与高税负标红全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Style0024VatBurdenAnalysisService {

    /** 导出：增值税税负分析表（含高税负标红）。 */
    void exportVatBurden(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的增值税税负分析表。 */
    List<JQuickRow> importVatBurden(@Param("field") String field, @Param("value") String value);
}
