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
 * 场景 94：工程项目进度款支付计划表（计划排期类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0094_style_project-payment-plan.xml} 一一对应。
 * 应付进度款、待付金额、各列汇总与待付最大节点标红全部由 XML 模板完成，Java 只构造数据。
 */
public interface Style0094ProjectPaymentPlanService {

    /** 导出：工程项目进度款支付计划表。 */
    void exportProjectPaymentPlan(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的工程项目进度款支付计划表。 */
    List<JQuickRow> importProjectPaymentPlan(@Param("field") String field, @Param("value") String value);
}
