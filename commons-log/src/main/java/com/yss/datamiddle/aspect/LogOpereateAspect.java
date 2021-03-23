package com.yss.datamiddle.aspect;

import com.yss.datamiddle.enums.AdviceEnum;
import com.yss.datamiddle.pojo.LogMessagePojo;
import com.yss.datamiddle.tools.GsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @description: 日志操作切面
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@Aspect
@Component
public class LogOpereateAspect {

    /**
     * @description: TODO
     * @param joinPoint joinPoint
     * @throws
     * @author yangjianlei
     * @date 2020/11/25 19:13
     */
    @Before("@annotation(Log.Before)")
    public void logBefore(JoinPoint joinPoint) {
        // 前置通知
        before(joinPoint, AdviceEnum.BEFORE);
    }

    @Around("@annotation(Log.Around)")
    public Object around(ProceedingJoinPoint joinPoint) {

        long beginTime = System.currentTimeMillis();
        // 前置通知
        before(joinPoint, AdviceEnum.AROUND);

        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
            logger.error("执行异常", throwable);
        }

        long endTime = System.currentTimeMillis();
        afterReturn(joinPoint, proceed, AdviceEnum.AROUND, beginTime, endTime);
        return proceed;
    }

    @After("@annotation(Log.After)")
    public void logAfter(JoinPoint joinPoint) {
        // 后置通知
        after(joinPoint);
    }

    @AfterReturning(value = "@annotation(Log.AfterReturn)", returning = "result")
    public void logAfterReturn(JoinPoint joinPoint, Object result) {
        // 返回通知
        afterReturn(joinPoint, result, AdviceEnum.AFTER_RETURN, 0L, 0L);
    }

    @AfterThrowing(throwing="e", value = "@annotation(Log.AfterThrowing)")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        LogMessagePojo logMessagePojo = new LogMessagePojo();
        logMessagePojo.setLogExceptionMessge("执行异常", className + "-" + methodName, e.getMessage());

        logger.error(GsonUtil.toJsonString(logMessagePojo), e);
    }

    /**
     * @description: 前置通知
     * @param joinPoint
     * @throws
     * @author yangjianlei
     * @date 2020/11/25 15:35
     */
    private void before(JoinPoint joinPoint, AdviceEnum adviceEnum) {
        // 请求的类名、方法名
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        String value = "";
        String type = "";
        LogLevel logLevel = null;

        switch (adviceEnum) {
            case BEFORE: {
                Log.Before logBefore = method.getAnnotation(Log.Before.class);
                value = logBefore.value();
                type = logBefore.type().getMsg();
                logLevel = logBefore.level();
                break;
            }
            case AROUND: {
                Log.Around logAround = method.getAnnotation(Log.Around.class);
                value = logAround.value();
                type = logAround.type().getMsg();
                logLevel = logAround.level();
                break;
            }
            default:
                break;
        }
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        String json = null;
        if (null != args && 0 < args.length) {
            try {
                json = GsonUtil.toJsonString(args);
            } catch (Exception e) {
                logger.error("JSON 转换异常：{}", e.getMessage());
            }
        }
        LogMessagePojo logMessagePojo = new LogMessagePojo();
        logMessagePojo.setLogBeforMessage("请求开始", className + "-" + methodName, type, value, json,uri,httpMethod);
        switch (logLevel) {
            case TRACE: {
                if (logger.isTraceEnabled()) {
                    if (AdviceEnum.AROUND.equals(adviceEnum)) {
                        // logger.trace("请求开始，进入方法：{}，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.trace(GsonUtil.toJsonString(logMessagePojo));
                    } else {
                        // logger.trace("请求已接收，开始进入{}方法，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.trace(GsonUtil.toJsonString(logMessagePojo));
                    }
                }
                break;
            }
            case DEBUG: {
                if (logger.isDebugEnabled()) {
                    if (AdviceEnum.AROUND.equals(adviceEnum)) {
                        // logger.debug("请求开始，进入方法：{}，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.debug(GsonUtil.toJsonString(logMessagePojo));
                    } else {
                        // logger.debug("请求已接收，开始进入{}方法，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.debug(GsonUtil.toJsonString(logMessagePojo));
                    }
                }
                break;
            }
            case INFO: {
                if (logger.isInfoEnabled()) {
                    if (AdviceEnum.AROUND.equals(adviceEnum)) {
                        // logger.info("请求开始，进入方法：{}，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json, logMessagePojo);
                        // logger.info("请求开始，{}", GsonUtil.toJsonString(logMessagePojo));
                        logger.info(GsonUtil.toJsonString(logMessagePojo));
                        // logger.info(Markers.append("hits", logMessagePojo), "请求开始，进入方法：{}，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                    } else {
                        // logger.info("请求已接收，开始进入{}方法，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.info(GsonUtil.toJsonString(logMessagePojo));
                    }
                }
                break;
            }
            case WARN: {
                if (logger.isWarnEnabled()) {
                    if (AdviceEnum.AROUND.equals(adviceEnum)) {
                        // logger.warn("请求开始，进入方法：{}，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.warn(GsonUtil.toJsonString(logMessagePojo));
                    } else {
                        // logger.warn("请求已接收，开始进入{}方法，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.warn(GsonUtil.toJsonString(logMessagePojo));
                    }
                }
                break;
            }
            case ERROR: {
                if (logger.isErrorEnabled()) {
                    if (AdviceEnum.AROUND.equals(adviceEnum)) {
                        // logger.error("请求开始，进入方法：{}，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.error(GsonUtil.toJsonString(logMessagePojo));
                    } else {
                        // logger.error("请求已接收，开始进入{}方法，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                        logger.error(GsonUtil.toJsonString(logMessagePojo));
                    }
                }
                break;
            }
            case FATAL: {
                if (AdviceEnum.AROUND.equals(adviceEnum)) {
                    // logger.error("请求开始，进入方法：{}，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                    logger.error(GsonUtil.toJsonString(logMessagePojo));
                } else{
                    // logger.error("请求已接收，开始进入{}方法，执行操作：{}, 类型：{}, 参数：{}", className + "-" + methodName, value, type, json);
                    logger.error(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case OFF:
            default:
                break;
        }
    }

    /**
     * @description: 后置通知(返回结果信息)
     * @param
     * @return
     * @throws
     * @author yangjianlei
     * @date 2020/11/25 15:47
     */
    private void afterReturn(JoinPoint joinPoint, Object proceed, AdviceEnum adviceEnum, long beginTime, long endTime) {
        // 请求的类名、方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        LogLevel logLevel = null;
        switch (adviceEnum) {
            case AFTER_RETURN: {
                Log.AfterReturn logBefore = method.getAnnotation(Log.AfterReturn.class);
                logLevel = logBefore.level();
                break;
            }
            case AROUND: {
                Log.Around logAround = method.getAnnotation(Log.Around.class);
                logLevel = logAround.level();
                break;
            }
            default:
                break;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        LogMessagePojo logMessagePojo = new LogMessagePojo();
        long excutetime = endTime - beginTime;
        long maxmememory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        long locatememory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        long locateavailable = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        long maxvailable = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        logMessagePojo.setLogReturnResultMessage("请求结束",
                className + "-" + methodName,
                proceed.toString(),
                excutetime + "ms",
                maxmememory + "m",
                locatememory + "m",
                locateavailable + "m",
                maxvailable + "m",
                uri,
                httpMethod);
        switch (logLevel) {
            case TRACE: {
                if (logger.isTraceEnabled()) {
                    /*logger.trace("请求结束，方法{}执行完毕，返回结果：{}，耗时： {}   最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                            className + "-" + methodName,
                            proceed,
                            (endTime - beginTime),
                            Runtime.getRuntime().maxMemory() / 1024 / 1024,
                            Runtime.getRuntime().totalMemory() / 1024 / 1024,
                            Runtime.getRuntime().freeMemory() / 1024 / 1024,
                            (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);*/
                    logger.trace(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case DEBUG: {
                if (logger.isDebugEnabled()) {
                    logger.debug(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case INFO: {
                if (logger.isInfoEnabled()) {
                    logger.info(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case WARN: {
                if (logger.isWarnEnabled()) {
                    logger.warn(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case ERROR: {
                if (logger.isErrorEnabled()) {
                    logger.error(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case FATAL: {
                logger.error(GsonUtil.toJsonString(logMessagePojo));
                break;
            }
            case OFF:
            default:
                break;
        }
    }

    /**
     * @description: 后置通知(不返回结果信息)
     * @param
     * @return
     * @throws
     * @author yangjianlei
     * @date 2020/11/25 15:47
     */
    private void after(JoinPoint joinPoint) {
        // 请求的类名、方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        Log.After logOperate = method.getAnnotation(Log.After.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        long maxmememory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        long locatememory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        long locateavailable = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        long maxvailable = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        LogMessagePojo logMessagePojo = new LogMessagePojo();
        logMessagePojo.setLogReturnMessage("请求结束",
                className + "-" + methodName,
                maxmememory + "m",
                locatememory + "m",
                locateavailable + "m",
                maxvailable + "m",
                uri,
                httpMethod);
        switch (logOperate.level()) {
            case TRACE: {
                if (logger.isTraceEnabled()) {
                    /*logger.trace("请求结束，方法{}执行完毕    最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                            className + "-" + methodName,
                            Runtime.getRuntime().maxMemory() / 1024 / 1024,
                            Runtime.getRuntime().totalMemory() / 1024 / 1024,
                            Runtime.getRuntime().freeMemory() / 1024 / 1024,
                            (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);*/
                    logger.trace(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case DEBUG: {
                if (logger.isDebugEnabled()) {
                    logger.debug(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case INFO: {
                if (logger.isInfoEnabled()) {
                    logger.info(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case WARN: {
                if (logger.isWarnEnabled()) {
                    logger.warn(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case ERROR: {
                if (logger.isErrorEnabled()) {
                    logger.error(GsonUtil.toJsonString(logMessagePojo));
                }
                break;
            }
            case FATAL: {
                logger.error(GsonUtil.toJsonString(logMessagePojo));
                break;
            }
            case OFF:
            default:
                break;
        }
    }
}
