package com.yss.datamiddle.annotations;

import com.yss.datamiddle.enums.CipherEnum;
import com.yss.datamiddle.enums.CipherPositionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 加密解密注解，包含枚举类型的加密解密标识，加密解密时机，指定加密解密业务类
 * @author: fangzhao
 * @create: 2020/7/17 14:57
 * @update: 2020/7/17 14:57
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataConnCipher {

    CipherEnum type() default CipherEnum.ENCRYPT;
    CipherPositionEnum postion() default CipherPositionEnum.SERVICE;
    String serviceName() default "cipherSymService";
}
