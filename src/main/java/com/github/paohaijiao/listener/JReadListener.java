package com.github.paohaijiao.listener;

import java.util.List;

public interface JReadListener<T> {
    void onSuccess(List<T> data);
    void onError(Exception e);
    void onComplete();
}
