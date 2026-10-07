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
 * style 子包服务契约：行样式 + 矩形区域样式。
 */
public interface StyleService {

    /** ROW 1 表头样式；A2:C100 数据区域居中、自动换行、下边框、浅色填充。 */
    void exportStyle(@Param("field") String field, @Param("value") String value);
}
