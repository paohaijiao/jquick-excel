# JQuick-Excel 性能基准测试 / Performance Benchmark

> 基于 `JQuickExcelExportXmlParseFactory` / `JQuickExcelImportXmlParseFactory` XML 解析流程的大批量导入导出基准测试。
>
> Benchmark for large-scale Excel import/export based on XML DSL parsing flow.

---

## 1. 测试环境建议 / Test Environment

| 项目 / Item | 推荐配置 / Recommended                                                    |
|-------------|-----------------------------------------------------------------------|
| JDK | 8+                                                                    |
| JVM 堆内存 / Heap | `-Xms2g -Xmx4g`（10 万行 / 100K rows）；`-Xms4g -Xmx8g`（50 万行 / 500K rows） |
| Apache POI | 5.x（项目 pom.xml 已声明 / declared in pom.xml）                             |
| 操作系统 / OS | Windows / Linux / macOS                                               |
| 磁盘 / Disk | SSD（SXSSF 临时文件写入需要 / required for SXSSF temp files）                   |

---

## 2. 测试类位置 / Test Class Location

```
src/test/java/com/github/paohaijiao/xml/demo/
├── JQuickExcelDemo.java          # 原有功能 Demo / Original Demo
└── JQuickExcelBenchmark.java     # 性能基准测试 / Performance Benchmark
```

---

## 3. 运行方式 / How to Run

### 3.1 一键运行全部基准 / Run All Benchmarks

直接执行 `main` 方法或运行 `@Test runAll()`：

```java
// 方式一：main 方法（推荐，避免 JUnit 钩子干扰内存统计）
// Option 1: main method (recommended, avoids JUnit hooks interfering with memory stats)
public static void main(String[] args)

// 方式二：JUnit
// Option 2: JUnit
@Test
public void runAll()
```

### 3.2 按场景独立运行 / Run Individual Scenarios

| 测试入口 / Entry | 说明 / Description |
|-----------------|-------------------|
| `@Test runXmlExporterBench()` | 仅导出基准（XSSF vs Streaming）/ Export benchmark only |
| `@Test runPrepareImportFixtures()` | 仅生成导入夹具文件（1 万 / 5 万 / 10 万行）/ Generate import fixtures only |
| `@Test runGenerateStudentBigData()` | 仅生成 `studentbigdata.xlsx`（默认 10 万行）/ Generate big data file only |
| `@Test runXmlImporterBench()` | 仅导入基准（OPCPackage ON vs OFF）/ Import benchmark only |
| `@Test runXmlImportOnlyStudentBigData()` | 仅对 `studentbigdata.xlsx` 跑一次导入验证 / Single import test for big data |

---

## 4. 测试架构 / Test Architecture

### 4.1 导出基准流程 / Export Benchmark Flow

```
数据构建（JQuickRow） / Data Construction
    ↓
JQuickExcelExportXmlParseFactory(rows, OutputStream)
    ↓
JQuickXmlFactory(parser, "jquick-excel.xml")
    ↓
JQuickExcelExportService.exportExcel("1", "2")
    ↓
触发 XML DSL 全链路 / Full XML DSL Pipeline:
  SHEET → HEADER → MAPPING → TRANSFORM → STYLE → FORMULAS
```

**对比维度 / Comparison Dimensions:**

| 模式 / Mode | 配置 / Config | 说明 / Description |
|-------------|--------------|-------------------|
| XSSF | `streamingExportEnabled=false` | 纯内存全量写入，无磁盘临时文件 / Full in-memory, no temp files |
| Streaming | `streamingExportEnabled=true`, `threshold=1000`, `window=100` | SXSSF 流式写入，仅 100 行驻留内存 / SXSSF streaming, only 100 rows in memory |

> **注意 / Note:** 当前 `jquick-excel.xml` 中 `exportExcel` 配置了 `FORMULAS` 和 `STYLE`，会触发 `needsRandomRowAccess=true`，强制降级为 XSSF。如需测试纯 SXSSF 流式性能，请移除 XML 中的 `FORMULAS` 和 `STYLE` 配置。
>
> The current `jquick-excel.xml` configures `FORMULAS` and `STYLE` for `exportExcel`, which triggers `needsRandomRowAccess=true` and forces fallback to XSSF. To test pure SXSSF streaming performance, remove `FORMULAS` and `STYLE` from the XML.

