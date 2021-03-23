package com.yss.datamiddle.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.yss.datamiddle.enums.MatchCondition;

/**
 * @description: 查询条件
 * @author: fangzhao
 * @create: 2020/4/1 15:34
 * @update: 2020/4/1 15:34
 */
@Target({ElementType.FIELD,ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCondition {

    /**
     * 数据库中字段名,默认为空字符串,则Query类中的字段要与数据库中字段一致
     */
    String column() default "";

    /**
     * @see MatchCondition
     */
    MatchCondition func() default MatchCondition.EQUAL;

    /**
     * object是否可以为null
     */
    boolean nullable() default false;

    /**
     * 字符串是否可为空
     */
    boolean emptyable() default false;
}
