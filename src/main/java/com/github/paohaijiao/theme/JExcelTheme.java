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
package com.github.paohaijiao.theme;

import com.github.paohaijiao.theme.enums.JExcelThemeType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public interface JExcelTheme {

    /**
     * 主题唯一编码（与 {@link JExcelThemeType#getCode()} 对应）。
     */
    String getCode();

    /**
     * 主题展示名称。
     */
    String getName();

    /**
     * 主题描述信息。
     */
    String getDescription();

    /**
     * 表头样式，对应 {@code JExcelCommonHandler#buildDefaultHeaderStyle}。
     */
    CellStyle buildHeaderStyle(Workbook workbook);

    /**
     * 数据行（偶数行）样式，对应 {@code JExcelCommonHandler#buildDefaultDataEvenStyle}。
     */
    CellStyle buildDataEvenStyle(Workbook workbook);

    /**
     * 数据行（奇数行）样式，对应 {@code JExcelCommonHandler#buildDefaultDataOddStyle}。
     */
    CellStyle buildDataOddStyle(Workbook workbook);

    /**
     * 标题行样式（用于合并单元格的大标题）。
     */
    CellStyle buildTitleStyle(Workbook workbook);

    /**
     * 页脚样式，对应 {@code JExcelCommonHandler#buildDefaultFooterStyle}。
     */
    CellStyle buildFooterStyle(Workbook workbook);

    /**
     * 公式单元格样式，对应 {@code JExcelCommonHandler#buildDefaultFormulaCellStyle}。
     */
    CellStyle buildFormulaCellStyle(Workbook workbook);

    /**
     * 构建页脚合并区域，对应 {@code JExcelCommonHandler#buildDefaultFooter}。
     *
     * @param workbook  工作簿
     * @param sheet     工作表
     * @param rowNum    页脚所在行号
     * @param maxColumn 合并到第几列（从 0 开始）
     * @param label     页脚文本
     */
    default void buildFooter(Workbook workbook, Sheet sheet, int rowNum, int maxColumn, String label) {
        CellStyle style = buildFormulaCellStyle(workbook);
        Row footerRow = sheet.createRow(rowNum);
        Cell footerCell = footerRow.createCell(0);
        footerCell.setCellValue(label);
        footerCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, maxColumn));
    }
}