### 4.2 导入基准流程 / Import Benchmark Flow

```
读取 xlsx 文件 → byte[] / Read xlsx file
    ↓
JQuickExcelImportXmlParseFactory(JContext, InputStream)
    ↓
JQuickXmlFactory(parser, "jquick-excel.xml")
    ↓
JQuickExcelExportService.importExcel("1", "2")
    ↓
触发 XML DSL 全链路 / Full XML DSL Pipeline:
  HEADER → SHEET → MAPPING → TRANSFORM
```

**对比维度 / Comparison Dimensions:**

| 模式 / Mode | 配置 / Config | 说明 / Description |
|-------------|--------------|-------------------|
| OPCPackage ON | `bigFileImportEnabled=true` | `OPCPackage.open(is)` 共享 zip 解析，降低峰值内存 / Shared zip parsing, lower peak memory |
| OPCPackage OFF | `bigFileImportEnabled=false` | `new XSSFWorkbook(is)` 传统方式，峰值内存更高 / Traditional approach, higher peak memory |

---

## 5. 测试数据规格 / Test Data Specification

### 5.1 导出数据（exportExcel）/ Export Data

| 字段 key / Key | 列名 / Column | 类型 / Type | 示例 / Example |
|----------------|--------------|------------|---------------|
| id | 主键 / Primary Key | String | S00000001 |
| name | 姓名 / Name | String | 学生_1 |
| gender | 性别 / Gender | int(0/1) | 0 |
| age | 年龄 / Age | int | 18-25 |
| enrollmentDate | 入学时间 / Enrollment Date | Date | 2018-09-01 |
| className | 班级 / Class | String | 计算机1班 |
| ignoreField | 是否忽略 / Ignore | String | true |

数据通过 `Random(20260808L)` 固定种子生成，保证可复现。
Data is generated with fixed seed `Random(20260808L)` for reproducibility.

### 5.2 导入数据（importExcel / studentbigdata.xlsx）/ Import Data

| 列名 / Column | 字段 key / Key | 类型 / Type | 格式 / Format |
|---------------|---------------|------------|--------------|
| 学号 / Student No | no | String | NO00000001 |
| 姓名 / Name | name | String | 学生_1 |
| 性别 / Gender | sex | String | 男/女 |
| 年龄 / Age | age | Integer | 18-27 |
| 出生日期 / Birthday | birthday | Date | yyyy-MM-dd |

Sheet 名 / Sheet name: `Sheet1`，表头第 1 行 / Header at row 1（headRowNumber=1）。

---

## 6. 测试矩阵 / Test Matrix

### 6.1 导出基准 / Export Benchmark

| 数据量 / Rows | XSSF | Streaming(SXSSF) |
|--------------|------|-------------------|
| 100 行 / 100 rows | ✅ | ✅（未达阈值，仍 XSSF / Below threshold, still XSSF） |
| 1,000 行 / 1K rows | ✅ | ✅ SXSSF |
| 5,000 行 / 5K rows | ✅ | ✅ SXSSF |
| 10,000 行 / 10K rows | ✅ | ✅ SXSSF |

### 6.2 导入基准 / Import Benchmark

| 文件 / File | 行数 / Rows | OPCPackage ON | OPCPackage OFF |
|------------|------------|---------------|----------------|
| xml_import_fixture_10000.xlsx | 10,000 | ✅ | ✅ |
| xml_import_fixture_50000.xlsx | 50,000 | ✅ | ✅ |
| xml_import_fixture_100000.xlsx | 100,000 | ✅ | ⏭ 跳过 / Skip（防 OOM / Prevent OOM） |
| studentbigdata.xlsx | 100,000 | ✅ | ⏭ 跳过 / Skip（防 OOM / Prevent OOM） |

---

## 7. 输出文件 / Output Files

