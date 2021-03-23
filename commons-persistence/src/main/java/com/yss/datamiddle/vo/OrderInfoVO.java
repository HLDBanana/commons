package com.yss.datamiddle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 排序
 * @author: fangzhao
 * @create: 2020/5/27 12:26
 * @update: 2020/5/27 12:26
 */
@ApiModel("排序信息")
public class OrderInfoVO {

    @ApiModelProperty("排序字段")
    private String orderField;
    @ApiModelProperty("排序类型（asc、desc）")
    private String orderType;

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
