package com.paohaijiao.javelin;

import com.paohaijiao.javelin.adaptor.JQuickAdaptor;
import com.paohaijiao.javelin.exception.JAntlrExecutionException;
import com.paohaijiao.javelin.executor.JThornRuleQuickExcelExecutor;
import com.paohaijiao.javelin.resource.JQuickReader;
import com.paohaijiao.javelin.resource.impl.JQuickReSourceFileReader;

public class ExcelMain {
    public static void main(String[] args) {
        JThornRuleQuickExcelExecutor executor = new JThornRuleQuickExcelExecutor();
        executor.addErrorListener(error -> {
            System.err.printf("错误: 行%d:%d - %s%n",
                    error.getLine(), error.getCharPosition(), error.getMessage());
            System.err.println("规则栈: " + error.getRuleStack());
        });
        try {
            JQuickReader fileReader = new JQuickReSourceFileReader("rule.txt");
            JQuickAdaptor context = new JQuickAdaptor(fileReader);
            System.out.println(context.getRuleContent());
            Object jsonObject = executor.execute(context.getRuleContent());
            System.out.println("结果: " + jsonObject);
        } catch (JAntlrExecutionException e) {
            System.err.println("解析失败: " + e.getMessage());
            e.getErrors().forEach(err ->
                    System.err.println(" - " + err.getMessage()));
        }
    }
}
