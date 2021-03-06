package com.yss.datamiddle.tools;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @description: bean工具类
 * @author: fangzhao
 * @create: 2020/4/22 11:16
 * @update: 2020/4/22 11:16
 */
public class BeanUtil extends BeanUtils {

    /**
     * 描述：重写copyProperties方法，只将source中不为空的字段，拷贝(覆盖)到target中，
     *
     * @param source
     * @param target
     * @return void
     * @author fangzhao at 2020/4/22 11:27
     */
    public static void copyProperties(Object source, Object target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (null != targetPd.getWriteMethod()) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (null != sourcePd && null != sourcePd.getReadMethod()) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 这里判断以下value是否为空（null），如果不为空，则值拷贝
                        if (null != value) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                    }
                }
            }
        }
    }

    /**
     * 描述：深拷贝
     *
     * @param source
     * @param target
     * @return void
     * @author fangzhao at 2020/6/12 10:01
     */
    public static void deepCopy(Object source, Object target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (null != targetPd.getWriteMethod()) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (null != sourcePd && null != sourcePd.getReadMethod()) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 这里判断以下value是否为空（null），如果不为空，则值拷贝
                        if (null != value) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                    }
                }
            }
        }
    }
}
