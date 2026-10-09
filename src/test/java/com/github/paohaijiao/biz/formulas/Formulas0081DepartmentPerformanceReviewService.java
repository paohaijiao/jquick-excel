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
 * 场景 81：部门季度绩效考核汇总表（绩效考核类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0081_formulas_department-performance-review.xml} 一一对应。
 * 加权综合得分、平均分与最低分标红全部由 XML 模板完成，Java 只构造考核数据。
 */
public interface Formulas0081DepartmentPerformanceReviewService {

    /** 导出：部门季度绩效考核汇总表。 */
    void exportDepartmentPerformanceReview(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的部门季度绩效考核汇总表。 */
    List<JQuickRow> importDepartmentPerformanceReview(@Param("field") String field, @Param("value") String value);
}
