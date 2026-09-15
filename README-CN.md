<p align="center">
  <img src="src/main/resources/static/jquick-logo.svg" width="180" alt="JQuick-Excel 徽标" />
</p>

<h1 align="center">JQuick-Excel</h1>

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.paohaijiao/jquick-excel"><img src="https://img.shields.io/maven-central/v/io.github.paohaijiao/jquick-excel.svg?label=Maven%20Central" alt="Maven Central" /></a>
  <a href="./LICENSE"><img src="https://img.shields.io/badge/license-Apache--2.0-blue.svg" alt="许可证" /></a>
  <img src="https://img.shields.io/badge/JDK-8%2B-orange.svg" alt="JDK 8+" />
  <a href="https://github.com/akullpp/awesome-java"><img src="https://awesome.re/mentioned-badge.svg" alt="Awesome Java" /></a>

</p>


<p align="center">
  <a href="./README.md">英文</a> | <b>简体中文</b>
</p>


[![Awesome Java](https://img.shields.io/badge/Awesome-Java-ff69b4.svg)](https://github.com/akullpp/awesome-java)
> 已被收录至 [Awesome Java](https://github.com/akullpp/awesome-java) 的 **Document Processing** 精选章节


# 项目简介

JQuick-Excel 是一个轻量级 Java 框架，用于导入和导出 `xls` 与 `xlsx` 工作簿。它将 XML 服务定义与声明式 DSL 结合，用于字段映射、类型转换、校验、公式、样式、合并汇总、图表和页脚。

XML 定义使 Excel 规则与服务契约保持一致。Java 调用方根据定义创建代理，并将业务数据、流和上下文值传入导入或导出操作。

## 支持的工作流

- 将工作表导入为 `List<JQuickRow>`。
- 将业务数据导出为工作簿。
- 按名称或索引选择工作表。
- 双向映射表头和字段。
- 使用上下文表达式转换值。
- 校验单元格、行、列和矩形范围。
- 生成公式、样式、合并、图表和页脚。

## 设计目标

DSL 用于表达清晰的配置，而不是生成 Java 等价代码。请准确保留规则关键字、分隔符和范围语法。使用 XML 绑定服务，使用 Java 提供流、数据对象和字典。

# 核心特性

- 支持 `xls` 与 `xlsx` 双格式工作簿。
- 通过 `IMPORT WITH` 和 `EXPORT WITH` 声明导入导出规则。
- 支持字段映射、格式化和求值器转换。
- 提供 20 个内置校验规则和明确的范围作用域。
- 提供数学、日期时间、文本、逻辑和查找公式。
- 通过 `GRAPH` DSL 配置 10 种图表。
- 支持行、列、单元格和范围样式。
- 支持 9 种合并聚合策略。
- 提供 42 个内置导出主题。
- 支持流式导出、大文件导入和样式缓存配置。

## 导入能力

导入可以选择工作表、识别第一行为表头、将表头映射到字段、转换值，并在消费结果前校验指定范围。

## 导出能力

导出可以将源字段映射到表头，并在一个规则中加入格式、公式、样式、合并汇总、图表和页脚。

## 配置能力

`JQuickExcelConfig` 统一管理流式导出、导入内存行为和单元格样式缓存，可在多次操作前集中设置。

# 快速开始

## Maven

```xml
<dependency>
  <groupId>io.github.paohaijiao</groupId>
  <artifactId>jquick-excel</artifactId>
  <version>3.6.0</version>
</dependency>
```

## Gradle

```gradle
implementation 'io.github.paohaijiao:jquick-excel:3.6.0'
```

## XML 配置

在 resources 目录创建 `jquick-excel.xml`。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE excels PUBLIC "-//PAOHAIJIAO//DTD API EXCEL 1.0//EN"
        "classpath:paohaijiao/dtd/Jquick-excel.dtd">
<excels namespace="com.github.paohaijiao.xml.service.JQuickExcelExportService">
    <excel name="exportExcel" returnClass="void">
        <![CDATA[
            EXPORT WITH
            SHEET="Students",
            HEADER=true,
            MAPPING={"id":"ID","name":"Name","age":"Age"}
        ]]>
    </excel>
    <excel name="importExcel" returnClass="java.util.List">
        <![CDATA[
            IMPORT WITH
            SHEET="Students",
            HEADER=true,
            MAPPING={"ID":"id","Name":"name","Age":"age"}
        ]]>
    </excel>
</excels>
```

## 服务接口

XML 的 `namespace` 指定作为代理暴露的服务接口。

```java
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

public interface JQuickExcelExportService {
    void exportExcel(@Param("field") String field, @Param("value") String value);

    List<JQuickRow> importExcel(@Param("field") String field, @Param("value") String value);
}
```

## 通过 XML 导出

导出解析器接收转换后的行和输出流。`JQuickXmlFactory` 根据 XML 文件创建服务代理。

```java
import com.github.paohaijiao.convert.JObjectConverter;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.JQuickFactory;
import com.github.paohaijiao.xml.JQuickXmlFactory;
import com.github.paohaijiao.xml.parse.JQuickParseHandler;
import com.github.paohaijiao.xml.parse.excel.JQuickExcelExportXmlParseFactory;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmlExportExample {
    public static void main(String[] args) throws Exception {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> student = new LinkedHashMap<>();
        student.put("id", "1001");
        student.put("name", "Alice");
        student.put("age", 20);
        data.add(student);

        List<JQuickRow> rows = JQuickRow.toRows(JObjectConverter.convert(data));
        try (OutputStream output = new FileOutputStream("students.xlsx")) {
            JQuickParseHandler parser = new JQuickExcelExportXmlParseFactory(rows, output);
            JQuickFactory factory = new JQuickXmlFactory(parser, "jquick-excel.xml");
            JQuickExcelExportService service = factory.createApi(JQuickExcelExportService.class);
            service.exportExcel("field", "value");
        }
    }
}
```

## 通过 XML 导入

导入解析器接收 `JContext` 和输入流。上下文值可供 DSL 转换使用。

```java
import com.github.paohaijiao.context.JContext;
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.JQuickFactory;
import com.github.paohaijiao.xml.JQuickXmlFactory;
import com.github.paohaijiao.xml.parse.JQuickParseHandler;
import com.github.paohaijiao.xml.parse.excel.JQuickExcelImportXmlParseFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlImportExample {
    public static void main(String[] args) {
        try (InputStream input = XmlImportExample.class.getClassLoader()
                .getResourceAsStream("students.xlsx")) {
            Map<String, Object> gender = new HashMap<>();
            gender.put("Male", "1");
            gender.put("Female", "2");
            JContext context = new JContext();
            context.put("dict", gender);

            JQuickParseHandler parser = new JQuickExcelImportXmlParseFactory(context, input);
            JQuickFactory factory = new JQuickXmlFactory(parser, "jquick-excel.xml");
            JQuickExcelExportService service = factory.createApi(JQuickExcelExportService.class);
            List<JQuickRow> rows = service.importExcel("field", "value");
            System.out.println(rows.size());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
```

# 使用细节

## 读取 Excel

使用 `IMPORT WITH` 选择工作表、识别表头、映射字段、转换值并校验输入。

```string
    IMPORT WITH
    SHEET="Students",
    HEADER=true,
    MAPPING={"ID":"id","Name":"name","Age":"age"},
    TRANSFORM={"name":toUpper(${name})}
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `SHEET` | 按名称或索引选择工作表。 | `SHEET="Students"` |
| `HEADER` | 声明第一行是否为表头。 | `HEADER=true` |
| `MAPPING` | 将表头映射到目标字段。 | `MAPPING={"ID":"id"}` |
| `TRANSFORM` | 转换导入值。 | `TRANSFORM={"name":toUpper(${name})}` |
| `VALIDATION` | 对选定范围应用规则。 | `VALIDATION={C2:C4:{integer{required:true}}}` |

### 校验范围

校验和公式目标支持行、列、单元格及矩形范围语法。

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| 行 | 单行或行区间。 | `ROW 5`、`ROW 1..10` |
| 列 | 单列或列区间。 | `COL A`、`COL A..D` |
| 单元格 | 一个单元格。 | `C1` |
| 范围 | 矩形单元格区域。 | `A1:B5` |

```string
IMPORT WITH VALIDATION={
  ROW 2..100:{
    integer{required:true,msg:'年龄必须是整数'}
  },
  C2:C100:{
    email{required:true,msg:'邮箱格式无效'}
  }
}
```

### 校验规则

所有校验规则均使用三列表形式，按规则需要提供 `required`、`msg` 和 `map`。

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `boolean` | 校验布尔值。 | `boolean{required:true}` |
| `date_format` | 校验日期字符串格式。 | `date_format{required:true,map:{'format':'yyyy-MM-dd'}}` |
| `max_date` | 校验日期上限。 | `max_date{required:true,map:{'format':'yyyy-MM-dd',maxDate:2025-01-01}}` |
| `min_date` | 校验日期下限。 | `min_date{required:true,map:{'format':'yyyy-MM-dd',minDate:2022-01-01}}` |
| `integer` | 校验整数。 | `integer{required:true}` |
| `decimal` | 校验小数。 | `decimal{required:true}` |
| `max_value` | 校验数值上限。 | `max_value{required:true,map:{'maxValue':50}}` |
| `min_value` | 校验数值下限。 | `min_value{required:true,map:{'minValue':2}}` |
| `dict` | 校验字典成员关系。 | `dict{required:true,map:{'1':'Male','2':'Female'}}` |
| `email` | 校验邮箱地址。 | `email{required:true}` |
| `mobile` | 校验手机号码。 | `mobile{required:true}` |
| `max_length` | 校验字符串最大长度。 | `max_length{required:true,map:{'maxLength':7}}` |
| `min_length` | 校验字符串最小长度。 | `min_length{required:true,map:{'minLength':1}}` |
| `regex` | 校验正则表达式。 | `regex{required:true,map:{pattern:'^\\d+$'}}` |
| `start_with` | 要求指定前缀。 | `start_with{required:true,map:{startWith:'A'}}` |
| `not_start_with` | 禁止指定前缀。 | `not_start_with{required:true,map:{notStartWith:'A'}}` |
| `end_with` | 要求指定后缀。 | `end_with{required:true,map:{endWith:'Z'}}` |
| `not_end_with` | 禁止指定后缀。 | `not_end_with{required:true,map:{notEndWith:'Z'}}` |
| `contain` | 要求包含子串。 | `contain{required:true,map:{contains:'key'}}` |
| `not_contain` | 禁止包含子串。 | `not_contain{required:true,map:{notContain:'key'}}` |

## 写入 Excel

使用 `EXPORT WITH` 定义工作表、表头、映射、格式、转换、公式、样式、合并、图表和页脚。

```string
    EXPORT WITH
    SHEET="Report",
    HEADER=true,
    MAPPING={"id":"ID","name":"Name","amount":"Amount"},
    FORMAT={"amount":"currency"},
    TRANSFORM={"name":toUpper(${name})},
    FOOTER="Generated by JQuickExcel"
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `SHEET` | 选择目标工作表。 | `SHEET="Report"` |
| `HEADER` | 控制表头生成。 | `HEADER=true` |
| `FORMAT` | 定义 Excel 显示格式。 | `FORMAT={"amount":"currency"}` |
| `MAPPING` | 将源字段映射到表头。 | `MAPPING={"id":"ID"}` |
| `TRANSFORM` | 在输出前转换值。 | `TRANSFORM={"name":toUpper(${name})}` |
| `FORMULAS` | 向目标写入公式。 | `FORMULAS={D2:'SUM(B2:C2)'}` |
| `STYLE` | 对目标应用样式。 | `STYLE={ROW 1:{bold:true}}` |
| `MERGE` | 定义合并聚合。 | `MERGE:{ROWS 1..1,COLS A..D WITH FIRST}` |
| `GRAPH` | 定义图表数据和类型。 | `GRAPH={TYPE=PIE,TITLE="Revenue",CATEGORIES=["Q1"],SERIES=[{NAME="2023",DATA=[1]}]}` |
| `FOOTER` | 设置页脚文本。 | `FOOTER="Generated by JQuickExcel"` |

## 大数据导出

`JQuickExcelConfig` 全局配置流式导出、大文件导入和样式缓存。

```java
JQuickExcelConfig cfg = JQuickExcelConfig.getInstance();
cfg.setStreamingExportEnabled(true)
   .setStreamingRowAccessWindowSize(100)
   .setStreamingExportThreshold(5000)
   .setStreamingCompressTempFiles(true)
   .setBigFileImportEnabled(true)
   .setImportBatchThreshold(20000)
   .setCellStyleCacheEnabled(true);
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `streamingExportEnabled` | 启用 SXSSF 流式导出。 | `setStreamingExportEnabled(true)` |
| `streamingRowAccessWindowSize` | 设置内存中保留的行数。 | `setStreamingRowAccessWindowSize(100)` |
| `streamingExportThreshold` | 设置启用流式处理的行数阈值。 | `setStreamingExportThreshold(5000)` |
| `streamingCompressTempFiles` | 压缩流式临时文件。 | `setStreamingCompressTempFiles(true)` |
| `bigFileImportEnabled` | 启用 OPCPackage 共享解析。 | `setBigFileImportEnabled(true)` |
| `importBatchThreshold` | 设置推荐批量阈值。 | `setImportBatchThreshold(20000)` |
| `cellStyleCacheEnabled` | 启用单元格样式缓存。 | `setCellStyleCacheEnabled(true)` |

大文件导入建议按批次消费，避免一次性保留全部行。

```java
int total = handler.importDataInBatch(model, 5000, batch -> {
    process(batch);
    return true;
});
```

## 模板渲染

使用 `TRANSFORM`、上下文值和 `FOOTER` 表达式，根据业务数据渲染输出。

```string
    EXPORT WITH
    MAPPING={"name":"Name","gender":"Gender"},
    TRANSFORM={"gender":trans(${dict},${gender})},
    FOOTER="Generated by JQuickExcel on ${current_date()}"
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `TRANSFORM` | 对字段应用求值器表达式。 | `TRANSFORM={"gender":trans(${dict},${gender})}` |
| `FOOTER` | 渲染文本或变量表达式。 | `FOOTER="Generated on ${current_date()}"` |
| `FORMAT` | 应用 Excel 显示格式。 | `FORMAT={"date":"yyyy-MM-dd"}` |

## 样式设置

`STYLE` 接受行、列、单元格或范围目标。

```string
EXPORT WITH STYLE={
  ROW 1:{
    fontName:Arial,
    fontHeightInPoints:12,
    italic:true,
    color:yellow,
    bold:true
  },
  A2:C100:{
    alignment:center,
    wrapText:true,
    borderBottom:thin,
    fillForegroundColor:lightYellow
  }
}
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `fontName` | 设置字体族。 | `fontName:Arial` |
| `fontHeightInPoints` | 设置磅值字号。 | `fontHeightInPoints:12` |
| `bold` | 启用粗体。 | `bold:true` |
| `italic` | 启用斜体。 | `italic:true` |
| `color` | 设置字体颜色。 | `color:yellow` |
| `alignment` | 设置水平对齐。 | `alignment:center` |
| `verticalAlignment` | 设置垂直对齐。 | `verticalAlignment:center` |
| `wrapText` | 启用自动换行。 | `wrapText:true` |
| `borderLeft` | 设置左边框。 | `borderLeft:thin` |
| `borderRight` | 设置右边框。 | `borderRight:thin` |
| `borderTop` | 设置上边框。 | `borderTop:thin` |
| `borderBottom` | 设置下边框。 | `borderBottom:thin` |
| `fillPattern` | 设置填充模式。 | `fillPattern:solid_foreground` |
| `fillForegroundColor` | 设置前景填充色。 | `fillForegroundColor:yellow` |
| `fillBackgroundColor` | 设置背景填充色。 | `fillBackgroundColor:white` |
| `dataFormatString` | 设置单元格显示模式。 | `dataFormatString:"yyyy-MM-dd"` |

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| 边框 | 无边框。 | `borderBottom:none` |
| 边框 | 细边框。 | `borderBottom:thin` |
| 边框 | 中等边框。 | `borderBottom:medium` |
| 边框 | 虚线边框。 | `borderBottom:dashed` |
| 边框 | 点线边框。 | `borderBottom:dotted` |
| 边框 | 粗边框。 | `borderBottom:thick` |
| 边框 | 双线边框。 | `borderBottom:double` |
| 填充 | 无填充。 | `fillPattern:no_fill` |
| 填充 | 实心前景填充。 | `fillPattern:solid_foreground` |
| 填充 | 细点填充。 | `fillPattern:fine_dots` |
| 填充 | 稀疏点填充。 | `fillPattern:sparse_dots` |
| 填充 | 细横线填充。 | `fillPattern:thin_horz_bands` |
| 填充 | 细竖线填充。 | `fillPattern:thin_vert_bands` |

## 主题模板

JQuick-Excel 提供 42 个内置主题。执行规则后、创建导出处理器前，可在 `JExcelExportModel` 上设置主题。

```java
JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
config.setTheme("jade");
JExcelExportHandler handler = new JExcelExportHandler(config, data);
Workbook workbook = handler.getWorkBook();
workbook.write(outputStream);
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `default`、`minimalistGrey`、`slate` | 经典和灰色主题。 | `setTheme("default")` |
| `charcoal`、`navyBlue`、`oceanBlue` | 深色和海洋蓝主题。 | `setTheme("oceanBlue")` |
| `skyBlue`、`azure`、`steelBlue` | 浅蓝和钢蓝主题。 | `setTheme("skyBlue")` |
| `denim`、`indigo`、`periwinkle` | 牛仔蓝和靛蓝主题。 | `setTheme("indigo")` |
| `forestGreen`、`emerald`、`jade` | 森林绿和翡翠绿主题。 | `setTheme("jade")` |
| `mintFresh`、`sage`、`oliveGreen` | 薄荷和低饱和绿主题。 | `setTheme("mintFresh")` |
| `tropicalTeal`、`turquoise`、`cyan` | 青绿色和青色主题。 | `setTheme("turquoise")` |
| `sunsetOrange`、`coral`、`peach` | 橙色和蜜桃主题。 | `setTheme("coral")` |
| `crimsonRed`、`wineRed`、`sakuraPink` | 深红、酒红和樱花主题。 | `setTheme("crimsonRed")` |
| `roseQuartz`、`lavenderPurple`、`amethyst` | 粉色和紫色主题。 | `setTheme("amethyst")` |
| `plum`、`royalGold`、`champagne` | 紫梅和金色主题。 | `setTheme("royalGold")` |
| `amber`、`mustard`、`bronze` | 琥珀、芥末和青铜主题。 | `setTheme("amber")` |
| `vintageSepia`、`espresso`、`mahogany` | 复古棕和深棕主题。 | `setTheme("espresso")` |
| `terracotta`、`midnightDark`、`pearl` | 陶土、深色和珍珠主题。 | `setTheme("midnightDark")` |

## 类型转换与 TRANSFORM 函数

`TRANSFORM` 为每一行计算表达式。`${field}` 读取当前行字段，`${key}` 可以读取 `JContext` 中的值，求值后的参数会传给求值器或 SPI provider。`FORMAT` 独立负责转换后的 Excel 单元格显示格式。

```java
Map<String, Object> gender = new HashMap<>();
gender.put("1", "Male");
gender.put("0", "Female");

JContext context = new JContext();
context.put("dict", gender);
```

```string
EXPORT WITH
FORMAT={"enrollmentDate":"yyyy-MM-dd"},
TRANSFORM={
  "name":toUpper(${name}),
  "enrollmentDate":formatDate(${enrollmentDate},'yyyy-MM-dd'),
  "gender":translate(${dict},${gender},'gender','Unknown')
}
```

### 求值器函数与 SPI 注册

JQuick-Excel 表达式可以使用现有求值器函数 `toUpper`、`dateFormat` 和 `trans`。外部 provider 目录维护在 [jquick-transform-function](https://github.com/paohaijiao/jquick-transform-function)。需要 SPI provider 时加入以下依赖。

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `toUpper` | 现有求值器，将文本转换为大写。 | `toUpper(${name})` |
| `dateFormat` | 现有求值器，格式化日期值。 | `dateFormat(${enrollmentDate},'yyyy-MM-dd')` |
| `trans` | 现有求值器，通过 `JContext` 字典映射值。 | `trans(${dict},${gender})` |

```xml
<dependency>
  <groupId>io.github.paohaijiao</groupId>
  <artifactId>jquick-transform-function</artifactId>
  <version>1.4.0</version>
</dependency>
```

Provider 实现 `com.github.paohaijiao.function.core.JQuickMethodFunctionProvider`：

```java
public interface JQuickMethodFunctionProvider {
    String getMethodName();
    Object invoke(List<Object> args);
    default String getDescription() { return getMethodName(); }
    default List<Class<?>> getParameterTypes() { return Collections.emptyList(); }
    default int getPriority() { return 5000; }
}
```

每个 provider 实现都列在 `META-INF/services/com.github.paohaijiao.function.core.JQuickMethodFunctionProvider` 中，由 Java SPI 发现并加载。`JQuickMethodInvocationManager` 调用 `ServiceLoader.loadServicesByPriority(...)`，按照 `getMethodName()` 建立注册表，再将求值后的参数列表传给匹配的 provider。`JFunctionExecutor` 暴露 `apply(List<Object> args)` 执行契约。自定义函数可以使用 `registerInvoker(...)` 注册，也可以使用 `registerOrReplaceInvoker(...)` 明确覆盖已有函数。

运行链路为：

```text
当前行字段或 JContext 值
    -> ${field}
    -> TRANSFORM 表达式
    -> 函数查找
    -> JQuickMethodFunctionProvider.invoke(List<Object>)
    -> 转换结果
    -> FORMAT 控制 Excel 显示
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `JContext` | 提供字典和外部上下文值。 | `context.put("dict", gender)` |
| `${field}` | 读取当前行字段值。 | `${gender}` |
| `TRANSFORM` | 在导入或导出前计算值表达式。 | `TRANSFORM={"name":toUpper(${name})}` |
| `JQuickMethodFunctionProvider` | 可调用函数的 SPI 契约。 | `getMethodName()` 和 `invoke(args)` |
| `META-INF/services/...` | Java SPI provider 描述文件。 | `META-INF/services/com.github.paohaijiao.function.core.JQuickMethodFunctionProvider` |
| `getPriority` | 提供 provider 加载优先级。 | `return 5000;` |
| `JQuickMethodInvocationManager` | 查找并调用已注册函数。 | `manager.invoke("toUpper", args)` |
| `JFunctionExecutor` | 定义函数执行适配契约。 | `executor.apply(args)` |
| `FORMAT` | 控制最终 Excel 显示格式。 | `FORMAT={"enrollmentDate":"yyyy-MM-dd"}` |

### 完整 TRANSFORM 函数目录

以下目录包含 `jquick-transform-function` 发布的全部 248 个函数。可选参数使用 `?`，`...` 表示可变参数。

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `array` | 数组和列表判断。 | `isArray(value)` |
| `bit` | 位运算。 | `bitAnd(a,b)`、`bitOr(a,b)`、`bitXor(a,b)` |
| `bool` | 布尔判断。 | `isBoolean(value)` |
| `business` | 银行卡、邮箱、性别、身份证和手机处理及校验。 | `bankCardMask(cardNo,keepStart?,keepEnd?)`；`bankCardValidate(cardNo)`；`emailMask(email)`；`genderName(code)`；`idCardAge(idCard,referenceDate?)`；`idCardBirthday(idCard,pattern?)`；`idCardGender(idCard,format?)`；`idCardInfo(idCard,field?)`；`idCardValidate(idCard)`；`phoneInfo(phone,field?)`；`phoneMask(phone,keepStart?,keepEnd?)`；`phoneValidate(phone)`；`isEmail(value)` |
| `collection` | 空值判断、集合拼接和长度。 | `isEmpty(value)`；`join(list,delimiter)`；`size(value)` |
| `condition` | 比较、范围、分支、空值默认值和多分支判断。 | `between(value,min,max,inclusive?)`；`caseWhen(condition1,result1,...,defaultResult)`；`coalesce(value1,value2,...)`；`defaultIfNull(value,defaultValue)`；`eq(a,b,ignoreCase?)`；`gte(a,b)`；`gt(a,b)`；`ifElse(condition1,value1,...,defaultValue)`；`if(condition,trueValue,falseValue)`；`lte(a,b)`；`lt(a,b)`；`ne(a,b,ignoreCase?)`；`nvl(value,defaultValue)`；`switch(value,case1,result1,...,defaultValue)` |
| `convert` | 布尔、日期、日期时间和 short 类型转换。 | `toBoolean(value,defaultValue?)`；`toDate(value,pattern?)`；`toDateTime(value,pattern?)`；`toShort(value,defaultValue?)` |
| `date` | 日期运算、提取、比较、边界、格式化和当前时间。 | `addDays(date,days)`；`addHours(datetime,hours)`；`addMinutes(datetime,minutes)`；`addMonths(date,months)`；`addSeconds(datetime,seconds)`；`addYears(date,years)`；`age(birthDate,referenceDate?)`；`day(date?)`；`dayOfWeek(date?,locale?)`；`dayOfYear(date?)`；`daysBetween(date1,date2)`；`endOfDay(date?)`；`endOfMonth(date?)`；`endOfYear(date?)`；`hour(datetime?)`；`hoursBetween(datetime1,datetime2)`；`isAfter(date1,date2)`；`isBefore(date1,date2)`；`isDate(value)`；`isLeapYear(year?)`；`isSameDay(date1,date2)`；`isWeekend(date?)`；`minute(datetime?)`；`month(date?)`；`monthsBetween(date1,date2)`；`second(datetime?)`；`startOfDay(date?)`；`startOfMonth(date?)`；`startOfYear(date?)`；`weekOfYear(date?)`；`year(date?)`；`yearsBetween(date1,date2)`；`formatDate(date,pattern)`；`now()`；`parseDate(dateStr,pattern)`；`timestamp()`；`today()`；`toIsoString(date)` |
| `geometry` | 几何、向量、矩阵、复数和数值映射。 | `areaCircle(radius)`；`areaRectangle(length,width)`；`areaTriangle(base,height)` 或 `areaTriangle(a,b,c)`；`circumference(radius)`；`clamp(value,min,max)`；`combination(n,k)`；`cross(x1,y1,x2,y2)`；`distance(x1,y1,x2,y2)` 或 `distance(x1,y1,z1,x2,y2,z2)`；`dot(vector1,vector2)`；`factorial(n)`；`fibonacci(n)`；`gcd(a,b,...)`；`hypot(x,y)`；`isPowerOfTwo(n)`；`isPrime(n)`；`lcm(a,b,...)`；`lerp(a,b,t)`；`map(value,fromLow,fromHigh,toLow,toHigh,clamp?)`；`permutation(n,k)`；`complexAdd(r1,i1,r2,i2)`；`complexMultiply(r1,i1,r2,i2)`；`matrixAdd(matrix1,matrix2)` |
| `extra` | 类型转换、数字格式化、集合构造和类型检查。 | `cast(value,targetClass)`；`formatNumber(number,pattern)`；`parseNumber(str,pattern)`；`toArray(value1,value2,...)`；`toCurrency(number,locale?)`；`toList(value1,value2,...)`；`toPercentage(number,decimals?)`；`typeOf(value)` |
| `json` | 对象序列化。 | `toJson(value)` |
| `math` | 四则运算、三角函数、常量、聚合、统计、进制和数值转换。 | `abs(value)`；`acos(value)`；`add(...)`；`asin(value)`；`atan(value)`；`atan2(y,x)`；`avg(...)`；`ceil(value)`；`ceilTo(value,places)`；`e()`；`pi()`；`cos(radians)`；`cosh(value)`；`divide(a,b,...)`；`exp(value)`；`expm1(value)`；`floor(value)`；`floorTo(value,places)`；`greatest(value1,value2,...)`；`isNumber(value)`；`least(value1,value2,...)`；`log(value)`；`log10(value)`；`log1p(value)`；`max(...)`；`median(numbers...)`；`min(...)`；`mode(numbers...)`；`mod(a,b)`；`multiply(...)`；`parseBinary(binaryStr)`；`parseHex(hexStr)`；`percentile(numbers...,percentile)`；`pow(base,exponent)`；`range(numbers...)`；`round(value)`；`roundTo(value,places)`；`signum(value)`；`sin(radians)`；`sinh(value)`；`sqrt(value)`；`stdDev(numbers...)`；`subtract(a,b,...)`；`tan(radians)`；`tanh(value)`；`toBinary(number)`；`toDegrees(radians)`；`toDouble(value,defaultValue?)`；`toFloat(value,defaultValue?)`；`toHex(number)`；`toInt(value,defaultValue?)`；`toLong(value,defaultValue?)`；`toNumberString(number,pattern?)`；`toOctal(number)`；`toRadians(degrees)`；`ulp(value)`；`variance(numbers...)`；`countDistinct(...)`；`count(...)`；`countNonNull(...)`；`product(...)`；`sum(...)` |
| `random` | 随机值、随机选择、颜色、日期、数组和 UUID。 | `randomBoolean()` 或 `randomBoolean(trueProbability?)`；`randomChoice(list)` 或 `randomChoice(elem1,elem2,...)`；`randomDouble()` 或 `randomDouble(min,max)`；`random(arr)`；`randomInt()`、`randomInt(max)` 或 `randomInt(min,max)`；`randomIntArray(size,min,max)`；`randomLong()`、`randomLong(max)` 或 `randomLong(min,max)`；`randomSample(list,count,allowRepeat?)`；`shuffle(list)`；`randomString(length)`；`randomUUID(withoutDashes?)`；`randomColor(type?)`；`randomDate(startDate,endDate,pattern?)` |
| `string` | 字符串比较、查找、填充、脱敏、转义、编码、替换、大小写转换和聚合。 | `abbreviate(str,maxWidth,ellipsis?)`；`capitalize(str)`；`centerPad(str,size,padChar?)`；`compareTo(str1,str2,ignoreCase?)`；`concat(...)`；`contains(str,sub)`；`tokenize(str,delimiters)`；`countChar(str,ch,ignoreCase?)`；`countMatches(str,sub,ignoreCase?)`；`equalsAny(str,target1,target2,...)`；`equalsIgnoreCase(str1,str2)`；`escapeHtml(str)`；`escapeRegex(str)`；`format(pattern,arg1,arg2,...)`；`indexOf(str,search,fromIndex?)`；`isAlpha(str)`；`isAlphaNumeric(str)`；`isBlank(str)`；`isNumeric(str)`；`isString(value)`；`left(str,n)`；`leftPad(str,size,padChar?)`；`length(str)`；`levenshtein(str1,str2)`；`maskEmail(email)`；`mask(str,start,end,maskChar?)`；`matches(str,regex)`；`mid(str,start,length?)`；`removeDuplicates(str)`；`removeEnd(str,suffix,ignoreCase?)`；`removeStart(str,prefix,ignoreCase?)`；`removeWhitespace(str)`；`repeat(str,count,separator?)`；`repeatChar(ch,count)`；`replace(str,target,replacement)`；`replaceAll(str,regex,replacement)`；`reverse(str)`；`right(str,n)`；`rightPad(str,size,padChar?)`；`similarity(str1,str2)`；`split(str,regex)`；`splitByLength(str,chunkSize)`；`substring(str,beginIndex)` 或 `substring(str,beginIndex,endIndex)`；`substringAfter(str,separator)`；`substringBefore(str,separator)`；`substringBetween(str,open,close)`；`swapCase(str)`；`toCamelCase(str,firstUpper?)`；`toLower(str)`；`toSnakeCase(str)`；`toString(value,pattern?)`；`toUpper(str)`；`trim(str)`；`uncapitalize(str)`；`unescapeHtml(str)`；`uniqueChars(str)`；`wordCount(str)`；`base64Decode(encodedStr)`；`base64Encode(str)`；`decodeUrl(str)`；`encodeUrl(str)`；`md5(str)`；`groupConcat(delimiter,...)`；`stringAgg(delimiter,...)` |
| `translate` | 上下文字典翻译。 | `translate(context,code,dictType,defaultValue?)` |

目录来自链接仓库；只有加入 `jquick-transform-function` 依赖后才会加载这些函数。`SUM`、`IF`、`TODAY` 等内置 Excel 公式仍属于独立的 `FORMULAS` 功能；名称相似的 TRANSFORM 函数（例如 `sum(...)`）是 Java 表达式函数，不是 Excel 公式。

## 常见参数

### 导入 DSL

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `SHEET` | 源工作表选择器。 | `SHEET="Students"` |
| `HEADER` | 表头行声明。 | `HEADER=true` |
| `MAPPING` | 表头到字段的映射。 | `MAPPING={"ID":"id"}` |
| `TRANSFORM` | 导入值转换。 | `TRANSFORM={"name":toUpper(${name})}` |
| `VALIDATION` | 选定范围校验。 | `VALIDATION={C2:C4:{integer{required:true}}}` |

### 导出 DSL

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `SHEET` | 目标工作表选择器。 | `SHEET="Report"` |
| `HEADER` | 表头生成开关。 | `HEADER=true` |
| `FORMAT` | 输出格式规则。 | `FORMAT={"amount":"currency"}` |
| `MAPPING` | 字段到表头的映射。 | `MAPPING={"id":"ID"}` |
| `TRANSFORM` | 输出前转换。 | `TRANSFORM={"name":toUpper(${name})}` |
| `FORMULAS` | 公式目标和表达式。 | `FORMULAS={D2:'SUM(B2:C2)'}` |
| `STYLE` | 样式目标和属性。 | `STYLE={ROW 1:{bold:true}}` |
| `MERGE` | 合并目标和策略。 | `MERGE:{ROWS 1..1,COLS A..D WITH FIRST}` |
| `GRAPH` | 图表规格。 | `GRAPH={TYPE=PIE,TITLE="Revenue",CATEGORIES=["Q1"],SERIES=[{NAME="2023",DATA=[1]}]}` |
| `FOOTER` | 页脚文本。 | `FOOTER="Generated by JQuickExcel"` |

### 公式

公式目标支持 `ROW 5`、`ROW 1..10`、`COL A:`、`COL A..D:`、`C1:` 和 `A1:B5`。

#### 数学函数

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `ABS` | 绝对值。 | `ABS(D2)` |
| `AVERAGE` | 算术平均值。 | `AVERAGE(D2:D4)` |
| `COUNT` | 统计数值数量。 | `COUNT(D2:D4)` |
| `MAX` | 最大值。 | `MAX(D2:D4)` |
| `MIN` | 最小值。 | `MIN(D2:D4)` |
| `POWER` | 幂运算。 | `POWER(2,3)` |
| `RAND` | `[0,1)` 的随机值。 | `RAND()` |
| `RANK` | 范围内排名。 | `RANK(20,D2:D4)` |
| `ROUND` | 四舍五入。 | `ROUND(3.1415926,3)` |
| `SQRT` | 平方根。 | `SQRT(4)` |
| `STDEV` | 标准差。 | `STDEV(D2:D4)` |
| `SUM` | 数值求和。 | `SUM(D2:D4)` |

```string
EXPORT WITH FORMULAS={
  D5:'SUM(D2:D4)',
  E5:'ROUND(AVERAGE(D2:D4),2)',
  F5:'RANK(D2,D2:D4)'
}
```

#### 日期时间函数

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `DATETIME` | 创建日期和时间。 | `DATETIME(2023,5,15,14,30,0)` |
| `DAY` | 提取月份中的日。 | `DAY("2025-01-23")` |
| `DAYS` | 计算日期差。 | `DAYS("2025-01-23","2025-01-28")` |
| `EDATE` | 按月偏移日期。 | `EDATE("2025-01-23",3)` |
| `EOMONTH` | 返回月份末日。 | `EOMONTH("2025-01-23",3)` |
| `HOUR` | 提取小时。 | `HOUR(A1)` |
| `NETWORKDAYS` | 统计工作日。 | `NETWORKDAYS(A1,A2)` |
| `NOW` | 返回当前日期时间。 | `NOW()` |
| `TODAY` | 返回当前日期。 | `TODAY()` |
| `WORKDAY` | 按工作日偏移。 | `WORKDAY(A1,3,A2)` |
| `MINUTE` | 提取分钟。 | `MINUTE(A1)` |
| `MONTH` | 提取月份。 | `MONTH(A1)` |
| `SECOND` | 提取秒。 | `SECOND(A1)` |
| `TIME` | 创建时间值。 | `TIME(14,30,0)` |
| `WEEKDAY` | 返回星期编号。 | `WEEKDAY(A1,2)` |
| `WEEKNUM` | 返回周数。 | `WEEKNUM(A1,1)` |
| `YEAR` | 提取年份。 | `YEAR(A1)` |

```string
EXPORT WITH FORMULAS={
  D5:'TODAY()',
  E5:'NETWORKDAYS(A1,A2)',
  F5:'WEEKNUM(A1,1)'
}
```

#### 文本函数

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `CONCAT` | 拼接文本。 | `CONCAT(A1,B1)` |
| `EXACT` | 区分大小写比较文本。 | `EXACT("a","A")` |
| `FIND` | 区分大小写查找文本。 | `FIND("n","apple")` |
| `LEFT` | 从左侧提取文本。 | `LEFT("hello",2)` |
| `RIGHT` | 从右侧提取文本。 | `RIGHT("hello",2)` |
| `LEN` | 统计字符数。 | `LEN("text")` |
| `MID` | 按位置提取文本。 | `MID("apple",2,3)` |
| `SUBSTITUTE` | 替换匹配文本。 | `SUBSTITUTE("a-a","a","b")` |
| `TRIM` | 去除两端空格。 | `TRIM(" a ")` |
| `CONCATENATE` | 拼接多个值。 | `CONCATENATE("A",1,TRUE)` |
| `LOWER` | 转为小写。 | `LOWER("ExCeL")` |
| `PROPER` | 将单词首字母大写。 | `PROPER("john o'reilly")` |
| `REPLACE` | 按位置替换。 | `REPLACE("ABCD",2,2,"XY")` |
| `SEARCH` | 不区分大小写查找。 | `SEARCH("n","Banana",3)` |
| `TEXT` | 将值格式化为文本。 | `TEXT(0.25,"0.0%")` |
| `UPPER` | 转为大写。 | `UPPER("email")` |
| `VALUE` | 将文本转为数字。 | `VALUE("1000")` |

```string
EXPORT WITH FORMULAS={
  D5:'CONCAT(A1,B1)',
  E5:'TEXT(C2,"0.0%")',
  F5:'UPPER(B2)'
}
```

#### 逻辑和查询函数

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `IF` | 返回两个值之一。 | `IF(D2>0,"Yes","No")` |
| `AND` | 测试全部条件。 | `AND(TRUE,FALSE)` |
| `OR` | 测试任一条件。 | `OR(TRUE,FALSE)` |
| `LOOKUP` | 执行向量查询。 | `LOOKUP(22,D2:D4,C2:C4)` |

```string
EXPORT WITH FORMULAS={
  D5:'IF(D2>0,"Yes","No")',
  E5:'AND(A1>0,B1>0)',
  F5:'LOOKUP(22,D2:D4,C2:C4)'
}
```

### 图表

图表 DSL 使用以下完整结构，`CATEGORY_AXIS` 和 `VALUE_AXIS` 为可选项。

```string
EXPORT WITH GRAPH = {
    TYPE = LINE,
    TITLE = "Chart Title",
    CATEGORY_AXIS = "Category Axis",
    VALUE_AXIS = "Value Axis",
    CATEGORIES = ["Jan", "Feb", "Mar"],
    SERIES = [
        {
            NAME = "Series Name",
            DATA = [10, 20, 30]
        }
    ]
}
```

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `TYPE` | 选择图表类型。 | `TYPE = LINE` |
| `TITLE` | 设置图表标题。 | `TITLE = "Monthly Sales"` |
| `CATEGORY_AXIS` | 设置分类轴标题。 | `CATEGORY_AXIS = "Month"` |
| `VALUE_AXIS` | 设置数值轴标题。 | `VALUE_AXIS = "Amount"` |
| `CATEGORIES` | 提供分类值。 | `CATEGORIES = ["Jan", "Feb"]` |
| `SERIES` | 提供数据系列对象。 | `SERIES = [{NAME = "Sales", DATA = [10, 20]}]` |
| `NAME` | 设置一个系列名称。 | `NAME = "Sales"` |
| `DATA` | 设置一个系列数据数组。 | `DATA = [10, 20]` |

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `LINE` | 趋势分析折线图。 | `TYPE = LINE` |
| `COLUMN` | 垂直对比柱状图。 | `TYPE = COLUMN` |
| `BAR` | 水平对比条形图。 | `TYPE = BAR` |
| `BAR3D` | 三维条形图。 | `TYPE = BAR3D` |
| `PIE` | 比例展示饼图。 | `TYPE = PIE` |
| `AREA` | 累积趋势面积图。 | `TYPE = AREA` |
| `AREA3D` | 三维面积图。 | `TYPE = AREA3D` |
| `SCATTER` | 相关性散点图。 | `TYPE = SCATTER` |
| `RADAR` | 多维比较雷达图。 | `TYPE = RADAR` |
| `SURFACE` | 曲面数据图。 | `TYPE = SURFACE` |

#### LINE

```string
EXPORT WITH GRAPH = {
    TYPE = LINE,
    TITLE = "Monthly Trend",
    CATEGORY_AXIS = "Month",
    VALUE_AXIS = "Amount",
    CATEGORIES = ["Jan", "Feb", "Mar"],
    SERIES = [{NAME = "Sales", DATA = [120, 150, 180]}]
}
```

#### COLUMN

```string
EXPORT WITH GRAPH = {
    TYPE = COLUMN,
    TITLE = "Product Sales",
    CATEGORY_AXIS = "Product",
    VALUE_AXIS = "Amount",
    CATEGORIES = ["A", "B", "C"],
    SERIES = [{NAME = "Sales", DATA = [120, 200, 150]}]
}
```

#### BAR

```string
EXPORT WITH GRAPH = {
    TYPE = BAR,
    TITLE = "Quarter Ranking",
    CATEGORY_AXIS = "Quarter",
    VALUE_AXIS = "Amount",
    CATEGORIES = ["Q1", "Q2", "Q3"],
    SERIES = [{NAME = "Sales", DATA = [450, 520, 480]}]
}
```

#### BAR3D

```string
EXPORT WITH GRAPH = {
    TYPE = BAR3D,
    TITLE = "Product Sales 3D",
    CATEGORY_AXIS = "Product",
    VALUE_AXIS = "Amount",
    CATEGORIES = ["Laptop", "Phone", "Tablet"],
    SERIES = [{NAME = "Q1", DATA = [450, 680, 320]}]
}
```

#### PIE

```string
EXPORT WITH GRAPH = {
    TYPE = PIE,
    TITLE = "Market Share",
    CATEGORIES = ["A", "B", "C"],
    SERIES = [{NAME = "Share", DATA = [38.5, 22.3, 15.7]}]
}
```

#### AREA

```string
EXPORT WITH GRAPH = {
    TYPE = AREA,
    TITLE = "Quarterly Trend",
    CATEGORY_AXIS = "Quarter",
    VALUE_AXIS = "Amount",
    CATEGORIES = ["Q1", "Q2", "Q3"],
    SERIES = [{NAME = "Line A", DATA = [120, 150, 180]}]
}
```

#### AREA3D

```string
EXPORT WITH GRAPH = {
    TYPE = AREA3D,
    TITLE = "Quarterly Trend 3D",
    CATEGORY_AXIS = "Quarter",
    VALUE_AXIS = "Amount",
    CATEGORIES = ["Q1", "Q2", "Q3"],
    SERIES = [{NAME = "Line A", DATA = [120, 150, 180]}]
}
```

#### SCATTER

```string
EXPORT WITH GRAPH = {
    TYPE = SCATTER,
    TITLE = "Height and Weight",
    CATEGORY_AXIS = "Height",
    VALUE_AXIS = "Weight",
    CATEGORIES = ["160", "170", "180"],
    SERIES = [{NAME = "People", DATA = [55, 65, 75]}]
}
```

#### RADAR

```string
EXPORT WITH GRAPH = {
    TYPE = RADAR,
    TITLE = "Capability Review",
    CATEGORY_AXIS = "Capability",
    CATEGORIES = ["Coding", "Design", "Communication"],
    SERIES = [{NAME = "Member A", DATA = [90, 85, 70]}]
}
```

#### SURFACE

```string
EXPORT WITH GRAPH = {
    TYPE = SURFACE,
    TITLE = "Terrain Height",
    CATEGORY_AXIS = "X",
    VALUE_AXIS = "Y",
    CATEGORIES = ["1", "2", "3"],
    SERIES = [{NAME = "Y=1", DATA = [10, 15, 25]}]
}
```

### 合并策略

每个合并定义先指定行和列，再在 `WITH` 后使用聚合策略。

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| `MERGE_WITH_MAX` | 使用最大值。 | `COLS A..F WITH MAX` |
| `MERGE_WITH_MIN` | 使用最小值。 | `COLS A..F WITH MIN` |
| `MERGE_WITH_AVG` | 使用平均值。 | `COLS A..F WITH AVG` |
| `MERGE_WITH_SUM` | 对值求和。 | `COLS A..F WITH SUM` |
| `MERGE_WITH_FIRST` | 使用第一个值。 | `COLS A..F WITH FIRST` |
| `MERGE_WITH_LAST` | 使用最后一个值。 | `COLS A..F WITH LAST` |
| `MERGE_WITH_CONCAT` | 拼接值。 | `COLS A..F WITH CONCAT` |
| `MERGE_WITH_COUNT` | 统计值数量。 | `COLS A..F WITH COUNT` |
| `MERGE_WITH_VALUE` | 使用固定值。 | `COLS A..F WITH VALUE` |

```string
EXPORT WITH MERGE:{
  ROWS 1..1,
  COLS A..F WITH FIRST
}
```

## 依赖与版本兼容

Maven 坐标为 `io.github.paohaijiao:jquick-excel`，本文使用版本 `3.6.0`。JQuick-Excel 要求 Java 8 或更高版本，并支持 `xls` 与 `xlsx` 文件。

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| Java | 最低运行环境。 | `Java 8+` |
| 工作簿 | 传统 Excel 格式。 | `xls` |
| 工作簿 | Office Open XML 格式。 | `xlsx` |
| 依赖 | Maven 坐标。 | `io.github.paohaijiao:jquick-excel` |

# 注意事项与常见问题

| 参数 | 描述 | 用法示例 |
| --- | --- | --- |
| 大文件 | 高行数导出时启用流式处理。 | `setStreamingExportEnabled(true)` |
| 大文件导入 | 大输入使用批量消费。 | `importDataInBatch(model, 5000, batch -> true)` |
| 样式上限 | 通过缓存复用样式。 | `setCellStyleCacheEnabled(true)` |
| XML 位置 | 将 XML 定义放在类路径中。 | `jquick-excel.xml` |
| DSL 语法 | 保持关键字、花括号、分隔符和范围不变。 | `FORMULAS={D5:'SUM(D2:D4)'}` |
| 流关闭 | 关闭输入流和输出流。 | `try (OutputStream output = ...) {}` |
| 上下文 | 将字典放入 `JContext`。 | `context.put("dict", gender)` |
| 图表 | 每个 `DATA` 数组应与 `CATEGORIES` 对齐。 | `DATA = [10, 20, 30]` |

# 许可证

本项目采用 Apache License 2.0。
