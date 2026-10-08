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
package com.github.paohaijiao.demo.header;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * header 子包服务契约：HEADER 表头开关。
 *
 * <p>方法名与 {@code demo/header/jquick-excel.xml} 中的 {@code <excel name=...>} 一一对应。
 */
public interface HeaderService {

    /** 导出：HEADER=true 写出表头行。 */
    void exportWithHeader(@Param("field") String field, @Param("value") String value);

    /** 导出：HEADER=false 不写表头行。 */
    void exportWithoutHeader(@Param("field") String field, @Param("value") String value);

    /** 导入：HEADER=true 跳过第 1 行表头。 */
    List<JQuickRow> importWithHeader(@Param("field") String field, @Param("value") String value);

    /** 导入：HEADER=false 把第 1 行也当作数据行。 */
    List<JQuickRow> importWithoutHeader(@Param("field") String field, @Param("value") String value);
}
