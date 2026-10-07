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
package com.github.paohaijiao.demo.basic;

import com.github.paohaijiao.xml.param.Param;

/**
 * basic 子包服务契约：基础导出。
 */
public interface BasicService {

    /** 字段 a/b/c 经 MAPPING 转成「姓名/性别/年龄」表头，性别码值经字典转中文。 */
    void exportBasic(@Param("field") String field, @Param("value") String value);
}
