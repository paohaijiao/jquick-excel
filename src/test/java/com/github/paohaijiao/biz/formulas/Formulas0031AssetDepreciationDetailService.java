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
 * 场景 31：固定资产折旧明细表（台账汇总类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0031_formulas_asset-depreciation-detail.xml} 一一对应。
 * 年 / 月折旧额、合计行与样式高亮全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Formulas0031AssetDepreciationDetailService {

    /** 导出：固定资产折旧明细表。 */
    void exportAssetDepreciation(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的固定资产折旧明细表。 */
    List<JQuickRow> importAssetDepreciation(@Param("field") String field, @Param("value") String value);
}
