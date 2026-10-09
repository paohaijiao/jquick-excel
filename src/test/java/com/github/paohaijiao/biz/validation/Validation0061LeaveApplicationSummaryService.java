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
 * 场景 61：员工请假申请汇总表（数据校验填报类，🟢 纯 XML + 导入校验）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/validation/0061_validation_leave-application-summary.xml} 一一对应。
 * 请假总天数与合计行高亮由 XML 模板完成；导入侧使用 VALIDATION 做填报规则校验。
 */
public interface Validation0061LeaveApplicationSummaryService {

    /** 导出：员工请假申请汇总表。 */
    void exportLeaveApplicationSummary(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取填报的请假申请并逐项校验。 */
    List<JQuickRow> importLeaveApplicationSummary(@Param("field") String field, @Param("value") String value);
}
