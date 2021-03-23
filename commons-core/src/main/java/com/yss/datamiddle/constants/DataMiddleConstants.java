package com.yss.datamiddle.constants;

/**
 *@ClassName DataMiddleConstants
 *@Description 数据中台常量
 *@Author fangzhao
 *@Date 2020/3/5 17:38
 */
public class DataMiddleConstants {
    /**
     * 状态：1-有效、2-无效
     */
    public static final byte STATUS_VALID = 1;
    public static final byte STATUS_INVALID = 2;

    /**
     * 删除标志：0-正常、1-删除
     */
    public static final byte DET_FLAG_NORM = 0;
    public static final byte DET_FLAG_DELETED = 1;

    /**
     * 统一包路径
     */
    public static final String BASE_PACKAGE = "com.yss.datamiddle";

    /**
     * 列表和导出操作，查询最大数量限制
     */
    public static final int LIST_QUERY_MAX_COUNT = 100;
}
