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
package com.github.paohaijiao.biz.merge;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 3：部门费用统计表（🟡 Java 仅分组 + 插小计行）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0003_merge_department-expense-stat.xml} 一一对应。
 * Java 只负责按部门分组并插入小计 / 合计占位行，金额求和仍由模板 FORMULAS 完成。
 */
public interface Merge0003DepartmentExpenseStatService {

    /** 导出：部门费用统计表（每部门一小计，末行合计）。 */
    void exportDepartmentExpense(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的部门费用统计表。 */
    List<JQuickRow> importDepartmentExpense(@Param("field") String field, @Param("value") String value);
}
