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
package com.github.paohaijiao.biz.transform;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 103：培训考核成绩等级表（🟢 纯 XML + TRANSFORM SPI 函数）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/transform/0103_transform_training-score-grade.xml} 中的
 * {@code <excel name=...>} 一一对应。分数转等级由 SPI 函数 {@code grade} 完成，
 * 考核日期归一由 {@code dateFormat} 完成。
 */
public interface Transform0103TrainingScoreGradeService {

    /** 导出：培训考核成绩等级表（分数换算为等级）。 */
    void exportTrainingScoreGrade(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的培训考核成绩等级表，按表头映射回字段。 */
    List<JQuickRow> importTrainingScoreGrade(@Param("field") String field, @Param("value") String value);
}
