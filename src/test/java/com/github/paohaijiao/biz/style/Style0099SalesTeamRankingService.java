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
 * 场景 99：销售团队业绩排行榜（绩效考核类，🟢 纯 XML）。
 *
 * <p>服务契约，方法名与 {@code jquick/biz/style/0099_style_sales-team-ranking.xml} 一一对应。
 * 完成率、目标 / 业绩合计与整体完成率、最低完成率标红全部由 XML 模板完成，Java 只构造数据。
 */
public interface Style0099SalesTeamRankingService {

    /** 导出：销售团队业绩排行榜。 */
    void exportSalesTeamRanking(@Param("field") String field, @Param("value") String value);

    /** 导入解析：读取导出的销售团队业绩排行榜。 */
    List<JQuickRow> importSalesTeamRanking(@Param("field") String field, @Param("value") String value);
}
