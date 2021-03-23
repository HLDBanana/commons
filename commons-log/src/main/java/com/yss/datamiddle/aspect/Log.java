package com.yss.datamiddle.aspect;

import com.yss.datamiddle.enums.LogOperateType;
import org.springframework.boot.logging.LogLevel;

import java.lang.annotation.*;

/**
 * @description: 日志前置通知操作注解
 * @title: LogBefore
 * @projectName logdemo
 * @author yangjianlei
 * @date 2020/11/2510:21
 */
public interface Log {

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Before {
        String value() default "";
        LogOperateType type() default LogOperateType.QUERY;
        LogLevel level();
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Around {
        String value() default "";
        LogOperateType type() default LogOperateType.QUERY;
        LogLevel level();
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface After {
        String value() default "";
        LogOperateType type() default LogOperateType.QUERY;
        LogLevel level();
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface AfterReturn {
        String value() default "";
        LogOperateType type() default LogOperateType.QUERY;
        LogLevel level();
    }

    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface AfterThrowing {
        String value() default "";
        LogOperateType type() default LogOperateType.QUERY;
    }
}
