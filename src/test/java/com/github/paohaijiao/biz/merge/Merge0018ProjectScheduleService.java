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
 * 场景 18：项目进度计划排期表（计划排期类，🟡 Java 预处理）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0018_merge_project-schedule.xml} 一一对应。
 * Java 只负责按阶段分组、插入小计 / 合计占位行；工期与完成率统计由模板 FORMULAS 计算，
 * 阶段名称列的纵向合并由模板 MERGE 声明。
 */
public interface Merge0018ProjectScheduleService {

    /** 导出：项目进度计划排期表（阶段分组 + 任务 + 工期小计 / 合计）。 */
    void exportProjectSchedule(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的项目进度计划排期表。 */
    List<JQuickRow> importProjectSchedule(@Param("field") String field, @Param("value") String value);
}
