package com.yss.datamiddle.enums;

/**
 * @author yangjianlei
 * @title: AdviceEnum
 * @projectName logdemo
 * @description: TODO
 * @date 2020/11/2514:37
 */
public enum AdviceEnum {
    BEFORE,
    AFTER,
    AROUND,
    AFTER_RETURN,
    AFTER_THROWING;

    private AdviceEnum() { }
}
