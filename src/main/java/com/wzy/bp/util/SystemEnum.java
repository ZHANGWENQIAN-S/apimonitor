package com.wzy.bp.util;

public enum SystemEnum {


    ALERT_CRITICAL(1,"严重"),
    ALERT_WARN(1,"警告"),
    ALERT_NORMAL(1,"一般"),
    ALERT_TIPS(1,"提示")
    ;


    private Integer code;

    private String msg;

    SystemEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