| 路径 / Path | 内容 / Content |
|------------|---------------|
| `d:/test/benchmark/` | 导出基准文件 + 导入夹具文件 / Export benchmark files + import fixtures |
| `d:/test/benchmark/xml_export_xssf_*.xlsx` | XSSF 模式导出文件 / XSSF mode export files |
| `d:/test/benchmark/xml_export_streaming_*.xlsx` | Streaming 模式导出文件 / Streaming mode export files |
| `d:/test/benchmark/xml_import_fixture_*.xlsx` | 导入基准夹具文件 / Import benchmark fixtures |
| `d:/test/studentbigdata.xlsx` | 10 万行大数据文件 / 100K rows big data file |

---

## 8. 性能指标 / Performance Metrics

### 8.1 导出基准指标 / Export Benchmark Metrics

| 指标 / Metric | 单位 / Unit | 说明 / Description |
|--------------|------------|-------------------|
| 耗时 / Elapsed Time | ms | 从 `exportExcel` 调用到文件写入完成的总耗时 / Total time from `exportExcel` call to file write completion |
| 峰值内存 / Peak Memory | MB | `Runtime.totalMemory() - freeMemory()`，导出完成后的堆占用 / Heap usage after export completes |
| 文件大小 / File Size | KB | 生成 xlsx 文件的磁盘大小 / Disk size of generated xlsx file |
| 数据行数 / Row Count | rows | 导出的数据行数（不含表头）/ Number of data rows exported (excluding header) |
| 吞吐量 / Throughput | rows/s | 每秒导出行数 = 行数 / (耗时 / 1000) / Rows exported per second |

**导出基准输出示例 / Export Benchmark Output Example:**

```
| 行数 / Rows | 模式 / Mode | 耗时 / Time(ms) | 峰值内存 / Peak Mem(MB) | 文件大小 / File Size(KB) | 吞吐量 / Throughput(rows/s) | 说明 / Note |
|---|---|---|---|---|---|---|
| 100       | XSSF      | 180     | 45.2   | 12.5    | 555      | XSSF → xml_export_xssf_100.xlsx |
| 100       | Streaming | 170     | 42.8   | 12.3    | 588      | Streaming → xml_export_streaming_100.xlsx |
| 1,000     | XSSF      | 420     | 78.5   | 125.0   | 2,380    | XSSF → xml_export_xssf_1000.xlsx |
| 1,000     | Streaming | 380     | 55.3   | 123.0   | 2,631    | Streaming → xml_export_streaming_1000.xlsx |
| 5,000     | XSSF      | 1,250   | 210.8  | 620.0   | 4,000    | XSSF → xml_export_xssf_5000.xlsx |
| 5,000     | Streaming | 1,080   | 85.6   | 610.0   | 4,629    | Streaming → xml_export_streaming_5000.xlsx |
| 10,000    | XSSF      | 3,200   | 512.3  | 1,856.0 | 3,125    | XSSF → xml_export_xssf_10000.xlsx |
| 10,000    | Streaming | 2,800   | 128.5  | 1,820.0 | 3,571    | Streaming → xml_export_streaming_10000.xlsx |
```

> **注：** 以上数据为示例值，实际结果取决于硬件环境与 JVM 配置。
>
> Note: The above data are example values. Actual results depend on hardware and JVM configuration.

### 8.2 导入基准指标 / Import Benchmark Metrics

| 指标 / Metric | 单位 / Unit | 说明 / Description |
|--------------|------------|-------------------|
| 耗时 / Elapsed Time | ms | 从 `importExcel` 调用到返回 List 的总耗时 / Total time from `importExcel` call to List return |
| 峰值内存 / Peak Memory | MB | `Runtime.totalMemory() - freeMemory()`，导入完成后的堆占用 / Heap usage after import completes |
| 读取行数 / Rows Read | rows | 实际解析并返回的数据行数 / Actual parsed and returned data rows |
| 吞吐量 / Throughput | rows/s | 每秒导入行数 = 行数 / (耗时 / 1000) / Rows imported per second |
| 内存效率 / Memory Efficiency | rows/MB | 每兆内存处理的行数 = 行数 / 峰值内存 / Rows processed per MB of memory |

