package com.yss.datamiddle.advice;

import com.yss.datamiddle.annotations.DataConnCipher;
import com.yss.datamiddle.enums.CipherEnum;
import com.yss.datamiddle.enums.CipherPositionEnum;
import com.yss.datamiddle.service.CipherService;
import com.yss.datamiddle.util.SpringContextUtil;
import com.yss.datamiddle.vo.RetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @description: 响应信息，解密处理，以数据连接功能为例，主要针对查询数据连接明细信息，对敏感信息进行解密处理
 *      响应信息格式大致两种，直接返回对象、包含对象的RetResult，统一使用RetResult吧
 * @author: fangzhao
 * @create: 2020/7/17 18:18
 * @update: 2020/7/17 18:18
 */
@Component
@ControllerAdvice(basePackages = "com.yss.datamiddle.controller")
public class DecryptResponseBodyAdvice<T> implements ResponseBodyAdvice<T> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 描述：
     * return returnType.hasMethodAnnotation(DataConnCipher.class);
     *
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return boolean
     * @author fangzhao at 2020/7/17 14:46
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        DataConnCipher dataConnCipher = returnType.getMethodAnnotation(DataConnCipher.class);
        return (null != dataConnCipher && dataConnCipher.postion().equals(CipherPositionEnum.RESPONSE)) ? true : false;
    }

    @Override
    public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType,
                             Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                             ServerHttpResponse response) {

        // 是否需要加密or解密，貌似不需要对RequestBodyAdvice接口进行实现，个人理接RequestBodyAdvice是对请求数据处进行处理，而数据连接信息是通过请求获取到的数据，数据处理的时机不同
        DataConnCipher encrypt = returnType.getMethodAnnotation(DataConnCipher.class);

        if (null == encrypt || 0 != encrypt.type().compareTo(CipherEnum.DECRYPT)) {
            return body;
        }

        CipherEnum type = encrypt.type();
        String serviceName = encrypt.serviceName();
        CipherService encryptService = SpringContextUtil.getBean(serviceName, CipherService.class);
        Object data = body;
        if (body instanceof RetResult) {
            data = ((RetResult) body).getData();
        }

        // 判断父类是否有加密需求
        Class superclass = data.getClass().getSuperclass();
        if (null != superclass) {
            doWrite(superclass.getDeclaredFields(), data, encryptService);
        }
        doWrite(data.getClass().getDeclaredFields(), data, encryptService);
        return body;
    }

    private void doWrite(Field[] fields, Object data, CipherService encryptService) {
        try {
            for (Field field : fields) {
                DataConnCipher dataConnEncrypt = field.getAnnotation(DataConnCipher.class);

                if (null != dataConnEncrypt) {
                    // 目前只针对字符串类型进行解密处理
                    if ("class java.lang.String".equals(field.getGenericType().toString())) {
                        field.setAccessible(true);
                        Method method = data.getClass().getMethod("get" + getMethodName(field.getName()));
                        String value = (String) method.invoke(data);
                        field.set(data, encryptService.decrypt(value));
                    }
                }
            }
        } catch (Exception e) {
            log.error("DecryptResponseBodyAdvice beforeBodyWrite exception:", e);
        }
    }

    private String getMethodName(String fieldName) {
        byte[] bytes = fieldName.getBytes();
        bytes[0] = (byte) (bytes[0] - 'a' + 'A');
        return new String(bytes);
    }
}