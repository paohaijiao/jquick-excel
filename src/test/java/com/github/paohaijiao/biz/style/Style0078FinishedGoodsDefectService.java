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
package com.github.paohaijiao.biz.style;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 78：成品出厂检验缺陷统计表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0078_style_finished-goods-defect.xml} 一一对应。
 * 逐行缺陷率、合计与超标标红全部由 XML 模板完成，Java 只构造检验数据。
 */
public interface Style0078FinishedGoodsDefectService {

    /** 导出：成品出厂检验缺陷统计表。 */
    void exportFinishedGoodsDefect(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的成品出厂检验缺陷统计表。 */
    List<JQuickRow> importFinishedGoodsDefect(@Param("field") String field, @Param("value") String value);
}
