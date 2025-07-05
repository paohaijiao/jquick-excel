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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * packageName com.github.paohaijiao.graph.model
 *
 * @author Martin
 * @version 1.0.0
 * @className JSeriesData
 * @date 2025/7/5
 * @description
 */
public class JSeriesData extends HashMap<String, Object> {
    private static final String NAME = "name";
    private static final String DATA = "data";

    /**
     * Sets the series name.
     * @param name The name of the series
     */
    public void setName(String name) {
        this.put(NAME, name);
    }

    /**
     * Gets the series name.
     * @return The name of the series
     */
    public String getName() {
        return (String) this.get(NAME);
    }

    /**
     * Sets the data points for the series.
     * @param data List of data points (supports Number types)
     */
    public void setData(List<? extends Number> data) {
        this.put(DATA, new ArrayList<>(data));
    }

    /**
     * Gets the data points for the series.
     * @return List of data points
     */
    @SuppressWarnings("unchecked")
    public List<Number> getData() {
        return (List<Number>) this.get(DATA);
    }
}
