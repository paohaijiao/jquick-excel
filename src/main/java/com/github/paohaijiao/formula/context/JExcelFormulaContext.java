package com.github.paohaijiao.formula.context;

import com.github.paohaijiao.formula.JAbstractExcelFormula;
import com.github.paohaijiao.formula.JExcelFormula;
import com.github.paohaijiao.formula.enums.JFormulaEnums;
import com.github.paohaijiao.formula.impl.JCustomFormula;
import com.github.paohaijiao.formula.impl.JVLookupFormula;
import com.github.paohaijiao.formula.impl.judge.JAndFormula;
import com.github.paohaijiao.formula.impl.judge.JIfFormula;
import com.github.paohaijiao.formula.impl.math.JAverageFormula;
import com.github.paohaijiao.formula.impl.math.JSumFormula;
import com.github.paohaijiao.model.JFormulateCallModel;
import com.github.paohaijiao.model.JMethodCallModel;
import com.github.paohaijiao.param.JContext;
import com.github.paohaijiao.parser.JQuickExcelLexer;
import com.github.paohaijiao.parser.JQuickExcelParser;
import com.github.paohaijiao.visitor.JQuickExcelExportComonVisitor;
import com.github.paohaijiao.visitor.JQuickExcelImportVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

public class JExcelFormulaContext {

    private final Workbook workbook;

    private final FormulaEvaluator evaluator;


    public JExcelFormulaContext(Workbook workbook) {
        this.workbook = workbook;
        this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    }

    public Cell applyFormula(Sheet sheet, int rowNum, int colNum, JExcelFormula formula) {
        Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
        Cell cell = row.getCell(colNum) != null ? row.getCell(colNum) : row.createCell(colNum);
        cell.setCellFormula(formula.getFormula());
        return cell;
    }

    public Cell applyFormula(Sheet sheet, String cellRef, JExcelFormula formula) {
        CellReference ref = new CellReference(cellRef);
        return applyFormula(sheet, ref.getRow(), ref.getCol(), formula);
    }

    public Object evaluateFormula(Cell cell) {
        if (cell.getCellType() != CellType.FORMULA) {
            throw new IllegalArgumentException("Cell does not contain a formula");
        }
        CellValue cellValue = evaluator.evaluate(cell);
        return new JAbstractExcelFormula("") {
        }.evaluate(evaluator, cell);
    }

    public static JAbstractExcelFormula createFormulaInstance(String formulaContent)  {
        JFormulaEnums type = JFormulaEnums.getByFormulaContent(formulaContent);
        if (type == null) {
            throw new IllegalArgumentException("Unsupported formula: " + formulaContent);
        }
        Class<? extends JAbstractExcelFormula> clazz=type.getFormulaClass();
        JQuickExcelLexer lexer = new JQuickExcelLexer(CharStreams.fromString(formulaContent));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JQuickExcelParser parser = new JQuickExcelParser(tokens);
        JQuickExcelParser.FormulateCallContext tree = parser.formulateCall();
        JQuickExcelExportComonVisitor visitor = new JQuickExcelExportComonVisitor(new ArrayList<>());
        JFormulateCallModel result =(JFormulateCallModel) visitor.visit(tree);
        if(result.getList().isEmpty()){
           return createInstance(clazz);
        }else if(result.getList().size() == 1){
            return createInstance(clazz, result.getList().get(0));
        }else{
            Object[] params = result.getList().toArray(new Object[0]);
            return createInstance(clazz, params);
        }

    }
    public static JAbstractExcelFormula createInstance(Class<? extends JAbstractExcelFormula> clazz, Object... params) {
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                boolean isVarArgs = constructor.isVarArgs();
                if (constructor.getParameterCount() == params.length) {
                    constructor.setAccessible(true);
                    if (constructor.isVarArgs()) {
                        Object[] varargs = new Object[1];
                        varargs[0] = params;
                        return (JAbstractExcelFormula) constructor.newInstance(varargs);
                    } else {
                        return (JAbstractExcelFormula) constructor.newInstance(params);
                    }
                }
                Object[] adaptedParams = adaptParameters(params, paramTypes, isVarArgs);
                if (adaptedParams != null) {
                    constructor.setAccessible(true);
                    return (JAbstractExcelFormula) constructor.newInstance(adaptedParams);
                }
            }

            throw new RuntimeException("no matching constructor found, number of parameters:" + params.length);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("failed to create  instance: " + clazz.getName(), e);
        }
    }
    private static Object[] adaptParameters(Object[] params, Class<?>[] paramTypes, boolean isVarArgs) {
        if (!isVarArgs) {
            if (params.length != paramTypes.length) {
                return null;
            }
            Object[] adapted = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                if (!isAssignable(paramTypes[i], params[i])) {
                    return null;
                }
                adapted[i] = params[i];
            }
            return adapted;
        } else {
            int fixedParams = paramTypes.length - 1;
            Object[] adapted = new Object[paramTypes.length];
            for (int i = 0; i < fixedParams; i++) {
                if (!isAssignable(paramTypes[i], params[i])) {
                    return null;
                }
                adapted[i] = params[i];
            }
            Class<?> varArgType = paramTypes[fixedParams].getComponentType();
            int varArgCount = params.length - fixedParams;
            Object varArgs = Array.newInstance(varArgType, varArgCount);
            for (int i = 0; i < varArgCount; i++) {
                Object param = params[fixedParams + i];
                if (!isAssignable(varArgType, param)) {
                    return null;
                }
                Array.set(varArgs, i, param);
            }
            adapted[fixedParams] = varArgs;
            return adapted;
        }
    }

    private static boolean isAssignable(Class<?> targetType, Object value) {
        if (value == null) {
            return !targetType.isPrimitive();
        }
        return targetType.isAssignableFrom(value.getClass());
    }
}
