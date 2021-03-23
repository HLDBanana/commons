package com.yss.datamiddle.vo;

import com.yss.datamiddle.enums.DmStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @description: 返回结果
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@ApiModel("返回结果")
public class RetResult<T> implements Serializable {

    private static final long serialVersionUID = 7424094087156251104L;

    /**
     * 响应代码
     */
    @ApiModelProperty("响应状态码")
    private int status;
    /**
     * 响应代码
     */
    @ApiModelProperty("响应业务类型的代码")
    private String bizCode;
    /**
     * 响应消息
     */
    @ApiModelProperty("响应消息")
    private String message;

    /**
     * 响应结果
     */
    @ApiModelProperty("响应数据")
    private T data;

    public RetResult(){}
    public RetResult(DmStatus dmStatus,String... params) {
        this.status = dmStatus.getHttpStatus().value();
        this.bizCode = dmStatus.getErrorCode();
        this.message = dmStatus.getFormattedErrorMessage(params);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RetResult{" +
                "status=" + status +
                ", bizCode='" + bizCode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
