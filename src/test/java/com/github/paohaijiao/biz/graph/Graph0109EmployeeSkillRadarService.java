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
package com.github.paohaijiao.biz.graph;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 109：员工能力评估（🟢 纯 XML + RADAR 雷达图）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/graph/0109_graph_employee-skill-radar.xml} 中的
 * {@code <excel name=...>} 一一对应。各维度评分与末行平均分由模板 FORMULAS 计算，
 * 雷达图由模板 GRAPH 声明，Java 仅负责构造模拟数据。
 */
public interface Graph0109EmployeeSkillRadarService {

    /** 导出：员工能力评估（多维评分 + 平均分 + 雷达图）。 */
    void exportEmployeeSkillRadar(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的员工能力评估，按表头映射回字段。 */
    List<JQuickRow> importEmployeeSkillRadar(@Param("field") String field, @Param("value") String value);
}
