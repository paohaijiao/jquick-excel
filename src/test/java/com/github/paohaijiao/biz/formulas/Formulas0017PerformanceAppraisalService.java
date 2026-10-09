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
 * 场景 17：员工绩效考核评分表（绩效考核类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0017_formulas_performance-appraisal.xml} 一一对应。
 * 加权综合得分、各维度平均分以及「待改进」标红全部由 XML 模板完成。
 */
public interface Formulas0017PerformanceAppraisalService {

    /** 导出：员工绩效考核评分表（加权得分 + 平均分 + 待改进标红）。 */
    void exportPerformanceAppraisal(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的员工绩效考核评分表。 */
    List<JQuickRow> importPerformanceAppraisal(@Param("field") String field, @Param("value") String value);
}
