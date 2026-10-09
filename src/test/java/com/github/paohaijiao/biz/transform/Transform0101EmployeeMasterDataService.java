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
package com.github.paohaijiao.biz.transform;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 场景 101：员工主数据码值转换表（🟢 纯 XML + TRANSFORM 字典）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/transform/0101_transform_employee-master-data.xml} 中的
 * {@code <excel name=...>} 一一对应。性别 / 部门 / 状态的中文转换由模板 TRANSFORM 用
 * {@code trans(${dict}, ...)} 完成，字典经 {@code JContext} 注入。
 */
public interface Transform0101EmployeeMasterDataService {

    /** 导出：员工主数据（内部码值按字典转为中文）。 */
    void exportEmployeeMasterData(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的员工主数据，按表头映射回字段。 */
    List<JQuickRow> importEmployeeMasterData(@Param("field") String field, @Param("value") String value);
}
