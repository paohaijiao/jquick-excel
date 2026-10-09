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
 * 场景 83：设备点检记录填报表（数据校验填报类，🟢 纯 XML + 导入校验）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/validation/0054_validation_equipment-inspection-record.xml} 一一对应。
 * 运行时长合计由 XML 模板完成，导入侧的字段校验由 {@code VALIDATION} 完成，Java 只构造数据。
 */
public interface Validation0054EquipmentInspectionRecordService {

    /** 导出：设备点检记录填报表。 */
    void exportEquipmentInspectionRecord(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取并校验设备点检记录填报表。 */
    List<JQuickRow> importEquipmentInspectionRecord(@Param("field") String field, @Param("value") String value);
}
