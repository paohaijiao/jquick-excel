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
 * 场景 52：客户拜访记录台账（其他拓展类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0052_style_customer-visit-record.xml} 一一对应。
 * 拜访总时长、合计行高亮与待跟进标红全部由 XML 模板完成，Java 只提供拜访记录数据。
 */
public interface Style0052CustomerVisitRecordService {

    /** 导出：客户拜访记录台账。 */
    void exportCustomerVisitRecord(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的客户拜访记录台账。 */
    List<JQuickRow> importCustomerVisitRecord(@Param("field") String field, @Param("value") String value);
}
