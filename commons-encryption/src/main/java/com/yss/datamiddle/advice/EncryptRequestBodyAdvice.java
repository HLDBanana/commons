package com.yss.datamiddle.advice;

import com.alibaba.fastjson.JSONObject;
import com.yss.datamiddle.annotations.DataConnCipher;
import com.yss.datamiddle.enums.CipherEnum;
import com.yss.datamiddle.enums.CipherPositionEnum;
import com.yss.datamiddle.service.CipherService;
import com.yss.datamiddle.util.SpringContextUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @description: 请求信息，加密处理，以数据连接功能为例，主要针对post请求所提交的数据连接信息（对象形式），对敏感信息进行加密处理
 * @author: fangzhao
 * @create: 2020/7/17 18:18
 * @update: 2020/7/17 18:18
 */
@Component
@ControllerAdvice(basePackages = "com.yss.datamiddle.controller")
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 描述：
     * return methodParameter.hasMethodAnnotation(DataConnCipher.class);
     *
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return boolean
     * @author fangzhao at 2020/7/17 14:46
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        DataConnCipher dataConnCipher = methodParameter.getMethodAnnotation(DataConnCipher.class);
        return (null != dataConnCipher && dataConnCipher.postion().equals(CipherPositionEnum.REQUEST)) ? true : false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        // 是否需要加密
        DataConnCipher encrypt = parameter.getMethodAnnotation(DataConnCipher.class);
        if (null == encrypt || 0 != encrypt.type().compareTo(CipherEnum.ENCRYPT)) {
            return inputMessage;
        }

        String serviceName = encrypt.serviceName();
        CipherService encryptService = SpringContextUtil.getBean(serviceName, CipherService.class);

        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                String bodyStr = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
                JSONObject jsonObject = JSONObject.parseObject(bodyStr);
                Class clazz = null;
                try {
                    clazz = Class.forName(targetType.getTypeName());
                } catch (ClassNotFoundException e) {
                    log.error("EncryptRequestBodyAdvice exception: " + e.getMessage(), e);
                }
                if (null == clazz) {
                    return (InputStream) inputMessage;
                }

                // 判断父类是否有加密需求
                Class superclass = clazz.getSuperclass();
                if (null != superclass) {
                    for (Field superField : superclass.getDeclaredFields()) {
                        DataConnCipher cipher = superField.getAnnotation(DataConnCipher.class);
                        if (null != cipher) {
                            jsonObject.put(superField.getName(), encryptService.encrypt(jsonObject.getString(superField.getName())));
                        }
                    }
                }
                for (Field field : clazz.getDeclaredFields()) {
                    DataConnCipher cipher = field.getAnnotation(DataConnCipher.class);
                    if (null != cipher) {
                        jsonObject.put(field.getName(), encryptService.encrypt(jsonObject.getString(field.getName())));
                    }
                }
                return IOUtils.toInputStream(jsonObject.toJSONString(), "utf-8");
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}