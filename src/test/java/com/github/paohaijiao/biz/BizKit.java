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
package com.github.paohaijiao.biz;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * biz 业务 Demo 公共支撑：统一输出目录与公式重算工具。
 *
 * <p>本类只提供测试基础设施（输出目录、Excel 公式重算），不承载任何业务聚合计算；
 * 各场景的统计、样式、格式化一律由对应的 XML 模板完成。
 *
 * <p>产物目录：{@code D:\test\excel\biz}。
 */
public final class BizKit {

    /** 全部 biz 场景的 Excel 产物目录。 */
    public static final File OUT_DIR = new File("D:" + File.separator + "test" + File.separator + "excel" + File.separator + "biz");

    static {
        if (!OUT_DIR.exists()) {
            OUT_DIR.mkdirs();
        }
    }

    private BizKit() {
    }

    /**
     * 按技术分类返回产物文件，并自动创建技术子目录。
     *
     * <p>biz 场景按主导技术元素分子包后，产物也落到对应技术子目录，
     * 例如 {@code outFile("transform", "0001_transform_demo.xlsx")}
     * 对应 {@code D:\test\excel\biz\transform\0001_transform_demo.xlsx}。
     *
     * @param tech 技术分类（同时作为子目录名，如 transform/formulas/merge/style/graph/footer/validation）
     * @param name 文件名（含扩展名）
     * @return 产物文件
     */
    public static File outFile(String tech, String name) {
        File dir = new File(OUT_DIR, tech);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, name);
    }

    /**
     * 用 POI 公式求值器重算工作簿内的全部公式并另存。
     *
     * <p>POI 写出公式时不会附带计算结果，直接回读公式单元格缓存值为空；
     * 导入前先重算并另存，可让导入侧读到公式的真实计算结果。
     *
     * @param src 源 Excel（含公式）
     * @param dst 重算后的目标 Excel
     * @return 目标 Excel（即 {@code dst}）
     */
    public static File recalc(File src, File dst) throws Exception {
        try (XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(src))) {
            wb.getCreationHelper().createFormulaEvaluator().evaluateAll();
            try (OutputStream os = new FileOutputStream(dst)) {
                wb.write(os);
            }
        }
        return dst;
    }
}
