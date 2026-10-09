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
 * 分类：<b>SPI</b> —— 自定义函数 Provider（姓名脱敏）。
 *
 * <p>SPI 契约是 {@code com.github.paohaijiao.function.core.JQuickMethodFunctionProvider}，
 * 这里继承抽象基类 {@link JQuickBaseFunctionFunctionProvider} 以获得
 * {@code asString/asInt/validateArgCount} 等参数处理辅助方法。
 *
 * <p><b>注册方式</b>：在 {@code src/test/resources/META-INF/services/} 下新建文件
 * {@code com.github.paohaijiao.function.core.JQuickMethodFunctionProvider}，
 * 逐行写入本类的全限定名。框架通过
 * {@code JQuickMethodInvocationManager.getInstance()} 加载全部 Provider。
 *
 * <p><b>命名注意</b>：{@code applyTransform} 会用
 * {@code methodName.toLowerCase()} 去查表，因此方法名请<b>全部小写</b>，
 * 才能命中 SPI 分支；否则会回退到内置 {@code JEvaluator}。
 */
@Priority(PriorityConstants.SYSTEM_HIGH)
public class JQuickMaskNameFunctionProvider extends JQuickBaseFunctionFunctionProvider {

    public JQuickMaskNameFunctionProvider() {
        super("maskname", "自定义 SPI 函数：姓名脱敏（保留首字符，其余以 * 代替）");
    }

    /**
     * @param args 仅 1 个参数：待脱敏的姓名
     * @return 脱敏后的姓名，例如「张三」->「张*」，「张三丰」->「张**」
     */
    @Override
    public Object invoke(List<Object> args) {
        validateArgCount(args, 1);
        String name = asString(args.get(0));
        if (null == name || name.isEmpty()) {
            return name;
        }
        StringBuilder result = new StringBuilder();
        result.append(name.charAt(0));
        for (int i = 1; i < name.length(); i++) {
            result.append('*');
        }
        return result.toString();
    }
}
