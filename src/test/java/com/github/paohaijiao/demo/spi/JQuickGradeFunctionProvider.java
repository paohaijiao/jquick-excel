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

import com.github.paohaijiao.function.domain.JQuickBaseFunctionFunctionProvider;
import com.github.paohaijiao.spi.anno.Priority;
import com.github.paohaijiao.spi.constants.PriorityConstants;

import java.util.List;

/**
 * 分类：<b>SPI</b> —— 自定义函数 Provider（分数转等级）。
 *
 * <p>演示第二个自定义函数的注册与调用：同一个
 * {@code META-INF/services} 文件里逐行登记多个 Provider 即可。
 */
@Priority(PriorityConstants.SYSTEM_HIGH)
public class JQuickGradeFunctionProvider extends JQuickBaseFunctionFunctionProvider {

    public JQuickGradeFunctionProvider() {
        super("grade", "自定义 SPI 函数：分数转等级（>=90 A，>=80 B，>=60 C，其余 D）");
    }

    /**
     * @param args 仅 1 个参数：分数（可为整数或小数）
     * @return 等级字符串 A/B/C/D
     */
    @Override
    public Object invoke(List<Object> args) {
        validateArgCount(args, 1);
        double score = asDouble(args.get(0));
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 60) {
            return "C";
        }
        return "D";
    }
}
