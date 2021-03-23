package com.yss.datamiddle.advice;

import com.yss.datamiddle.annotations.DataConnCipher;
import com.yss.datamiddle.enums.CipherEnum;
import com.yss.datamiddle.enums.CipherPositionEnum;
import com.yss.datamiddle.service.CipherService;
import com.yss.datamiddle.util.SpringContextUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @description: 加密解密时机，下放到业务层
 * @author: fangzhao
 * @create: 2020/7/17 18:18
 * @update: 2020/7/17 18:18
 */
@Aspect
@Component
public class CipherAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.yss.datamiddle.annotations.DataConnCipher)")
    public void cipherAnnotation() {
    }

    @Around("cipherAnnotation()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        signature.getDeclaringType();
        Method method = signature.getMethod();
        DataConnCipher dataConnCipher = method.getAnnotation(DataConnCipher.class);
        if (null != dataConnCipher && dataConnCipher.postion().equals(CipherPositionEnum.SERVICE)) {

            String serviceName = dataConnCipher.serviceName();
            CipherService cipherService = SpringContextUtil.getBean(serviceName, CipherService.class);

            // 加密 解密分别处理
            CipherEnum cipherEnum = dataConnCipher.type();
            switch (cipherEnum) {
                case ENCRYPT:
                    // 执行
                    Object[] args = joinPoint.getArgs();
                    for (Object arg : args) {
                        dealObj(arg, cipherService, true);
                    }
                    break;
                case DECRYPT:
                    // 执行
                    Object obj = joinPoint.proceed();
                    dealObj(obj, cipherService, false);
                    return obj;
                default:
            }
        }
        return joinPoint.proceed();
    }

    private void dealObj(Object obj, CipherService cipherService, boolean encrypt) throws Throwable {
        if (null != obj && !"".equals(obj)) {
            Class clazz = obj.getClass();
            Field[] fieldInfo = clazz.getDeclaredFields();
            for (Field field : fieldInfo) {
                DataConnCipher decrypt = field.getAnnotation(DataConnCipher.class);
                if (null != decrypt) {
                    // 成员变量为private，故必须进行此操做
                    field.setAccessible(true);
                    Object fieldValue = field.get(obj);
                    if (null != fieldValue && !"".equals(fieldValue)) {
                        field.set(obj, encrypt ? cipherService.encrypt(fieldValue.toString()) : cipherService.decrypt(fieldValue.toString()));
                    }
                }
            }
            Class superclass = clazz.getSuperclass();
            if (null != superclass) {
                dealObj(superclass.newInstance(), cipherService, encrypt);
            }
        }
    }
}