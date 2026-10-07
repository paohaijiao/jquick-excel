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
package com.github.paohaijiao.demo.config;

import com.github.paohaijiao.config.JQuickExcelConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * config 子包 demo：{@link JQuickExcelConfig} 单例链式配置（纯 Java，无 XML）。
 *
 * <p>配置项：流式导出开关 / 内存窗口行数 / 流式阈值 / 临时文件压缩 /
 * 大文件导入（OPCPackage）/ 分批建议阈值 / CellStyle 缓存。
 */
public class ConfigDemo {

    /** 单例是全局状态，用例结束后还原默认值，避免影响其它测试。 */
    @After
    public void reset() {
        JQuickExcelConfig.resetDefault();
    }

    @Test
    public void chainConfig() {
        JQuickExcelConfig config = JQuickExcelConfig.getInstance()
                .setStreamingExportEnabled(true)
                .setStreamingRowAccessWindowSize(100)
                .setStreamingExportThreshold(5000)
                .setStreamingCompressTempFiles(true)
                .setBigFileImportEnabled(true)
                .setImportBatchThreshold(20000)
                .setCellStyleCacheEnabled(true);

        System.out.println(config);

        Assert.assertEquals(100, config.getStreamingRowAccessWindowSize());
        Assert.assertEquals(5000, config.getStreamingExportThreshold());
        Assert.assertTrue(config.isStreamingCompressTempFiles());
        Assert.assertTrue(config.isBigFileImportEnabled());
        Assert.assertEquals(20000, config.getImportBatchThreshold());
        Assert.assertTrue(config.isCellStyleCacheEnabled());

        // 数据量达到阈值才切流式
        Assert.assertFalse(config.shouldUseStreaming(4999));
        Assert.assertTrue(config.shouldUseStreaming(5000));
    }

    @Test
    public void disableStreaming() {
        JQuickExcelConfig config = JQuickExcelConfig.getInstance().setStreamingExportEnabled(false);
        Assert.assertFalse("关闭后任意数据量都不应切流式", config.shouldUseStreaming(1_000_000));
        System.out.println("关闭流式后 shouldUseStreaming(1000000)=" + config.shouldUseStreaming(1_000_000));
    }
}
