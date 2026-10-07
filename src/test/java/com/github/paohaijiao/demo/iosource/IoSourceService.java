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
package com.github.paohaijiao.demo.iosource;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * iosource 子包服务契约：同一套规则对任意 InputStream / OutputStream 生效。
 */
public interface IoSourceService {

    /** 从任意输入流导入。 */
    List<JQuickRow> importRows(@Param("field") String field, @Param("value") String value);

    /** 向任意输出流导出。 */
    void exportRows(@Param("field") String field, @Param("value") String value);
}
