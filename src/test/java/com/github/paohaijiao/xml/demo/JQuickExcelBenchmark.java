package com.github.paohaijiao.xml.demo;

import com.github.paohaijiao.anno.JExcelColumn;
import com.github.paohaijiao.anno.JExcelSheet;
import com.github.paohaijiao.config.JQuickExcelConfig;
import com.github.paohaijiao.excel.JExcelExporter;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.ex.JQuickExcelExportXmlParseFactory;
import com.github.paohaijiao.xml.factory.JQuickFactory;
import com.github.paohaijiao.xml.factory.JQuickXmlFactory;
import com.github.paohaijiao.xml.handler.JQuickParseHandler;
import com.github.paohaijiao.xml.im.JQuickExcelImportXmlParseFactory;
import com.github.paohaijiao.xml.service.JQuickExcelExportService;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 */
public class JQuickExcelBenchmark {

    private static final Path OUT_DIR = Paths.get("d:/test/benchmark");

    private static final Path STUDENT_BIGDATA_DIR = Paths.get("d:/test");

    private static final String STUDENT_BIGDATA_NAME = "studentbigdata.xlsx";
    private static final int STUDENT_BIGDATA_ROWS = 100_000;

    public static void main(String[] args) throws Exception {
        Files.createDirectories(OUT_DIR);
        Files.createDirectories(STUDENT_BIGDATA_DIR);
        System.out.println("==== 基准输出目录: " + OUT_DIR.toAbsolutePath());
        System.out.println("==== 大数据样本目录: " + STUDENT_BIGDATA_DIR.toAbsolutePath());
        System.out.println("==== JVM 最大堆: " + fmtSize(Runtime.getRuntime().maxMemory()));
        System.out.println("\n########## 1. XML 导出基准（JQuickExcelExportXmlParseFactory） ##########");
        benchmarkXmlExporter();
        System.out.println("\n########## 2. 生成 xml_import_fixture 夹具（Sheet1/学号/姓名/性别/年龄/出生日期，配合 importExcel） ##########");
        prepareImportFixture(10_000);
        prepareImportFixture(50_000);
        prepareImportFixture(100_000);
        System.out.println("\n########## 3. 生成 studentbigdata.xlsx（" + STUDENT_BIGDATA_ROWS + " 行） ##########");
        generateStudentBigData(STUDENT_BIGDATA_ROWS);

        System.out.println("\n########## 4. XML 导入基准（JQuickExcelImportXmlParseFactory） ##########");
        benchmarkXmlImporter();
        System.out.println("\n==== 全部基准完成 ====");
    }

    private static void benchmarkXmlExporter() throws Exception {
        int[] sizes = {100, 1_000, 5_000, 10_000};
        System.out.println("| 行数 | 模式 | 耗时(ms) | 峰值内存(MB) | 文件大小(KB) | 说明 |");
        System.out.println("|---|---|---|---|---|---|");
        for (int size : sizes) {
            List<JQuickRow> rows = buildQuickRowsForExport(size);
            {
                JQuickExcelConfig.resetDefault();
                JQuickExcelConfig.getInstance()
                .setStreamingExportEnabled(false)
                .setCellStyleCacheEnabled(true);
                Result r = runXmlExport(rows, size, "XSSF");
                System.out.printf("| %,d | XSSF | %,d | %.1f | %.1f | %s |\n", size, r.timeMs, r.peakMemMB, r.fileSizeKB, r.note);
            }

            {
                JQuickExcelConfig.resetDefault();
                JQuickExcelConfig.getInstance()
                .setStreamingExportEnabled(true)
                .setStreamingExportThreshold(1_000)
                .setStreamingRowAccessWindowSize(100)
                .setStreamingCompressTempFiles(true)
                .setCellStyleCacheEnabled(true);
                Result r = runXmlExport(rows, size, "Streaming");
                System.out.printf("| %,d | Streaming | %,d | %.1f | %.1f | %s |\n", size, r.timeMs, r.peakMemMB, r.fileSizeKB, r.note);
            }
        }
    }


