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
 * 场景 13：费用报销单（🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0013_merge_expense-reimbursement.xml} 中的
 * {@code <excel name=...>} 一一对应。可报销金额与合计由模板 FORMULAS 计算，
 * 合计行标签横向合并由模板 MERGE 完成，Java 仅负责构造模拟数据。
 */
public interface Merge0013ExpenseReimbursementService {

    /** 导出：费用报销单（行内「金额 - 自付 = 可报销」，末行合计）。 */
    void exportExpenseReimbursement(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的费用报销单，按表头映射回字段。 */
    List<JQuickRow> importExpenseReimbursement(@Param("field") String field, @Param("value") String value);
}
