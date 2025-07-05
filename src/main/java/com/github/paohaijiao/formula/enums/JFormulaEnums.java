package com.github.paohaijiao.formula.enums;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import com.github.paohaijiao.formula.impl.date.*;
import com.github.paohaijiao.formula.impl.judge.JAndFormula;
import com.github.paohaijiao.formula.impl.judge.JIfFormula;
import com.github.paohaijiao.formula.impl.judge.JORFormula;
import com.github.paohaijiao.formula.impl.math.*;
import com.github.paohaijiao.formula.impl.string.*;
import lombok.Getter;

@Getter
public enum JFormulaEnums {
    ABS("ABS(", JABSFormula.class),
    AVERAGE("AVERAGE(", JAverageFormula.class),
    COUNT("COUNT(", JCountFormula.class),
    MAX("MAX(", JMaxFormula.class),
    MIN("MIN(", JMinFormula.class),
    POWER("POWER(", JPowerFormula.class),
    RAND("RAND(", JRandFormula.class),
    RANK("RANK(", JRankFormula.class),
    ROUND("ROUND(", JRoundFormula.class),
    SQRT("SQRT(", JSQRTFormula.class),
    STDEV("STDEV(", JSTDEVFormula.class),
    SUM("SUM(", JSumFormula.class),
    SUMIF("SUMIF(", JSumIFFormula.class),

    DATEDIF("DATEDIF(", JDateDifFormula.class),
    DATE("DATE(", JDateFormula.class),
    DATE_PLUS_TIME("DATE(", JDatePlusTimeFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && formulaContent.contains("TIME(");
        }
    },
    DATETIME("DATETIME(", JDateTimeFormula.class),
    DAY("DAY(", JDayFormula.class),
    DAYS("DAYS(", JDaysFormula.class),
    EDATE("EDATE(", JEDATEFormula.class),
    EOMONTH("EOMONTH(", JEOMONTHFormula.class),
    HOUR("HOUR(", JHourFormula.class),
    MINUTE("MINUTE(", JMinuteFormula.class),
    MONTH("MONTH(", JMonthFormula.class),
    NETWORKDAYS("NETWORKDAYS(", JNetworkDayFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            int paramCount = countParams(formulaContent);
            return super.matches(formulaContent) && (paramCount == 2 || paramCount == 3);
        }
    },
    NOW("NOW(", JNowFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return formulaContent.equals("NOW()");
        }
    },
    SECOND("SECOND(", JSecondFormula.class),
    TIME("TIME(", JTimeFormula.class),
    TODAY("TODAY(", JTodayFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return formulaContent.equals("TODAY()");
        }
    },
    WEEKDAY("WEEKDAY(", JWeekDayFormula.class),
    WEEKNUM("WEEKNUM(", JWeekNumFormula.class),
    WORKDAY("WORKDAY(", JWorkDayFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            int paramCount = countParams(formulaContent);
            return super.matches(formulaContent) && (paramCount == 2 || paramCount == 3);
        }
    },
    YEAR("YEAR(", JYearFormula.class),
    YEARFRAC("YEARFRAC(", JYearFracFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            int paramCount = countParams(formulaContent);
            return super.matches(formulaContent) && (paramCount == 2 || paramCount == 3);
        }
    },

    CONCATENATE("CONCATENATE(", JConcatenateFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) >= 1;
        }
    },
    CONCAT("CONCAT(", JConcatFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) >= 1;
        }
    },
    EXACT("EXACT(", JExactFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 2;
        }
    },
    FIND("FIND(", JFindFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            int paramCount = countParams(formulaContent);
            return super.matches(formulaContent) && (paramCount == 2 || paramCount == 3);
        }
    },
    LEFT("LEFT(", JLeftFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 2;
        }
    },
    LEN("LEN(", JLenFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 1;
        }
    },
    LOWER("LOWER(", JLowerFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 1;
        }
    },
    MID("MID(", JMidFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 3;
        }
    },
    PROPER("PROPER(", JProperFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 1;
        }
    },
    REPLACE("REPLACE(", JReplaceFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 4;
        }
    },
    RIGHT("RIGHT(", JRightFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 2;
        }
    },
    SEARCH("SEARCH(", JSearchFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            int paramCount = countParams(formulaContent);
            return super.matches(formulaContent) && (paramCount == 2 || paramCount == 3);
        }
    },
    SUBSTITUTE("SUBSTITUTE(", JSubstituteFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            int paramCount = countParams(formulaContent);
            return super.matches(formulaContent) && (paramCount == 3 || paramCount == 4);
        }
    },
    TEXT("TEXT(", JTextFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 1;
        }
    },
    TRIM("TRIM(", JTrimFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 1;
        }
    },
    UPPER("UPPER(", JupperFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 1;
        }
    },
    VALUE("VALUE(", JValueFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 1;
        }
    },
    AND("AND(", JAndFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) >= 1;
        }
    },
    OR("OR(", JORFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) >= 1;
        }
    },
    IF("IF(", JIfFormula.class) {
        @Override
        public boolean matches(String formulaContent) {
            return super.matches(formulaContent) && countParams(formulaContent) == 3;
        }
    };
    private final String prefix;
    private final Class<? extends JAbstractExcelFormula> formulaClass;


    private JFormulaEnums(String prefix, Class<? extends JAbstractExcelFormula> formulaClass) {
        this.prefix = prefix;
        this.formulaClass = formulaClass;
    }

    public boolean matches(String formulaContent) {
        return formulaContent.startsWith(prefix);
    }
    private static int countParams(String formulaContent) {
        int start = formulaContent.indexOf('(') + 1;
        int end = formulaContent.lastIndexOf(')');
        if (start >= end) return 0;
        String params = formulaContent.substring(start, end);
        return params.split(",").length;
    }
    public static JFormulaEnums getByFormulaContent(String formulaContent) {
            if (formulaContent == null || formulaContent.isEmpty()) {
                return null;
            }

            String upperContent = formulaContent.toUpperCase();
            for (JFormulaEnums type : values()) {
                if (type.matches(upperContent)) {
                    return type;
                }
            }
            return null;
        }
    }
