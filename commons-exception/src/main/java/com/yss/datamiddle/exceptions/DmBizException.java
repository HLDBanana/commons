package com.yss.datamiddle.exceptions;

import com.yss.datamiddle.enums.DmStatus;

import java.util.List;

/**
 * @author daomingzhu
 */
public class DmBizException extends RuntimeException {
    private final DmStatus dmStatus;

    public DmBizException(DmStatus dmStatus, String ... params) {
        super(dmStatus.getFormattedErrorMessage(params));
        this.dmStatus = dmStatus;
    }

    public DmBizException(final DmStatus errorCode, final List<String> params) {
        super(errorCode.getFormattedErrorMessage(params.toArray(new String[0])));
        this.dmStatus = errorCode;
    }

    public DmBizException() {
        this(DmStatus.INTERNAL_ERROR);
    }

    public DmBizException(String message) {
        super(message);
        this.dmStatus = DmStatus.INTERNAL_ERROR;
    }

    public DmBizException(DmStatus errorCode, Throwable cause, String... params) {
        super(errorCode.getFormattedErrorMessage(params), cause);
        this.dmStatus = errorCode;
    }

    public DmBizException(String message, Throwable cause) {
        super(message, cause);
        this.dmStatus = DmStatus.INTERNAL_ERROR;
    }

    public DmBizException(Throwable cause) {
        super(cause);
        this.dmStatus = DmStatus.INTERNAL_ERROR;
    }

    public DmBizException(DmStatus errorCode, Throwable cause, boolean enableSuppression,
                          boolean writableStackTrace, String ... params) {
        super(errorCode.getFormattedErrorMessage(params), cause, enableSuppression, writableStackTrace);
        this.dmStatus = DmStatus.INTERNAL_ERROR;
    }

    public DmBizException(String message, Throwable cause, boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.dmStatus = DmStatus.INTERNAL_ERROR;
    }

    public DmBizException(final DmStatus errorCode, Throwable cause, final List<String> params) {
        super(errorCode.getFormattedErrorMessage(params.toArray(new String[0])), cause);
        this.dmStatus = errorCode;
    }

    public DmStatus getDmErrorCode() {
        return dmStatus;
    }
}
