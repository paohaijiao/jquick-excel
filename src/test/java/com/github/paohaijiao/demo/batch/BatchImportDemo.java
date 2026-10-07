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
package com.github.paohaijiao.demo.batch;

import com.github.paohaijiao.demo.support.DemoKit;
import com.github.paohaijiao.executor.JQuickExcelCommonImportExecutor;
import com.github.paohaijiao.handler.JExcelImportHandler;
import com.github.paohaijiao.model.JExcelImportModel;
import com.github.paohaijiao.statement.JQuickRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * batch 子包 demo：大文件分批导入 {@code importDataInBatch}。
 *
 * <p>分批消费走的是编程式路径（不经过 XML 代理），配置用同一份 IMPORT WITH DSL
 * 由 {@link JQuickExcelCommonImportExecutor} 解析成 {@link JExcelImportModel}，
 * 每读满 pageSize 行回调一次；回调返回 false 可提前终止，方法返回总读取行数。
 */
public class BatchImportDemo {

    @Test
    public void importDataInBatch() throws Exception {
        // 本用例独立的 DSL 配置（等价于 XML 中的 IMPORT WITH 规则）
        String dsl = "IMPORT WITH SHEET=1, HEADER=true";
        JExcelImportModel model = (JExcelImportModel) new JQuickExcelCommonImportExecutor().execute(dsl);

        File file = DemoKit.prepareImportFile();
        AtomicInteger batches = new AtomicInteger();
        try (InputStream in = new FileInputStream(file)) {
            JExcelImportHandler handler = new JExcelImportHandler(in);
            int total = handler.importDataInBatch(model, 5000, (List<JQuickRow> batch) -> {
                System.out.println("本批 " + batch.size() + " 行");
                batches.incrementAndGet();
                return true;
            });
            System.out.println("总计读取：" + total);
            Assert.assertEquals(3, total);
            Assert.assertEquals(1, batches.get());
        }
    }
}
