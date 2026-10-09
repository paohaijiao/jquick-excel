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
 * 场景 43：员工培训档案登记（档案名册类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/formulas/0043_formulas_training-record.xml} 一一对应。
 * 总学时、平均成绩、合计行与不及格标红全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Formulas0043TrainingRecordService {

    /** 导出：员工培训档案登记。 */
    void exportTrainingRecord(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的员工培训档案登记。 */
    List<JQuickRow> importTrainingRecord(@Param("field") String field, @Param("value") String value);
}
