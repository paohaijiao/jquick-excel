<p align="center">
  <img src="src/main/resources/static/jquick-logo.svg" width="180" alt="JQuick-Excel logo" />
</p>

<h1 align="center">JQuick-Excel</h1>

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.paohaijiao/jquick-excel"><img src="https://img.shields.io/maven-central/v/io.github.paohaijiao/jquick-excel.svg?label=Maven%20Central" alt="Maven Central" /></a>
  <a href="./LICENSE"><img src="https://img.shields.io/badge/license-Apache--2.0-blue.svg" alt="License" /></a>
  <img src="https://img.shields.io/badge/JDK-8%2B-orange.svg" alt="JDK 8+" />
</p>

<p align="center">
  <b>English</b> | <a href="./README-CN.md">Chinese</a>
</p>

[![Awesome Java](https://img.shields.io/badge/Awesome-Java-ff69b4.svg)](https://github.com/akullpp/awesome-java)
> Featured in the [Awesome Java](https://github.com/akullpp/awesome-java) curated list — **Document Processing**

# Project Introduction

JQuick-Excel is a lightweight Java framework for importing and exporting `xls` and `xlsx` workbooks. It combines an XML service definition with a declarative DSL for mapping, conversion, validation, formulas, styles, merge summaries, charts, and footers.

The XML definition keeps Excel rules close to the service contract. Java callers create a proxy from that definition and pass application data, streams, and context values into the import or export operation.

## Supported Workflows

- Import a worksheet into `List<JQuickRow>`.
- Export application data to a workbook.
- Select a sheet by name or index.
- Map headers and fields in either direction.
- Transform values with context-aware expressions.
- Validate cells, rows, columns, and rectangular ranges.
- Render formulas, styles, merges, charts, and footers.

## Design Goals

The DSL is intended for readable configuration rather than generated Java code. Keep rule keywords, delimiters, and range syntax exact. Use XML for service binding and Java for streams, data objects, and dictionaries.

# Core Features

- Dual workbook support for `xls` and `xlsx`.
- Declarative import and export rules through `IMPORT WITH` and `EXPORT WITH`.
- Field mapping, formatting, and evaluator-based transformations.
- Twenty built-in validation rules with explicit range scopes.
- Mathematical, date/time, text, logical, and lookup formulas.
- Ten graph types configured by `GRAPH` DSL.
- Row, column, cell, and range style settings.
- Nine merge aggregation strategies.
- Forty-two built-in export themes.
- Streaming export, large-file import, and style-cache configuration.

## Import Capabilities

Imports can select a worksheet, treat the first row as headers, map headers to fields, transform values, and validate the selected areas before the result is consumed.

## Export Capabilities

Exports can map source fields to headers and add output formatting, formulas, style rules, merge summaries, charts, and footer text in one rule.

## Configuration Capabilities

`JQuickExcelConfig` centralizes streaming export, import memory behavior, and cell-style cache settings. It can be configured once before repeated operations.

# Quick Start

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

## XML Configuration

Create `jquick-excel.xml` in the resources directory.

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

## Service Interface

The XML `namespace` names the service interface that is exposed as a proxy.

```java
import com.github.paohaijiao.statement.JQuickRow;
import com.github.paohaijiao.xml.param.Param;

import java.util.List;

public interface JQuickExcelExportService {

    void exportExcel(@Param("field") String field, @Param("value") String value);

    List<JQuickRow> importExcel(@Param("field") String field, @Param("value") String value);
}
```

## Export Through XML

The export parser receives converted rows and an output stream. `JQuickXmlFactory` creates the service proxy from the XML file.

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

## Import Through XML

The import parser receives a `JContext` and an input stream. Context values are available to DSL transformations.

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

# Usage Details

## Read Excel

Use `IMPORT WITH` to select a sheet, interpret headers, map fields, transform values, and validate input.

```string
    IMPORT WITH
    SHEET="Students",
    HEADER=true,
    MAPPING={"ID":"id","Name":"name","Age":"age"},
    TRANSFORM={"name":toUpper(${name})}
```

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `SHEET` | Selects a worksheet by name or index. | `SHEET="Students"` |
| `HEADER` | Declares whether the first row is a header. | `HEADER=true` |
| `MAPPING` | Maps headers to target fields. | `MAPPING={"ID":"id"}` |
| `TRANSFORM` | Converts imported values. | `TRANSFORM={"name":toUpper(${name})}` |
| `VALIDATION` | Applies rules to a selected scope. | `VALIDATION={C2:C4:{integer{required:true}}}` |

### Validation Scopes

Validation and formula targets support row, column, cell, and rectangle syntax.

| Parameter | Description | Usage Example |
| --- | --- | --- |
| Row | One row or a row interval. | `ROW 5`, `ROW 1..10` |
| Column | One column or a column interval. | `COL A`, `COL A..D` |
| Cell | One cell. | `C1` |
| Range | A rectangular cell range. | `A1:B5` |

```string
IMPORT WITH VALIDATION={
  ROW 2..100:{
    integer{required:true,msg:'Age must be an integer'}
  },
  C2:C100:{
    email{required:true,msg:'Invalid email'}
  }
}
```

### Validation Rules

All validation rules use the same three-column form. `required`, `msg`, and `map` are supplied as needed by a rule.

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `boolean` | Validates a boolean value. | `boolean{required:true}` |
| `date_format` | Validates a date string format. | `date_format{required:true,map:{'format':'yyyy-MM-dd'}}` |
| `max_date` | Validates a maximum date. | `max_date{required:true,map:{'format':'yyyy-MM-dd',maxDate:2025-01-01}}` |
| `min_date` | Validates a minimum date. | `min_date{required:true,map:{'format':'yyyy-MM-dd',minDate:2022-01-01}}` |
| `integer` | Validates an integer. | `integer{required:true}` |
| `decimal` | Validates a decimal number. | `decimal{required:true}` |
| `max_value` | Validates a numeric maximum. | `max_value{required:true,map:{'maxValue':50}}` |
| `min_value` | Validates a numeric minimum. | `min_value{required:true,map:{'minValue':2}}` |
| `dict` | Validates dictionary membership. | `dict{required:true,map:{'1':'Male','2':'Female'}}` |
| `email` | Validates an email address. | `email{required:true}` |
| `mobile` | Validates a mobile number. | `mobile{required:true}` |
| `max_length` | Validates maximum string length. | `max_length{required:true,map:{'maxLength':7}}` |
| `min_length` | Validates minimum string length. | `min_length{required:true,map:{'minLength':1}}` |
| `regex` | Validates a regular expression. | `regex{required:true,map:{pattern:'^\\d+$'}}` |
| `start_with` | Requires a prefix. | `start_with{required:true,map:{startWith:'A'}}` |
| `not_start_with` | Forbids a prefix. | `not_start_with{required:true,map:{notStartWith:'A'}}` |
| `end_with` | Requires a suffix. | `end_with{required:true,map:{endWith:'Z'}}` |
| `not_end_with` | Forbids a suffix. | `not_end_with{required:true,map:{notEndWith:'Z'}}` |
| `contain` | Requires a substring. | `contain{required:true,map:{contains:'key'}}` |
| `not_contain` | Forbids a substring. | `not_contain{required:true,map:{notContain:'key'}}` |

## Write Excel

Use `EXPORT WITH` to define sheets, headers, mappings, formats, transformations, formulas, styles, merges, graphs, and footer text.

```string
    EXPORT WITH
    SHEET="Report",
    HEADER=true,
    MAPPING={"id":"ID","name":"Name","amount":"Amount"},
    FORMAT={"amount":"currency"},
    TRANSFORM={"name":toUpper(${name})},
    FOOTER="Generated by JQuickExcel"
```

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `SHEET` | Selects the target worksheet. | `SHEET="Report"` |
| `HEADER` | Controls header generation. | `HEADER=true` |
| `FORMAT` | Defines Excel display formats. | `FORMAT={"amount":"currency"}` |
| `MAPPING` | Maps source fields to headers. | `MAPPING={"id":"ID"}` |
| `TRANSFORM` | Converts values before output. | `TRANSFORM={"name":toUpper(${name})}` |
| `FORMULAS` | Writes formulas to targets. | `FORMULAS={D2:'SUM(B2:C2)'}` |
| `STYLE` | Applies styles to targets. | `STYLE={ROW 1:{bold:true}}` |
| `MERGE` | Defines merge aggregation. | `MERGE:{ROWS 1..1,COLS A..D WITH FIRST}` |
| `GRAPH` | Defines graph data and type. | `GRAPH={TYPE=PIE,TITLE="Revenue",CATEGORIES=["Q1"],SERIES=[{NAME="2023",DATA=[1]}]}` |
| `FOOTER` | Sets footer text. | `FOOTER="Generated by JQuickExcel"` |

## Large Data Export

`JQuickExcelConfig` configures streaming export, large-file import, and style caching globally.

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

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `streamingExportEnabled` | Enables automatic SXSSF streaming export. | `setStreamingExportEnabled(true)` |
| `streamingRowAccessWindowSize` | Sets rows retained in memory. | `setStreamingRowAccessWindowSize(100)` |
| `streamingExportThreshold` | Sets the streaming row threshold. | `setStreamingExportThreshold(5000)` |
| `streamingCompressTempFiles` | Compresses streaming temporary files. | `setStreamingCompressTempFiles(true)` |
| `bigFileImportEnabled` | Enables OPCPackage shared parsing. | `setBigFileImportEnabled(true)` |
| `importBatchThreshold` | Sets the recommended batch threshold. | `setImportBatchThreshold(20000)` |
| `cellStyleCacheEnabled` | Enables the cell style cache. | `setCellStyleCacheEnabled(true)` |

For large imports, consume batches rather than retaining all rows at once.

```java
int total = handler.importDataInBatch(model, 5000, batch -> {
    process(batch);
    return true;
});
```

## Template Rendering

Use `TRANSFORM`, context values, and `FOOTER` expressions to render output from application data.

```string
EXPORT WITH
MAPPING={"name":"Name","gender":"Gender"},
TRANSFORM={"gender":trans(${dict},${gender})},
FOOTER="Generated by JQuickExcel on ${current_date()}"
```

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `TRANSFORM` | Applies an evaluator expression. | `TRANSFORM={"gender":trans(${dict},${gender})}` |
| `FOOTER` | Renders literal text or a variable expression. | `FOOTER="Generated on ${current_date()}"` |
| `FORMAT` | Applies the Excel display format. | `FORMAT={"date":"yyyy-MM-dd"}` |

## Style Settings

`STYLE` accepts a row, column, cell, or range target.

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

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `fontName` | Sets the font family. | `fontName:Arial` |
| `fontHeightInPoints` | Sets font size in points. | `fontHeightInPoints:12` |
| `bold` | Enables bold text. | `bold:true` |
| `italic` | Enables italic text. | `italic:true` |
| `color` | Sets font color. | `color:yellow` |
| `alignment` | Sets horizontal alignment. | `alignment:center` |
| `verticalAlignment` | Sets vertical alignment. | `verticalAlignment:center` |
| `wrapText` | Enables wrapping. | `wrapText:true` |
| `borderLeft` | Sets the left border. | `borderLeft:thin` |
| `borderRight` | Sets the right border. | `borderRight:thin` |
| `borderTop` | Sets the top border. | `borderTop:thin` |
| `borderBottom` | Sets the bottom border. | `borderBottom:thin` |
| `fillPattern` | Sets the fill pattern. | `fillPattern:solid_foreground` |
| `fillForegroundColor` | Sets fill foreground color. | `fillForegroundColor:yellow` |
| `fillBackgroundColor` | Sets fill background color. | `fillBackgroundColor:white` |
| `dataFormatString` | Sets the cell display pattern. | `dataFormatString:"yyyy-MM-dd"` |

| Parameter | Description | Usage Example |
| --- | --- | --- |
| Border | No border. | `borderBottom:none` |
| Border | Thin border. | `borderBottom:thin` |
| Border | Medium border. | `borderBottom:medium` |
| Border | Dashed border. | `borderBottom:dashed` |
| Border | Dotted border. | `borderBottom:dotted` |
| Border | Thick border. | `borderBottom:thick` |
| Border | Double border. | `borderBottom:double` |
| Fill | No fill. | `fillPattern:no_fill` |
| Fill | Solid foreground fill. | `fillPattern:solid_foreground` |
| Fill | Fine dots. | `fillPattern:fine_dots` |
| Fill | Sparse dots. | `fillPattern:sparse_dots` |
| Fill | Thin horizontal bands. | `fillPattern:thin_horz_bands` |
| Fill | Thin vertical bands. | `fillPattern:thin_vert_bands` |

## Theme Templates

JQuick-Excel provides 42 built-in themes. Set a theme on `JExcelExportModel` after executing the rule and before creating the export handler.

```java
JExcelExportModel config = (JExcelExportModel) executor.execute(rule);
config.setTheme("jade");
JExcelExportHandler handler = new JExcelExportHandler(config, data);
Workbook workbook = handler.getWorkBook();
workbook.write(outputStream);
```

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `default`, `minimalistGrey`, `slate` | Classic and grey themes. | `setTheme("default")` |
| `charcoal`, `navyBlue`, `oceanBlue` | Dark and ocean blue themes. | `setTheme("oceanBlue")` |
| `skyBlue`, `azure`, `steelBlue` | Light and steel blue themes. | `setTheme("skyBlue")` |
| `denim`, `indigo`, `periwinkle` | Denim and indigo themes. | `setTheme("indigo")` |
| `forestGreen`, `emerald`, `jade` | Forest and jade green themes. | `setTheme("jade")` |
| `mintFresh`, `sage`, `oliveGreen` | Fresh and muted green themes. | `setTheme("mintFresh")` |
| `tropicalTeal`, `turquoise`, `cyan` | Teal and cyan themes. | `setTheme("turquoise")` |
| `sunsetOrange`, `coral`, `peach` | Orange and peach themes. | `setTheme("coral")` |
| `crimsonRed`, `wineRed`, `sakuraPink` | Red and sakura themes. | `setTheme("crimsonRed")` |
| `roseQuartz`, `lavenderPurple`, `amethyst` | Pink and purple themes. | `setTheme("amethyst")` |
| `plum`, `royalGold`, `champagne` | Plum and gold themes. | `setTheme("royalGold")` |
| `amber`, `mustard`, `bronze` | Amber, mustard, and bronze themes. | `setTheme("amber")` |
| `vintageSepia`, `espresso`, `mahogany` | Sepia and brown themes. | `setTheme("espresso")` |
| `terracotta`, `midnightDark`, `pearl` | Terracotta, dark, and pearl themes. | `setTheme("midnightDark")` |

## Type Conversion and TRANSFORM Functions

`TRANSFORM` evaluates an expression for each row. `${field}` reads the current row, `${key}` can read a value from `JContext`, and the resulting arguments are passed to an evaluator or an SPI provider. `FORMAT` is separate: it controls the final Excel cell display after transformation.

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

### Evaluator Functions and SPI Registration

JQuick-Excel expressions can use the existing evaluator functions `toUpper`, `dateFormat`, and `trans`. The external provider catalog is maintained in [jquick-transform-function](https://github.com/paohaijiao/jquick-transform-function). Add it when the SPI providers are required.

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `toUpper` | Existing evaluator that converts text to uppercase. | `toUpper(${name})` |
| `dateFormat` | Existing evaluator that formats a date value. | `dateFormat(${enrollmentDate},'yyyy-MM-dd')` |
| `trans` | Existing evaluator that maps a value through a `JContext` dictionary. | `trans(${dict},${gender})` |

```xml
<dependency>
  <groupId>io.github.paohaijiao</groupId>
  <artifactId>jquick-transform-function</artifactId>
  <version>1.4.0</version>
</dependency>
```

Providers implement `com.github.paohaijiao.function.core.JQuickMethodFunctionProvider`:

```java
public interface JQuickMethodFunctionProvider {
    String getMethodName();
    Object invoke(List<Object> args);
    default String getDescription() { return getMethodName(); }
    default List<Class<?>> getParameterTypes() { return Collections.emptyList(); }
    default int getPriority() { return 5000; }
}
```

Each provider implementation is listed in `META-INF/services/com.github.paohaijiao.function.core.JQuickMethodFunctionProvider`. Java SPI discovery loads these classes. `JQuickMethodInvocationManager` calls `ServiceLoader.loadServicesByPriority(...)`, registers providers by `getMethodName()`, and invokes the matching provider with the evaluated argument list. `JFunctionExecutor` exposes the execution contract `apply(List<Object> args)`. Custom functions can be added with `registerInvoker(...)` or deliberately replace an existing function with `registerOrReplaceInvoker(...)`.

The runtime path is:

```text
row field or JContext value
    -> ${field}
    -> TRANSFORM expression
    -> function lookup
    -> JQuickMethodFunctionProvider.invoke(List<Object>)
    -> transformed value
    -> FORMAT for Excel display
```

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `JContext` | Supplies dictionaries and external values. | `context.put("dict", gender)` |
| `${field}` | Reads a value from the current row. | `${gender}` |
| `TRANSFORM` | Evaluates a value expression before import or export. | `TRANSFORM={"name":toUpper(${name})}` |
| `JQuickMethodFunctionProvider` | SPI contract for a callable function. | `getMethodName()` and `invoke(args)` |
| `META-INF/services/...` | Java SPI provider descriptor. | `META-INF/services/com.github.paohaijiao.function.core.JQuickMethodFunctionProvider` |
| `getPriority` | Supplies the provider loading priority. | `return 5000;` |
| `JQuickMethodInvocationManager` | Looks up and invokes registered functions. | `manager.invoke("toUpper", args)` |
| `JFunctionExecutor` | Defines a function execution adapter. | `executor.apply(args)` |
| `FORMAT` | Controls final Excel display formatting. | `FORMAT={"enrollmentDate":"yyyy-MM-dd"}` |

### Complete TRANSFORM Function Catalog

The following catalog contains all 248 functions published by `jquick-transform-function`. Optional arguments use `?`; `...` means a variable number of arguments.

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `array` | Array and list checks. | `isArray(value)` |
| `bit` | Bitwise operations. | `bitAnd(a,b)`, `bitOr(a,b)`, `bitXor(a,b)` |
| `bool` | Boolean checks. | `isBoolean(value)` |
| `business` | Card, email, gender, ID-card, phone, and email validation. | `bankCardMask(cardNo,keepStart?,keepEnd?)`; `bankCardValidate(cardNo)`; `emailMask(email)`; `genderName(code)`; `idCardAge(idCard,referenceDate?)`; `idCardBirthday(idCard,pattern?)`; `idCardGender(idCard,format?)`; `idCardInfo(idCard,field?)`; `idCardValidate(idCard)`; `phoneInfo(phone,field?)`; `phoneMask(phone,keepStart?,keepEnd?)`; `phoneValidate(phone)`; `isEmail(value)` |
| `collection` | Empty checks, joining, and size. | `isEmpty(value)`; `join(list,delimiter)`; `size(value)` |
| `condition` | Comparisons, ranges, branching, null defaults, and switches. | `between(value,min,max,inclusive?)`; `caseWhen(condition1,result1,...,defaultResult)`; `coalesce(value1,value2,...)`; `defaultIfNull(value,defaultValue)`; `eq(a,b,ignoreCase?)`; `gte(a,b)`; `gt(a,b)`; `ifElse(condition1,value1,...,defaultValue)`; `if(condition,trueValue,falseValue)`; `lte(a,b)`; `lt(a,b)`; `ne(a,b,ignoreCase?)`; `nvl(value,defaultValue)`; `switch(value,case1,result1,...,defaultValue)` |
| `convert` | Boolean, date, datetime, and short conversion. | `toBoolean(value,defaultValue?)`; `toDate(value,pattern?)`; `toDateTime(value,pattern?)`; `toShort(value,defaultValue?)` |
| `date` | Date arithmetic, extraction, comparison, boundaries, and formatting. | `addDays(date,days)`; `addHours(datetime,hours)`; `addMinutes(datetime,minutes)`; `addMonths(date,months)`; `addSeconds(datetime,seconds)`; `addYears(date,years)`; `age(birthDate,referenceDate?)`; `day(date?)`; `dayOfWeek(date?,locale?)`; `dayOfYear(date?)`; `daysBetween(date1,date2)`; `endOfDay(date?)`; `endOfMonth(date?)`; `endOfYear(date?)`; `hour(datetime?)`; `hoursBetween(datetime1,datetime2)`; `isAfter(date1,date2)`; `isBefore(date1,date2)`; `isDate(value)`; `isLeapYear(year?)`; `isSameDay(date1,date2)`; `isWeekend(date?)`; `minute(datetime?)`; `month(date?)`; `monthsBetween(date1,date2)`; `second(datetime?)`; `startOfDay(date?)`; `startOfMonth(date?)`; `startOfYear(date?)`; `weekOfYear(date?)`; `year(date?)`; `yearsBetween(date1,date2)`; `formatDate(date,pattern)`; `now()`; `parseDate(dateStr,pattern)`; `timestamp()`; `today()`; `toIsoString(date)` |
| `geometry` | Geometry, vectors, matrices, complex numbers, and numeric mapping. | `areaCircle(radius)`; `areaRectangle(length,width)`; `areaTriangle(base,height)` or `areaTriangle(a,b,c)`; `circumference(radius)`; `clamp(value,min,max)`; `combination(n,k)`; `cross(x1,y1,x2,y2)`; `distance(x1,y1,x2,y2)` or `distance(x1,y1,z1,x2,y2,z2)`; `dot(vector1,vector2)`; `factorial(n)`; `fibonacci(n)`; `gcd(a,b,...)`; `hypot(x,y)`; `isPowerOfTwo(n)`; `isPrime(n)`; `lcm(a,b,...)`; `lerp(a,b,t)`; `map(value,fromLow,fromHigh,toLow,toHigh,clamp?)`; `permutation(n,k)`; `complexAdd(r1,i1,r2,i2)`; `complexMultiply(r1,i1,r2,i2)`; `matrixAdd(matrix1,matrix2)` |
| `extra` | General casting, numeric formatting, collections, and type inspection. | `cast(value,targetClass)`; `formatNumber(number,pattern)`; `parseNumber(str,pattern)`; `toArray(value1,value2,...)`; `toCurrency(number,locale?)`; `toList(value1,value2,...)`; `toPercentage(number,decimals?)`; `typeOf(value)` |
| `json` | Object serialization. | `toJson(value)` |
| `math` | Arithmetic, trigonometry, constants, aggregation, statistics, bases, and numeric conversion. | `abs(value)`; `acos(value)`; `add(...)`; `asin(value)`; `atan(value)`; `atan2(y,x)`; `avg(...)`; `ceil(value)`; `ceilTo(value,places)`; `e()`; `pi()`; `cos(radians)`; `cosh(value)`; `divide(a,b,...)`; `exp(value)`; `expm1(value)`; `floor(value)`; `floorTo(value,places)`; `greatest(value1,value2,...)`; `isNumber(value)`; `least(value1,value2,...)`; `log(value)`; `log10(value)`; `log1p(value)`; `max(...)`; `median(numbers...)`; `min(...)`; `mode(numbers...)`; `mod(a,b)`; `multiply(...)`; `parseBinary(binaryStr)`; `parseHex(hexStr)`; `percentile(numbers...,percentile)`; `pow(base,exponent)`; `range(numbers...)`; `round(value)`; `roundTo(value,places)`; `signum(value)`; `sin(radians)`; `sinh(value)`; `sqrt(value)`; `stdDev(numbers...)`; `subtract(a,b,...)`; `tan(radians)`; `tanh(value)`; `toBinary(number)`; `toDegrees(radians)`; `toDouble(value,defaultValue?)`; `toFloat(value,defaultValue?)`; `toHex(number)`; `toInt(value,defaultValue?)`; `toLong(value,defaultValue?)`; `toNumberString(number,pattern?)`; `toOctal(number)`; `toRadians(degrees)`; `ulp(value)`; `variance(numbers...)`; `countDistinct(...)`; `count(...)`; `countNonNull(...)`; `product(...)`; `sum(...)` |
| `random` | Random values, choices, colors, dates, arrays, and UUIDs. | `randomBoolean()` or `randomBoolean(trueProbability?)`; `randomChoice(list)` or `randomChoice(elem1,elem2,...)`; `randomDouble()` or `randomDouble(min,max)`; `random(arr)`; `randomInt()`, `randomInt(max)`, or `randomInt(min,max)`; `randomIntArray(size,min,max)`; `randomLong()`, `randomLong(max)`, or `randomLong(min,max)`; `randomSample(list,count,allowRepeat?)`; `shuffle(list)`; `randomString(length)`; `randomUUID(withoutDashes?)`; `randomColor(type?)`; `randomDate(startDate,endDate,pattern?)` |
| `string` | String comparison, search, padding, masking, escaping, encoding, replacement, case conversion, and aggregation. | `abbreviate(str,maxWidth,ellipsis?)`; `capitalize(str)`; `centerPad(str,size,padChar?)`; `compareTo(str1,str2,ignoreCase?)`; `concat(...)`; `contains(str,sub)`; `tokenize(str,delimiters)`; `countChar(str,ch,ignoreCase?)`; `countMatches(str,sub,ignoreCase?)`; `equalsAny(str,target1,target2,...)`; `equalsIgnoreCase(str1,str2)`; `escapeHtml(str)`; `escapeRegex(str)`; `format(pattern,arg1,arg2,...)`; `indexOf(str,search,fromIndex?)`; `isAlpha(str)`; `isAlphaNumeric(str)`; `isBlank(str)`; `isNumeric(str)`; `isString(value)`; `left(str,n)`; `leftPad(str,size,padChar?)`; `length(str)`; `levenshtein(str1,str2)`; `maskEmail(email)`; `mask(str,start,end,maskChar?)`; `matches(str,regex)`; `mid(str,start,length?)`; `removeDuplicates(str)`; `removeEnd(str,suffix,ignoreCase?)`; `removeStart(str,prefix,ignoreCase?)`; `removeWhitespace(str)`; `repeat(str,count,separator?)`; `repeatChar(ch,count)`; `replace(str,target,replacement)`; `replaceAll(str,regex,replacement)`; `reverse(str)`; `right(str,n)`; `rightPad(str,size,padChar?)`; `similarity(str1,str2)`; `split(str,regex)`; `splitByLength(str,chunkSize)`; `substring(str,beginIndex)` or `substring(str,beginIndex,endIndex)`; `substringAfter(str,separator)`; `substringBefore(str,separator)`; `substringBetween(str,open,close)`; `swapCase(str)`; `toCamelCase(str,firstUpper?)`; `toLower(str)`; `toSnakeCase(str)`; `toString(value,pattern?)`; `toUpper(str)`; `trim(str)`; `uncapitalize(str)`; `unescapeHtml(str)`; `uniqueChars(str)`; `wordCount(str)`; `base64Decode(encodedStr)`; `base64Encode(str)`; `decodeUrl(str)`; `encodeUrl(str)`; `md5(str)`; `groupConcat(delimiter,...)`; `stringAgg(delimiter,...)` |
| `translate` | Context dictionary translation. | `translate(context,code,dictType,defaultValue?)` |

The catalog is sourced from the linked repository and is loaded only when the `jquick-transform-function` dependency is present. Built-in Excel formulas such as `SUM`, `IF`, and `TODAY` remain a separate `FORMULAS` feature; similarly named TRANSFORM functions such as `sum(...)` are Java expression functions, not Excel formulas.

## Common Parameters

### Import DSL

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `SHEET` | Source worksheet selector. | `SHEET="Students"` |
| `HEADER` | Header-row declaration. | `HEADER=true` |
| `MAPPING` | Header-to-field mapping. | `MAPPING={"ID":"id"}` |
| `TRANSFORM` | Import value conversion. | `TRANSFORM={"name":toUpper(${name})}` |
| `VALIDATION` | Selected-scope validation. | `VALIDATION={C2:C4:{integer{required:true}}}` |

### Export DSL

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `SHEET` | Target worksheet selector. | `SHEET="Report"` |
| `HEADER` | Header generation switch. | `HEADER=true` |
| `FORMAT` | Output formatting rules. | `FORMAT={"amount":"currency"}` |
| `MAPPING` | Field-to-header mapping. | `MAPPING={"id":"ID"}` |
| `TRANSFORM` | Pre-output conversion. | `TRANSFORM={"name":toUpper(${name})}` |
| `FORMULAS` | Formula target and expression. | `FORMULAS={D2:'SUM(B2:C2)'}` |
| `STYLE` | Style target and properties. | `STYLE={ROW 1:{bold:true}}` |
| `MERGE` | Merge target and strategy. | `MERGE:{ROWS 1..1,COLS A..D WITH FIRST}` |
| `GRAPH` | Graph specification. | `GRAPH={TYPE=PIE,TITLE="Revenue",CATEGORIES=["Q1"],SERIES=[{NAME="2023",DATA=[1]}]}` |
| `FOOTER` | Footer text. | `FOOTER="Generated by JQuickExcel"` |

### Formulas

Formula targets support `ROW 5`, `ROW 1..10`, `COL A:`, `COL A..D:`, `C1:`, and `A1:B5`.

#### Mathematical Functions

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `ABS` | Absolute value. | `ABS(D2)` |
| `AVERAGE` | Arithmetic mean. | `AVERAGE(D2:D4)` |
| `COUNT` | Counts numeric values. | `COUNT(D2:D4)` |
| `MAX` | Maximum value. | `MAX(D2:D4)` |
| `MIN` | Minimum value. | `MIN(D2:D4)` |
| `POWER` | Exponentiation. | `POWER(2,3)` |
| `RAND` | Random value in `[0,1)`. | `RAND()` |
| `RANK` | Rank in a range. | `RANK(20,D2:D4)` |
| `ROUND` | Rounds a value. | `ROUND(3.1415926,3)` |
| `SQRT` | Square root. | `SQRT(4)` |
| `STDEV` | Standard deviation. | `STDEV(D2:D4)` |
| `SUM` | Sum of values. | `SUM(D2:D4)` |

```string
EXPORT WITH FORMULAS={
  D5:'SUM(D2:D4)',
  E5:'ROUND(AVERAGE(D2:D4),2)',
  F5:'RANK(D2,D2:D4)'
}
```

#### Date and Time Functions

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `DATETIME` | Creates a date and time. | `DATETIME(2023,5,15,14,30,0)` |
| `DAY` | Extracts day of month. | `DAY("2025-01-23")` |
| `DAYS` | Calculates day difference. | `DAYS("2025-01-23","2025-01-28")` |
| `EDATE` | Offsets a date by months. | `EDATE("2025-01-23",3)` |
| `EOMONTH` | Returns a month end. | `EOMONTH("2025-01-23",3)` |
| `HOUR` | Extracts an hour. | `HOUR(A1)` |
| `NETWORKDAYS` | Counts workdays. | `NETWORKDAYS(A1,A2)` |
| `NOW` | Returns current date and time. | `NOW()` |
| `TODAY` | Returns current date. | `TODAY()` |
| `WORKDAY` | Offsets by workdays. | `WORKDAY(A1,3,A2)` |
| `MINUTE` | Extracts a minute. | `MINUTE(A1)` |
| `MONTH` | Extracts a month. | `MONTH(A1)` |
| `SECOND` | Extracts a second. | `SECOND(A1)` |
| `TIME` | Creates a time value. | `TIME(14,30,0)` |
| `WEEKDAY` | Returns weekday number. | `WEEKDAY(A1,2)` |
| `WEEKNUM` | Returns week number. | `WEEKNUM(A1,1)` |
| `YEAR` | Extracts a year. | `YEAR(A1)` |

```string
EXPORT WITH FORMULAS={
  D5:'TODAY()',
  E5:'NETWORKDAYS(A1,A2)',
  F5:'WEEKNUM(A1,1)'
}
```

#### Text Functions

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `CONCAT` | Concatenates text. | `CONCAT(A1,B1)` |
| `EXACT` | Compares text with case. | `EXACT("a","A")` |
| `FIND` | Finds case-sensitive text. | `FIND("n","apple")` |
| `LEFT` | Extracts text from the left. | `LEFT("hello",2)` |
| `RIGHT` | Extracts text from the right. | `RIGHT("hello",2)` |
| `LEN` | Counts characters. | `LEN("text")` |
| `MID` | Extracts text by position. | `MID("apple",2,3)` |
| `SUBSTITUTE` | Replaces matching text. | `SUBSTITUTE("a-a","a","b")` |
| `TRIM` | Trims surrounding spaces. | `TRIM(" a ")` |
| `CONCATENATE` | Concatenates values. | `CONCATENATE("A",1,TRUE)` |
| `LOWER` | Converts to lowercase. | `LOWER("ExCeL")` |
| `PROPER` | Capitalizes words. | `PROPER("john o'reilly")` |
| `REPLACE` | Replaces by position. | `REPLACE("ABCD",2,2,"XY")` |
| `SEARCH` | Finds case-insensitive text. | `SEARCH("n","Banana",3)` |
| `TEXT` | Formats a value as text. | `TEXT(0.25,"0.0%")` |
| `UPPER` | Converts to uppercase. | `UPPER("email")` |
| `VALUE` | Converts text to a number. | `VALUE("1000")` |

```string
EXPORT WITH FORMULAS={
  D5:'CONCAT(A1,B1)',
  E5:'TEXT(C2,"0.0%")',
  F5:'UPPER(B2)'
}
```

#### Logical and Lookup Functions

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `IF` | Returns one of two values. | `IF(D2>0,"Yes","No")` |
| `AND` | Tests all conditions. | `AND(TRUE,FALSE)` |
| `OR` | Tests any condition. | `OR(TRUE,FALSE)` |
| `LOOKUP` | Performs vector lookup. | `LOOKUP(22,D2:D4,C2:C4)` |

```string
EXPORT WITH FORMULAS={
  D5:'IF(D2>0,"Yes","No")',
  E5:'AND(A1>0,B1>0)',
  F5:'LOOKUP(22,D2:D4,C2:C4)'
}
```

### Charts

The graph DSL uses the following complete structure. `CATEGORY_AXIS` and `VALUE_AXIS` are optional.

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

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `TYPE` | Selects a graph type. | `TYPE = LINE` |
| `TITLE` | Sets the graph title. | `TITLE = "Monthly Sales"` |
| `CATEGORY_AXIS` | Sets the category axis title. | `CATEGORY_AXIS = "Month"` |
| `VALUE_AXIS` | Sets the value axis title. | `VALUE_AXIS = "Amount"` |
| `CATEGORIES` | Supplies category values. | `CATEGORIES = ["Jan", "Feb"]` |
| `SERIES` | Supplies series objects. | `SERIES = [{NAME = "Sales", DATA = [10, 20]}]` |
| `NAME` | Names one series. | `NAME = "Sales"` |
| `DATA` | Supplies one series data array. | `DATA = [10, 20]` |

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `LINE` | Trend analysis. | `TYPE = LINE` |
| `COLUMN` | Vertical comparison. | `TYPE = COLUMN` |
| `BAR` | Horizontal comparison. | `TYPE = BAR` |
| `BAR3D` | Three-dimensional bars. | `TYPE = BAR3D` |
| `PIE` | Proportion display. | `TYPE = PIE` |
| `AREA` | Accumulated trend. | `TYPE = AREA` |
| `AREA3D` | Three-dimensional area. | `TYPE = AREA3D` |
| `SCATTER` | Correlation display. | `TYPE = SCATTER` |
| `RADAR` | Multidimensional comparison. | `TYPE = RADAR` |
| `SURFACE` | Surface data display. | `TYPE = SURFACE` |

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

### Merge Strategies

Each merge definition identifies rows and columns, then applies a strategy after `WITH`.

| Parameter | Description | Usage Example |
| --- | --- | --- |
| `MERGE_WITH_MAX` | Uses the maximum value. | `COLS A..F WITH MAX` |
| `MERGE_WITH_MIN` | Uses the minimum value. | `COLS A..F WITH MIN` |
| `MERGE_WITH_AVG` | Uses the average value. | `COLS A..F WITH AVG` |
| `MERGE_WITH_SUM` | Sums values. | `COLS A..F WITH SUM` |
| `MERGE_WITH_FIRST` | Uses the first value. | `COLS A..F WITH FIRST` |
| `MERGE_WITH_LAST` | Uses the last value. | `COLS A..F WITH LAST` |
| `MERGE_WITH_CONCAT` | Concatenates values. | `COLS A..F WITH CONCAT` |
| `MERGE_WITH_COUNT` | Counts values. | `COLS A..F WITH COUNT` |
| `MERGE_WITH_VALUE` | Uses a fixed value. | `COLS A..F WITH VALUE` |

```string
EXPORT WITH MERGE:{
  ROWS 1..1,
  COLS A..F WITH FIRST
}
```

## Dependency & Compatibility

The Maven coordinate is `io.github.paohaijiao:jquick-excel`. This document uses version `3.6.0`. JQuick-Excel requires Java 8 or later and supports both `xls` and `xlsx` files.

| Parameter | Description | Usage Example |
| --- | --- | --- |
| Java | Minimum runtime. | `Java 8+` |
| Workbook | Legacy Excel format. | `xls` |
| Workbook | Office Open XML format. | `xlsx` |
| Dependency | Maven coordinate. | `io.github.paohaijiao:jquick-excel` |

# Notes / FAQ

| Parameter | Description | Usage Example |
| --- | --- | --- |
| Large files | Enable streaming export for high row counts. | `setStreamingExportEnabled(true)` |
| Large imports | Consume batches when input is large. | `importDataInBatch(model, 5000, batch -> true)` |
| Style limit | Reuse styles through the cache. | `setCellStyleCacheEnabled(true)` |
| XML location | Put the XML definition on the classpath. | `jquick-excel.xml` |
| DSL syntax | Preserve keywords, braces, separators, and ranges. | `FORMULAS={D5:'SUM(D2:D4)'}` |
| Streams | Close input and output streams. | `try (OutputStream output = ...) {}` |
| Context | Put dictionary values in `JContext`. | `context.put("dict", gender)` |
| Charts | Keep each `DATA` array aligned with `CATEGORIES`. | `DATA = [10, 20, 30]` |

# License

This project is licensed under the Apache License 2.0.
