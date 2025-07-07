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

import com.github.paohaijiao.exception.JAssert;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * packageName com.github.paohaijiao.validate
 *
 * @author Martin
 * @version 1.0.0
 * @since 2025/7/6
 */
public abstract class JAbstractValidationRule implements JExcelValidationRule{


    protected final boolean required;

    protected final Map<String,Object> map;

    protected final String customMessage;



    protected JAbstractValidationRule(boolean required, Map<String,Object> map,String customMessage) {
        this.required = required;
        this.map = map;
        this.customMessage = customMessage;
    }

    protected boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public boolean test(String value) {
        if (!required) {
            return true;
        }
        if (null==value&&required) {
            JAssert.throwNewException(this.buildMsg());
        }
        boolean bool= doValidate(value);

        if(!bool){
            if(customMessage != null){
                JAssert.throwNewException(customMessage);
            }else{
                JAssert.throwNewException(getDefaultMsg());
            }
        }
        return bool;
    }

    public String buildMsg(){
        if(StringUtils.isNotEmpty(customMessage)){
            return customMessage;
        }else{
            return getDefaultMsg();
        }

    }

    protected abstract boolean doValidate(String value);


}
