package com.github.paohaijiao.merge;

import com.github.paohaijiao.param.JContext;

import java.util.Map;

public interface JMergeHandler {

    void merge(Map<String, Object> mergeProperties);

}
