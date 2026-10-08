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
package com.github.paohaijiao.demo.template;

import com.github.paohaijiao.xml.param.Param;

/**
 * 分类：<b>导出模板</b> —— 主题模板。
 *
 * <p>方法名与 {@code demo/template/jquick-excel.xml} 中的 {@code <excel name=...>} 一一对应；
 * 主题编码由 {@code JQuickExcelExportXmlParseFactory} 的构造参数传入，不在 XML 内。
 */
public interface TemplateService {

    /** 用指定主题模板导出。 */
    void exportTheme(@Param("field") String field, @Param("value") String value);
}
