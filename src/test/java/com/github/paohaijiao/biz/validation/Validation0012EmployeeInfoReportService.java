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
package com.github.paohaijiao.biz.validation;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 12：员工信息填报表（🟡 Java 预处理 + XML 统计 / 导入校验）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/validation/0012_validation_employee-info-report.xml} 中的
 * {@code <excel name=...>} 一一对应。导出侧统计由 FORMULAS 计算，
 * 导入侧规则校验由 VALIDATION 完成，Java 仅负责构造填报数据。
 */
public interface Validation0012EmployeeInfoReportService {

    /** 导出：员工信息填报表（末行统计人数 / 平均年龄 / 薪资合计）。 */
    void exportEmployeeInfo(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取填报结果，逐项校验后返回数据行。 */
    List<JQuickRow> importEmployeeInfo(@Param("field") String field, @Param("value") String value);
}
