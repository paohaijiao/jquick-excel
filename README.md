# jquick excel Document
```string
    jquick excel is a lightweight, high-performance java excel operation framework 
designed to simplify common requirements such as reading and writing excel files,
formatting, and exporting large data volumes. it combines ease of use, flexibility,
and efficiency, supports mainstream formats such as xls and xlsx, and provides a rich 
api to help developers quickly implement excel import and export functions while 
avoiding memory overflow issues.
```
```xml
<dependency>
    <groupId>io.github.paohaijiao</groupId>
     <artifactId>jquick-excel</artifactId>
</dependency>
```
## Table of Contents
1. [Overview](#overview)
2. [Basic Syntax](#basic-syntax)
- [Import Configuration](#import-configuration)
- [Export Configuration](#export-configuration)
3. [Import Options](#import-options)
4. [Export Options](#export-options)
5. [Data Types](#data-types)
6. [Complete Examples](#complete-examples)

## Overview
- Import/export configuration
- Advanced formatting
- Formula automation
- Data validation rules
- Chart generation

## Basic Syntax

### Import Configuration
```STRING
IMPORT [WITH option1, option2, ...]
```
| Option        | Syntax                                  | Description                                                                 |
|---------------|-----------------------------------------|-----------------------------------------------------------------------------|
| `SHEET`       | `SHEET '=' (STRING \| NUMBER)`         | Specifies worksheet by name (string) or index (number)                      |
| `HEADER`      | `HEADER '=' BOOLEAN`                   | Whether data contains headers (`true`/`false`)                              |
| `MAPPING`     | `MAPPING '=' '{' mappings '}'`         | Field mappings between source and target                                    |
| `TRANSFORM`   | `TRANSFORM '=' '{' rules '}'`          | Data transformation rules                                                   |
| `VALIDATION`  | `VALIDATION '=' '{' validations '}'`   | Data validation rules                                                       |
## SHEET Syntax
```string 
IMPORT WITH SHEET="Sheet1"
```
## HEADER Syntax
```string 
IMPORT WITH HEADER=true
```
## Field Mapping Syntax
```string 
IMPORT WITH MAPPING={
    "Name": "full_name",
    "Age": "user_age",
    "Email": to_lowercase("EMAIL")
}
```
## Transform Rule Syntax
```string 
IMPORT WITH TRANSFORM={
    "Price": to_number(price),
    "Date": format_date(date, "yyyy-MM-dd")
}
```
## Validation  Rule Syntax
| Rule Name         | Parameter Key       | Parameter Type | Description                                                                 |
|-------------------|---------------------|----------------|-----------------------------------------------------------------------------|
| BOOLEAN          | -                   | -              | Validates if the value is a boolean                                         |
| DATE_FORMAT      | format              | String         | Validates if the string matches the specified date format                   |
| MAX_DATE         | maxDate, format     | Date, String   | Validates if the date is not after the specified maxDate                    |
| MIN_DATE         | minDate, format     | Date, String   | Validates if the date is not before the specified minDate                   |
| INTEGER          | -                   | -              | Validates if the value is an integer                                        |
| DECIMAL          | -                   | -              | Validates if the value is a decimal number                                  |
| MAX_VALUE        | maxValue            | BigDecimal     | Validates if the numeric value is not greater than specified maxValue       |
| MIN_VALUE        | minValue            | BigDecimal     | Validates if the numeric value is not less than specified minValue          |
| DICT             | key-value pairs     | Map            | Validates if the value exists in the provided dictionary                    |
| EMAIL            | -                   | -              | Validates if the string is a properly formatted email address               |
| MOBILE           | -                   | -              | Validates if the string is a valid mobile number format (Chinese standard)  |
| MAX_LENGTH       | maxLength           | BigDecimal     | Validates if string length does not exceed specified maxLength              |
| MIN_LENGTH       | minLength           | BigDecimal     | Validates if string length meets or exceeds specified minLength             |
| REGEX            | pattern             | String         | Validates if string matches the specified regular expression pattern        |
| START_WITH       | startWith           | String         | Validates if string starts with the specified substring                     |
| NOT_START_WITH   | notStartWith        | String         | Validates if string does not start with the specified substring             |
| END_WITH         | endWith             | String         | Validates if string ends with the specified substring                       |
| NOT_END_WITH     | notEndWith          | String         | Validates if string does not end with the specified substring               |
| CONTAIN          | contains            | String         | Validates if string contains the specified substring                        |
| NOT_CONTAIN      | notContain          | String         | Validates if string does not contain the specified substring                |
| COMPOSITE        | rules               | Collection     | Validates using multiple combined validation rules                          |
```string 
IMPORT WITH VALIDATION={
    ROW 1..10: {
        required {required: true, msg: "error1"},
        range {required: true, msg: "error2", map: {min: 1, max: 100}}
    },
    COL A: {
        required {required: true, msg: "error3"}
    },
    B1:C5: {
        regex {required: true, msg: "error4", map: {pattern: "^\\d+$"}}
    }
}
```
##  sample Syntax
```string 
IMPORT WITH 
    SHEET="Employees",
    HEADER=true,
    MAPPING={
        "Emp ID": "employee_id",
        "Full Name": concat(first_name, " ", last_name),
        "Hire Date": parse_date(hire_date)
    },
    TRANSFORM={
        "Salary": round(salary, 2),
        "Department": uppercase(dept)
    },
    VALIDATION={
        ROW 2..1000: {
            required {required: true, msg: "row_required_error"},
            unique {required: true, msg: "id_unique_error"}
        },
        COL B: {
            length {required: true, msg: "name_length_error", map: {min: 2, max: 50}}
        }
    }
```
#### EXPORT Configuration
Import Options
| Option            | Syntax Example           | Description                                      |
|-------------------|--------------------------|--------------------------------------------------|
| Worksheet         | `SHEET="Sheet1"`         | Supports name or index (e.g., `SHEET=1`)         |
| Header            | `HEADER=true`            | Whether to include headers                      |
| Field Mapping     | `MAPPING={"source":"target"}` | Maps source fields to target columns          |
| Data Transformation | `TRANSFORM={"field":trim()}` | Applies cleansing functions (e.g., `trim()`) |

sample
VALIDATION={
ROW 1..10: {
required {required:true, msg:"Required field"}
},
COL A: {
range {map:{min:1,max:100}}
}
}
```STRING
EXPORT [WITH option1, option2, ...]
```
## Export Options

| Option Category | Syntax Example | Description |
|-----------------|----------------|-------------|
| **Worksheet** | `SHEET="Report"`<br>`SHEET=1` | Specifies worksheet by name or index (1-based) |
| **Header** | `HEADER=true`<br>`HEADER=false` | Controls whether to include column headers |
| **Formatting** | `FORMAT={"A1":"yyyy-MM-dd", "B2":"$#,##0.00"}` | Defines custom formats for cells/columns |
| **Formulas** | `FORMULAS={<br>  ROW 5: "SUM(A1:A4)",<br>  D2: "VLOOKUP(A2,Data!A:B,2,FALSE)"<br>}` | Applies Excel formulas to specific cells/rows/columns |
| **Cell Merging** | `MERGE:{<br>  ROWS 1..3 WITH FIRST,<br>  COLS A..C WITH CONCAT<br>}` | Merges cells with specified consolidation method |
| **Styling** | `STYLE={<br>  ROW 1: {font:"Arial", bold:true},<br>  A1:B2: {border:"thin"}<br>}` | Applies visual styles to cells/ranges |
| **Charts** | `GRAPH={<br>  TYPE=COLUMN,<br>  TITLE="Sales",<br>  SERIES=[{NAME:"Q1",DATA:[120,150,180]}]<br>}` | Generates charts with specified parameters |
| **Page Footer** | `FOOTER="Page &P of &N"`<br>`FOOTER=reportFooter` | Sets footer text (supports variables & placeholders) |

### Merge Policies
| Policy | Description |
|--------|-------------|
| `FIRST` | Keeps value from first cell |
| `LAST` | Keeps value from last cell |
| `SUM` | Calculates sum of values |
| `AVG` | Calculates average |
| `MAX` | Keeps maximum value |
| `MIN` | Keeps minimum value |
| `CONCAT` | Concatenates all values |
| `COUNT` | Counts non-empty cells |




# JEvaluator Function Reference
## 📊 Type Conversion Functions
| Function       | Syntax                  | Parameters       | Return Type | Description                     |
|----------------|-------------------------|------------------|-------------|---------------------------------|
| `toInteger`    | `toInteger(value)`      | 1 (any type)     | Integer     | Converts value to Integer       |
| `toDouble`     | `toDouble(value)`       | 1 (any type)     | Double      | Converts value to Double        |
| `toFloat`      | `toFloat(value)`        | 1 (any type)     | Float       | Converts value to Float         |
| `toString`     | `toString(value)`       | 1 (any type)     | String      | Converts value to String        |
| `parseToDate`  | `parseToDate(str,format)` | 2 (String)     | Date        | Parses string to Date           |
## 🔢 Math Functions
| Function | Syntax              | Parameters            | Return Type | Description                     |
|----------|---------------------|-----------------------|-------------|---------------------------------|
| `ceil`   | `ceil(number)`      | 1 (Number)            | Double      | Rounds up to nearest integer    |
| `floor`  | `floor(number)`     | 1 (Number)            | Double      | Rounds down to nearest integer  |
| `round`  | `round(num,digits)` | 2 (Number, Integer)   | Double      | Rounds to specified decimals    |
| `sum`    | `sum(values...)`    | ≥1 (Numbers)          | Number      | Sums all arguments              |
| `max`    | `max(values...)`    | ≥1 (Numbers)          | Number      | Returns maximum value           |
| `min`    | `min(values...)`    | ≥1 (Numbers)          | Number      | Returns minimum value           |
| `avg`    | `avg(values...)`    | ≥1 (Numbers)          | Double      | Calculates average              |
## 🔤 String Functions
| Function     | Syntax                    | Parameters            | Return Type | Description                     |
|--------------|---------------------------|-----------------------|-------------|---------------------------------|
| `toLower`    | `toLower(str)`            | 1 (String)            | String      | Converts to lowercase           |
| `toUpper`    | `toUpper(str)`            | 1 (String)            | String      | Converts to uppercase           |
| `contains`   | `contains(str,substr)`    | 2 (String)            | Boolean     | Checks if contains substring    |
| `join`       | `join(delimiter,items...)`| ≥2 (String, Objects)  | String      | Joins with delimiter            |
| `split`      | `split(str,delimiter)`    | 2 (String)            | String[]    | Splits string by delimiter      |
| `substring`  | `substring(str,start,end)`| 3 (String, int, int)  | String      | Extracts substring              |
| `replace`    | `replace(str,target,rep)` | 3 (String)            | String      | Replaces all occurrences        |
| `startsWith` | `startsWith(str,prefix)`  | 2 (String)            | Boolean     | Checks string prefix            |
| `endsWith`   | `endsWith(str,suffix)`    | 2 (String)            | Boolean     | Checks string suffix            |

## 📅 Date Functions

| Function      | Syntax                  | Parameters       | Return Type | Description                     |
|---------------|-------------------------|------------------|-------------|---------------------------------|
| `dateFormat`  | `dateFormat(date,format)` | 2 (Date, String) | String    | Formats date to string          |

## 📦 Collection Functions

| Function | Syntax            | Parameters | Return Type | Description                     |
|----------|-------------------|------------|-------------|---------------------------------|
| `length` | `length(array)`   | 1 (Array)  | Integer     | Returns array/list length       |
| `trans`  | `trans(src,dest)` | 2 (Objects)| Object      | Transforms between types        |
# Excel Formula Reference (Complete)

## 📊 Math Formula (16)
| Formula       | Syntax                | Params | Description                     | Class               |
|---------------|-----------------------|--------|---------------------------------|---------------------|
| `ABS`         | `ABS(number)`         | 1      | Absolute value                  | `JABSFormula`       |
| `AVERAGE`     | `AVERAGE(n1,n2...)`   | ≥1     | Arithmetic mean                 | `JAverageFormula`   |
| `COUNT`       | `COUNT(v1,v2...)`     | ≥1     | Count numerical values          | `JCountFormula`     |
| `MAX`         | `MAX(n1,n2...)`       | ≥1     | Maximum value                   | `JMaxFormula`       |
| `MIN`         | `MIN(n1,n2...)`       | ≥1     | Minimum value                   | `JMinFormula`       |
| `POWER`       | `POWER(base,exp)`     | 2      | Exponentiation                  | `JPowerFormula`     |
| `RAND`        | `RAND()`              | 0      | Random number [0,1)             | `JRandFormula`      |
| `RANK`        | `RANK(num,ref)`       | 2      | Ranking in list                 | `JRankFormula`      |
| `ROUND`       | `ROUND(num,digits)`   | 2      | Round to digits                 | `JRoundFormula`     |
| `SQRT`        | `SQRT(number)`        | 1      | Square root                     | `JSQRTFormula`      |
| `STDEV`       | `STDEV(n1,n2...)`     | ≥1     | Standard deviation              | `JSTDEVFormula`     |
| `SUM`         | `SUM(n1,n2...)`       | ≥1     | Summation                       | `JSumFormula`       |

## 📅 Date/Time Formula (15)
| Formula       | Syntax                | Special Rules                  | Class                   |
|---------------|-----------------------|--------------------------------|-------------------------|
| `DATE_PLUS_TIME` | `DATE(...)TIME(...)` | Combined datetime             | `JDatePlusTimeFormula`  |
| `DATETIME`    | `DATETIME()`          | Current datetime               | `JDateTimeFormula`      |
| `DAY`         | `DAY(date)`           | Extract day (1-31)             | `JDayFormula`           |
| `DAYS`        | `DAYS(end,start)`     | Days between dates             | `JDaysFormula`          |
| `EDATE`       | `EDATE(start,months)` | Add months to date             | `JEDATEFormula`         |
| `EOMONTH`     | `EOMONTH(start,mos)`  | End of month                   | `JEOMONTHFormula`       |
| `HOUR`        | `HOUR(datetime)`      | Extract hour (0-23)            | `JHourFormula`          |
| `NETWORKDAYS` | `NETWORKDAYS(s,e,[h])`| 2-3 params                    | `JNetworkDayFormula`    |
| `NOW`         | `NOW()`               | Exact match required           | `JNowFormula`           |
| `TODAY`       | `TODAY()`             | Exact match required           | `JTodayFormula`         |
| `WORKDAY`     | `WORKDAY(s,days,[h])` | 2-3 params                    | `JWorkDayFormula`       |
| `MINUTE`    | `MINUTE(time_value)`  | 1 (time serial)   | Minute (0-59)             | `JMinuteFormula`    |
| `MONTH`     | `MONTH(date_value)`   | 1 (date serial)   | Month (1-12)              | `JMonthFormula`     |
| `SECOND`    | `SECOND(time_value)`  | 1 (time serial)   | Second (0-59)             | `JSecondFormula`   |
| `TIME`      | `TIME(hour,min,sec)`  | 3 (h,m,s)         | Excel time serial (0-0.999)| `JTimeFormula`     |
| `TODAY`     | `TODAY()`             | Exact match       | Current date serial       | `JTodayFormula`    |
| `WEEKDAY`   | `WEEKDAY(date,[type])`| 1-2 params        | Day of week (configurable)| `JWeekDayFormula`  |
| `WEEKNUM`   | `WEEKNUM(date,[type])`| 1-2 params        | Week number               | `JWeekNumFormula`  |
| `YEAR`      | `YEAR(date_value)`    | 1 (date serial)   | Year (1900-9999)          | `JYearFormula`     |


## 🔤 String Formula (17)
| Formula       | Syntax                | Param Rules       | Example                     | Class               |
|---------------|-----------------------|-------------------|-----------------------------|---------------------|
| `CONCAT`      | `CONCAT(s1,s2...)`    | ≥1                | `CONCAT("A","B")` → "AB"    | `JConcatFormula`    |
| `EXACT`       | `EXACT(s1,s2)`        | 2 (case-sensitive)| `EXACT("a","A")` → FALSE    | `JExactFormula`     |
| `FIND`        | `FIND(sub,str,[pos])` | 2-3               | `FIND("n","apple")` → 0     | `JFindFormula`      |
| `LEFT/RIGHT`  | `LEFT(text,len)`      | 2                 | `LEFT("hello",2)` → "he"    | `JLeftFormula`      |
| `LEN`         | `LEN(text)`           | 1                 | `LEN("text")` → 4           | `JLenFormula`       |
| `MID`         | `MID(text,start,len)` | 3                 | `MID("apple",2,3)` → "ppl"  | `JMidFormula`       |
| `SUBSTITUTE`  | `SUBSTITUTE(s,o,n,[i])`| 3-4              | `SUBSTITUTE("a-a","a","b")` | `JSubstituteFormula`|
| `TRIM`        | `TRIM(text)`          | 1                 | `TRIM(" a ")` → "a"         | `JTrimFormula`      |
|`CONCATENATE`	|`CONCATENATE(text1, [text2]...)`|	`≥1 parameter`|	`CONCATENATE("A",1,TRUE) → "A1TRUE"`|	`JConcatenateFormula`|
|`LOWER`	|`LOWER(text)`	|`1 parameter`|	`LOWER("ExCeL") → "excel"`	|`JLowerFormula`|
|`PROPER`	|`PROPER(text)`|	`1 parameter`|	`PROPER("john o'reilly") → "John O'Reilly"`|	`JProperFormula`|
|`REPLACE`	|`REPLACE(old,start,num,new)`	|`4 parameters`	|`REPLACE("ABCD",2,2,"XY") → "AXYD"`|	`JReplaceFormula`|
|`SEARCH`	|`SEARCH(find,within,[start])`|	`2-3 parameters`	|`SEARCH("n","Banana",3) → 5`|	`JSearchFormula`|
|`SUBSTITUTE`|`SUBSTITUTE(text,old,new,[nth])`|	`3-4 parameters`|	`SUBSTITUTE("A-A-A","A","B",2) → "A-B-A"`|	`JSubstituteFormula`|
|`TEXT`	|`TEXT(value,format)`|	`2 parameters`	|`TEXT(0.25,"0.0%") → "25.0%"`|	`JTextFormulav`|
|`UPPER`	|`UPPER(text)`|	`1 parameter`	|`UPPER("email") → "EMAIL"`	|`JupperFormula`|
|`VALUE`	|`VALUE(text)`|	`1 parameter`|	`VALUE("¥1,000") → 1000.0`| `JValueFormula`|



## 🔍 Logic Formula (3)
| Formula | Syntax             | Params | Example                      | Class           |
|---------|--------------------|--------|------------------------------|-----------------|
| `IF`    | `IF(cond,t,f)`     | 3      | `IF(A1>0,"Yes","No")`        | `JIfFormula`    |
| `AND`   | `AND(b1,b2...)`    | ≥1     | `AND(TRUE,FALSE)` → FALSE    | `JAndFormula`   |
| `OR`    | `OR(b1,b2...)`     | ≥1     | `OR(TRUE,FALSE)` → TRUE      | `JORFormula`    |






### Chart Types
| Type | Description |
|------|-------------|
| `COLUMN` | Vertical bar chart |
| `BAR`/`BAR3D` | Horizontal bar chart |
| `LINE` | Line chart |
| `PIE` | Pie chart |
| `AREA`/`AREA3D` | Area chart |
| `SCATTER` | Scatter plot |
| `RADAR` | Radar chart |
| `SURFACE` | 3D surface chart |

#### Formatting
FORMAT={"A1":"yyyy-MM-dd", "ColumnB":"#,##0.00"}
#### dFormulas
FORMULAS={
    ROW 5: "SUM(A1:A4)",
    D2: "VLOOKUP(A2,Data!A:B,2,FALSE)"
}
####  Cell Merging
MERGE:{
ROWS 1..3 WITH FIRST,
RANGE D1:D5 WITH SUM
}
#### Chart:
GRAPH={
TYPE=COLUMN,
TITLE="Sales Report",
SERIES=[
{NAME:"ProductA", DATA:[120,150,180]}
]
}
#### Style :
STYLE={
ROW 1: {font:"Arial", bold:true},
A1:B2: {border:"thin"}
}
