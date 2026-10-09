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
package com.github.paohaijiao.biz.merge;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 47：售后服务工单台账（其他拓展类，🟡 少量 Java 预处理）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0047_merge_after-sales-order.xml} 一一对应。
 * Java 只做分组与占位行插入，服务费、小计 / 合计与样式由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Merge0047AfterSalesOrderService {

    /** 导出：售后服务工单台账。 */
    void exportAfterSalesOrder(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的售后服务工单台账。 */
    List<JQuickRow> importAfterSalesOrder(@Param("field") String field, @Param("value") String value);
}
