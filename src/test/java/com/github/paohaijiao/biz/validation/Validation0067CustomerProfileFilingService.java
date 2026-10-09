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
package com.github.paohaijiao.biz.validation;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 67：客户档案信息补录填报表（数据校验填报类，🟢 纯 XML + 导入校验）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/validation/0067_validation_customer-profile-filing.xml} 一一对应。
 * 年采购额合计与合计行高亮由导出侧 XML 完成，填报内容的规则校验由导入侧 VALIDATION 完成。
 */
public interface Validation0067CustomerProfileFilingService {

    /** 导出：客户档案信息补录填报表。 */
    void exportCustomerProfileFiling(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取补录结果并逐项校验。 */
    List<JQuickRow> importCustomerProfileFiling(@Param("field") String field, @Param("value") String value);
}
