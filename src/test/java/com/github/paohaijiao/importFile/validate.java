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
package com.github.paohaijiao.importFile;

import com.github.paohaijiao.factory.JExcelValidationRuleFactory;
import com.github.paohaijiao.formula.context.JExcelFormulaContext;
import com.github.paohaijiao.model.JExcelExportModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.util.JObjectConverter;
import com.github.paohaijiao.validate.JExcelValidationRule;
import com.github.paohaijiao.validate.impl.JCompositeRule;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.importFile
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class validate {

    @Test
    public void testRowFormulas() throws IOException {
        JCompositeRule userRule = JExcelValidationRuleFactory.composite()
                .addRule(JExcelValidationRuleFactory.required())
                .addRule(JExcelValidationRuleFactory.minLength(3, true))
                .addRule(JExcelValidationRuleFactory.maxLength(20, true))
                .addRule(JExcelValidationRuleFactory.regex("^[a-zA-Z0-9_]+$", true));
        testValidation(userRule, "valid_user", "a@");
        JCompositeRule ageRule = JExcelValidationRuleFactory.composite()
                .addRule(JExcelValidationRuleFactory.integer(true))
                .addRule(JExcelValidationRuleFactory.minValue(18, true))
                .addRule(JExcelValidationRuleFactory.maxValue(120, true));

        testValidation(ageRule, "25", "17");
    }

    private static void testValidation(JExcelValidationRule rule, String validValue, String invalidValue) {
        System.out.println("验证规则: " + rule.getErrorMessage());
        System.out.println("有效值 '" + validValue + "': " + rule.test(validValue));
        System.out.println("无效值 '" + invalidValue + "': " + rule.test(invalidValue));
        System.out.println("----------------------------------");
    }
}
