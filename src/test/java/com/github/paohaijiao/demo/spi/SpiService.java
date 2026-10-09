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
package com.github.paohaijiao.demo.spi;

import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

/**
 * 分类：<b>SPI</b> —— 自定义函数 Provider 的注册与调用。
 *
 * <p>{@code TRANSFORM} 中的函数名会先查 SPI 注册表
 * （{@code JQuickMethodInvocationManager.getInvoker}），命中则走自定义 Provider，
 * 未命中才回退到内置 {@code JEvaluator}。方法名与
 * {@code demo/spi/jquick-excel.xml} 中的 {@code <excel name=...>} 一一对应。
 */
public interface SpiService {

    /** 导入：姓名脱敏 maskname（自定义 SPI）。 */
    List<JQuickRow> importSpiTransform(@Param("field") String field, @Param("value") String value);

    /** 导出：姓名脱敏 maskname + 分数转等级 grade（自定义 SPI）+ add/trim（内置 SPI）。 */
    void exportSpiTransform(@Param("field") String field, @Param("value") String value);
}
