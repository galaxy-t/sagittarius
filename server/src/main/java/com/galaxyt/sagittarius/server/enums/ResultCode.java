package com.galaxyt.sagittarius.server.enums;

/**
 * 全局返回状态码
 * @author zhouqi
 * @date 2019-06-24 18:05
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-06-24 18:05     zhouqi          v1.0.0           Created
 *
 */
public enum ResultCode {

    /**
     * 请求成功
     */
    SUCCESS(0, "SUCCESS"),

    ERROR(1, "ERROR"),

    APPLICATION_EXISTED(2, "应用名已存在"),

    REMOVE_FALIURE(3, "数据删除失败"),

    APPLICATION_NAMESPACE_EXISTED(4, "此应用下已经包含该命名空间"),

    DATA_IS_NULL(5, "未查询到数据");


    private final int code;

    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }



}
