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
package com.github.paohaijiao.demo.formulas;

import com.github.paohaijiao.xml.param.Param;

/**
 * 分类：<b>FORMULAS</b> —— 单元格公式写入。
 *
 * <p>方法名与 {@code demo/formulas/jquick-excel.xml} 中的 {@code <excel name=...>} 一一对应。
 */
public interface FormulaService {

    /** E2/E3/E4 单格 SUM、ROW 6 整行公式、COL F 整列公式。 */
    void exportFormula(@Param("field") String field, @Param("value") String value);
}
