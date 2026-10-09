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
 * 场景 72：来料检验不合格记录表（质检缺陷类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0072_style_incoming-inspection-defect.xml} 一一对应。
 * 不合格率、数量汇总与超标标红全部由 XML 模板完成，Java 只构造检验记录数据。
 */
public interface Style0072IncomingInspectionDefectService {

    /** 导出：来料检验不合格记录表。 */
    void exportIncomingInspectionDefect(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的来料检验记录。 */
    List<JQuickRow> importIncomingInspectionDefect(@Param("field") String field, @Param("value") String value);
}
