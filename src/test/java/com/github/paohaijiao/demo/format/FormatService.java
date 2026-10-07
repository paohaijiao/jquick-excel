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
package com.github.paohaijiao.demo.format;

import com.github.paohaijiao.xml.param.Param;

/**
 * format 子包服务契约：Excel 原生数字格式码导出。
 */
public interface FormatService {

    /** 000000 补零 / 千分位 / 人民币 / 百分比 / 日期五种格式。 */
    void exportFormat(@Param("field") String field, @Param("value") String value);
}
