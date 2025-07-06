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
package com.github.paohaijiao.validate;

/**
 * packageName com.github.paohaijiao.validate
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public abstract class JAbstractValidationRule implements JExcelValidationRule{

    protected final boolean required;

    protected JAbstractValidationRule(boolean required) {
        this.required = required;
    }

    protected boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public boolean test(String value) {
        if (isEmpty(value)) {
            return !required;
        }
        return doValidate(value);
    }

    protected abstract boolean doValidate(String value);


}
