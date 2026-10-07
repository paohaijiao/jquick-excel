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
package com.github.paohaijiao.demo.validation;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * validation 子包服务契约：校验通过返回数据行，校验失败直接抛异常（message 为规则 msg）。
 */
public interface ValidationService {

    /** 对夹具数据应通过的规则集。 */
    List<JQuickRow> importWithValidation(@Param("field") String field, @Param("value") String value);

    /** 学号长度上限 3 位的故意失败规则。 */
    List<JQuickRow> importWithValidationFail(@Param("field") String field, @Param("value") String value);
}
