package com.yss.datamiddle.enums;

/**
 * @description: 日志操作类型
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
public enum LogOperateType {

    /**
     * 描述：日志操作类型定义
     *
     * @param null
     * @return
     * @author fangzhao at 2020/4/7 16:55
     */
    QUERY(1, "查询"),ADD(2, "新增"), MODIFY(3, "修改"),DELETE(4, "删除"),
    UPLOAD(5, "上传"), DOWNLOAD(6, "下载"), IMPORT(7, "导入"), EXPORT(8, "导出"),
    OTHER(9,"其它操作");

    private int code;

    private String msg;

    LogOperateType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
     * @param code 数据库中存储的自定义code属性
     * @return code对应的枚举类
     */
    public static com.yss.datamiddle.enums.LogOperateType locateIEnum(int code) {
        for(com.yss.datamiddle.enums.LogOperateType status : com.yss.datamiddle.enums.LogOperateType.values()) {
            if(status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + code);
    }

}
