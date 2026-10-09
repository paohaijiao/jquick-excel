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
 * 场景 59：生产设备 OEE 分析表（绩效考核类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0059_style_equipment-oee-analysis.xml} 一一对应。
 * OEE 连乘、平均 OEE 与样式全部由 XML 模板完成，Java 只构造设备数据。
 */
public interface Style0059EquipmentOeeAnalysisService {

    /** 导出：生产设备 OEE 分析表。 */
    void exportEquipmentOeeAnalysis(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的生产设备 OEE 分析表。 */
    List<JQuickRow> importEquipmentOeeAnalysis(@Param("field") String field, @Param("value") String value);
}
