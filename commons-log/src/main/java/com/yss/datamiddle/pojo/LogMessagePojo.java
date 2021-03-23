package com.yss.datamiddle.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangjianlei
 * @title: LogMessagePojo
 * @projectName logback-elasticsearch-appender
 * @description: TODO
 * @date 2020/12/810:09
 */
@Data
public class LogMessagePojo {
    /** 请求地址 */
    String uri;
    /** 异常信息 */
    String httpMethod;
    /** 方法执行时间 */
    private String excutetime;
    /** 已分配内存 */
    private String locatememory;
    /** 日志切面名称 */
    private String action;
    /** 方法名称 */
    private String methodname;
    /** 方法描述 */
    private String methoddesc;
    /** 方法类型 */
    private String methodtype;
    /** 方法参数 */
    private String methodparams;
    /** 方法返回结果 */
    private String returnresult;
    /** 最大内存 */
    private String maxmemory;
    /** 已分配内存中的剩余空间 */
    private String locateavailable;
    /** 最大可用内存 */
    private String maxvailable;
    /** 异常信息 */
    private String exception;


    public LogMessagePojo() {
    }

    /**
     * @description: 前置信息
     * @param methodname methodname
     * @param methoddesc methoddesc
     * @param methodparams methodparams
     * @return: null
     * @throws
     * @author yangjianlei
     * @date 2020/12/8 10:49
     */
    public void setLogBeforMessage(String action, String methodname, String methodtype, String methoddesc, String methodparams,String uri,String httpMethod) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.setAction(action);
        this.setMethodname(methodname);
        this.setMethodtype(methodtype);
        this.setMethoddesc(methoddesc);
        this.setMethodparams(methodparams);
    }

    /**
     * @description: 后置返回信息
     * @param methodname methodname
     * @param returnresult returnresult
     * @param excutetime excutetime
     * @param maxmemory maxmemory
     * @param locatememory locatememory
     * @param locateavailable locateavailable
     * @param maxvailable maxvailable
     * @return: null
     * @throws
     * @author yangjianlei
     * @date 2020/12/8 10:51
     */
    public void setLogReturnResultMessage(String action, String methodname, String returnresult, String excutetime, String maxmemory, String locatememory, String locateavailable, String maxvailable,String uri,String httpMethod) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.action = action;
        this.methodname = methodname;
        this.returnresult = returnresult;
        this.excutetime = excutetime;
        this.maxmemory = maxmemory;
        this.locatememory = locatememory;
        this.locateavailable = locateavailable;
        this.maxmemory = maxmemory;
        this.maxvailable = maxvailable;
    }

    /**
     * @description: 后置返回信息（不带返回结果）
     * @param methodname methodname
     * @param maxmemory maxmemory
     * @param locatememory locatememory
     * @param locateavailable locateavailable
     * @param maxvailable maxvailable
     * @return: null
     * @throws
     * @author yangjianlei
     * @date 2020/12/8 10:51
     */
    public void setLogReturnMessage(String action, String methodname, String maxmemory, String locatememory, String locateavailable, String maxvailable,String uri,String httpMethod) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.action = action;
        this.methodname = methodname;
        this.maxmemory = maxmemory;
        this.locatememory = locatememory;
        this.locateavailable = locateavailable;
        this.maxvailable = maxvailable;
    }

    /**
     * @description: 异常信息
     * @param methodname methodname
     * @param exception exception
     * @return: null
     * @throws
     * @author yangjianlei
     * @date 2020/12/8 10:52
     */
    public void setLogExceptionMessge(String action, String methodname, String exception) {
        this.action = action;
        this.methodname = methodname;
        this.exception = exception;
    }
}