**导入基准输出示例 / Import Benchmark Output Example:**

```
| 行数 / Rows | OPCPackage | 耗时 / Time(ms) | 峰值内存 / Peak Mem(MB) | 读取行数 / Rows Read | 吞吐量 / Throughput(rows/s) | 内存效率 / Mem Eff(rows/MB) | 文件 / File |
|---|---|---|---|---|---|---|---|
| 10,000  | ON  | 1,800  | 120.5 | 10,000  | 5,555  | 83.0  | xml_import_fixture_10000.xlsx |
| 10,000  | OFF | 2,100  | 256.8 | 10,000  | 4,761  | 38.9  | xml_import_fixture_10000.xlsx |
| 50,000  | ON  | 7,500  | 280.3 | 50,000  | 6,666  | 178.4 | xml_import_fixture_50000.xlsx |
| 50,000  | OFF | 9,200  | 580.6 | 50,000  | 5,434  | 86.1  | xml_import_fixture_50000.xlsx |
| 100,000 | ON  | 15,200 | 389.1 | 100,000 | 6,578  | 257.0 | xml_import_fixture_100000.xlsx |
| 100,000 | ON  | 15,800 | 395.2 | 100,000 | 6,329  | 253.0 | studentbigdata.xlsx |
```

> **注：** OPCPackage OFF 在 10 万行场景下可能 OOM，故跳过。
>
> Note: OPCPackage OFF may cause OOM at 100K rows, so it is skipped.

### 8.3 studentbigdata.xlsx 单独导入输出 / Single Import Output

```
==== studentbigdata.xlsx 导入完成 / Import Completed ====
文件 / File: d:\test\studentbigdata.xlsx / 100,000 行 / rows
耗时 / Elapsed Time: 15,200 ms
峰值内存 / Peak Memory: 389.1 MB
实际读取 / Rows Read: 100,000 行 / rows
吞吐量 / Throughput: 6,578 rows/s
内存效率 / Memory Efficiency: 257.0 rows/MB
```

### 8.4 导出模式对比总结 / Export Mode Comparison Summary

| 数据量 / Rows | XSSF 耗时 / Time(ms) | SXSSF 耗时 / Time(ms) | XSSF 内存 / Mem(MB) | SXSSF 内存 / Mem(MB) | 内存降幅 / Memory Reduction | 速度变化 / Speed Change |
|--------------|---------------------|----------------------|---------------------|----------------------|---------------------------|------------------------|
| 100 | 180 | 170 | 45.2 | 42.8 | -5.3% | +5.9% |
| 1,000 | 420 | 380 | 78.5 | 55.3 | -29.6% | +9.5% |
| 5,000 | 1,250 | 1,080 | 210.8 | 85.6 | -59.4% | +13.6% |
| 10,000 | 3,200 | 2,800 | 512.3 | 128.5 | -74.9% | +12.5% |

> **结论 / Conclusion:** 随着数据量增大，SXSSF 流式写入的内存优势显著放大（10,000 行时内存降低 ~75%），同时耗时也有 10-15% 的提升。
>
> As data volume grows, SXSSF streaming shows significant memory advantage (up to ~75% reduction at 10K rows) while also improving speed by 10-15%.

### 8.5 导入模式对比总结 / Import Mode Comparison Summary

| 行数 / Rows | ON 耗时 / Time(ms) | OFF 耗时 / Time(ms) | ON 内存 / Mem(MB) | OFF 内存 / Mem(MB) | 内存降幅 / Memory Reduction | 速度变化 / Speed Change |
|------------|-------------------|---------------------|-------------------|-------------------|---------------------------|------------------------|
| 10,000 | 1,800 | 2,100 | 120.5 | 256.8 | -53.1% | +14.3% |
| 50,000 | 7,500 | 9,200 | 280.3 | 580.6 | -51.7% | +18.5% |
| 100,000 | 15,200 | — | 389.1 | — | — | — |

