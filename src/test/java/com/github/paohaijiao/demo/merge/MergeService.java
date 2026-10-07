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
package com.github.paohaijiao.demo.merge;

import com.github.paohaijiao.xml.param.Param;

/**
 * merge 子包服务契约：行合并与列合并（分别导出，避免合并区域重叠）。
 */
public interface MergeService {

    /** ROWS：同一行跨列聚合。 */
    void mergeRows(@Param("field") String field, @Param("value") String value);

    /** COLS：同一列跨行聚合。 */
    void mergeCols(@Param("field") String field, @Param("value") String value);
}
