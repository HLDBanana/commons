package com.yss.datamiddle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 分页结果
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@ApiModel("分页结果")
public class PageModel<T> {

    @ApiModelProperty("数据总量")
    private long total;

    @ApiModelProperty("列表信息")
    private List<T> list = new ArrayList<>();

    public PageModel() {
    }

    public PageModel(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
