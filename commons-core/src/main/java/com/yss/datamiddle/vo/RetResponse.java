package com.yss.datamiddle.vo;

import com.yss.datamiddle.enums.DmStatus;

/**
 * @description: 返回响应
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
public final class RetResponse {
    private RetResponse() {
    }

    /**
     * @description: 返回成功
     * @creater: fangzhao
     * @updater:
     * @create: 2020/3/24 13:09
     * @update: 2020/3/24 13:09
     * @Param: request
     * @return:
     */
    public static <T> RetResult<T> makeOKRsp() {
        return new RetResult<>(DmStatus.OK,"");
    }

    /**
     * 该方法已过时，请使用 makeOKRspForData方法
     * @description: 返回成功信息
     * @creater: fangzhao
     * @updater:
     * @create: 2020/3/24 13:09
     * @update: 2020/3/24 13:09
     * @Param: request
     * @return:
     */
    @Deprecated
    public static <T> RetResult<T> makeOKRsp(T data) {
        RetResult<T> retResult = new RetResult(DmStatus.OK, DmStatus.OK.getFormattedErrorMessage());
        retResult.setData(data);
        return retResult;
    }
    /**
     * @description: 返回成功信息
     * @creater: fangzhao
     * @updater:
     * @create: 2020/3/24 13:09
     * @update: 2020/3/24 13:09
     * @Param: request
     * @return:
     */
    public static <T> RetResult<T> makeOKRspForData(T data) {
        RetResult<T> retResult = new RetResult(DmStatus.OK, DmStatus.OK.getFormattedErrorMessage());
        retResult.setData(data);
        return retResult;
    }
    public static <T> RetResult<T> makeOKRsp(String message) {
        return new RetResult(DmStatus.OK_MSG, message);
    }
    public static <T> RetResult<T> makeRsp(DmStatus commonEnum) {
        return new RetResult(commonEnum,"");
    }

    /**
     * @description: 返回失败
     * @creater: fangzhao
     * @updater:
     * @create: 2020/3/24 13:09
     * @update: 2020/3/24 13:09
     * @Param: request
     * @return:
     */
    public static <T> RetResult<T> makeErrRsp(String message) {
        return new RetResult<>(DmStatus.BAD_REQUEST, message);
    }
    public static <T> RetResult<T> makeErrRsp(DmStatus dmStatus,String message) {
        return new RetResult<>(dmStatus, message);
    }

}
