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
 * 场景 34：销售订单执行跟踪表（其他拓展类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0034_style_sales-order-tracking.xml} 一一对应。
 * 未发货金额、执行率、合计行与未发货标红全部由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Style0034SalesOrderTrackingService {

    /** 导出：销售订单执行跟踪表（含未发货订单标红）。 */
    void exportSalesOrderTracking(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的销售订单执行跟踪表。 */
    List<JQuickRow> importSalesOrderTracking(@Param("field") String field, @Param("value") String value);
}