> **结论 / Conclusion:** OPCPackage 开启后，导入内存稳定降低 50%+，耗时也有 15-20% 的提升。10 万行时 OFF 模式有 OOM 风险。
>
> With OPCPackage enabled, import memory is consistently reduced by 50%+, and speed improves by 15-20%. At 100K rows, OFF mode risks OOM.

---

## 9. 关键优化项对照 / Key Optimizations

### 9.1 导出优化 / Export Optimizations

| 优化项 / Optimization | 机制 / Mechanism | 配置 / Config |
|----------------------|-----------------|--------------|
| SXSSF 流式写入 / SXSSF Streaming | 仅 window 行驻留内存，超出行 flush 到磁盘临时文件 / Only window rows in memory, rest flushed to disk | `streamingExportEnabled=true` + `streamingRowAccessWindowSize=100` |
| 临时文件压缩 / Temp File Compression | SXSSF 临时文件启用 GZIP 压缩 / GZIP compression for SXSSF temp files | `streamingCompressTempFiles=true` |
| 样式缓存 / Style Cache | `JCellStyleCache` 按 Workbook+key 双层 WeakHashMap 缓存 / Two-level WeakHashMap cache by Workbook+key | `cellStyleCacheEnabled=true` |
| 自动降级保护 / Auto Fallback | 配置含 FORMULAS/STYLE/MERGE/GRAPH/FOOTER 时自动禁用 SXSSF 回退 XSSF / Auto fallback to XSSF when config requires random row access | `needsRandomRowAccess(config)` |

### 9.2 导入优化 / Import Optimizations

| 优化项 / Optimization | 机制 / Mechanism | 配置 / Config |
|----------------------|-----------------|--------------|
| OPCPackage 共享解析 / OPCPackage Shared Parsing | `OPCPackage.open(is)` 替代 `new XSSFWorkbook(is)`，降低 zip 解压峰值内存 / Replaces `new XSSFWorkbook(is)` to reduce zip decompression peak memory | `bigFileImportEnabled=true` |
| 自动回退 / Auto Fallback | OPCPackage 失败时自动回退到 `new XSSFWorkbook(is)` / Falls back to `new XSSFWorkbook(is)` on failure | 内置容错 / Built-in |

### 9.3 样式缓存优化 / Style Cache Optimization

| 优化项 / Optimization | 之前 / Before | 之后 / After |
|----------------------|--------------|-------------|
| 数据行斑马纹 / Data Row Zebra Stripes | 每行每列 `createCellStyle()` / New style per cell | 每 workbook 仅 2 份（奇/偶）/ Only 2 per workbook (odd/even) |
| 日期格式叠加 / Date Format Overlay | 每单元格新建 / New per cell | 按 `theme+奇偶+format` 三元组缓存 / Cached by `theme+odd+format` triplet |
| 表头样式 / Header Style | 每列新建 / New per column | 整行共用 1 份 / Shared for entire row |
| 合并单元格居中 / Merge Center Style | 每次合并新建 / New per merge | 每 workbook 1 份 / 1 per workbook |
| 10 万行×7 列样式总量 / 100K×7 Style Count | ~90 万（超 64000 上限）/ ~900K (exceeds 64000 limit) | ~10 份 / ~10 |

---

## 10. JQuickExcelConfig 配置速查 / Configuration Reference

```java
JQuickExcelConfig cfg = JQuickExcelConfig.getInstance();

// 导出配置 / Export Config
cfg.setStreamingExportEnabled(true)           // 开启流式导出 / Enable streaming export
   .setStreamingRowAccessWindowSize(100)      // SXSSF 内存窗口 / SXSSF memory window
   .setStreamingExportThreshold(5000)         // 自动切换阈值 / Auto switch threshold
   .setStreamingCompressTempFiles(true)       // 临时文件压缩 / Temp file compression
   .setCellStyleCacheEnabled(true);           // 样式缓存 / Style cache

// 导入配置 / Import Config
cfg.setBigFileImportEnabled(true)             // OPCPackage 优化 / OPCPackage optimization
   .setImportBatchThreshold(20000);           // 分批建议阈值 / Batch threshold

// 重置为默认 / Reset to default
JQuickExcelConfig.resetDefault();
```

