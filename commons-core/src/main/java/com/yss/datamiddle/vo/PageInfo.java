package com.yss.datamiddle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 分页信息
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@ApiModel("分页信息")
public class PageInfo {
    /**
     * 默认第一页，每页显示10条
     */
    @ApiModelProperty("页码")
    private Integer pageNum = 1;
    @ApiModelProperty("每页显示条数")
    private Integer pageSize = 10;

    public PageInfo() {}

    public PageInfo(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
