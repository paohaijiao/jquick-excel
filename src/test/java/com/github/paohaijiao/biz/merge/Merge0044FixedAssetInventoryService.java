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
 * 场景 44：固定资产盘点表（库存盘点类，🟡 少量 Java 预处理）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/merge/0044_merge_fixed-asset-inventory.xml} 一一对应。
 * Java 只做分组与占位行插入，盘盈盘亏、小计 / 合计与样式由 XML 模板的 FORMULAS / STYLE 完成。
 */
public interface Merge0044FixedAssetInventoryService {

    /** 导出：固定资产盘点表。 */
    void exportFixedAssetInventory(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的固定资产盘点表。 */
    List<JQuickRow> importFixedAssetInventory(@Param("field") String field, @Param("value") String value);
}
