package com.yss.datamiddle.enums;

/**
 * @description: 查询条件关系匹配
 * @author: fangzhao
 * @create: 2020/4/1 15:34
 * @update: 2020/4/1 15:34
 */
public enum MatchCondition {
    /**
     * equal-相等，notEqual-不等于，like-模糊匹配，notLike-，
     * gt-大于，ge-大于等于，lt-小于，le-小于等于，
     * greaterThan-大于，greaterThanOrEqualTo-大于等于，lessThan-小于，lessThanOrEqualTo-小于等于
     */
    EQUAL,
    NOTEQUAL,
    LIKE,
    NOTLIKE,

    GT,
    GE,
    LT,
    LE,

    GREATERTHAN,
    GREATERTHANOREQUALTO,
    LESSTHAN,
    LESSTHANOREQUALTO
}
