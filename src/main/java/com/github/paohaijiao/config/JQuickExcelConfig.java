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
package com.github.paohaijiao.config;

import java.io.Serializable;
import java.util.Objects;

/**
 * JQuick-Excel 全局配置中心（单例）。
 *
 * <p>用于在不破坏原有 DSL/API 兼容性的前提下，统一控制：
 * <ul>
 *   <li>导出是否启用流式写入（SXSSFWorkbook）及窗口大小、切换阈值</li>
 *   <li>导入是否启用 OPCPackage 共享解析、自动分批阈值</li>
 *   <li>CellStyle 缓存开关（避免 CellStyle 超 64000）</li>
 * </ul>
 *
 * <p>示例：
 * <pre>
 *   JQuickExcelConfig cfg = JQuickExcelConfig.getInstance();
 *   cfg.setStreamingExportEnabled(true)           // 开启导出流式
 *      .setStreamingRowAccessWindowSize(100)      // 内存中保留 100 行
 *      .setStreamingExportThreshold(5000)         // 超过 5000 行自动切 SXSSF
 *      .setBigFileImportEnabled(true)             // 导入开启 OPCPackage
 *      .setImportBatchThreshold(20000)            // 超过 20000 行建议分批
 *      .setCellStyleCacheEnabled(true);           // 开启样式缓存
 * </pre>
 */
public final class JQuickExcelConfig implements Serializable {

    public static final int DEFAULT_STREAMING_WINDOW = 100;
    public static final int DEFAULT_EXPORT_BIG_THRESHOLD = 5000;
    public static final int DEFAULT_IMPORT_BATCH_THRESHOLD = 20000;
    private static final long serialVersionUID = 1L;
    private static volatile JQuickExcelConfig INSTANCE;
    /**
     * 是否允许在大数据量时自动切换 SXSSF 流式写入
     */
    private boolean streamingExportEnabled = true;
    /**
     * 流式写入内存保留行数（SXSSF rowAccessWindowSize）
     */
    private int streamingRowAccessWindowSize = DEFAULT_STREAMING_WINDOW;
    /**
     * 当数据量 ≥ 该行数时自动切流式；设 <=0 表示强制关流式自动切换
     */
    private int streamingExportThreshold = DEFAULT_EXPORT_BIG_THRESHOLD;
    /**
     * 流式写入时压缩临时文件
     */
    private boolean streamingCompressTempFiles = true;
    /**
     * 导入时采用 OPCPackage.open + XSSFWorkbook(pkg)，降低峰值内存
     */
    private boolean bigFileImportEnabled = true;
    /**
     * 分批导入建议阈值（仅供用户端判断，不强制）
     */
    private int importBatchThreshold = DEFAULT_IMPORT_BATCH_THRESHOLD;
    /**
     * CellStyle 缓存开关，避免 POI 64000 样式上限
     */
    private boolean cellStyleCacheEnabled = true;

    private JQuickExcelConfig() {
    }

    public static JQuickExcelConfig getInstance() {
        if (INSTANCE == null) {
            synchronized (JQuickExcelConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JQuickExcelConfig();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 允许在测试场景下显式还原为默认配置
     */
    public static void resetDefault() {
        synchronized (JQuickExcelConfig.class) {
            INSTANCE = null;
        }
    }


    public boolean isStreamingExportEnabled() {
        return streamingExportEnabled;
    }

    public JQuickExcelConfig setStreamingExportEnabled(boolean streamingExportEnabled) {
        this.streamingExportEnabled = streamingExportEnabled;
        return this;
    }

    public int getStreamingRowAccessWindowSize() {
        return streamingRowAccessWindowSize;
    }

    public JQuickExcelConfig setStreamingRowAccessWindowSize(int streamingRowAccessWindowSize) {
        if (streamingRowAccessWindowSize <= 0) {
            throw new IllegalArgumentException("streamingRowAccessWindowSize must > 0");
        }
        this.streamingRowAccessWindowSize = streamingRowAccessWindowSize;
        return this;
    }

    public int getStreamingExportThreshold() {
        return streamingExportThreshold;
    }

    public JQuickExcelConfig setStreamingExportThreshold(int streamingExportThreshold) {
        this.streamingExportThreshold = streamingExportThreshold;
        return this;
    }

    public boolean isStreamingCompressTempFiles() {
        return streamingCompressTempFiles;
    }

    public JQuickExcelConfig setStreamingCompressTempFiles(boolean streamingCompressTempFiles) {
        this.streamingCompressTempFiles = streamingCompressTempFiles;
        return this;
    }

    public boolean isBigFileImportEnabled() {
        return bigFileImportEnabled;
    }

    public JQuickExcelConfig setBigFileImportEnabled(boolean bigFileImportEnabled) {
        this.bigFileImportEnabled = bigFileImportEnabled;
        return this;
    }

    public int getImportBatchThreshold() {
        return importBatchThreshold;
    }

    public JQuickExcelConfig setImportBatchThreshold(int importBatchThreshold) {
        this.importBatchThreshold = importBatchThreshold;
        return this;
    }

    public boolean isCellStyleCacheEnabled() {
        return cellStyleCacheEnabled;
    }

    public JQuickExcelConfig setCellStyleCacheEnabled(boolean cellStyleCacheEnabled) {
        this.cellStyleCacheEnabled = cellStyleCacheEnabled;
        return this;
    }

    /**
     * 根据数据量判断当前导出是否应该用 SXSSF 流式。
     */
    public boolean shouldUseStreaming(int dataSize) {
        if (!streamingExportEnabled) return false;
        if (streamingExportThreshold <= 0) return false;
        return dataSize >= streamingExportThreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JQuickExcelConfig)) return false;
        JQuickExcelConfig that = (JQuickExcelConfig) o;
        return streamingExportEnabled == that.streamingExportEnabled
                && streamingRowAccessWindowSize == that.streamingRowAccessWindowSize
                && streamingExportThreshold == that.streamingExportThreshold
                && streamingCompressTempFiles == that.streamingCompressTempFiles
                && bigFileImportEnabled == that.bigFileImportEnabled
                && importBatchThreshold == that.importBatchThreshold
                && cellStyleCacheEnabled == that.cellStyleCacheEnabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(streamingExportEnabled, streamingRowAccessWindowSize, streamingExportThreshold,
                streamingCompressTempFiles, bigFileImportEnabled, importBatchThreshold, cellStyleCacheEnabled);
    }

    @Override
    public String toString() {
        return "JQuickExcelConfig{" +
                "streamingExportEnabled=" + streamingExportEnabled +
                ", streamingRowAccessWindowSize=" + streamingRowAccessWindowSize +
                ", streamingExportThreshold=" + streamingExportThreshold +
                ", streamingCompressTempFiles=" + streamingCompressTempFiles +
                ", bigFileImportEnabled=" + bigFileImportEnabled +
                ", importBatchThreshold=" + importBatchThreshold +
                ", cellStyleCacheEnabled=" + cellStyleCacheEnabled +
                '}';
    }
}
