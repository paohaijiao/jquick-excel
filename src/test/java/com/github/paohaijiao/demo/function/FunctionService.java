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
package com.github.paohaijiao.demo.function;

import com.github.paohaijiao.xml.param.Param;

/**
 * function 子包服务契约：FORMULAS 公式（单元格 / 行 / 列 / 行区间四类目标）。
 */
public interface FunctionService {

    /** 单元格 D5、整行 ROW 6、整列 COL E 三种目标各写一条公式。 */
    void exportFormulas(@Param("field") String field, @Param("value") String value);

    /** 行区间 ROW 5..10 批量写公式。 */
    void exportFormulasRowRange(@Param("field") String field, @Param("value") String value);
}
