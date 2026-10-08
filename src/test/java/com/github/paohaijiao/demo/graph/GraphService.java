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
package com.github.paohaijiao.demo.graph;

import com.github.paohaijiao.xml.param.Param;

/**
 * graph 子包服务契约：声明式图表导出（柱形 / 折线 / 饼图）。
 */
public interface GraphService {

    /** TYPE=COLUMN 柱形图。 */
    void exportColumnChart(@Param("field") String field, @Param("value") String value);

    /** TYPE=LINE 折线图。 */
    void exportLineChart(@Param("field") String field, @Param("value") String value);

    /** TYPE=PIE 饼图。 */
    void exportPieChart(@Param("field") String field, @Param("value") String value);
}
