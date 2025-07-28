// Generated from D:/my/jthornruleGrammer/excel/JQuickExcel.g4 by ANTLR 4.13.2
package com.github.paohaijiao.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JQuickExcelParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JQuickExcelVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#config}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfig(JQuickExcelParser.ConfigContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#importConfig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportConfig(JQuickExcelParser.ImportConfigContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#exportConfig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportConfig(JQuickExcelParser.ExportConfigContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#importOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportOption(JQuickExcelParser.ImportOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#exportOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportOption(JQuickExcelParser.ExportOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#sheetOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSheetOption(JQuickExcelParser.SheetOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#headerOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeaderOption(JQuickExcelParser.HeaderOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rangeOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeOption(JQuickExcelParser.RangeOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#mappingOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMappingOption(JQuickExcelParser.MappingOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#formatOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormatOption(JQuickExcelParser.FormatOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#transformOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformOption(JQuickExcelParser.TransformOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#formulaOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormulaOption(JQuickExcelParser.FormulaOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#styleOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleOption(JQuickExcelParser.StyleOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#styleTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleTarget(JQuickExcelParser.StyleTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rowStyle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowStyle(JQuickExcelParser.RowStyleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#colStyle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColStyle(JQuickExcelParser.ColStyleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#cellStyle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCellStyle(JQuickExcelParser.CellStyleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rangeStyle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeStyle(JQuickExcelParser.RangeStyleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rangeRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeRef(JQuickExcelParser.RangeRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#styleProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleProperty(JQuickExcelParser.StylePropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#validationOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidationOption(JQuickExcelParser.ValidationOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#validationRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValidationRule(JQuickExcelParser.ValidationRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#targetSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetSpec(JQuickExcelParser.TargetSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rowTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowTarget(JQuickExcelParser.RowTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#colTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColTarget(JQuickExcelParser.ColTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#cellTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCellTarget(JQuickExcelParser.CellTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rangeTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeTarget(JQuickExcelParser.RangeTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#ruleSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleSpec(JQuickExcelParser.RuleSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#ruleItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleItem(JQuickExcelParser.RuleItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#ruleRequired}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleRequired(JQuickExcelParser.RuleRequiredContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#ruleMsg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleMsg(JQuickExcelParser.RuleMsgContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap(JQuickExcelParser.MapContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#mapItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapItem(JQuickExcelParser.MapItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#mapkey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapkey(JQuickExcelParser.MapkeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#mapValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapValue(JQuickExcelParser.MapValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#mergeOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeOption(JQuickExcelParser.MergeOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#mergeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeSpec(JQuickExcelParser.MergeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rowMerge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowMerge(JQuickExcelParser.RowMergeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rowRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowRange(JQuickExcelParser.RowRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#singleRow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleRow(JQuickExcelParser.SingleRowContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#multiRowRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiRowRange(JQuickExcelParser.MultiRowRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#colMerge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColMerge(JQuickExcelParser.ColMergeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#colRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColRange(JQuickExcelParser.ColRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#singleCol}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleCol(JQuickExcelParser.SingleColContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#multiColRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiColRange(JQuickExcelParser.MultiColRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rangeMerge}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeMerge(JQuickExcelParser.RangeMergeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#mergePolicy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergePolicy(JQuickExcelParser.MergePolicyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#formulaTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormulaTarget(JQuickExcelParser.FormulaTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rowFormula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowFormula(JQuickExcelParser.RowFormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#colFormula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColFormula(JQuickExcelParser.ColFormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#cellFormula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCellFormula(JQuickExcelParser.CellFormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#cellRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCellRef(JQuickExcelParser.CellRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#formulaSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormulaSpec(JQuickExcelParser.FormulaSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#graphOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGraphOption(JQuickExcelParser.GraphOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#graphSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGraphSpec(JQuickExcelParser.GraphSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#chartType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChartType(JQuickExcelParser.ChartTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#chartTypeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChartTypeValue(JQuickExcelParser.ChartTypeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#chartTitle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChartTitle(JQuickExcelParser.ChartTitleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#categoryAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCategoryAxis(JQuickExcelParser.CategoryAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#valueAxis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueAxis(JQuickExcelParser.ValueAxisContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#categories}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCategories(JQuickExcelParser.CategoriesContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#series}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeries(JQuickExcelParser.SeriesContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#seriesSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeriesSpec(JQuickExcelParser.SeriesSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#seriesName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeriesName(JQuickExcelParser.SeriesNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#seriesData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeriesData(JQuickExcelParser.SeriesDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#footerOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFooterOption(JQuickExcelParser.FooterOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rowSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowSpec(JQuickExcelParser.RowSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#colSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColSpec(JQuickExcelParser.ColSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#rangeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeSpec(JQuickExcelParser.RangeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#fieldMapping}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldMapping(JQuickExcelParser.FieldMappingContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#cellFormat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCellFormat(JQuickExcelParser.CellFormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#transformRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformRule(JQuickExcelParser.TransformRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#transformValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransformValue(JQuickExcelParser.TransformValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(JQuickExcelParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#formulateCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormulateCall(JQuickExcelParser.FormulateCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#functionArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArg(JQuickExcelParser.FunctionArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(JQuickExcelParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(JQuickExcelParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#date}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate(JQuickExcelParser.DateContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(JQuickExcelParser.IdentifierContext ctx);
}