| 配置项 / Config | 默认值 / Default | 说明 / Description |
|----------------|-----------------|-------------------|
| `streamingExportEnabled` | `true` | 导出允许自动切 SXSSF / Allow auto SXSSF for export |
| `streamingRowAccessWindowSize` | `100` | SXSSF 内存保留行数 / SXSSF rows kept in memory |
| `streamingExportThreshold` | `5000` | 超过该行数自动升级流式 / Auto upgrade to streaming above this |
| `streamingCompressTempFiles` | `true` | SXSSF 临时文件启用压缩 / Compress SXSSF temp files |
| `bigFileImportEnabled` | `true` | 导入启用 OPCPackage / Enable OPCPackage for import |
| `importBatchThreshold` | `20000` | 分批导入建议阈值 / Batch import threshold |
| `cellStyleCacheEnabled` | `true` | CellStyle 缓存开关 / CellStyle cache switch |
| `shouldUseStreaming(size)` | — | 便捷判定：是否对给定数量启用流式 / Helper: should use streaming for given size |

---

## 11. 注意事项 / Notes

1. **SXSSF 与公式/样式/合并的兼容性 / SXSSF Compatibility with Formulas/Styles/Merges:**
   SXSSF 刷盘后的行不允许回写。当 XML 配置包含 `FORMULAS`、`STYLE`（ROW/COLUMN/CELL/RANGE）、`MERGE`、`GRAPH`、`FOOTER` 时，框架会自动降级为 XSSF，避免 `Attempting to write a row[N] ... already written to disk` 异常。
   
   SXSSF does not allow writing back to flushed rows. When XML config includes `FORMULAS`, `STYLE`, `MERGE`, `GRAPH`, or `FOOTER`, the framework auto-falls back to XSSF to avoid the exception.

2. **CellStyle 64000 上限 / CellStyle 64000 Limit:**
   POI 的 `.xlsx` 规范允许的 Workbook 级 CellStyle 上限为 64000。框架已通过 `JCellStyleCache` 将样式数量从 O(N×列) 降至 O(常量)，10 万行导出仅产生约 10 份样式。
   
   POI's `.xlsx` spec allows up to 64000 CellStyles per Workbook. The framework reduces style count from O(N×cols) to O(constant) via `JCellStyleCache` — only ~10 styles for 100K rows.

3. **大文件导入 OOM 防护 / Large File Import OOM Protection:**
   `OPCPackage OFF` 模式在 10 万行以上时可能 OOM，基准测试中已对 10 万行场景跳过 OFF 模式。
   
   `OPCPackage OFF` mode may OOM at 100K+ rows. The benchmark skips OFF mode for 100K rows.

4. **`studentbigdata.xlsx` 行数调整 / Adjusting `studentbigdata.xlsx` Row Count:**
   修改 `JQuickExcelBenchmark` 中的常量 `STUDENT_BIGDATA_ROWS` 即可调整生成行数（如 50_000 / 200_000 / 500_000）。
   
   Modify the constant `STUDENT_BIGDATA_ROWS` in `JQuickExcelBenchmark` to adjust row count (e.g., 50K / 200K / 500K).

5. **内存统计方式 / Memory Measurement:**
   采用 `Runtime.totalMemory() - freeMemory()` 简易统计，每组测试前执行 `System.gc()` + `Thread.sleep(200)` 尽量减少干扰。
   
   Uses `Runtime.totalMemory() - freeMemory()` for simple measurement. Each test group runs `System.gc()` + `Thread.sleep(200)` before execution to minimize interference.

6. **示例数据说明 / Example Data Disclaimer:**
   本文档第 8 节中的所有性能数据均为示例值，实际结果取决于 CPU、内存、磁盘、JVM 参数和操作系统等环境因素。请以实际运行结果为准。
   
   All performance data in Section 8 are example values. Actual results depend on CPU, memory, disk, JVM parameters, and OS. Please refer to actual run results.