    private static Result runXmlExport(List<JQuickRow> rows, int size, String label) throws Exception {
        System.gc();
        Thread.sleep(200);
        String fileName = "xml_export_" + label.toLowerCase() + "_" + size + ".xlsx";
        Path outFile = OUT_DIR.resolve(fileName);
        long t0 = System.currentTimeMillis();
        try (OutputStream os = new FileOutputStream(outFile.toFile())) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, os);
            JQuickFactory factory = new JQuickXmlFactory(parser, "jquick-excel.xml");
            JQuickExcelExportService svc = factory.createApi(JQuickExcelExportService.class);
            svc.exportExcel("1", "2");
        }
        long timeMs = System.currentTimeMillis() - t0;
        double peakMemMB = memUsedBytes() / 1024.0 / 1024.0;
        return new Result(timeMs, peakMemMB,
                Files.exists(outFile) ? Files.size(outFile) / 1024.0 : 0.0,
                label + " → " + outFile.getFileName() + "（XML: exportExcel + MAPPING/TRANSFORM/STYLE/FORMULAS）");
    }

    private static void prepareImportFixture(int size) throws IOException {
        Path target = OUT_DIR.resolve("xml_import_fixture_" + size + ".xlsx");
        writeImportFixture(target, size, false);
    }

    /**
     * 生成 d:/test/studentbigdata.xlsx —— 用户要求的大量数据文件。
     *
     * <p>文件规格与 jquick-excel.xml 中 importExcel 定义保持一致：
     * <pre>
     *   SHEET='Sheet1'
     *   MAPPING={"学号":"no","姓名":"name","性别":"sex","年龄":"age","出生日期":"birthday"}
     *   TRANSFORM: sex=trans(dict,sex), birthday=dateFormat(birthday,'yyyy-MM-dd')
     * </pre>
     *
     * @param size 生成的行数（不含表头），例如 100_000 / 500_000
     * @return 实际生成的文件路径，通常为 d:/test/studentbigdata.xlsx
     */
    public static Path generateStudentBigData(int size) throws IOException {
        Path target = STUDENT_BIGDATA_DIR.resolve(STUDENT_BIGDATA_NAME);
        writeImportFixture(target, size, true);
        return target;
    }

    private static void writeImportFixture(Path target, int size, boolean forceOverwrite) throws IOException {
        if (!forceOverwrite && Files.exists(target)) {
            System.out.println("  [已存在] " + target + "，跳过生成");
            return;
        }
        Files.createDirectories(target.getParent());
        List<ImportFixture> data = new ArrayList<>(size);
        Random r = new Random(20260808L);
        Calendar cal = Calendar.getInstance();
        long startMs = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            cal.set(1998 + r.nextInt(10), r.nextInt(12), 1 + r.nextInt(27));
            data.add(new ImportFixture(
                    String.format("NO%08d", i + 1),
                    "学生_" + (i + 1),
                    (i & 1) == 0 ? "男" : "女",
                    18 + r.nextInt(10),
                    cal.getTime()));
        }
        // 大数据量强制走 SXSSF 流式写出，防止生成本身 OOM
        JQuickExcelConfig.resetDefault();
        JQuickExcelConfig.getInstance()
                .setStreamingExportEnabled(true)
                .setStreamingExportThreshold(5_000)
                .setStreamingRowAccessWindowSize(100)
                .setStreamingCompressTempFiles(true);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(Math.max(4096, size * 256))) {
            JExcelExporter<ImportFixture> exporter = new JExcelExporter<>(ImportFixture.class);
            exporter.write(baos, data);
            Files.write(target, baos.toByteArray());
        }
        long seconds = (System.currentTimeMillis() - startMs + 500) / 1000;
        System.out.printf("  [生成] %s → %,d 行 / %s（耗时 %,ds）%n",
                target, size, fmtSize(Files.size(target)), seconds);
    }

    private static void benchmarkXmlImporter() throws Exception {
        System.out.println("| 行数 | OPCPackage | 耗时(ms) | 峰值内存(MB) | 读取行数 | 文件 |");
        System.out.println("|---|---|---|---|---|---|");
        List<BenchmarkFile> files = new ArrayList<>();
        for (int size : new int[]{10_000, 50_000, 100_000}) {
            Path f = OUT_DIR.resolve("xml_import_fixture_" + size + ".xlsx");
            if (Files.exists(f)) files.add(new BenchmarkFile(size, f));
        }
        Path big = STUDENT_BIGDATA_DIR.resolve(STUDENT_BIGDATA_NAME);
        if (Files.exists(big)) {
            files.add(new BenchmarkFile(STUDENT_BIGDATA_ROWS, big, true));
        }
        for (BenchmarkFile bf : files) {
            byte[] bytes = Files.readAllBytes(bf.path);
            {
                JQuickExcelConfig.resetDefault();
                JQuickExcelConfig.getInstance().setBigFileImportEnabled(true);
                XmlImportResult r = runXmlImport(bytes, bf.rows, "ON", bf.isStudentBigdata);
                System.out.printf("| %,d | ON  | %,d | %.1f | %,d | %s |\n", bf.rows, r.timeMs, r.peakMemMB, r.rows, bf.path.getFileName());
            }
            if (bf.rows <= 50_000) {
                JQuickExcelConfig.resetDefault();
                JQuickExcelConfig.getInstance().setBigFileImportEnabled(false);
                XmlImportResult r = runXmlImport(bytes, bf.rows, "OFF", bf.isStudentBigdata);
                System.out.printf("| %,d | OFF | %,d | %.1f | %,d | %s |\n", bf.rows, r.timeMs, r.peakMemMB, r.rows, bf.path.getFileName());
            }
        }
    }

    private static XmlImportResult runXmlImport(byte[] bytes, int size, String label, boolean forStudentBigdata) throws Exception {
        System.gc();
        Thread.sleep(200);

        long t0 = System.currentTimeMillis();
        JContext jContext = new JContext();
        Map<String, Object> sexDict = new HashMap<>();
        sexDict.put("男", "1");
        sexDict.put("女", "2");
        jContext.put("dict", sexDict);
        int rows;
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(jContext, in);
            JQuickFactory factory = new JQuickXmlFactory(parser, "jquick-excel.xml");
            JQuickExcelExportService svc = factory.createApi(JQuickExcelExportService.class);
            List<JQuickRow> list = svc.importExcel("1", "2");
            rows = list == null ? 0 : list.size();
        }
        long timeMs = System.currentTimeMillis() - t0;
        double peakMemMB = memUsedBytes() / 1024.0 / 1024.0;
        String note = "OPCPackage=" + label + (forStudentBigdata ? "（" + STUDENT_BIGDATA_NAME + "）" : "");
        return new XmlImportResult(timeMs, peakMemMB, rows, note);
    }

    /**
     * 适配 jquick-excel.xml 中 exportExcel 映射字段的 JQuickRow：
     * MAPPING={"id":"主键","name":"姓名","gender":"性别","age":"年龄","enrollmentDate":"入学时间","className":"班级","ignoreField":"是否忽略"}
     */
    private static List<JQuickRow> buildQuickRowsForExport(int n) {
        Random r = new Random(20260808L);
        String[] classes = {"计算机1班", "计算机2班", "计算机3班", "软件1班", "软件2班", "数学1班", "英语1班"};
        Calendar cal = Calendar.getInstance();
        List<JQuickRow> rows = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            JQuickRow row = new JQuickRow();
            row.put("id", String.format("S%08d", i + 1));
            row.put("name", "学生_" + (i + 1));
            row.put("gender", i & 1);
            row.put("age", 18 + r.nextInt(8));
            cal.set(2018 + (i % 5), Calendar.SEPTEMBER, 1 + (i % 28));
            row.put("enrollmentDate", cal.getTime());
            row.put("className", classes[i % classes.length]);
            row.put("ignoreField", "true");
            rows.add(row);
        }
        return rows;
    }

    private static long memUsedBytes() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    private static String fmtSize(long bytes) {
        if (bytes < 1024) return bytes + "B";
        if (bytes < 1024 * 1024) return String.format("%.1fKB", bytes / 1024.0);
        if (bytes < 1024L * 1024 * 1024) return String.format("%.1fMB", bytes / 1024.0 / 1024.0);
        return String.format("%.1fGB", bytes / 1024.0 / 1024.0 / 1024.0);
    }

    @Test
    public void runXmlExporterBench() throws Exception {
        Files.createDirectories(OUT_DIR);
        benchmarkXmlExporter();
    }

    @Test
    public void runPrepareImportFixtures() throws Exception {
        Files.createDirectories(OUT_DIR);
        prepareImportFixture(10_000);
        prepareImportFixture(50_000);
        prepareImportFixture(100_000);
    }

    /**
     * 仅生成 studentbigdata.xlsx（默认 100_000 行），适合单独执行。
     * 如需调整行数可修改常量 STUDENT_BIGDATA_ROWS 或直接调用 generateStudentBigData(行数值)。
     */
    @Test
    public void runGenerateStudentBigData() throws Exception {
        Files.createDirectories(STUDENT_BIGDATA_DIR);
        generateStudentBigData(STUDENT_BIGDATA_ROWS);
    }

    @Test
    public void runXmlImporterBench() throws Exception {
        Files.createDirectories(OUT_DIR);
        Files.createDirectories(STUDENT_BIGDATA_DIR);
        // 确保夹具存在
        for (int size : new int[]{10_000, 50_000, 100_000}) {
            if (!Files.exists(OUT_DIR.resolve("xml_import_fixture_" + size + ".xlsx"))) {
                prepareImportFixture(size);
            }
        }
        if (!Files.exists(STUDENT_BIGDATA_DIR.resolve(STUDENT_BIGDATA_NAME))) {
            generateStudentBigData(STUDENT_BIGDATA_ROWS);
        }
        benchmarkXmlImporter();
    }

    /**
     * 针对 studentbigdata.xlsx 单独运行一次导入测试（仅 OPCackage=ON 场景），
     * 便于用户快速验证 runXmlImport 大文件路径。
     */
    @Test
    public void runXmlImportOnlyStudentBigData() throws Exception {
        Path big = STUDENT_BIGDATA_DIR.resolve(STUDENT_BIGDATA_NAME);
        if (!Files.exists(big)) {
            generateStudentBigData(STUDENT_BIGDATA_ROWS);
        }
        byte[] bytes = Files.readAllBytes(big);
        JQuickExcelConfig.resetDefault();
        JQuickExcelConfig.getInstance().setBigFileImportEnabled(true);
        XmlImportResult r = runXmlImport(bytes, STUDENT_BIGDATA_ROWS, "ON", true);
        System.out.println();
        System.out.println("==== studentbigdata.xlsx 导入完成 ====");
        System.out.printf("文件: %s / %,d 行%n", big, STUDENT_BIGDATA_ROWS);
        System.out.printf("耗时: %,d ms%n", r.timeMs);
        System.out.printf("峰值内存: %.1f MB%n", r.peakMemMB);
        System.out.printf("实际读取: %,d 行%n", r.rows);
    }

    @Test
    public void runAll() throws Exception {
        Files.createDirectories(OUT_DIR);
        Files.createDirectories(STUDENT_BIGDATA_DIR);
        main(new String[0]);
    }

    /**
     * 导入夹具：通过注解对齐 importExcel 配置的 SHEET / 列名 / 格式
     * SHEET='Sheet1', headRowNumber=1 (1-based 第 1 行表头)
     * MAPPING={"学号":"no","姓名":"name","性别":"sex","年龄":"age","出生日期":"birthday"}
     */
    @JExcelSheet(name = "Sheet1", headRowNumber = 1)
    public static class ImportFixture {
        @JExcelColumn(name = "学号", index = 0)
        private String no;
        @JExcelColumn(name = "姓名", index = 1)
        private String name;
        @JExcelColumn(name = "性别", index = 2)
        private String sex;
        @JExcelColumn(name = "年龄", index = 3)
        private Integer age;
        @JExcelColumn(name = "出生日期", index = 4, format = "yyyy-MM-dd")
        private Date birthday;

        public ImportFixture() {
        }

        public ImportFixture(String no, String name, String sex, Integer age, Date birthday) {
            this.no = no;
            this.name = name;
            this.sex = sex;
            this.age = age;
            this.birthday = birthday;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }

    private static class BenchmarkFile {
        final int rows;
        final Path path;
        final boolean isStudentBigdata;

        BenchmarkFile(int rows, Path path) {
            this(rows, path, false);
        }

        BenchmarkFile(int rows, Path path, boolean isStudentBigdata) {
            this.rows = rows;
            this.path = path;
            this.isStudentBigdata = isStudentBigdata;
        }
    }

    private static class Result {
        final long timeMs;
        final double peakMemMB;
        final double fileSizeKB;
        final String note;

        Result(long timeMs, double peakMemMB, double fileSizeKB, String note) {
            this.timeMs = timeMs;
            this.peakMemMB = peakMemMB;
            this.fileSizeKB = fileSizeKB;
            this.note = note;
        }
    }

    private static class XmlImportResult {
        final long timeMs;
        final double peakMemMB;
        final int rows;
        final String note;

        XmlImportResult(long timeMs, double peakMemMB, int rows, String note) {
            this.timeMs = timeMs;
            this.peakMemMB = peakMemMB;
            this.rows = rows;
            this.note = note;
        }
    }
}
