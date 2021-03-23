package com.yss.datamiddle.enums;

/**
 * @author yangjianlei
 * @title: LogOperaterType
 * @projectName logdemo
 * @description: TODO
 * @date 2020/11/2510:23
 */
public enum LogOperateType {
    QUERY(1, "查询"),
    ADD(2, "新增"),
    MODIFY(3, "修改"),
    DELETE(4, "删除"),
    UPLOAD(5, "上传"),
    DOWNLOAD(6, "下载"),
    IMPORT(7, "导入"),
    EXPORT(8, "导出"),
    OTHER(9, "其它操作");

    private int code;
    private String msg;

    private LogOperateType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static LogOperateType locateIEnum(int code) {
        LogOperateType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            LogOperateType status = var1[var3];
            if (status.getCode() == code) {
                return status;
            }
        }

        throw new IllegalArgumentException("未知的枚举类型：" + code);
    }
}
