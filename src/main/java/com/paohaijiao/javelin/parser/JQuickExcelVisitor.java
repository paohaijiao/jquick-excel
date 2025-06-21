// Generated from D:/idea/jthornruleGrammer/excel/JQuickExcel.g4 by ANTLR 4.13.2
package com.paohaijiao.javelin.parser;
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
	 * Visit a parse tree produced by {@link JQuickExcelParser#source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSource(JQuickExcelParser.SourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#destination}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDestination(JQuickExcelParser.DestinationContext ctx);
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
	 * Visit a parse tree produced by {@link JQuickExcelParser#excelFormula}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExcelFormula(JQuickExcelParser.ExcelFormulaContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#formatSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormatSpec(JQuickExcelParser.FormatSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#dateFormat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateFormat(JQuickExcelParser.DateFormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#numberFormat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberFormat(JQuickExcelParser.NumberFormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#stringFormat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringFormat(JQuickExcelParser.StringFormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#customFormat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCustomFormat(JQuickExcelParser.CustomFormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(JQuickExcelParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#functionArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionArg(JQuickExcelParser.FunctionArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#quotedFunctionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuotedFunctionCall(JQuickExcelParser.QuotedFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(JQuickExcelParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(JQuickExcelParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link JQuickExcelParser#fileName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFileName(JQuickExcelParser.FileNameContext ctx);
}