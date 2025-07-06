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
	 * @param ctx the parse tree
	 */
	void enterConfig(JQuickExcelParser.ConfigContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#config}.
	 * @param ctx the parse tree
	 */
	void exitConfig(JQuickExcelParser.ConfigContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#importConfig}.
	 * @param ctx the parse tree
	 */
	void enterImportConfig(JQuickExcelParser.ImportConfigContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#importConfig}.
	 * @param ctx the parse tree
	 */
	void exitImportConfig(JQuickExcelParser.ImportConfigContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#exportConfig}.
	 * @param ctx the parse tree
	 */
	void enterExportConfig(JQuickExcelParser.ExportConfigContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#exportConfig}.
	 * @param ctx the parse tree
	 */
	void exitExportConfig(JQuickExcelParser.ExportConfigContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#importOption}.
	 * @param ctx the parse tree
	 */
	void enterImportOption(JQuickExcelParser.ImportOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#importOption}.
	 * @param ctx the parse tree
	 */
	void exitImportOption(JQuickExcelParser.ImportOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#exportOption}.
	 * @param ctx the parse tree
	 */
	void enterExportOption(JQuickExcelParser.ExportOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#exportOption}.
	 * @param ctx the parse tree
	 */
	void exitExportOption(JQuickExcelParser.ExportOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#sheetOption}.
	 * @param ctx the parse tree
	 */
	void enterSheetOption(JQuickExcelParser.SheetOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#sheetOption}.
	 * @param ctx the parse tree
	 */
	void exitSheetOption(JQuickExcelParser.SheetOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#headerOption}.
	 * @param ctx the parse tree
	 */
	void enterHeaderOption(JQuickExcelParser.HeaderOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#headerOption}.
	 * @param ctx the parse tree
	 */
	void exitHeaderOption(JQuickExcelParser.HeaderOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rangeOption}.
	 * @param ctx the parse tree
	 */
	void enterRangeOption(JQuickExcelParser.RangeOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rangeOption}.
	 * @param ctx the parse tree
	 */
	void exitRangeOption(JQuickExcelParser.RangeOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#mappingOption}.
	 * @param ctx the parse tree
	 */
	void enterMappingOption(JQuickExcelParser.MappingOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#mappingOption}.
	 * @param ctx the parse tree
	 */
	void exitMappingOption(JQuickExcelParser.MappingOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#formatOption}.
	 * @param ctx the parse tree
	 */
	void enterFormatOption(JQuickExcelParser.FormatOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#formatOption}.
	 * @param ctx the parse tree
	 */
	void exitFormatOption(JQuickExcelParser.FormatOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#transformOption}.
	 * @param ctx the parse tree
	 */
	void enterTransformOption(JQuickExcelParser.TransformOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#transformOption}.
	 * @param ctx the parse tree
	 */
	void exitTransformOption(JQuickExcelParser.TransformOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#formulaOption}.
	 * @param ctx the parse tree
	 */
	void enterFormulaOption(JQuickExcelParser.FormulaOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#formulaOption}.
	 * @param ctx the parse tree
	 */
	void exitFormulaOption(JQuickExcelParser.FormulaOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#styleOption}.
	 * @param ctx the parse tree
	 */
	void enterStyleOption(JQuickExcelParser.StyleOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#styleOption}.
	 * @param ctx the parse tree
	 */
	void exitStyleOption(JQuickExcelParser.StyleOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#styleTarget}.
	 * @param ctx the parse tree
	 */
	void enterStyleTarget(JQuickExcelParser.StyleTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#styleTarget}.
	 * @param ctx the parse tree
	 */
	void exitStyleTarget(JQuickExcelParser.StyleTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rowStyle}.
	 * @param ctx the parse tree
	 */
	void enterRowStyle(JQuickExcelParser.RowStyleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rowStyle}.
	 * @param ctx the parse tree
	 */
	void exitRowStyle(JQuickExcelParser.RowStyleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#colStyle}.
	 * @param ctx the parse tree
	 */
	void enterColStyle(JQuickExcelParser.ColStyleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#colStyle}.
	 * @param ctx the parse tree
	 */
	void exitColStyle(JQuickExcelParser.ColStyleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#cellStyle}.
	 * @param ctx the parse tree
	 */
	void enterCellStyle(JQuickExcelParser.CellStyleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#cellStyle}.
	 * @param ctx the parse tree
	 */
	void exitCellStyle(JQuickExcelParser.CellStyleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rangeStyle}.
	 * @param ctx the parse tree
	 */
	void enterRangeStyle(JQuickExcelParser.RangeStyleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rangeStyle}.
	 * @param ctx the parse tree
	 */
	void exitRangeStyle(JQuickExcelParser.RangeStyleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rangeRef}.
	 * @param ctx the parse tree
	 */
	void enterRangeRef(JQuickExcelParser.RangeRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rangeRef}.
	 * @param ctx the parse tree
	 */
	void exitRangeRef(JQuickExcelParser.RangeRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#styleProperty}.
	 * @param ctx the parse tree
	 */
	void enterStyleProperty(JQuickExcelParser.StylePropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#styleProperty}.
	 * @param ctx the parse tree
	 */
	void exitStyleProperty(JQuickExcelParser.StylePropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#validationOption}.
	 * @param ctx the parse tree
	 */
	void enterValidationOption(JQuickExcelParser.ValidationOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#validationOption}.
	 * @param ctx the parse tree
	 */
	void exitValidationOption(JQuickExcelParser.ValidationOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#validationRule}.
	 * @param ctx the parse tree
	 */
	void enterValidationRule(JQuickExcelParser.ValidationRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#validationRule}.
	 * @param ctx the parse tree
	 */
	void exitValidationRule(JQuickExcelParser.ValidationRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#targetSpec}.
	 * @param ctx the parse tree
	 */
	void enterTargetSpec(JQuickExcelParser.TargetSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#targetSpec}.
	 * @param ctx the parse tree
	 */
	void exitTargetSpec(JQuickExcelParser.TargetSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rowTarget}.
	 * @param ctx the parse tree
	 */
	void enterRowTarget(JQuickExcelParser.RowTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rowTarget}.
	 * @param ctx the parse tree
	 */
	void exitRowTarget(JQuickExcelParser.RowTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#colTarget}.
	 * @param ctx the parse tree
	 */
	void enterColTarget(JQuickExcelParser.ColTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#colTarget}.
	 * @param ctx the parse tree
	 */
	void exitColTarget(JQuickExcelParser.ColTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#cellTarget}.
	 * @param ctx the parse tree
	 */
	void enterCellTarget(JQuickExcelParser.CellTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#cellTarget}.
	 * @param ctx the parse tree
	 */
	void exitCellTarget(JQuickExcelParser.CellTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rangeTarget}.
	 * @param ctx the parse tree
	 */
	void enterRangeTarget(JQuickExcelParser.RangeTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rangeTarget}.
	 * @param ctx the parse tree
	 */
	void exitRangeTarget(JQuickExcelParser.RangeTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#ruleSpec}.
	 * @param ctx the parse tree
	 */
	void enterRuleSpec(JQuickExcelParser.RuleSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#ruleSpec}.
	 * @param ctx the parse tree
	 */
	void exitRuleSpec(JQuickExcelParser.RuleSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#mergeOption}.
	 * @param ctx the parse tree
	 */
	void enterMergeOption(JQuickExcelParser.MergeOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#mergeOption}.
	 * @param ctx the parse tree
	 */
	void exitMergeOption(JQuickExcelParser.MergeOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#mergeSpec}.
	 * @param ctx the parse tree
	 */
	void enterMergeSpec(JQuickExcelParser.MergeSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#mergeSpec}.
	 * @param ctx the parse tree
	 */
	void exitMergeSpec(JQuickExcelParser.MergeSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rowMerge}.
	 * @param ctx the parse tree
	 */
	void enterRowMerge(JQuickExcelParser.RowMergeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rowMerge}.
	 * @param ctx the parse tree
	 */
	void exitRowMerge(JQuickExcelParser.RowMergeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void enterRowRange(JQuickExcelParser.RowRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void exitRowRange(JQuickExcelParser.RowRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#singleRow}.
	 * @param ctx the parse tree
	 */
	void enterSingleRow(JQuickExcelParser.SingleRowContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#singleRow}.
	 * @param ctx the parse tree
	 */
	void exitSingleRow(JQuickExcelParser.SingleRowContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#multiRowRange}.
	 * @param ctx the parse tree
	 */
	void enterMultiRowRange(JQuickExcelParser.MultiRowRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#multiRowRange}.
	 * @param ctx the parse tree
	 */
	void exitMultiRowRange(JQuickExcelParser.MultiRowRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#colMerge}.
	 * @param ctx the parse tree
	 */
	void enterColMerge(JQuickExcelParser.ColMergeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#colMerge}.
	 * @param ctx the parse tree
	 */
	void exitColMerge(JQuickExcelParser.ColMergeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#colRange}.
	 * @param ctx the parse tree
	 */
	void enterColRange(JQuickExcelParser.ColRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#colRange}.
	 * @param ctx the parse tree
	 */
	void exitColRange(JQuickExcelParser.ColRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#singleCol}.
	 * @param ctx the parse tree
	 */
	void enterSingleCol(JQuickExcelParser.SingleColContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#singleCol}.
	 * @param ctx the parse tree
	 */
	void exitSingleCol(JQuickExcelParser.SingleColContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#multiColRange}.
	 * @param ctx the parse tree
	 */
	void enterMultiColRange(JQuickExcelParser.MultiColRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#multiColRange}.
	 * @param ctx the parse tree
	 */
	void exitMultiColRange(JQuickExcelParser.MultiColRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rangeMerge}.
	 * @param ctx the parse tree
	 */
	void enterRangeMerge(JQuickExcelParser.RangeMergeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rangeMerge}.
	 * @param ctx the parse tree
	 */
	void exitRangeMerge(JQuickExcelParser.RangeMergeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#mergePolicy}.
	 * @param ctx the parse tree
	 */
	void enterMergePolicy(JQuickExcelParser.MergePolicyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#mergePolicy}.
	 * @param ctx the parse tree
	 */
	void exitMergePolicy(JQuickExcelParser.MergePolicyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#formulaTarget}.
	 * @param ctx the parse tree
	 */
	void enterFormulaTarget(JQuickExcelParser.FormulaTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#formulaTarget}.
	 * @param ctx the parse tree
	 */
	void exitFormulaTarget(JQuickExcelParser.FormulaTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rowFormula}.
	 * @param ctx the parse tree
	 */
	void enterRowFormula(JQuickExcelParser.RowFormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rowFormula}.
	 * @param ctx the parse tree
	 */
	void exitRowFormula(JQuickExcelParser.RowFormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#colFormula}.
	 * @param ctx the parse tree
	 */
	void enterColFormula(JQuickExcelParser.ColFormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#colFormula}.
	 * @param ctx the parse tree
	 */
	void exitColFormula(JQuickExcelParser.ColFormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#cellFormula}.
	 * @param ctx the parse tree
	 */
	void enterCellFormula(JQuickExcelParser.CellFormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#cellFormula}.
	 * @param ctx the parse tree
	 */
	void exitCellFormula(JQuickExcelParser.CellFormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#cellRef}.
	 * @param ctx the parse tree
	 */
	void enterCellRef(JQuickExcelParser.CellRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#cellRef}.
	 * @param ctx the parse tree
	 */
	void exitCellRef(JQuickExcelParser.CellRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#formulaSpec}.
	 * @param ctx the parse tree
	 */
	void enterFormulaSpec(JQuickExcelParser.FormulaSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#formulaSpec}.
	 * @param ctx the parse tree
	 */
	void exitFormulaSpec(JQuickExcelParser.FormulaSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#graphOption}.
	 * @param ctx the parse tree
	 */
	void enterGraphOption(JQuickExcelParser.GraphOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#graphOption}.
	 * @param ctx the parse tree
	 */
	void exitGraphOption(JQuickExcelParser.GraphOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#graphSpec}.
	 * @param ctx the parse tree
	 */
	void enterGraphSpec(JQuickExcelParser.GraphSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#graphSpec}.
	 * @param ctx the parse tree
	 */
	void exitGraphSpec(JQuickExcelParser.GraphSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#chartType}.
	 * @param ctx the parse tree
	 */
	void enterChartType(JQuickExcelParser.ChartTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#chartType}.
	 * @param ctx the parse tree
	 */
	void exitChartType(JQuickExcelParser.ChartTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#chartTypeValue}.
	 * @param ctx the parse tree
	 */
	void enterChartTypeValue(JQuickExcelParser.ChartTypeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#chartTypeValue}.
	 * @param ctx the parse tree
	 */
	void exitChartTypeValue(JQuickExcelParser.ChartTypeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#chartTitle}.
	 * @param ctx the parse tree
	 */
	void enterChartTitle(JQuickExcelParser.ChartTitleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#chartTitle}.
	 * @param ctx the parse tree
	 */
	void exitChartTitle(JQuickExcelParser.ChartTitleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#categoryAxis}.
	 * @param ctx the parse tree
	 */
	void enterCategoryAxis(JQuickExcelParser.CategoryAxisContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#categoryAxis}.
	 * @param ctx the parse tree
	 */
	void exitCategoryAxis(JQuickExcelParser.CategoryAxisContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#valueAxis}.
	 * @param ctx the parse tree
	 */
	void enterValueAxis(JQuickExcelParser.ValueAxisContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#valueAxis}.
	 * @param ctx the parse tree
	 */
	void exitValueAxis(JQuickExcelParser.ValueAxisContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#categories}.
	 * @param ctx the parse tree
	 */
	void enterCategories(JQuickExcelParser.CategoriesContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#categories}.
	 * @param ctx the parse tree
	 */
	void exitCategories(JQuickExcelParser.CategoriesContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#series}.
	 * @param ctx the parse tree
	 */
	void enterSeries(JQuickExcelParser.SeriesContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#series}.
	 * @param ctx the parse tree
	 */
	void exitSeries(JQuickExcelParser.SeriesContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#seriesSpec}.
	 * @param ctx the parse tree
	 */
	void enterSeriesSpec(JQuickExcelParser.SeriesSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#seriesSpec}.
	 * @param ctx the parse tree
	 */
	void exitSeriesSpec(JQuickExcelParser.SeriesSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#seriesName}.
	 * @param ctx the parse tree
	 */
	void enterSeriesName(JQuickExcelParser.SeriesNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#seriesName}.
	 * @param ctx the parse tree
	 */
	void exitSeriesName(JQuickExcelParser.SeriesNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#seriesData}.
	 * @param ctx the parse tree
	 */
	void enterSeriesData(JQuickExcelParser.SeriesDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#seriesData}.
	 * @param ctx the parse tree
	 */
	void exitSeriesData(JQuickExcelParser.SeriesDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rowSpec}.
	 * @param ctx the parse tree
	 */
	void enterRowSpec(JQuickExcelParser.RowSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rowSpec}.
	 * @param ctx the parse tree
	 */
	void exitRowSpec(JQuickExcelParser.RowSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#colSpec}.
	 * @param ctx the parse tree
	 */
	void enterColSpec(JQuickExcelParser.ColSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#colSpec}.
	 * @param ctx the parse tree
	 */
	void exitColSpec(JQuickExcelParser.ColSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#rangeSpec}.
	 * @param ctx the parse tree
	 */
	void enterRangeSpec(JQuickExcelParser.RangeSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#rangeSpec}.
	 * @param ctx the parse tree
	 */
	void exitRangeSpec(JQuickExcelParser.RangeSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#fieldMapping}.
	 * @param ctx the parse tree
	 */
	void enterFieldMapping(JQuickExcelParser.FieldMappingContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#fieldMapping}.
	 * @param ctx the parse tree
	 */
	void exitFieldMapping(JQuickExcelParser.FieldMappingContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#cellFormat}.
	 * @param ctx the parse tree
	 */
	void enterCellFormat(JQuickExcelParser.CellFormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#cellFormat}.
	 * @param ctx the parse tree
	 */
	void exitCellFormat(JQuickExcelParser.CellFormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#transformRule}.
	 * @param ctx the parse tree
	 */
	void enterTransformRule(JQuickExcelParser.TransformRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#transformRule}.
	 * @param ctx the parse tree
	 */
	void exitTransformRule(JQuickExcelParser.TransformRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#transformValue}.
	 * @param ctx the parse tree
	 */
	void enterTransformValue(JQuickExcelParser.TransformValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#transformValue}.
	 * @param ctx the parse tree
	 */
	void exitTransformValue(JQuickExcelParser.TransformValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(JQuickExcelParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(JQuickExcelParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#formulateCall}.
	 * @param ctx the parse tree
	 */
	void enterFormulateCall(JQuickExcelParser.FormulateCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#formulateCall}.
	 * @param ctx the parse tree
	 */
	void exitFormulateCall(JQuickExcelParser.FormulateCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void enterFunctionArg(JQuickExcelParser.FunctionArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#functionArg}.
	 * @param ctx the parse tree
	 */
	void exitFunctionArg(JQuickExcelParser.FunctionArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(JQuickExcelParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(JQuickExcelParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(JQuickExcelParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(JQuickExcelParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#date}.
	 * @param ctx the parse tree
	 */
	void enterDate(JQuickExcelParser.DateContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#date}.
	 * @param ctx the parse tree
	 */
	void exitDate(JQuickExcelParser.DateContext ctx);
	/**
	 * Enter a parse tree produced by {@link JQuickExcelParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(JQuickExcelParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JQuickExcelParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(JQuickExcelParser.IdentifierContext ctx);
}