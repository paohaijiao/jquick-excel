// Generated from D:/idea/jthornruleGrammer/excel/JQuickExcel.g4 by ANTLR 4.13.2
package com.github.paohaijiao.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JQuickExcelParser}.
 */
public interface JQuickExcelListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#config}.
     *
     * @param ctx the parse tree
     */
    void enterConfig(JQuickExcelParser.ConfigContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#config}.
     *
     * @param ctx the parse tree
     */
    void exitConfig(JQuickExcelParser.ConfigContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#importConfig}.
     *
     * @param ctx the parse tree
     */
    void enterImportConfig(JQuickExcelParser.ImportConfigContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#importConfig}.
     *
     * @param ctx the parse tree
     */
    void exitImportConfig(JQuickExcelParser.ImportConfigContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#exportConfig}.
     *
     * @param ctx the parse tree
     */
    void enterExportConfig(JQuickExcelParser.ExportConfigContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#exportConfig}.
     *
     * @param ctx the parse tree
     */
    void exitExportConfig(JQuickExcelParser.ExportConfigContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#importOption}.
     *
     * @param ctx the parse tree
     */
    void enterImportOption(JQuickExcelParser.ImportOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#importOption}.
     *
     * @param ctx the parse tree
     */
    void exitImportOption(JQuickExcelParser.ImportOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#exportOption}.
     *
     * @param ctx the parse tree
     */
    void enterExportOption(JQuickExcelParser.ExportOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#exportOption}.
     *
     * @param ctx the parse tree
     */
    void exitExportOption(JQuickExcelParser.ExportOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#source}.
     *
     * @param ctx the parse tree
     */
    void enterSource(JQuickExcelParser.SourceContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#source}.
     *
     * @param ctx the parse tree
     */
    void exitSource(JQuickExcelParser.SourceContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#destination}.
     *
     * @param ctx the parse tree
     */
    void enterDestination(JQuickExcelParser.DestinationContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#destination}.
     *
     * @param ctx the parse tree
     */
    void exitDestination(JQuickExcelParser.DestinationContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#sheetOption}.
     *
     * @param ctx the parse tree
     */
    void enterSheetOption(JQuickExcelParser.SheetOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#sheetOption}.
     *
     * @param ctx the parse tree
     */
    void exitSheetOption(JQuickExcelParser.SheetOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#headerOption}.
     *
     * @param ctx the parse tree
     */
    void enterHeaderOption(JQuickExcelParser.HeaderOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#headerOption}.
     *
     * @param ctx the parse tree
     */
    void exitHeaderOption(JQuickExcelParser.HeaderOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rangeOption}.
     *
     * @param ctx the parse tree
     */
    void enterRangeOption(JQuickExcelParser.RangeOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rangeOption}.
     *
     * @param ctx the parse tree
     */
    void exitRangeOption(JQuickExcelParser.RangeOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#mappingOption}.
     *
     * @param ctx the parse tree
     */
    void enterMappingOption(JQuickExcelParser.MappingOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#mappingOption}.
     *
     * @param ctx the parse tree
     */
    void exitMappingOption(JQuickExcelParser.MappingOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#formatOption}.
     *
     * @param ctx the parse tree
     */
    void enterFormatOption(JQuickExcelParser.FormatOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#formatOption}.
     *
     * @param ctx the parse tree
     */
    void exitFormatOption(JQuickExcelParser.FormatOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#transformOption}.
     *
     * @param ctx the parse tree
     */
    void enterTransformOption(JQuickExcelParser.TransformOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#transformOption}.
     *
     * @param ctx the parse tree
     */
    void exitTransformOption(JQuickExcelParser.TransformOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#formulaOption}.
     *
     * @param ctx the parse tree
     */
    void enterFormulaOption(JQuickExcelParser.FormulaOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#formulaOption}.
     *
     * @param ctx the parse tree
     */
    void exitFormulaOption(JQuickExcelParser.FormulaOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#styleOption}.
     *
     * @param ctx the parse tree
     */
    void enterStyleOption(JQuickExcelParser.StyleOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#styleOption}.
     *
     * @param ctx the parse tree
     */
    void exitStyleOption(JQuickExcelParser.StyleOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#styleTarget}.
     *
     * @param ctx the parse tree
     */
    void enterStyleTarget(JQuickExcelParser.StyleTargetContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#styleTarget}.
     *
     * @param ctx the parse tree
     */
    void exitStyleTarget(JQuickExcelParser.StyleTargetContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rowStyle}.
     *
     * @param ctx the parse tree
     */
    void enterRowStyle(JQuickExcelParser.RowStyleContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rowStyle}.
     *
     * @param ctx the parse tree
     */
    void exitRowStyle(JQuickExcelParser.RowStyleContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#colStyle}.
     *
     * @param ctx the parse tree
     */
    void enterColStyle(JQuickExcelParser.ColStyleContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#colStyle}.
     *
     * @param ctx the parse tree
     */
    void exitColStyle(JQuickExcelParser.ColStyleContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#cellStyle}.
     *
     * @param ctx the parse tree
     */
    void enterCellStyle(JQuickExcelParser.CellStyleContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#cellStyle}.
     *
     * @param ctx the parse tree
     */
    void exitCellStyle(JQuickExcelParser.CellStyleContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rangeStyle}.
     *
     * @param ctx the parse tree
     */
    void enterRangeStyle(JQuickExcelParser.RangeStyleContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rangeStyle}.
     *
     * @param ctx the parse tree
     */
    void exitRangeStyle(JQuickExcelParser.RangeStyleContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rangeRef}.
     *
     * @param ctx the parse tree
     */
    void enterRangeRef(JQuickExcelParser.RangeRefContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rangeRef}.
     *
     * @param ctx the parse tree
     */
    void exitRangeRef(JQuickExcelParser.RangeRefContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#styleProperty}.
     *
     * @param ctx the parse tree
     */
    void enterStyleProperty(JQuickExcelParser.StylePropertyContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#styleProperty}.
     *
     * @param ctx the parse tree
     */
    void exitStyleProperty(JQuickExcelParser.StylePropertyContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#mergeOption}.
     *
     * @param ctx the parse tree
     */
    void enterMergeOption(JQuickExcelParser.MergeOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#mergeOption}.
     *
     * @param ctx the parse tree
     */
    void exitMergeOption(JQuickExcelParser.MergeOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#mergeSpec}.
     *
     * @param ctx the parse tree
     */
    void enterMergeSpec(JQuickExcelParser.MergeSpecContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#mergeSpec}.
     *
     * @param ctx the parse tree
     */
    void exitMergeSpec(JQuickExcelParser.MergeSpecContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rowMerge}.
     *
     * @param ctx the parse tree
     */
    void enterRowMerge(JQuickExcelParser.RowMergeContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rowMerge}.
     *
     * @param ctx the parse tree
     */
    void exitRowMerge(JQuickExcelParser.RowMergeContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rowRange}.
     *
     * @param ctx the parse tree
     */
    void enterRowRange(JQuickExcelParser.RowRangeContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rowRange}.
     *
     * @param ctx the parse tree
     */
    void exitRowRange(JQuickExcelParser.RowRangeContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#singleRow}.
     *
     * @param ctx the parse tree
     */
    void enterSingleRow(JQuickExcelParser.SingleRowContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#singleRow}.
     *
     * @param ctx the parse tree
     */
    void exitSingleRow(JQuickExcelParser.SingleRowContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#multiRowRange}.
     *
     * @param ctx the parse tree
     */
    void enterMultiRowRange(JQuickExcelParser.MultiRowRangeContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#multiRowRange}.
     *
     * @param ctx the parse tree
     */
    void exitMultiRowRange(JQuickExcelParser.MultiRowRangeContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#colMerge}.
     *
     * @param ctx the parse tree
     */
    void enterColMerge(JQuickExcelParser.ColMergeContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#colMerge}.
     *
     * @param ctx the parse tree
     */
    void exitColMerge(JQuickExcelParser.ColMergeContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#colRange}.
     *
     * @param ctx the parse tree
     */
    void enterColRange(JQuickExcelParser.ColRangeContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#colRange}.
     *
     * @param ctx the parse tree
     */
    void exitColRange(JQuickExcelParser.ColRangeContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#singleCol}.
     *
     * @param ctx the parse tree
     */
    void enterSingleCol(JQuickExcelParser.SingleColContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#singleCol}.
     *
     * @param ctx the parse tree
     */
    void exitSingleCol(JQuickExcelParser.SingleColContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#multiColRange}.
     *
     * @param ctx the parse tree
     */
    void enterMultiColRange(JQuickExcelParser.MultiColRangeContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#multiColRange}.
     *
     * @param ctx the parse tree
     */
    void exitMultiColRange(JQuickExcelParser.MultiColRangeContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rangeMerge}.
     *
     * @param ctx the parse tree
     */
    void enterRangeMerge(JQuickExcelParser.RangeMergeContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rangeMerge}.
     *
     * @param ctx the parse tree
     */
    void exitRangeMerge(JQuickExcelParser.RangeMergeContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#mergeProperties}.
     *
     * @param ctx the parse tree
     */
    void enterMergeProperties(JQuickExcelParser.MergePropertiesContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#mergeProperties}.
     *
     * @param ctx the parse tree
     */
    void exitMergeProperties(JQuickExcelParser.MergePropertiesContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#formulaTarget}.
     *
     * @param ctx the parse tree
     */
    void enterFormulaTarget(JQuickExcelParser.FormulaTargetContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#formulaTarget}.
     *
     * @param ctx the parse tree
     */
    void exitFormulaTarget(JQuickExcelParser.FormulaTargetContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rowFormula}.
     *
     * @param ctx the parse tree
     */
    void enterRowFormula(JQuickExcelParser.RowFormulaContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rowFormula}.
     *
     * @param ctx the parse tree
     */
    void exitRowFormula(JQuickExcelParser.RowFormulaContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#colFormula}.
     *
     * @param ctx the parse tree
     */
    void enterColFormula(JQuickExcelParser.ColFormulaContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#colFormula}.
     *
     * @param ctx the parse tree
     */
    void exitColFormula(JQuickExcelParser.ColFormulaContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#cellFormula}.
     *
     * @param ctx the parse tree
     */
    void enterCellFormula(JQuickExcelParser.CellFormulaContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#cellFormula}.
     *
     * @param ctx the parse tree
     */
    void exitCellFormula(JQuickExcelParser.CellFormulaContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#cellRef}.
     *
     * @param ctx the parse tree
     */
    void enterCellRef(JQuickExcelParser.CellRefContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#cellRef}.
     *
     * @param ctx the parse tree
     */
    void exitCellRef(JQuickExcelParser.CellRefContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#formulaSpec}.
     *
     * @param ctx the parse tree
     */
    void enterFormulaSpec(JQuickExcelParser.FormulaSpecContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#formulaSpec}.
     *
     * @param ctx the parse tree
     */
    void exitFormulaSpec(JQuickExcelParser.FormulaSpecContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rowSpec}.
     *
     * @param ctx the parse tree
     */
    void enterRowSpec(JQuickExcelParser.RowSpecContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rowSpec}.
     *
     * @param ctx the parse tree
     */
    void exitRowSpec(JQuickExcelParser.RowSpecContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#colSpec}.
     *
     * @param ctx the parse tree
     */
    void enterColSpec(JQuickExcelParser.ColSpecContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#colSpec}.
     *
     * @param ctx the parse tree
     */
    void exitColSpec(JQuickExcelParser.ColSpecContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#rangeSpec}.
     *
     * @param ctx the parse tree
     */
    void enterRangeSpec(JQuickExcelParser.RangeSpecContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#rangeSpec}.
     *
     * @param ctx the parse tree
     */
    void exitRangeSpec(JQuickExcelParser.RangeSpecContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#fieldMapping}.
     *
     * @param ctx the parse tree
     */
    void enterFieldMapping(JQuickExcelParser.FieldMappingContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#fieldMapping}.
     *
     * @param ctx the parse tree
     */
    void exitFieldMapping(JQuickExcelParser.FieldMappingContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#cellFormat}.
     *
     * @param ctx the parse tree
     */
    void enterCellFormat(JQuickExcelParser.CellFormatContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#cellFormat}.
     *
     * @param ctx the parse tree
     */
    void exitCellFormat(JQuickExcelParser.CellFormatContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#transformRule}.
     *
     * @param ctx the parse tree
     */
    void enterTransformRule(JQuickExcelParser.TransformRuleContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#transformRule}.
     *
     * @param ctx the parse tree
     */
    void exitTransformRule(JQuickExcelParser.TransformRuleContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#transformValue}.
     *
     * @param ctx the parse tree
     */
    void enterTransformValue(JQuickExcelParser.TransformValueContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#transformValue}.
     *
     * @param ctx the parse tree
     */
    void exitTransformValue(JQuickExcelParser.TransformValueContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#excelFormula}.
     *
     * @param ctx the parse tree
     */
    void enterExcelFormula(JQuickExcelParser.ExcelFormulaContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#excelFormula}.
     *
     * @param ctx the parse tree
     */
    void exitExcelFormula(JQuickExcelParser.ExcelFormulaContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#formatSpec}.
     *
     * @param ctx the parse tree
     */
    void enterFormatSpec(JQuickExcelParser.FormatSpecContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#formatSpec}.
     *
     * @param ctx the parse tree
     */
    void exitFormatSpec(JQuickExcelParser.FormatSpecContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#dateFormat}.
     *
     * @param ctx the parse tree
     */
    void enterDateFormat(JQuickExcelParser.DateFormatContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#dateFormat}.
     *
     * @param ctx the parse tree
     */
    void exitDateFormat(JQuickExcelParser.DateFormatContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#numberFormat}.
     *
     * @param ctx the parse tree
     */
    void enterNumberFormat(JQuickExcelParser.NumberFormatContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#numberFormat}.
     *
     * @param ctx the parse tree
     */
    void exitNumberFormat(JQuickExcelParser.NumberFormatContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#stringFormat}.
     *
     * @param ctx the parse tree
     */
    void enterStringFormat(JQuickExcelParser.StringFormatContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#stringFormat}.
     *
     * @param ctx the parse tree
     */
    void exitStringFormat(JQuickExcelParser.StringFormatContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#customFormat}.
     *
     * @param ctx the parse tree
     */
    void enterCustomFormat(JQuickExcelParser.CustomFormatContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#customFormat}.
     *
     * @param ctx the parse tree
     */
    void exitCustomFormat(JQuickExcelParser.CustomFormatContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#functionCall}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionCall(JQuickExcelParser.FunctionCallContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#functionCall}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionCall(JQuickExcelParser.FunctionCallContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#functionArg}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionArg(JQuickExcelParser.FunctionArgContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#functionArg}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionArg(JQuickExcelParser.FunctionArgContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#quotedFunctionCall}.
     *
     * @param ctx the parse tree
     */
    void enterQuotedFunctionCall(JQuickExcelParser.QuotedFunctionCallContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#quotedFunctionCall}.
     *
     * @param ctx the parse tree
     */
    void exitQuotedFunctionCall(JQuickExcelParser.QuotedFunctionCallContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#variable}.
     *
     * @param ctx the parse tree
     */
    void enterVariable(JQuickExcelParser.VariableContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#variable}.
     *
     * @param ctx the parse tree
     */
    void exitVariable(JQuickExcelParser.VariableContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void enterIdentifier(JQuickExcelParser.IdentifierContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#identifier}.
     *
     * @param ctx the parse tree
     */
    void exitIdentifier(JQuickExcelParser.IdentifierContext ctx);

    /**
     * Enter a parse tree produced by {@link JQuickExcelParser#fileName}.
     *
     * @param ctx the parse tree
     */
    void enterFileName(JQuickExcelParser.FileNameContext ctx);

    /**
     * Exit a parse tree produced by {@link JQuickExcelParser#fileName}.
     *
     * @param ctx the parse tree
     */
    void exitFileName(JQuickExcelParser.FileNameContext ctx);
}