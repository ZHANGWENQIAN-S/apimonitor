package com.wzy.bp.util;
public enum ResponseEnum {

    /**
     * 请求成功
     */
    RESPONSE_SUCCESS_200("200", "请求成功"),
    /**
     * 请求成功，但参数不存在
     */
    RESPONSE_SUCCESS_201("201", "不存在"),
    /**
     * 用户不存在
     */
    RESPONSE_ERROR_401("401", "用户不存在"),
    /**
     * 请求参数有误
     */
    RESPONSE_ERROR_403("403", "请求参数错误"),
    /**
     * 该流程配置未发布
     */
    /**
     * 搜索关键字有误
     */
    RESPONSE_ERROR_405("405", "搜索关键字有误"),
    /**
     * 请求失败
     */
    RESPONSE_ERROR_400("400", "请求失败"),
    /**
     * 请求异常
     */
    RESPONSE_ERROR_EXCEPTION("404", "请求异常"),
    /**
     * 请求服务器异常
     */
    RESPONSE_INNER_ERROR_EXCEPTION("500", "请求服务异常"),
    RESPONSE_NO_SCORE("1001", "没有玩家叫分"),
    RESPONSE_NO_CALL("1002", "没有玩家叫地主"),
    RESPONSE_NO_DOUBLE("1003","加倍信息错误")
    ;

    private String code;

    private String msg;

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
