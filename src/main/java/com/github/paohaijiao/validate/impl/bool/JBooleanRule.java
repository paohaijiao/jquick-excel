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
package com.github.paohaijiao.validate.impl.bool;

import com.github.paohaijiao.exception.JAssert;
import com.github.paohaijiao.validate.JAbstractValidationRule;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.validate.impl
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public class JBooleanRule extends JAbstractValidationRule {


    public JBooleanRule(boolean required, Map<String,Object> map, String customMessage) {
        super(required, map, customMessage);
        JAssert.notNull(map, "the map must not be null");
    }

        @Override
    protected boolean doValidate(String value) {
        boolean bool=this.map.containsValue(value);
        if(bool){
            return true;
        }
        JAssert.throwNewException(this.buildMsg() );
        return false;

    }

    @Override
    public String getDefaultMsg() {
        String scope=StringUtils.join(map.values(),",");
        return String.format("it must be a boolean value scope is:%s", scope);
    }
}
