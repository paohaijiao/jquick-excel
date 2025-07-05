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
package com.github.paohaijiao.graph.model;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * packageName com.github.paohaijiao.graph.model
 *
 * @author Martin
 * @version 1.0.0
 * @className JChartData
 * @date 2025/7/5
 * @description
 */
public class JChartData extends HashMap<String, Object> {
    private static final String TITLE = "title";
    private static final String CATEGORY_AXIS_TITLE = "categoryAxisTitle";
    private static final String VALUE_AXIS_TITLE = "valueAxisTitle";
    private static final String CATEGORIES = "categories";
    private static final String SERIES = "series";
    private static final String chartType = "chartType";
    /**
     * Sets the chart title.
     * @param title The title of the chart
     */
    public void setTitle(String title) {
        this.put(TITLE, title);
    }

    /**
     * Gets the chart title.
     * @return The title of the chart
     */
    public String getTitle() {
        return (String) this.get(TITLE);
    }

    /**
     * Sets the category axis title.
     * @param categoryAxisTitle The title for the category axis
     */
    public void setCategoryAxisTitle(String categoryAxisTitle) {
        this.put(CATEGORY_AXIS_TITLE, categoryAxisTitle);
    }

    /**
     * Gets the category axis title.
     * @return The title for the category axis
     */
    public String getCategoryAxisTitle() {
        return (String) this.get(CATEGORY_AXIS_TITLE);
    }

    /**
     * Sets the value axis title.
     * @param valueAxisTitle The title for the value axis
     */
    public void setValueAxisTitle(String valueAxisTitle) {
        this.put(VALUE_AXIS_TITLE, valueAxisTitle);
    }

    /**
     * Gets the value axis title.
     * @return The title for the value axis
     */
    public String getValueAxisTitle() {
        return (String) this.get(VALUE_AXIS_TITLE);
    }

    /**
     * Sets the categories for the chart.
     * @param categories List of category names
     */
    public void setCategories(List<String> categories) {
        this.put(CATEGORIES, new ArrayList<>(categories));
    }

    /**
     * Sets the categories for the chart using varargs.
     * @param categories Array of category names
     */
    public void setCategories(String... categories) {
        setCategories(Arrays.asList(categories));
    }

    /**
     * Gets the categories for the chart.
     * @return List of category names
     */
    @SuppressWarnings("unchecked")
    public List<String> getCategories() {
        return (List<String>) this.get(CATEGORIES);
    }

    /**
     * Sets the series data for the chart.
     * @param series List of series data
     */
    public void setSeries(List<JSeriesData> series) {
        this.put(SERIES, new ArrayList<>(series));
    }

    /**
     * Gets the series data for the chart.
     * @return List of series data
     */
    @SuppressWarnings("unchecked")
    public List<JSeriesData> getSeries() {
        return (List<JSeriesData>) this.get(SERIES);
    }

    /**
     * Adds a single series to the chart.
     * @param series The series data to add
     */
    public void addSeries(JSeriesData series) {
        if (!this.containsKey(SERIES)) {
            this.put(SERIES, new ArrayList<>());
        }
        this.getSeries().add(series);
    }
    public void setChartType(String type) {
        this.put(chartType, type);
    }
    public String getChartType() {
        return (String) this.get(chartType);
    }
}
