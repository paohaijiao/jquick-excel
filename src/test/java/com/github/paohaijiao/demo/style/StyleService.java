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
package com.github.paohaijiao.demo.style;

import com.github.paohaijiao.xml.param.Param;

/**
 * style 子包服务契约：行 / 列 / 单元格 / 区域四类样式目标。
 */
public interface StyleService {

    /** ROW 1 行样式；COL B 列样式；C2 单元格样式；A2:A4 区域样式。 */
    void exportStyle(@Param("field") String field, @Param("value") String value);
}
